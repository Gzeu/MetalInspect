package com.metalinspect.app.utils

object Constants {
    
    // Database
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "inspection_database"
    
    // File Storage
    const val PHOTO_DIRECTORY = "inspection_photos"
    const val PDF_DIRECTORY = "reports"
    const val BACKUP_DIRECTORY = "backups"
    const val SIGNATURE_DIRECTORY = "signatures"
    
    // Image Processing
    const val MAX_IMAGE_WIDTH = 1920
    const val MAX_IMAGE_HEIGHT = 1080
    const val IMAGE_QUALITY_HIGH = 95
    const val IMAGE_QUALITY_MEDIUM = 85
    const val IMAGE_QUALITY_LOW = 75
    
    // PDF Generation
    const val PDF_PAGE_SIZE_A4_WIDTH = 595f
    const val PDF_PAGE_SIZE_A4_HEIGHT = 842f
    const val PDF_MARGIN = 50f
    
    // Performance Targets
    const val COLD_START_TARGET_MS = 2000L
    const val FORM_SAVE_TARGET_MS = 200L
    const val GALLERY_OPEN_TARGET_MS = 1000L
    
    // Validation
    const val MAX_LOT_NUMBER_LENGTH = 50
    const val MAX_CONTAINER_NUMBER_LENGTH = 30
    const val MAX_NOTES_LENGTH = 1000
    const val MAX_DEFECT_DESCRIPTION_LENGTH = 500
    const val MAX_PHOTO_CAPTION_LENGTH = 200
    
    // Export
    const val MAX_EXPORT_BATCH_SIZE = 100
    const val CSV_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val PDF_DATE_FORMAT = "dd/MM/yyyy HH:mm"
    
    // Permissions
    const val PERMISSION_REQUEST_CAMERA = 1001
    const val PERMISSION_REQUEST_STORAGE = 1002
    const val PERMISSION_REQUEST_LOCATION = 1003
    
    // Shared Preferences Keys
    const val PREF_ACTIVE_INSPECTOR_ID = "active_inspector_id"
    const val PREF_IMAGE_QUALITY = "image_quality"
    const val PREF_AUTO_BACKUP = "auto_backup"
    const val PREF_GPS_ENABLED = "gps_enabled"
    const val PREF_DARK_MODE = "dark_mode"
    
    // File Provider Authority
    const val FILE_PROVIDER_AUTHORITY = "com.metalinspect.app.fileprovider"
    
    // Intent Actions
    const val ACTION_EXPORT_COMPLETE = "com.metalinspect.app.EXPORT_COMPLETE"
    const val ACTION_BACKUP_COMPLETE = "com.metalinspect.app.BACKUP_COMPLETE"
    
    // Notification Channels
    const val NOTIFICATION_CHANNEL_EXPORT = "export_channel"
    const val NOTIFICATION_CHANNEL_BACKUP = "backup_channel"
    
    // Work Manager Tags
    const val WORK_TAG_EXPORT = "export_work"
    const val WORK_TAG_BACKUP = "backup_work"
    
    // Bundle Keys
    const val KEY_INSPECTION_ID = "inspection_id"
    const val KEY_INSPECTOR_ID = "inspector_id"
    const val KEY_PRODUCT_TYPE_ID = "product_type_id"
    const val KEY_DEFECT_ID = "defect_id"
    const val KEY_PHOTO_ID = "photo_id"
    
    // Default Values
    const val DEFAULT_IMAGE_QUALITY = IMAGE_QUALITY_MEDIUM
    const val DEFAULT_BACKUP_RETENTION_DAYS = 30
    const val DEFAULT_MAX_PHOTOS_PER_INSPECTION = 200
}
