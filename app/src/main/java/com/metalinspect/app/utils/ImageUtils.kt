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
        quality: Int = Constants.DEFAULT_IMAGE_QUALITY
    ): Boolean {
        return try {
            val bitmap = BitmapFactory.decodeFile(inputPath)
            val rotatedBitmap = rotateImageIfRequired(bitmap, inputPath)
            val resizedBitmap = resizeImageIfRequired(rotatedBitmap)
            
            FileOutputStream(outputPath).use { out ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            }
            
            bitmap.recycle()
            rotatedBitmap.recycle()
            resizedBitmap.recycle()
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    private fun rotateImageIfRequired(bitmap: Bitmap, imagePath: String): Bitmap {
        return try {
            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: IOException) {
            bitmap
        }
    }
    
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height,
            matrix, true
        )
    }
    
    private fun resizeImageIfRequired(bitmap: Bitmap): Bitmap {
        val maxWidth = Constants.MAX_IMAGE_WIDTH
        val maxHeight = Constants.MAX_IMAGE_HEIGHT
        
        if (bitmap.width <= maxWidth && bitmap.height <= maxHeight) {
            return bitmap
        }
        
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        
        val (newWidth, newHeight) = if (aspectRatio > 1) {
            // Landscape
            maxWidth to (maxWidth / aspectRatio).toInt()
        } else {
            // Portrait or square
            (maxHeight * aspectRatio).toInt() to maxHeight
        }
        
        return Bitmap.createScaledBitmap(
            bitmap,
            newWidth,
            newHeight,
            true
        )
    }
    
    fun getImageDimensions(imagePath: String): Pair<Int, Int> {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, options)
            Pair(options.outWidth, options.outHeight)
        } catch (e: Exception) {
            Pair(0, 0)
        }
    }
    
    fun createThumbnail(imagePath: String, maxSize: Int = 200): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, options)
            
            val scale = calculateInSampleSize(options, maxSize, maxSize)
            options.inSampleSize = scale
            options.inJustDecodeBounds = false
            
            BitmapFactory.decodeFile(imagePath, options)
        } catch (e: Exception) {
            null
        }
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
            
            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
}
