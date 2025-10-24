package com.metalinspect.app.data.pdf

import android.graphics.Bitmap
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.UnitValue
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Service that generates a PDF report for a given inspection with optional photos.
 * Photos are downscaled and compressed to keep file size under control.
 */
class PdfReportGenerator(
    private val maxImageWidthPx: Int = 1200,
    private val thumbWidthPx: Int = 240,
    private val jpegQuality: Int = 70
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun generate(
        output: OutputStream,
        inspection: Inspection,
        defects: List<Defect>,
        photos: List<Bitmap> = emptyList()
    ) {
        val pdf = PdfDocument(PdfWriter(output))
        val doc = Document(pdf, PageSize.A4)

        // Title
        doc.add(Paragraph("Inspection Report").setBold().setFontSize(18f))

        // Header table
        val header = Table(UnitValue.createPercentArray(floatArrayOf(1f, 2f))).useAllAvailableWidth()
        fun addRow(label: String, value: String) {
            header.addCell(label)
            header.addCell(value)
        }
        addRow("Vessel", inspection.vesselName)
        addRow("Date", dateFormat.format(inspection.inspectionDate))
        addRow("Inspector", inspection.inspectorName)
        addRow("Location", inspection.location)
        addRow("Cargo", "${inspection.cargoDescription} (${inspection.cargoQuantity} ${inspection.cargoUnit})")
        doc.add(header)
        doc.add(Paragraph("\n"))

        // Defects table
        val defectsTable = Table(UnitValue.createPercentArray(floatArrayOf(1.2f, 0.8f, 3f, 1f))).useAllAvailableWidth()
        listOf("Type", "Severity", "Description", "%/Qty").forEach { defectsTable.addHeaderCell(it) }
        defects.forEach { d ->
            defectsTable.addCell(d.defectType)
            defectsTable.addCell(d.severity)
            defectsTable.addCell(d.description)
            val pct = d.estimatedAffectedPercentage?.let { String.format(Locale.US, "%.1f%%", it) } ?: "-"
            val qty = d.estimatedAffectedQuantity?.let { String.format(Locale.US, "%.1f", it) } ?: "-"
            defectsTable.addCell("$pct / $qty")
        }
        doc.add(Paragraph("Defects").setBold().setFontSize(14f))
        doc.add(defectsTable)

        // Photos grid (thumbnails)
        if (photos.isNotEmpty()) {
            doc.add(Paragraph("Photos").setBold().setFontSize(14f))
            val cols = 3
            val table = Table(UnitValue.createPercentArray(FloatArray(cols) { 1f })).useAllAvailableWidth()
            photos.forEach { bmp ->
                val thumb = bmp.scaleToWidth(thumbWidthPx)
                val img = Image(ImageDataFactory.create(thumb.toJpeg(jpegQuality))).apply {
                    setAutoScale(true)
                    setHorizontalAlignment(HorizontalAlignment.CENTER)
                }
                table.addCell(img)
            }
            doc.add(table)
        }

        // Notes
        doc.add(Paragraph("\nNotes:").setBold())
        doc.add(Paragraph(inspection.notes ?: "-") )

        doc.close()
    }
}

private fun Bitmap.scaleToWidth(targetWidth: Int): Bitmap {
    if (width <= targetWidth) return this
    val ratio = targetWidth.toFloat() / width
    val targetHeight = (height * ratio).toInt()
    return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
}

private fun Bitmap.toJpeg(quality: Int): ByteArray {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, quality.coerceIn(40, 90), bos)
    return bos.toByteArray()
}
