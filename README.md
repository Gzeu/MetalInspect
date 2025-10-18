# MetalInspect - Android Inspection App

[![Android](https://img.shields.io/badge/platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.10-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## Overview

**MetalInspect** is a production-grade Android application for conducting **100% offline** ferrous product inspections in port operations. The app enables inspectors to create, execute, and finalize metal cargo inspections with photos, defect tagging, and signed PDF reports, operating entirely on entry-level devices without network connectivity.

## Features

### 🔧 **Core Functionality**
- **100% Offline Operation** - No network dependency for core workflows
- **Inspector Profile Management** - Multi-inspector support with signature capture
- **Product Catalog Management** - Local reference tables for steel products
- **Complete Inspection Lifecycle** - Draft → In Progress → Completed → Cancelled
- **Advanced Photo Capture** - CameraX integration with compression and organization
- **Defect Classification System** - Comprehensive taxonomy with severity levels
- **PDF Report Generation** - Professional reports with embedded photos and signatures
- **Data Export & Backup** - CSV export and local backup/restore functionality

### 📱 **Technical Specifications**
- **Platform**: Android API 21+ (Android 5.0+)
- **Language**: Kotlin
- **Architecture**: MVVM with Clean Architecture principles
- **Database**: Room SQLite with optional encryption
- **Dependency Injection**: Hilt
- **Camera**: CameraX for reliable photo capture
- **PDF Generation**: iTextPDF 7
- **Performance**: Cold start ≤2s, Form save ≤200ms, Gallery ≤1s (200 photos)

## Architecture

```
app/
├── data/                    # Data layer
│   ├── database/           # Room entities, DAOs, database
│   ├── repository/         # Repository implementations
│   ├── pdf/               # PDF report generation
│   └── export/            # CSV export and backup
├── domain/                 # Domain layer
│   ├── usecases/          # Business logic
│   ├── models/            # Domain models
│   └── validators/        # Input validation
├── presentation/           # Presentation layer
│   ├── inspection/        # Inspection CRUD screens
│   ├── camera/           # Photo capture
│   ├── defects/          # Defect management
│   ├── reports/          # Report generation
│   └── settings/         # App settings
├── di/                    # Dependency injection
└── utils/                 # Utility classes
```

## Key Dependencies

| Library | Version | Purpose |
|---------|---------|--------|
| **Room** | 2.5.0 | Local SQLite database |
| **Hilt** | 2.48 | Dependency injection |
| **CameraX** | 1.3.0 | Camera functionality |
| **iTextPDF** | 7.2.5 | PDF report generation |
| **SQLCipher** | 4.5.4 | Database encryption |
| **Glide** | 4.15.1 | Image loading and processing |
| **Navigation** | 2.6.0 | Fragment navigation |
| **WorkManager** | 2.8.1 | Background tasks |

## Database Schema

### Core Entities
- **Inspector** - Inspector profiles with signatures
- **Inspection** - Main inspection records
- **ProductType** - Steel product catalog
- **DefectRecord** - Defect classifications
- **InspectionPhoto** - Photo metadata and organization
- **ChecklistItem/Response** - Configurable checklists

### Key Features
- Comprehensive indexing for performance
- Foreign key relationships
- Optional encryption with SQLCipher
- Migration support
- Prepopulated reference data

## Getting Started

### Prerequisites
- **Android Studio**: Arctic Fox or later
- **JDK**: 8 or higher
- **Android SDK**: API 21-33
- **Device**: Android 5.0+ with camera

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Gzeu/MetalInspect.git
   cd MetalInspect
   ```

2. **Open in Android Studio**
   - Import the project
   - Sync Gradle files
   - Wait for indexing to complete

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Configuration

#### Database Encryption (Optional)
```kotlin
// In build.gradle
buildConfigField "boolean", "DATABASE_ENCRYPTION", "true"
```

#### Performance Monitoring
```kotlin
// Enable StrictMode in debug builds
if (BuildConfig.DEBUG) {
    enableStrictMode()
}
```

## Usage

### 1. **Inspector Setup**
- Create inspector profiles
- Capture digital signatures
- Manage multiple inspectors per device

### 2. **Inspection Workflow**
```
Create Inspection → Add Basic Info → Take Photos → 
Record Defects → Complete Checklists → Generate Report
```

### 3. **Photo Management**
- Organize photos by inspection
- Link photos to specific defects
- Add captions and descriptions
- Automatic compression and resizing

### 4. **Defect Classification**
```
Categories: Surface | Dimensional | Material | Packaging
Severity: Critical | Major | Minor | Cosmetic
```

### 5. **Report Generation**
- Professional PDF reports
- Embedded photos and signatures
- Structured defect summaries
- Exportable and shareable

## Performance Targets

| Metric | Target | Status |
|--------|--------|---------|
| **Cold Start** | ≤ 2000ms | ✅ |
| **Form Save** | ≤ 200ms | ✅ |
| **Gallery Load** | ≤ 1000ms | ✅ |
| **Photo Capture** | ≤ 3000ms | ✅ |
| **PDF Generation** | ≤ 5000ms | ✅ |

## Security Features

- **Optional database encryption** with SQLCipher
- **App-scoped storage** for sensitive files
- **No PII logging** in production builds
- **Runtime permission handling**
- **Secure file sharing** with FileProvider

## Offline Capabilities

✅ **Complete offline functionality**  
✅ **Local data storage and retrieval**  
✅ **Photo capture and processing**  
✅ **PDF generation and export**  
✅ **Data backup and restore**  
❌ **No network dependency**  
❌ **No cloud synchronization**  

## Contributing

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Code Style
- **Kotlin** coding conventions
- **MVVM** architecture patterns
- **Clean Architecture** principles
- **Comprehensive** documentation

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Android Jetpack** libraries for robust foundation
- **Material Design** for consistent UI/UX
- **iTextPDF** for professional report generation
- **SQLCipher** for security features

## Contact

**Developer**: George Pricop  
**GitHub**: [@Gzeu](https://github.com/Gzeu)  
**Email**: Available via GitHub profile

---

**MetalInspect** - Professional metal cargo inspection made simple and reliable.
