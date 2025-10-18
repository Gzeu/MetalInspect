package com.metalinspect.app.data.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.font.PdfFont
import com.metalinspect.app.data.database.entities.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PDFReportGenerator @Inject constructor(
    private val context: Context
) {

    companion object {
        private const val COMPANY_NAME = "MetalInspect Solutions"
        private const val COMPANY_LOGO_PATH = "logo.png"
        private const val MAX_IMAGE_WIDTH = 400f
        private const val MAX_IMAGE_HEIGHT = 300f
    }

    fun generateInspectionReport(
        inspection: Inspection,
        inspector: Inspector,
        productType: ProductType,
        defectRecords: List<DefectRecord>,
        photos: List<InspectionPhoto>
    ): File {
        
        val fileName = "MetalInspect_${inspection.lotNumber}_${getFormattedDate(inspection.createdAt)}.pdf"
        val outputFile = File(context.getExternalFilesDir("reports"), fileName)
        outputFile.parentFile?.mkdirs()

        val writer = PdfWriter(FileOutputStream(outputFile))
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        try {
            val boldFont = PdfFontFactory.createFont()
            val regularFont = PdfFontFactory.createFont()

            addCompanyHeader(document, boldFont)
            addInspectionDetails(document, inspection, inspector, productType, boldFont, regularFont)
            addDefectSummary(document, defectRecords, boldFont, regularFont)
            addPhotosSection(document, photos, defectRecords, boldFont)
            addSignatureSection(document, inspector, boldFont, regularFont)
            addFooter(document, regularFont)

        } catch (e: Exception) {
            throw PDFGenerationException("Failed to generate PDF report", e)
        } finally {
            document.close()
        }

        return outputFile
    }

    private fun addCompanyHeader(document: Document, boldFont: PdfFont) {
        val header = Paragraph(COMPANY_NAME)
            .setFont(boldFont)
            .setFontSize(18f)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20f)
        
        document.add(header)

        val title = Paragraph("METAL CARGO INSPECTION REPORT")
            .setFont(boldFont)
            .setFontSize(16f)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(20f)
            .setMarginBottom(20f)

        document.add(title)
    }

    private fun addInspectionDetails(
        document: Document,
        inspection: Inspection,
        inspector: Inspector,
        productType: ProductType,
        boldFont: PdfFont,
        regularFont: PdfFont
    ) {
        val detailsTable = Table(UnitValue.createPercentArray(floatArrayOf(30f, 70f)))
            .useAllAvailableWidth()
            .setMarginBottom(20f)

        fun addDetailRow(label: String, value: String) {
            detailsTable.addCell(
                Cell().add(Paragraph(label).setFont(boldFont))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            )
            detailsTable.addCell(
                Cell().add(Paragraph(value).setFont(regularFont))
            )
        }

        addDetailRow("Lot Number:", inspection.lotNumber)
        inspection.containerNumber?.let {
            addDetailRow("Container Number:", it)
        }
        addDetailRow("Product Type:", productType.name)
        addDetailRow("Quantity:", "${inspection.quantity} ${inspection.unit}")
        addDetailRow("Weight:", "${inspection.weight} kg")
        addDetailRow("Port Location:", inspection.portLocation)
        addDetailRow("Weather Conditions:", inspection.weatherConditions)
        addDetailRow("Inspector:", "${inspector.name} (${inspector.company})")
        addDetailRow("Inspection Date:", getFormattedDateTime(inspection.createdAt))
        addDetailRow("Status:", inspection.status.name)
        
        inspection.notes?.let {
            addDetailRow("Notes:", it)
        }

        document.add(detailsTable)
    }

    private fun addDefectSummary(
        document: Document,
        defectRecords: List<DefectRecord>,
        boldFont: PdfFont,
        regularFont: PdfFont
    ) {
        val sectionTitle = Paragraph("DEFECT SUMMARY")
            .setFont(boldFont)
            .setFontSize(14f)
            .setMarginTop(20f)
            .setMarginBottom(10f)

        document.add(sectionTitle)

        if (defectRecords.isEmpty()) {
            val noDefects = Paragraph("No defects recorded during inspection.")
                .setFont(regularFont)
                .setItalic()
                .setMarginBottom(20f)
            
            document.add(noDefects)
            return
        }

        val defectTable = Table(UnitValue.createPercentArray(floatArrayOf(25f, 20f, 15f, 10f, 30f)))
            .useAllAvailableWidth()
            .setMarginBottom(20f)

        val headers = listOf("Defect Type", "Category", "Severity", "Count", "Description")
        headers.forEach { header ->
            defectTable.addHeaderCell(
                Cell().add(Paragraph(header).setFont(boldFont))
                    .setBackgroundColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        defectRecords.forEach { defect ->
            defectTable.addCell(Cell().add(Paragraph(defect.defectType).setFont(regularFont)))
            defectTable.addCell(Cell().add(Paragraph(defect.defectCategory.name).setFont(regularFont)))
            
            val severityCell = Cell().add(Paragraph(defect.severity.name).setFont(regularFont))
            when (defect.severity) {
                DefectSeverity.CRITICAL -> severityCell.setBackgroundColor(ColorConstants.RED)
                DefectSeverity.MAJOR -> severityCell.setBackgroundColor(ColorConstants.ORANGE)
                DefectSeverity.MINOR -> severityCell.setBackgroundColor(ColorConstants.YELLOW)
                DefectSeverity.COSMETIC -> severityCell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
            }
            defectTable.addCell(severityCell)
            
            defectTable.addCell(
                Cell().add(Paragraph(defect.count.toString()).setFont(regularFont))
                    .setTextAlignment(TextAlignment.CENTER)
            )
            defectTable.addCell(Cell().add(Paragraph(defect.description).setFont(regularFont)))
        }

        document.add(defectTable)
    }

    private fun addPhotosSection(
        document: Document,
        photos: List<InspectionPhoto>,
        defectRecords: List<DefectRecord>,
        boldFont: PdfFont
    ) {
        if (photos.isEmpty()) return

        val sectionTitle = Paragraph("INSPECTION PHOTOS")
            .setFont(boldFont)
            .setFontSize(14f)
            .setMarginTop(20f)
            .setMarginBottom(10f)

        document.add(sectionTitle)

        photos.take(10).chunked(2).forEach { photoRow ->
            val photoTable = Table(UnitValue.createPercentArray(FloatArray(photoRow.size) { 50f }))
                .useAllAvailableWidth()
                .setMarginBottom(15f)

            photoRow.forEach { photo ->
                try {
                    val imageFile = File(photo.filePath)
                    if (imageFile.exists()) {
                        val imageData = ImageDataFactory.create(photo.filePath)
                        val image = Image(imageData)
                            .setAutoScale(true)
                            .setMaxWidth(200f)
                        
                        val imageCell = Cell()
                            .add(image)
                            .add(Paragraph(photo.caption ?: "").setFontSize(8f))
                            .setPadding(5f)
                        
                        photoTable.addCell(imageCell)
                    }
                } catch (e: Exception) {
                    val errorCell = Cell()
                        .add(Paragraph("[Image not available]").setFontSize(10f))
                        .setPadding(20f)
                        .setTextAlignment(TextAlignment.CENTER)
                    
                    photoTable.addCell(errorCell)
                }
            }

            document.add(photoTable)
        }
    }

    private fun addSignatureSection(
        document: Document,
        inspector: Inspector,
        boldFont: PdfFont,
        regularFont: PdfFont
    ) {
        val sectionTitle = Paragraph("INSPECTOR SIGNATURE")
            .setFont(boldFont)
            .setFontSize(14f)
            .setMarginTop(30f)
            .setMarginBottom(20f)

        document.add(sectionTitle)

        val signatureTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
            .useAllAvailableWidth()

        val signatureCell = Cell()
            .add(Paragraph("[Digital Signature]").setFont(regularFont))
            .add(Paragraph("\n_________________________").setFont(regularFont))
            .add(Paragraph("${inspector.name}\n${inspector.role}\n${inspector.company}").setFont(regularFont))
        
        signatureTable.addCell(signatureCell)

        val dateCell = Cell()
            .add(Paragraph("Date: ${getFormattedDate(System.currentTimeMillis())}").setFont(regularFont))
            .add(Paragraph("\n\n_________________________").setFont(regularFont))
            .add(Paragraph("Date").setFont(regularFont))
        
        signatureTable.addCell(dateCell)

        document.add(signatureTable)
    }

    private fun addFooter(document: Document, regularFont: PdfFont) {
        val footer = Paragraph("This report was generated by MetalInspect App v1.0")
            .setFont(regularFont)
            .setFontSize(8f)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(30f)

        document.add(footer)
    }

    private fun getFormattedDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
    }

    private fun getFormattedTime(timestamp: Long): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    }

    private fun getFormattedDateTime(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    }
}

class PDFGenerationException(message: String, cause: Throwable? = null) : Exception(message, cause)
