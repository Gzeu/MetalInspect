package com.metalinspect.app.data.pdf

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Service that generates a PDF report for a given inspection
 */
class PdfReportGenerator(
    private val context: Context
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun generate(
        output: OutputStream,
        inspection: Inspection,
        defects: List<Defect>,
        photos: List<Bitmap> = emptyList()
    ) {
        val writer = PdfWriter(output)
        val pdf = PdfDocument(writer)
        val doc = Document(pdf, PageSize.A4)
        val font = PdfFontFactory.createFont()
        doc.setFont(font)

        // Title
        doc.add(
            Paragraph("Inspection Report")
                .setBold()
                .setFontSize(18f)
                .setTextAlignment(TextAlignment.CENTER)
        )

        // Header table
        val header = Table(floatArrayOf(1f, 2f)).useAllAvailableWidth()
        header.addCell(headerCell("Vessel"))
        header.addCell(valueCell(inspection.vesselName))
        header.addCell(headerCell("Date"))
        header.addCell(valueCell(dateFormat.format(inspection.inspectionDate)))
        header.addCell(headerCell("Inspector"))
        header.addCell(valueCell(inspection.inspectorName))
        header.addCell(headerCell("Location"))
        header.addCell(valueCell(inspection.location))
        header.addCell(headerCell("Cargo"))
        header.addCell(valueCell("${inspection.cargoDescription} (${inspection.cargoQuantity} ${inspection.cargoUnit})"))
        doc.add(header)

        doc.add(Paragraph("\n"))

        // Defects table
        val defectTable = Table(floatArrayOf(1.2f, 0.8f, 3f, 1f)).useAllAvailableWidth()
        listOf("Type", "Severity", "Description", "%/Qty").forEach { defectTable.addHeaderCell(headerCell(it)) }
        defects.forEach { d ->
            defectTable.addCell(valueCell(d.defectType))
            defectTable.addCell(valueCell(d.severity))
            defectTable.addCell(valueCell(d.description))
            val pct = d.estimatedAffectedPercentage?.let { String.format(Locale.US, "%.1f%%", it) } ?: "-"
            val qty = d.estimatedAffectedQuantity?.let { String.format(Locale.US, "%.1f", it) } ?: "-"
            defectTable.addCell(valueCell("$pct / $qty"))
        }
        doc.add(Paragraph("Defects").setBold().setFontSize(14f))
        doc.add(defectTable)

        // Summary
        val criticalCount = defects.count { it.isCritical }
        val summary = Table(floatArrayOf(1f, 1f, 1f)).useAllAvailableWidth()
        summary.addCell(headerCell("Total Defects"))
        summary.addCell(headerCell("Critical"))
        summary.addCell(headerCell("Completed"))
        summary.addCell(valueCell(defects.size.toString()))
        summary.addCell(valueCell(criticalCount.toString()))
        summary.addCell(valueCell(if (inspection.isCompleted) "Yes" else "No"))
        doc.add(Paragraph("Summary").setBold().setFontSize(14f))
        doc.add(summary)

        doc.add(Paragraph("\nNotes:")).add(Paragraph(inspection.notes ?: "-")
            .setBorder(SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)))

        doc.close()
    }

    private fun headerCell(text: String): Cell =
        Cell().add(Paragraph(text).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY)

    private fun valueCell(text: String): Cell =
        Cell().add(Paragraph(text))
}
