#!/bin/bash

# MetalInspect Unit Test Runner
# Runs unit tests with coverage reporting and result formatting

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

print_header() {
    echo -e "${BLUE}"
    echo "⚙️  MetalInspect Unit Tests"
    echo "========================="
    echo -e "${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Parse command line arguments
RUN_COVERAGE=false
CLEAN_BUILD=false
VERBOSE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --coverage|-c)
            RUN_COVERAGE=true
            shift
            ;;
        --clean)
            CLEAN_BUILD=true
            shift
            ;;
        --verbose|-v)
            VERBOSE=true
            shift
            ;;
        --help|-h)
            echo "Usage: $0 [options]"
            echo "Options:"
            echo "  --coverage, -c    Generate test coverage report"
            echo "  --clean          Clean build before running tests"
            echo "  --verbose, -v    Verbose output"
            echo "  --help, -h       Show this help message"
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

print_header

# Check if gradlew exists
if [[ ! -f "./gradlew" ]]; then
    print_error "Gradle wrapper not found. Make sure you're in the project root."
    exit 1
fi

# Clean build if requested
if [[ "$CLEAN_BUILD" == "true" ]]; then
    print_info "Cleaning build cache..."
    ./gradlew clean --quiet
    print_success "Build cache cleaned"
fi

# Prepare Gradle command
GRADLE_CMD="./gradlew test"

if [[ "$VERBOSE" == "false" ]]; then
    GRADLE_CMD="$GRADLE_CMD --quiet"
fi

if [[ "$RUN_COVERAGE" == "true" ]]; then
    GRADLE_CMD="$GRADLE_CMD jacocoTestReport"
fi

print_info "Running unit tests..."
print_info "Command: $GRADLE_CMD"
echo ""

# Run the tests
start_time=$(date +%s)

if [[ "$VERBOSE" == "true" ]]; then
    $GRADLE_CMD
else
    # Run with progress indicator for quiet mode
    $GRADLE_CMD &
    PID=$!
    
    spin='-\|/'
    i=0
    while kill -0 $PID 2>/dev/null; do
        i=$(( (i+1) %4 ))
        printf "\r${BLUE}${spin:$i:1} Running tests...${NC}"
        sleep 0.2
    done
    
    wait $PID
    TEST_RESULT=$?
    printf "\r                    \r" # Clear the spinner
fi

end_time=$(date +%s)
execution_time=$((end_time - start_time))

# Check test results
if [[ ${TEST_RESULT:-$?} -eq 0 ]]; then
    echo ""
    print_success "All unit tests passed! 🎉"
    print_info "Execution time: ${execution_time}s"
    
    # Show test summary
    TEST_RESULTS_DIR="app/build/test-results/testDebugUnitTest"
    if [[ -d "$TEST_RESULTS_DIR" ]]; then
        # Parse test results
        TOTAL_TESTS=0
        FAILED_TESTS=0
        SKIPPED_TESTS=0
        
        for xml_file in "$TEST_RESULTS_DIR"/*.xml; do
            if [[ -f "$xml_file" ]]; then
                # Extract test counts using basic text processing
                if command -v xmllint >/dev/null 2>&1; then
                    TESTS=$(xmllint --xpath "string(//testsuite/@tests)" "$xml_file" 2>/dev/null || echo "0")
                    FAILURES=$(xmllint --xpath "string(//testsuite/@failures)" "$xml_file" 2>/dev/null || echo "0")
                    SKIPPED=$(xmllint --xpath "string(//testsuite/@skipped)" "$xml_file" 2>/dev/null || echo "0")
                    
                    TOTAL_TESTS=$((TOTAL_TESTS + TESTS))
                    FAILED_TESTS=$((FAILED_TESTS + FAILURES))
                    SKIPPED_TESTS=$((SKIPPED_TESTS + SKIPPED))
                fi
            fi
        done
        
        if [[ $TOTAL_TESTS -gt 0 ]]; then
            PASSED_TESTS=$((TOTAL_TESTS - FAILED_TESTS - SKIPPED_TESTS))
            echo ""
            echo "Test Summary:"
            echo "  Total: $TOTAL_TESTS"
            echo "  Passed: $PASSED_TESTS"
            echo "  Failed: $FAILED_TESTS"
            echo "  Skipped: $SKIPPED_TESTS"
        fi
    fi
    
    # Show coverage report if generated
    if [[ "$RUN_COVERAGE" == "true" ]]; then
        COVERAGE_REPORT="app/build/reports/jacoco/jacocoTestReport/html/index.html"
        if [[ -f "$COVERAGE_REPORT" ]]; then
            print_success "Coverage report generated"
            print_info "Open: $COVERAGE_REPORT"
            
            # Try to extract coverage percentage
            if command -v grep >/dev/null 2>&1; then
                COVERAGE_PERCENT=$(grep -o 'Total[^>]*>[^>]*>[^>]*>[^0-9]*[0-9]*%' "$COVERAGE_REPORT" | grep -o '[0-9]*%' | head -1 2>/dev/null || echo "")
                if [[ -n "$COVERAGE_PERCENT" ]]; then
                    print_info "Total Coverage: $COVERAGE_PERCENT"
                fi
            fi
        fi
    fi
    
else
    echo ""
    print_error "Unit tests failed!"
    print_info "Execution time: ${execution_time}s"
    
    # Show failure details
    TEST_REPORTS_DIR="app/build/reports/tests/testDebugUnitTest"
    if [[ -d "$TEST_REPORTS_DIR" ]]; then
        print_info "Detailed report: $TEST_REPORTS_DIR/index.html"
    fi
    
    echo ""
    echo "Common solutions:"
    echo "  1. Run with --verbose flag for detailed output"
    echo "  2. Check test reports in app/build/reports/tests/"
    echo "  3. Run ./gradlew test --info for full Gradle output"
    echo "  4. Ensure all dependencies are properly mocked"
    
    exit 1
fi

# Show next steps
echo ""
echo "Next steps:"
echo "  • Run instrumented tests: ./scripts/run-instrumented.sh"
echo "  • Run lint checks: ./gradlew lint"
echo "  • Build debug APK: ./gradlew assembleDebug"

if [[ "$RUN_COVERAGE" == "false" ]]; then
    echo "  • Generate coverage: ./scripts/run-unit.sh --coverage"
fi