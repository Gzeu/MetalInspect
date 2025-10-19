#!/bin/bash

# MetalInspect Release Build Script
# Builds signed release APK/AAB with environment variable configuration

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

print_header() {
    echo -e "${BLUE}"
    echo "🚀 MetalInspect Release Build"
    echo "============================="
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
BUILD_TYPE="apk"
CLEAN_BUILD=true
VERBOSE=false
SKIP_LINT=false
SKIP_TESTS=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --aab|--bundle)
            BUILD_TYPE="aab"
            shift
            ;;
        --apk)
            BUILD_TYPE="apk"
            shift
            ;;
        --no-clean)
            CLEAN_BUILD=false
            shift
            ;;
        --verbose|-v)
            VERBOSE=true
            shift
            ;;
        --skip-lint)
            SKIP_LINT=true
            shift
            ;;
        --skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        --help|-h)
            echo "Usage: $0 [options]"
            echo "Options:"
            echo "  --apk            Build APK (default)"
            echo "  --aab, --bundle  Build Android App Bundle"
            echo "  --no-clean       Skip clean build"
            echo "  --verbose, -v    Verbose output"
            echo "  --skip-lint      Skip lint checks"
            echo "  --skip-tests     Skip unit tests"
            echo "  --help, -h       Show this help message"
            echo ""
            echo "Environment Variables (for signing):"
            echo "  KEYSTORE_FILE    Path to keystore file"
            echo "  KEYSTORE_PASSWORD Keystore password"
            echo "  KEY_ALIAS        Key alias"
            echo "  KEY_PASSWORD     Key password"
            echo ""
            echo "Example:"
            echo "  export KEYSTORE_FILE=../keystore/release.jks"
            echo "  export KEYSTORE_PASSWORD=mypassword"
            echo "  export KEY_ALIAS=mykey"
            echo "  export KEY_PASSWORD=mykeypassword"
            echo "  ./scripts/assemble-release.sh --aab"
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

