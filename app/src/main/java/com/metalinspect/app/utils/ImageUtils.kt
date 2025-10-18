package com.metalinspect.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUtils @Inject constructor(
    private val context: Context
) {
    
    fun compressAndSaveImage(
        inputPath: String,
        outputPath: String,
        quality: Int = Constants.DEFAULT_IMAGE_QUALITY,
        maxWidth: Int = Constants.MAX_IMAGE_WIDTH,
        maxHeight: Int = Constants.MAX_IMAGE_HEIGHT
    ): Boolean {
        return try {
            val originalBitmap = BitmapFactory.decodeFile(inputPath)
                ?: return false
            
            // Correct orientation
            val correctedBitmap = correctImageOrientation(originalBitmap, inputPath)
            
            // Resize if necessary
            val resizedBitmap = resizeBitmap(correctedBitmap, maxWidth, maxHeight)
            
            // Save compressed image
            FileOutputStream(outputPath).use { output ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
            }
            
            // Clean up
            if (correctedBitmap != originalBitmap) {
                correctedBitmap.recycle()
            }
            resizedBitmap.recycle()
            originalBitmap.recycle()
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun createThumbnail(
        imagePath: String,
        thumbnailSize: Int = Constants.THUMBNAIL_SIZE
    ): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(imagePath, options)
            
            // Calculate sample size
            val sampleSize = calculateInSampleSize(options, thumbnailSize, thumbnailSize)
            
            options.apply {
                inJustDecodeBounds = false
                inSampleSize = sampleSize
            }
            
            val bitmap = BitmapFactory.decodeFile(imagePath, options)
            resizeBitmap(bitmap, thumbnailSize, thumbnailSize)
            
        } catch (e: Exception) {
            null
        }
    }
    
    fun getImageDimensions(imagePath: String): Pair<Int, Int> {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(imagePath, options)
            Pair(options.outWidth, options.outHeight)
        } catch (e: Exception) {
            Pair(0, 0)
        }
    }
    
    private fun correctImageOrientation(bitmap: Bitmap, imagePath: String): Bitmap {
        return try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
                else -> return bitmap
            }
            
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: IOException) {
            bitmap
        }
    }
    
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }
        
        val aspectRatio = width.toFloat() / height.toFloat()
        
        val (newWidth, newHeight) = if (aspectRatio > 1) {
            // Landscape
            if (width > maxWidth) {
                maxWidth to (maxWidth / aspectRatio).toInt()
            } else {
                width to height
            }
        } else {
            // Portrait
            if (height > maxHeight) {
                (maxHeight * aspectRatio).toInt() to maxHeight
            } else {
                width to height
            }
        }
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
    
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
}