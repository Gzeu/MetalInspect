# Internal API Documentation

## Repository Interfaces

### InspectionRepository
```kotlin
interface InspectionRepository {
    suspend fun createInspection(inspection: Inspection): Result<String>
    suspend fun updateInspection(inspection: Inspection): Result<Unit>
    suspend fun deleteInspection(inspectionId: String): Result<Unit>
    fun getInspections(): Flow<List<Inspection>>
    fun getInspectionById(id: String): Flow<Inspection?>
    fun getInspectionWithDetails(id: String): Flow<InspectionWithDetails?>
    suspend fun completeInspection(inspectionId: String): Result<Unit>
}
```

### PhotoRepository
```kotlin
interface PhotoRepository {
    suspend fun savePhoto(photo: InspectionPhoto): Result<String>
    suspend fun deletePhoto(photoId: String): Result<Unit>
    suspend fun updatePhotoCaption(photoId: String, caption: String): Result<Unit>
    fun getPhotosByInspection(inspectionId: String): Flow<List<InspectionPhoto>>
    suspend fun reorderPhotos(inspectionId: String, newOrder: List<String>): Result<Unit>
}
```

### DefectRepository
```kotlin
interface DefectRepository {
    suspend fun addDefect(defect: DefectRecord): Result<String>
    suspend fun updateDefect(defect: DefectRecord): Result<Unit>
    suspend fun deleteDefect(defectId: String): Result<Unit>
    fun getDefectsByInspection(inspectionId: String): Flow<List<DefectRecord>>
    suspend fun getDefectCategories(): List<DefectCategory>
}
```

## Use Case Interfaces

### CreateInspectionUseCase
```kotlin
class CreateInspectionUseCaseImpl @Inject constructor(
    private val repository: InspectionRepository,
    private val validator: InspectionValidator
) : CreateInspectionUseCase {
    suspend operator fun invoke(inspection: Inspection): Result<String>
}
```

### GenerateReportUseCase
```kotlin
class GenerateReportUseCaseImpl @Inject constructor(
    private val pdfGenerator: PDFReportGenerator,
    private val inspectionRepository: InspectionRepository
) : GenerateReportUseCase {
    suspend operator fun invoke(inspectionId: String): Result<File>
}
```

## Domain Models

### InspectionWithDetails
```kotlin
data class InspectionWithDetails(
    val inspection: Inspection,
    val inspector: Inspector,
    val productType: ProductType,
    val defectRecords: List<DefectRecord>,
    val photos: List<InspectionPhoto>,
    val checklistResponses: List<ChecklistResponse>,
    // Computed properties
    val totalDefects: Int,
    val criticalDefects: Int,
    val majorDefects: Int,
    val minorDefects: Int,
    val photoCount: Int,
    val completionPercentage: Float
)
```

### PhotoWithMetadata
```kotlin
data class PhotoWithMetadata(
    val photo: InspectionPhoto,
    val linkedDefect: DefectRecord? = null,
    val fileExists: Boolean,
    val fileSizeMB: Double,
    val thumbnailPath: String? = null
)
```

## Validation API

### InspectionValidator
```kotlin
class InspectionValidator {
    fun validateInspection(inspection: Inspection): ValidationResult
    fun validateLotNumber(lotNumber: String): ValidationResult
    fun validateQuantity(quantity: Double, unit: String): ValidationResult
    fun validatePortLocation(location: String): ValidationResult
}
```

### FormValidator
```kotlin
class FormValidator {
    fun validateRequired(value: String?, fieldName: String): ValidationResult
    fun validateNumeric(value: String?, min: Double?, max: Double?): ValidationResult
    fun validateLength(value: String?, minLength: Int, maxLength: Int): ValidationResult
}
```

## File Management API

### FileUtils
```kotlin
class FileUtils {
    fun getInspectionPhotoDirectory(inspectionId: String): File
    fun getReportsDirectory(): File
    fun getBackupDirectory(): File
    fun createTempImageFile(inspectionId: String): File
    fun getStorageInfo(): StorageInfo
    fun cleanupTempFiles()
}
```

### ImageUtils  
```kotlin
class ImageUtils {
    fun compressAndSaveImage(inputPath: String, outputPath: String, quality: Int): Boolean
    fun createThumbnail(imagePath: String, thumbnailSize: Int): Bitmap?
    fun getImageDimensions(imagePath: String): Pair<Int, Int>
}
```

## Error Handling

### Result Types
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    
    inline fun <R> map(transform: (T) -> R): Result<R>
    inline fun onSuccess(action: (T) -> Unit): Result<T>
    inline fun onError(action: (Exception) -> Unit): Result<T>
}
```

### ValidationResult
```kotlin
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    val isValid: Boolean
    val errorMessage: String?
}
```

## Constants & Configuration

### Performance Limits
```kotlin
object Constants {
    const val MAX_PHOTOS_PER_INSPECTION = 50
    const val MAX_IMAGE_WIDTH = 1920
    const val MAX_IMAGE_HEIGHT = 1080
    const val DEFAULT_IMAGE_QUALITY = 85
    const val CAMERA_CAPTURE_TIMEOUT = 30000L
    const val PDF_GENERATION_TIMEOUT = 60000L
}
```

### File Naming Patterns
```kotlin
// Photos: "IMG_20251019_103045_001.jpg"
// PDFs: "MetalInspect_LOT-2025-001_20251019_103045.pdf"
// Backups: "MetalInspect_backup_20251019_103045.zip"
// CSVs: "inspections_export_20251019_103045.csv"
```

## Database Schema

### Key Relationships
- Inspection → Inspector (many-to-one)
- Inspection → ProductType (many-to-one)
- Inspection → DefectRecord (one-to-many)
- Inspection → InspectionPhoto (one-to-many)
- Inspection → ChecklistResponse (one-to-many)
- DefectRecord → InspectionPhoto (one-to-many, optional)

### Indexes for Performance
- Inspections: lot_number, status, inspector_id, created_at, product_type_id
- Photos: inspection_id, defect_record_id, created_at
- Defects: inspection_id, defect_type, severity
- Inspectors: company, name

---

**Need more details?** Check the inline documentation in each class or interface.