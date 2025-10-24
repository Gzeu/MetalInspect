# Security Policy for MetalInspect

This document outlines production security controls and practices.

- Data-at-rest: Encrypted Room DB via SQLCipher in release builds
- Key management: Derive passphrase at runtime from Android Keystore or user secret; never hardcode
- Logging: No sensitive data in release; custom Timber ReleaseTree
- Backups: Disabled via android:allowBackup="false"
- Screenshots: Disabled on sensitive screens via FLAG_SECURE (release flavors)
- Storage: App-scoped; FileProvider URIs only; no world-readable files
- Network: Cleartext disabled; consider cert pinning when backend exists
- ProGuard/R8: Obfuscation enabled; rules for Room, Hilt, Glide, iText
- Input validation: All forms validated; lengths and types enforced
