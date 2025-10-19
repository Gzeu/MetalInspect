# Deployment Guide

## Production Build

### 1. Pre-build Checklist
- [ ] Update `versionName` and `versionCode` in `app/build.gradle`
- [ ] Enable database encryption: `DATABASE_ENCRYPTION = "true"`
- [ ] Disable debug logging: `DEBUG_LOGGING = "false"`
- [ ] Test on minimum API 21 device
- [ ] Verify ProGuard rules don't break functionality

### 2. Build Commands
```bash
# Clean build
./gradlew clean

# Generate signed release APK
./gradlew assembleRelease

# Generate App Bundle (recommended for Play Store)
./gradlew bundleRelease
```

### 3. Signing Configuration
```gradle
// In app/build.gradle
signingConfigs {
    release {
        storeFile file('../keystore/metalinspect-release.jks')
        storePassword System.getenv("KEYSTORE_PASSWORD")
        keyAlias System.getenv("KEY_ALIAS")
        keyPassword System.getenv("KEY_PASSWORD")
    }
}
```

## Quality Assurance

### Pre-release Testing
```bash
# Full test suite
./gradlew test connectedAndroidTest

# Lint checks
./gradlew lintRelease

# APK analysis
./gradlew analyzeReleaseBundle
```

### Device Testing Matrix
- **Android 5.0** (API 21) - Minimum support
- **Android 8.0** (API 26) - Scoped storage transition  
- **Android 10** (API 29) - Scoped storage enforced
- **Android 13** (API 33) - Granular media permissions

### Performance Validation
- Cold start time ≤ 2000ms
- Form save operations ≤ 200ms  
- Photo gallery load ≤ 1000ms
- Photo capture ≤ 3000ms
- PDF generation ≤ 5000ms
- Memory usage ≤ 256MB peak
- APK size ≤ 50MB

## Distribution

### Google Play Store
1. **App Bundle**: Use `bundleRelease` for optimal size
2. **Target API**: Currently 33, update annually
3. **Permissions**: Justify camera, storage in store listing
4. **Privacy Policy**: Required for data handling
5. **Content Rating**: Business/Productivity app

### Enterprise Distribution
1. **APK Distribution**: Direct install via device management
2. **MDM Integration**: Compatible with major MDM solutions
3. **Offline Installation**: No Play Services dependency
4. **Custom Branding**: Logo and company name configurable

### Side-loading
1. **Enable Unknown Sources** on target devices
2. **Share APK** via USB, email, or local file sharing
3. **Version Management**: Include version in filename
4. **Update Notifications**: Manual process required

## Production Configuration

### Database
```kotlin
// Enable encryption for production
BuildConfig.DATABASE_ENCRYPTION = true

// Set production database name
const val DATABASE_NAME = "metalinspect_production"
```

### File Storage
- Photos: `Android/data/com.metalinspect.app/files/inspection_photos/`
- Reports: `Android/data/com.metalinspect.app/files/reports/`
- Backups: `Android/data/com.metalinspect.app/files/backups/`

### Monitoring
- Enable StrictMode in debug only
- Log non-PII errors to local file for debugging
- Monitor storage usage and cleanup old files automatically

## Rollback Strategy

### Version Rollback
1. Keep previous APK version available
2. Database downgrade not supported - backup before update
3. Export data before major updates

### Data Recovery
1. Local backup/restore functionality built-in
2. Export to CSV for data migration
3. Manual database recovery from backup ZIP

## Support & Maintenance

### Update Schedule
- **Security updates**: As needed
- **Minor features**: Quarterly
- **Major features**: Bi-annually

### Compatibility
- **Minimum API**: Update annually (currently API 21)
- **Target API**: Update with each major release
- **Dependencies**: Update quarterly for security

---

**Ready for production deployment!** Follow the checklist above for a successful release.