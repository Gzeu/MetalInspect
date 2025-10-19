# Security & Privacy Guide

## Database Encryption (SQLCipher)
- **Optional encryption** enabled via `BuildConfig.DATABASE_ENCRYPTION`
- **Local key generation** - never hardcode keys in production
- **Performance impact**: ~5-10% overhead, acceptable for security-critical deployments
- **Setup**: Toggle in `build.gradle` buildConfigField

```kotlin
// Debug build - no encryption for development
buildConfigField "boolean", "DATABASE_ENCRYPTION", "false"

// Release build - encryption enabled
buildConfigField "boolean", "DATABASE_ENCRYPTION", "true"
```

## Runtime Permissions
### Required Permissions
- **CAMERA** - Essential for photo capture functionality
- **READ_MEDIA_IMAGES** (API 33+) or **READ/WRITE_EXTERNAL_STORAGE** (API < 33) - File storage access

### Optional Permissions  
- **ACCESS_FINE_LOCATION** / **ACCESS_COARSE_LOCATION** - GPS coordinates for inspections

### Permission Handling
- Runtime permission requests with clear rationales
- Graceful degradation when permissions denied
- Educational dialogs explaining permission necessity

## Data Backup & Extraction Policies

### Cloud Backup Rules (`res/xml/backup_rules.xml`)
```xml
<!-- Exclude sensitive data from cloud backup -->
<exclude domain="database" />
<exclude domain="file" path="signatures/" />
<exclude domain="sharedpref" path="inspector_signatures" />
```

### Data Extraction Rules (`res/xml/data_extraction_rules.xml`)
```xml
<!-- Device transfer includes only non-PII files -->
<include domain="file" path="inspection_photos/" />
<include domain="file" path="reports/" />
<exclude domain="database" />
```

## Secure File Sharing (FileProvider)

### Configuration (`res/xml/file_provider_paths.xml`)
- **photos/** - Inspection photos for sharing/viewing
- **reports/** - PDF reports for export
- **signatures/** - Digital signatures (excluded from sharing)
- **backups/** - Local backup files

### Usage
- All file sharing uses `content://` URIs through FileProvider
- Direct file path exposure prevented
- Temporary access only for sharing operations

## Logging & Debug Information

### Production Logging Policy
- **No PII logging** in release builds
- **Error tracking** without sensitive data exposure  
- **Timber** integration with configurable log levels
- **BuildConfig.DEBUG_LOGGING** controls verbosity

### Debug Information Exclusion
- Inspector names, signatures, location data
- Database content, file paths, device identifiers
- Only anonymized error codes and performance metrics

## Security Recommendations

### Build Security
1. **Sign APK/App Bundle** with strong keystore
2. **Enable Play Integrity** for app authentication
3. **ProGuard/R8 obfuscation** - pre-configured
4. **Validate dependencies** for known vulnerabilities

### Runtime Security
1. **Test permissions** on API 21, 28, 33+ devices
2. **Validate external storage** availability
3. **Handle security exceptions** gracefully
4. **Regular security audits** of dependencies

### Data Protection
1. **Local data only** - no cloud dependencies
2. **Encrypted backups** when SQLCipher enabled
3. **Secure file deletion** after processing
4. **Access control** for sensitive directories

## Compliance Considerations
- **GDPR compliance** through local-only data processing
- **Industry standards** for inspection data integrity
- **Audit trail** preservation in database
- **Data retention policies** configurable through settings

## Security Testing Checklist
- [ ] Permission flows on multiple API levels
- [ ] SQLCipher encryption/decryption cycles
- [ ] FileProvider URI security
- [ ] ProGuard obfuscation verification
- [ ] Backup/restore data integrity
- [ ] PDF generation security (no data leaks)