# Contributing to MetalInspect

We welcome contributions to MetalInspect! This guide will help you get started with contributing to the project.

## 🚀 Quick Start

### Prerequisites
- **Java 11+** (recommended: Java 17)
- **Android SDK** with API 21-33
- **Git** for version control
- **Android Studio** (recommended) or command line tools

### Setup
```bash
# 1. Fork and clone the repository
git clone https://github.com/yourusername/MetalInspect.git
cd MetalInspect

# 2. Run the setup script
chmod +x scripts/setup.sh
./scripts/setup.sh

# 3. Open in Android Studio or start developing
```

## 📋 Development Workflow

### 1. Create a Feature Branch
```bash
# Create and switch to a new branch
git checkout -b feature/your-feature-name

# Or for bug fixes
git checkout -b fix/issue-description
```

### 2. Make Your Changes
- Follow the existing code style and architecture patterns
- Add tests for new functionality
- Update documentation as needed
- Test your changes thoroughly

### 3. Run Tests and Checks
```bash
# Run unit tests
./scripts/run-unit.sh --coverage

# Run instrumented tests (requires connected device)
./scripts/run-instrumented.sh

# Run lint checks
./gradlew lint

# Build debug to ensure compilation
./gradlew assembleDebug
```

### 4. Commit Your Changes
```bash
# Stage your changes
git add .

# Commit with a descriptive message
git commit -m "feat: add photo annotation feature

- Add annotation tools for defect marking
- Implement touch-based drawing interface
- Add undo/redo functionality
- Update PDF generation to include annotations

Resolves #123"
```

### 5. Push and Create Pull Request
```bash
# Push your branch
git push origin feature/your-feature-name

# Create a pull request on GitHub
# - Use the provided PR template
# - Link related issues
# - Add reviewers if known
```

## 🎯 Commit Message Format

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### Types
- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, semicolons, etc.)
- **refactor**: Code refactoring
- **perf**: Performance improvements
- **test**: Adding or modifying tests
- **chore**: Build process, dependencies, tooling

### Examples
```bash
# Feature
git commit -m "feat(camera): add manual focus controls"

# Bug fix
git commit -m "fix(pdf): resolve memory leak in report generation"

# Documentation
git commit -m "docs: update API documentation for new endpoints"

# Breaking change
git commit -m "feat!: change inspection status enum values

BREAKING CHANGE: InspectionStatus.IN_PROGRESS renamed to ACTIVE"
```

## 🏗️ Code Style Guidelines

### Kotlin Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use meaningful variable and function names

### Architecture Guidelines
- Follow Clean Architecture principles
- Use MVVM pattern for presentation layer
- Keep business logic in Use Cases
- Use Repository pattern for data access

### Naming Conventions
```kotlin
// Classes: PascalCase
class InspectionRepository
interface PhotoManager

// Functions and variables: camelCase
fun createInspection()
val inspectionId = "123"

// Constants: SCREAMING_SNAKE_CASE
const val MAX_PHOTO_SIZE = 1920

// Resources: snake_case
res/layout/fragment_inspection_detail.xml
res/string/error_network_unavailable
```

### Documentation
- Add KDoc comments for public APIs
- Document complex business logic
- Keep README and docs/ up to date

```kotlin
/**
 * Creates a new inspection with the provided details.
 * 
 * @param inspection The inspection data to create
 * @return Result containing the created inspection ID or error
 * @throws ValidationException if inspection data is invalid
 */
suspend fun createInspection(inspection: Inspection): Result<String>
```

## 🧪 Testing Guidelines

### Unit Tests
- Write tests for all business logic
- Mock external dependencies
- Aim for 80%+ coverage
- Place in `src/test/`

```kotlin
@Test
fun `createInspection with valid data returns success`() = runTest {
    // Given
    val inspection = createValidInspection()
    
    // When
    val result = useCase.createInspection(inspection)
    
    // Then
    assertTrue(result.isSuccess)
    assertEquals("inspection-id", result.getOrNull())
}
```

### Instrumented Tests
- Test UI interactions and database operations
- Test on minimum API 21 device
- Place in `src/androidTest/`

```kotlin
@Test
fun createInspection_savesToDatabase() {
    // Given
    val inspection = createTestInspection()
    
    // When
    val id = runBlocking { repository.createInspection(inspection) }
    
    // Then
    val saved = runBlocking { repository.getInspectionById(id) }
    assertEquals(inspection.lotNumber, saved?.lotNumber)
}
```

## 🐛 Bug Reports

When reporting bugs, please include:

1. **Device information**: Model, Android version, API level
2. **App version**: Check About screen or build.gradle
3. **Steps to reproduce**: Detailed step-by-step instructions
4. **Expected vs actual behavior**: What should happen vs what happens
5. **Screenshots/videos**: If applicable
6. **Logs**: Relevant error logs if available

Use our [bug report template](.github/ISSUE_TEMPLATE/bug_report.md).

## ✨ Feature Requests

For feature requests, please:

1. **Check existing issues**: Avoid duplicates
2. **Describe the use case**: Why is this feature needed?
3. **Provide context**: Industry workflows, user scenarios
4. **Consider alternatives**: What other solutions exist?
5. **Add mockups**: Visual representations help

Use our [feature request template](.github/ISSUE_TEMPLATE/feature_request.md).

## 📱 Device Testing

### Minimum Requirements
- **API Level**: 21 (Android 5.0)
- **RAM**: 1GB minimum, 2GB recommended
- **Storage**: 100MB free space
- **Camera**: Rear camera with autofocus

### Recommended Testing Matrix
- **Entry-level**: API 21-23, 1GB RAM
- **Mid-range**: API 26-28, 2-4GB RAM  
- **Modern**: API 29-33, 4GB+ RAM
- **Tablets**: 7"+ screens, various resolutions

## 🔍 Code Review Process

### Pull Request Requirements
- [ ] All tests pass locally
- [ ] Code follows style guidelines
- [ ] Documentation updated if needed
- [ ] No breaking changes (or properly documented)
- [ ] Linked to related issues

### Review Criteria
1. **Functionality**: Does it work as intended?
2. **Code quality**: Is it readable and maintainable?
3. **Performance**: Any negative impact?
4. **Security**: Are there any security concerns?
5. **Testing**: Adequate test coverage?

### Review Timeline
- **Simple fixes**: 1-2 business days
- **Features**: 3-5 business days
- **Major changes**: 1+ weeks

## 🏷️ Release Process

### Version Numbering
- Follow [Semantic Versioning](https://semver.org/)
- Format: `MAJOR.MINOR.PATCH`
- Example: `1.2.3`

### Release Criteria
- All tests passing
- Documentation updated
- CHANGELOG.md updated
- No critical bugs
- Performance benchmarks met

## 🤝 Community Guidelines

### Code of Conduct
- Be respectful and inclusive
- Focus on constructive feedback
- Help others learn and grow
- Follow GitHub's Community Guidelines

### Getting Help
- **Documentation**: Check docs/ directory first
- **Issues**: Search existing issues before creating new ones
- **Discussions**: Use GitHub Discussions for questions
- **Email**: Contact maintainers for private matters

### Recognition
Contributors will be:
- Listed in CONTRIBUTORS.md
- Mentioned in release notes
- Credited in commit history
- Invited to become maintainers (for significant contributions)

## 📚 Additional Resources

- [Android Development Guide](https://developer.android.com/guide)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Material Design Guidelines](https://material.io/design)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVVM Pattern](https://developer.android.com/jetpack/guide)

---

**Thank you for contributing to MetalInspect!** 🎉

Your contributions help make industrial inspections more efficient and reliable.