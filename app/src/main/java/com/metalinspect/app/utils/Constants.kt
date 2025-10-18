package com.metalinspect.app.utils

object Constants {
    
    // App Configuration
    const val APP_NAME = "MetalInspect"
    const val APP_VERSION = "1.0.0"
    
    // Database
    const val DATABASE_NAME = "inspection_database"
    const val DATABASE_VERSION = 1
    
    // File Provider
    const val FILE_PROVIDER_AUTHORITY = "com.metalinspect.app.fileprovider"
    
    // Directory Names
    const val PHOTO_DIRECTORY = "inspection_photos"
    const val PDF_DIRECTORY = "reports"
    const val BACKUP_DIRECTORY = "backups"
    const val SIGNATURE_DIRECTORY = "signatures"
    const val TEMP_DIRECTORY = "temp"
    
    // File Extensions
    const val PDF_EXTENSION = ".pdf"
    const val CSV_EXTENSION = ".csv"
    const val XLSX_EXTENSION = ".xlsx"
    const val JPG_EXTENSION = ".jpg"
    const val PNG_EXTENSION = ".png"
    const val ZIP_EXTENSION = ".zip"
    
    // Image Processing
    const val DEFAULT_IMAGE_QUALITY = 85
    const val MAX_IMAGE_WIDTH = 1920
    const val MAX_IMAGE_HEIGHT = 1080
    const val THUMBNAIL_SIZE = 200
    
    // Camera
    const val CAMERA_CAPTURE_TIMEOUT = 30000L // 30 seconds
    const val MAX_PHOTOS_PER_INSPECTION = 50
    
    // Validation Limits
    const val MAX_LOT_NUMBER_LENGTH = 50
    const val MAX_CONTAINER_NUMBER_LENGTH = 50
    const val MAX_DESCRIPTION_LENGTH = 500
    const val MAX_NOTES_LENGTH = 1000
    const val MAX_CAPTION_LENGTH = 200
    const val MAX_NAME_LENGTH = 100
    const val MIN_NAME_LENGTH = 2
    const val MAX_DEFECT_COUNT = 1000
    const val MAX_QUANTITY = 1000000.0
    const val MAX_WEIGHT = 100000.0
    
    // Performance
    const val COLD_START_TARGET_MS = 2000L
    const val FORM_SAVE_TARGET_MS = 200L
    const val GALLERY_LOAD_TARGET_MS = 1000L
    const val PHOTO_CAPTURE_TARGET_MS = 3000L
    const val PDF_GENERATION_TARGET_MS = 5000L
    
    // Backup
    const val MAX_BACKUP_AGE_MS = 30L * 24L * 60L * 60L * 1000L // 30 days
    const val MAX_BACKUP_COUNT = 10
    
    // Request Codes
    const val REQUEST_CODE_CAMERA = 100
    const val REQUEST_CODE_GALLERY = 101
    const val REQUEST_CODE_PERMISSIONS = 102
    const val REQUEST_CODE_LOCATION = 103
    
    // Shared Preferences
    const val PREFS_NAME = "MetalInspectPrefs"
    const val PREF_ACTIVE_INSPECTOR = "active_inspector_id"
    const val PREF_IMAGE_QUALITY = "image_quality"
    const val PREF_AUTO_BACKUP = "auto_backup_enabled"
    const val PREF_LOCATION_ENABLED = "location_enabled"
    
    // PDF Report
    const val PDF_COMPANY_NAME = "MetalInspect Solutions"
    const val PDF_COMPANY_LOGO = "logo.png"
    const val PDF_MAX_IMAGES_PER_PAGE = 2
    const val PDF_IMAGE_MAX_WIDTH = 400f
    const val PDF_IMAGE_MAX_HEIGHT = 300f
    
    // Export
    const val CSV_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val EXPORT_DATE_FORMAT = "yyyyMMdd_HHmmss"
    const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy HH:mm"
    const val DISPLAY_TIME_FORMAT = "HH:mm"
    
    // Error Messages
    const val ERROR_NETWORK_UNAVAILABLE = "Network connection unavailable"
    const val ERROR_STORAGE_UNAVAILABLE = "Storage not available"
    const val ERROR_CAMERA_UNAVAILABLE = "Camera not available"
    const val ERROR_PERMISSION_DENIED = "Permission denied"
    const val ERROR_UNKNOWN = "An unknown error occurred"
}