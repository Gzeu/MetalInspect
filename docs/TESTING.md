# Testing Guide

## Quick Start
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Test coverage report
./gradlew jacocoTestReport
```

## Test Structure

### Unit Tests (`test/`)
**Target Coverage: 80%+**
- **DAOs**: CRUD operations, queries, constraints
- **UseCases**: Business logic, validation, error handling
- **ViewModels**: State management, user interactions
- **Utils**: File operations, date formatting, validation
- **Repositories**: Data transformation, caching

### Instrumented Tests (`androidTest/`)
**Target Coverage: 60%+**
- **Database migrations**: Schema changes, data integrity
- **Camera integration**: Photo capture, file storage
- **PDF generation**: Report creation, content validation
- **UI workflows**: End-to-end user journeys

## Testing Framework

### Dependencies (Already configured)
```gradle
// Unit testing
testImplementation 'junit:junit:4.13.2'
testImplementation 'com.google.truth:truth:1.1.4'
testImplementation 'org.mockito.kotlin:mockito-kotlin:5.0.0'
testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
testImplementation 'androidx.arch.core:core-testing:2.2.0'

// Instrumented testing
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation 'androidx.room:room-testing:2.5.0'
```

## Test Examples

### Room DAO Testing
```kotlin
@Test
fun insertAndGetInspection() = runTest {
    val inspection = Inspection(/*...*/)
    dao.insertInspection(inspection)
    
    val retrieved = dao.getInspectionById(inspection.id)
    assertThat(retrieved).isEqualTo(inspection)
}
```

### UseCase Testing
```kotlin
@Test
fun createInspection_validData_returnsSuccess() = runTest {
    // Given
    val inspection = validInspection()
    
    // When
    val result = useCase(inspection)
    
    // Then
    assertThat(result.isSuccess).isTrue()
}
```

### ViewModel Testing
```kotlin
@Test
fun loadInspections_updatesStateFlow() = runTest {
    // Given
    val inspections = listOf(mockInspection())
    `when`(useCase.getAllInspections()).thenReturn(flowOf(inspections))
    
    // When
    viewModel.loadInspections()
    
    // Then
    assertThat(viewModel.inspections.value).isEqualTo(inspections)
}
```

## Test Data Builders
```kotlin
fun validInspection() = Inspection(
    id = "test-id",
    lotNumber = "LOT-TEST-001",
    productTypeId = "steel-sheet",
    quantity = 100.0,
    weight = 2500.0,
    unit = "kg",
    portLocation = "Test Port",
    weatherConditions = "Clear",
    inspectorId = "inspector-1",
    status = InspectionStatus.DRAFT,
    createdAt = System.currentTimeMillis()
)
```

## CI/CD Integration
```yaml
# GitHub Actions example
- name: Run Unit Tests
  run: ./gradlew test

- name: Run Instrumented Tests
  run: ./gradlew connectedAndroidTest

- name: Upload Test Results
  uses: actions/upload-artifact@v3
  with:
    name: test-results
    path: app/build/test-results/
```

## Performance Testing
- **Cold start time**: Measure from app launch to first UI
- **Database operations**: Batch insert/update performance
- **PDF generation**: Memory usage and processing time
- **Camera preview**: Frame rate and focus performance

## Quality Gates
- **Unit test coverage**: ≥80%
- **Instrumented test coverage**: ≥60%
- **Build time**: ≤2 minutes
- **APK size**: ≤50MB
- **Memory usage**: ≤256MB peak

## Manual Testing Checklist
### Core Flows
- [ ] Create inspector profile with signature
- [ ] Create new inspection (draft → in progress)
- [ ] Capture and organize photos
- [ ] Add defects with categorization
- [ ] Complete inspection and generate PDF
- [ ] Export data and create backup

### Edge Cases
- [ ] Low storage scenarios
- [ ] Camera permission denied
- [ ] Database corruption recovery
- [ ] Large photo handling (>5MB)
- [ ] Network connectivity changes

### Device Compatibility
- [ ] Android 5.0 (API 21) minimum
- [ ] Entry-level devices (1GB RAM)
- [ ] Different screen sizes and orientations
- [ ] Dark mode support

## Debugging Tools
- **Room Inspector**: Database content verification
- **Layout Inspector**: UI hierarchy debugging
- **LeakCanary**: Memory leak detection (debug builds)
- **StrictMode**: Threading and disk I/O monitoring

---

**Ready to test?** Run `./gradlew test connectedAndroidTest` to execute the full test suite.