# Check signing configuration
check_signing_config() {
    local missing_vars=()
    
    [[ -z "$KEYSTORE_FILE" ]] && missing_vars+=("KEYSTORE_FILE")
    [[ -z "$KEYSTORE_PASSWORD" ]] && missing_vars+=("KEYSTORE_PASSWORD")
    [[ -z "$KEY_ALIAS" ]] && missing_vars+=("KEY_ALIAS")
    [[ -z "$KEY_PASSWORD" ]] && missing_vars+=("KEY_PASSWORD")
    
    if [[ ${#missing_vars[@]} -gt 0 ]]; then
        print_warning "Signing environment variables not set: ${missing_vars[*]}"
        print_info "Release will be built unsigned (for testing only)"
        return 1
    fi
    
    if [[ ! -f "$KEYSTORE_FILE" ]]; then
        print_error "Keystore file not found: $KEYSTORE_FILE"
        return 1
    fi
    
    print_success "Signing configuration verified"
    return 0
}

# Check version information
check_version() {
    local version_name=$(grep 'versionName' app/build.gradle | head -1 | sed 's/.*"\(.*\)".*/\1/')
    local version_code=$(grep 'versionCode' app/build.gradle | head -1 | sed 's/.*versionCode \([0-9]*\).*/\1/')
    
    print_info "Building version: $version_name ($version_code)"
    
    # Check if this is a development version
    if [[ "$version_name" == *"-dev"* || "$version_name" == *"-SNAPSHOT"* ]]; then
        print_warning "Building development version: $version_name"
        print_info "Consider updating version for production release"
    fi
}

# Pre-build checks
SIGNED_BUILD=false
if check_signing_config; then
    SIGNED_BUILD=true
fi

check_version

# Clean build if requested
if [[ "$CLEAN_BUILD" == "true" ]]; then
    print_info "Cleaning build cache..."
    ./gradlew clean --quiet
    print_success "Build cache cleaned"
fi

# Run unit tests unless skipped
if [[ "$SKIP_TESTS" == "false" ]]; then
    print_info "Running unit tests..."
    ./gradlew test --quiet
    if [[ $? -eq 0 ]]; then
        print_success "Unit tests passed"
    else
        print_error "Unit tests failed"
        print_info "Use --skip-tests to bypass test failures"
        exit 1
    fi
else
    print_warning "Skipping unit tests"
fi

# Run lint checks unless skipped
if [[ "$SKIP_LINT" == "false" ]]; then
    print_info "Running lint checks..."
    ./gradlew lintRelease --quiet
    if [[ $? -eq 0 ]]; then
        print_success "Lint checks passed"
    else
        print_warning "Lint checks found issues"
        print_info "Check app/build/reports/lint-results-release.html for details"
        print_info "Use --skip-lint to bypass lint issues"
    fi
else
    print_warning "Skipping lint checks"
fi

# Prepare Gradle command
if [[ "$BUILD_TYPE" == "aab" ]]; then
    GRADLE_CMD="./gradlew bundleRelease"
    OUTPUT_NAME="Android App Bundle (AAB)"
else
    GRADLE_CMD="./gradlew assembleRelease"
    OUTPUT_NAME="APK"
fi

if [[ "$VERBOSE" == "false" ]]; then
    GRADLE_CMD="$GRADLE_CMD --quiet"
fi

print_info "Building release $OUTPUT_NAME..."
if [[ "$SIGNED_BUILD" == "true" ]]; then
    print_info "Build will be signed with provided keystore"
else
    print_warning "Build will be unsigned (debug signed)"
fi

echo ""

# Build the release
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
        printf "\r${BLUE}${spin:$i:1} Building release...${NC}"
        sleep 0.3
    done
    
    wait $PID
    BUILD_RESULT=$?
    printf "\r                    \r" # Clear the spinner
fi

end_time=$(date +%s)
execution_time=$((end_time - start_time))

# Check build results
if [[ ${BUILD_RESULT:-$?} -eq 0 ]]; then
    echo ""
    print_success "Release build completed! 🎉"
    print_info "Build time: ${execution_time}s"
    
    # Show output file information
    if [[ "$BUILD_TYPE" == "aab" ]]; then
        OUTPUT_FILE="app/build/outputs/bundle/release/app-release.aab"
    else
        OUTPUT_FILE="app/build/outputs/apk/release/app-release.apk"
    fi
    
    if [[ -f "$OUTPUT_FILE" ]]; then
        FILE_SIZE=$(du -h "$OUTPUT_FILE" | cut -f1)
        print_success "Output: $OUTPUT_FILE ($FILE_SIZE)"
        
        # Show signing information
        if [[ "$BUILD_TYPE" == "apk" ]]; then
            if command -v aapt >/dev/null 2>&1; then
                APK_INFO=$(aapt dump badging "$OUTPUT_FILE" 2>/dev/null | head -5)
                if [[ -n "$APK_INFO" ]]; then
                    print_info "APK Information:"
                    echo "$APK_INFO" | while read line; do
                        echo "    $line"
                    done
                fi
            fi
        fi
        
        echo ""
        print_info "Next steps:"
        if [[ "$SIGNED_BUILD" == "true" ]]; then
            echo "  • Upload to Google Play Console or distribute directly"
            echo "  • Test on physical devices before release"
        else
            echo "  • Sign the $OUTPUT_NAME with a release keystore"
            echo "  • Set up signing environment variables for production"
        fi
        echo "  • Run final QA testing on target devices"
        echo "  • Update CHANGELOG.md with release notes"
        
    else
        print_error "Output file not found: $OUTPUT_FILE"
        exit 1
    fi
    
else
    echo ""
    print_error "Release build failed!"
    print_info "Build time: ${execution_time}s"
    
    echo ""
    echo "Common solutions:"
    echo "  1. Run with --verbose flag for detailed output"
    echo "  2. Check signing configuration if using custom keystore"
    echo "  3. Verify all dependencies are available"
    echo "  4. Run ./gradlew assembleRelease --info for full output"
    echo "  5. Check for ProGuard/R8 obfuscation issues"
    
    exit 1
fi