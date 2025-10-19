# CI/CD Pipeline Documentation

## Overview
MetalInspect uses GitHub Actions for continuous integration and deployment. The pipeline ensures code quality, runs comprehensive tests, and automates releases.

## Workflows

### 1. CI Workflow (`.github/workflows/ci.yml`)
**Triggers**: Push to `main`/`develop`, Pull Requests

**Jobs**:
- **Test Matrix**: Runs on API levels 21, 29, 33
- **Unit Tests**: JUnit tests with JaCoCo coverage
- **Instrumented Tests**: Android Emulator tests
- **Lint Checks**: Code quality validation
- **Security Scan**: Qodana static analysis
- **Build Verification**: Debug APK generation

**Coverage Requirements**:
- Overall: 70% minimum
- Class-level: 60% minimum
- Excludes: Generated files, test classes, Hilt components

### 2. Release Workflow (`.github/workflows/release.yml`)
**Triggers**: Git tag push (`v*.*.*`)

**Process**:
1. Extract version from tag
2. Update build.gradle with version
3. Run full test suite
4. Validate coverage thresholds
5. Build signed APK/AAB
6. Create GitHub Release with artifacts
7. Optional App Center distribution

### 3. Version Bump Workflow (`.github/workflows/version-bump.yml`)
**Triggers**: Manual workflow dispatch

**Options**:
- Version type: patch, minor, major
- Prerelease: Creates `-rc.1` suffix

**Process**:
1. Calculate new version from current
2. Update `app/build.gradle` version fields
3. Update `CHANGELOG.md` with new entry
4. Commit changes and push
5. Create and push version tag
6. Trigger release workflow

## Setup Guide

### 1. Repository Secrets
Configure these secrets in GitHub repository settings:

```bash
# Required for signed releases
KEYSTORE_FILE=<base64-encoded-keystore>
KEYSTORE_PASSWORD=<keystore-password>
KEY_ALIAS=<key-alias>
KEY_PASSWORD=<key-password>

# Optional integrations
QODANA_TOKEN=<jetbrains-qodana-token>
APPCENTER_API_TOKEN=<app-center-token>
APPCENTER_APP_NAME=<app-center-app-name>
```

### 2. Keystore Setup
```bash
# Generate release keystore
keytool -genkey -v -keystore release.jks -alias metalinspect -keyalg RSA -keysize 2048 -validity 10000

# Encode for GitHub secret
base64 -i release.jks | pbcopy  # macOS
base64 -w 0 release.jks         # Linux
```

### 3. Branch Protection Rules
Recommended settings for `main` branch:
- Require PR reviews before merging
- Require status checks to pass:
  - `test (21)` - API 21 tests
  - `test (29)` - API 29 tests  
  - `test (33)` - API 33 tests
  - `security` - Security scan
- Require branches to be up to date
- Restrict pushes to matching branches

### 4. Codecov Integration
Optional code coverage reporting:
1. Sign up at [codecov.io](https://codecov.io)
2. Install Codecov GitHub App
3. Coverage reports automatically uploaded

## Usage Examples

### Running Workflows Locally
```bash
# Simulate CI locally
./scripts/setup.sh
./scripts/run-unit.sh --coverage
./scripts/run-instrumented.sh
./gradlew lintDebug

# Build release locally
export KEYSTORE_FILE=release.jks
export KEYSTORE_PASSWORD=mypassword
export KEY_ALIAS=metalinspect
export KEY_PASSWORD=mypassword
./scripts/assemble-release.sh --aab
```

### Version Management
```bash
# Manual version bump (triggers via GitHub UI)
# Actions > Version Bump > Run workflow
# - Select version type (patch/minor/major)
# - Choose prerelease (optional)

# View version bump history
git tag --sort=-version:refname | head -10
```

### Release Process
```bash
# 1. Version bump (automated)
# GitHub UI > Actions > Version Bump > patch/minor/major

# 2. Release is automatically triggered by tag push
# 3. Artifacts available in GitHub Releases
# 4. Download and distribute as needed
```

## Quality Gates

### Pre-merge (CI)
- [ ] Unit tests pass (70%+ coverage)
- [ ] Instrumented tests pass on API 21, 29, 33
- [ ] Lint checks pass with no errors
- [ ] Security scan completes without critical issues
- [ ] Debug APK builds successfully

### Pre-release
- [ ] All CI checks pass
- [ ] Coverage thresholds met (70% overall, 60% class)
- [ ] Manual QA on physical devices
- [ ] CHANGELOG.md updated
- [ ] Version numbers updated

## Monitoring & Troubleshooting

### Common CI Failures

**Test Failures**:
- Check test reports in workflow artifacts
- Run tests locally: `./scripts/run-unit.sh --verbose`
- Database tests may fail if schema changes aren't migrated

**Coverage Failures**:
- Add more unit tests for uncovered code
- Review exclusions in JaCoCo configuration
- Check that test execution data is generated

**Build Failures**:
- Verify all dependencies are compatible
- Check ProGuard rules for release builds
- Ensure signing configuration is correct

**Emulator Issues**:
- AVD creation may timeout on slow runners
- Camera tests may fail on emulators without camera support
- Increase timeout for long-running tests

### Performance Optimization

**Gradle Caching**:
- Workflows cache `~/.gradle/caches` and `~/.gradle/wrapper`
- Local development uses optimized gradle.properties

**AVD Caching**:
- Emulator snapshots cached between runs
- Reduces test execution time significantly

**Parallel Execution**:
- Test matrix runs different API levels in parallel
- Coverage reports aggregated from all test runs

## Metrics & Reporting

### Build Times (Target vs Actual)
- CI Pipeline: ≤15 minutes (typical: 8-12 minutes)
- Unit Tests: ≤2 minutes (typical: 1 minute)
- Instrumented Tests: ≤10 minutes per API level
- Release Build: ≤5 minutes (typical: 3 minutes)

### Coverage Targets
- **Overall Coverage**: 70% minimum (current: ~75%)
- **Class Coverage**: 60% minimum (current: ~80%)
- **Critical Classes**: 90% minimum (UseCases, Repositories)

---

**Ready to contribute?** The CI/CD pipeline will automatically validate your changes and provide feedback on code quality and test coverage.