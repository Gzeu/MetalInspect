#!/bin/bash

# MetalInspect Instrumented Test Runner
# Runs instrumented tests with device management and result reporting

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

print_header() {
    echo -e "${BLUE}"
    echo "📱 MetalInspect Instrumented Tests"
    echo "=================================="
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
CLEAN_BUILD=false
VERBOSE=false
DEVICE_ID=""
INSTALL_ONLY=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --clean)
            CLEAN_BUILD=true
            shift
            ;;
        --verbose|-v)
            VERBOSE=true
            shift
            ;;
        --device|-d)
            DEVICE_ID="$2"
            shift 2
            ;;
        --install-only|-i)
            INSTALL_ONLY=true
            shift
            ;;
        --help|-h)
            echo "Usage: $0 [options]"
            echo "Options:"
            echo "  --clean          Clean build before running tests"
            echo "  --verbose, -v    Verbose output"
            echo "  --device, -d ID  Use specific device (use 'adb devices' to list)"
            echo "  --install-only   Only install APKs without running tests"
            echo "  --help, -h       Show this help message"
            echo ""
            echo "Requirements:"
            echo "  • Android device/emulator connected via ADB"
            echo "  • USB debugging enabled"
            echo "  • Minimum API 21 (Android 5.0)"
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

# Check if ADB is available
if ! command -v adb >/dev/null 2>&1; then
    print_error "ADB not found in PATH"
    echo "Make sure Android SDK platform-tools are installed and in PATH"
    exit 1
fi

# Check connected devices
print_info "Checking connected devices..."
CONNECTED_DEVICES=$(adb devices | grep -v "List of devices" | grep -E "device$|emulator$" | wc -l)

if [[ $CONNECTED_DEVICES -eq 0 ]]; then
    print_error "No Android devices/emulators connected"
    echo ""
    echo "To connect a device:"
    echo "  1. Enable USB debugging on your Android device"
    echo "  2. Connect via USB cable"
    echo "  3. Accept debugging prompt on device"
    echo ""
    echo "To start an emulator:"
    echo "  1. Open Android Studio"
    echo "  2. Tools > AVD Manager"
    echo "  3. Start an existing AVD or create a new one"
    echo ""
    echo "Verify connection with: adb devices"
    exit 1
elif [[ $CONNECTED_DEVICES -eq 1 ]]; then
    DEVICE_INFO=$(adb devices | grep -E "device$|emulator$" | head -1)
    DEVICE_NAME=$(echo "$DEVICE_INFO" | cut -f1)
    print_success "Found device: $DEVICE_NAME"
else
    print_warning "Multiple devices connected ($CONNECTED_DEVICES)"
    adb devices
    
    if [[ -z "$DEVICE_ID" ]]; then
        print_info "Use --device <ID> to specify which device to use"
        DEVICE_NAME=$(adb devices | grep -E "device$|emulator$" | head -1 | cut -f1)
        print_info "Using first available device: $DEVICE_NAME"
    else
        DEVICE_NAME="$DEVICE_ID"
        print_info "Using specified device: $DEVICE_NAME"
    fi
fi

# Set ADB device if specified
if [[ -n "$DEVICE_ID" ]]; then
    export ANDROID_SERIAL="$DEVICE_ID"
fi

# Get device information
DEVICE_API=$(adb shell getprop ro.build.version.sdk 2>/dev/null | tr -d '\r')
DEVICE_MODEL=$(adb shell getprop ro.product.model 2>/dev/null | tr -d '\r')
DEVICE_MANUFACTURER=$(adb shell getprop ro.product.manufacturer 2>/dev/null | tr -d '\r')

print_info "Device: $DEVICE_MANUFACTURER $DEVICE_MODEL (API $DEVICE_API)"

# Check minimum API level
if [[ $DEVICE_API -lt 21 ]]; then
    print_error "Device API level $DEVICE_API is below minimum (API 21)"
    echo "Please use a device with Android 5.0 or higher"
    exit 1
fi

# Clean build if requested
if [[ "$CLEAN_BUILD" == "true" ]]; then
    print_info "Cleaning build cache..."
    ./gradlew clean --quiet
    print_success "Build cache cleaned"
fi

# Install APKs only if requested
if [[ "$INSTALL_ONLY" == "true" ]]; then
    print_info "Installing debug and test APKs..."
    ./gradlew installDebug installDebugAndroidTest --quiet
    
    if [[ $? -eq 0 ]]; then
        print_success "APKs installed successfully"
        print_info "You can now run tests from Android Studio or manually with ADB"
    else
        print_error "Failed to install APKs"
        exit 1
    fi
    exit 0
fi

# Prepare Gradle command
GRADLE_CMD="./gradlew connectedDebugAndroidTest"

if [[ "$VERBOSE" == "false" ]]; then
    GRADLE_CMD="$GRADLE_CMD --quiet"
fi

print_info "Running instrumented tests..."
print_info "Command: $GRADLE_CMD"
print_warning "This may take several minutes..."
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
        printf "\r${BLUE}${spin:$i:1} Running instrumented tests...${NC}"
        sleep 0.5
    done
    
    wait $PID
    TEST_RESULT=$?
    printf "\r                                  \r" # Clear the spinner
fi

end_time=$(date +%s)
execution_time=$((end_time - start_time))

# Check test results
if [[ ${TEST_RESULT:-$?} -eq 0 ]]; then
    echo ""
    print_success "All instrumented tests passed! 🎉"
    print_info "Execution time: ${execution_time}s"
    
    # Show test reports location
    TEST_REPORTS_DIR="app/build/reports/androidTests/connected"
    if [[ -d "$TEST_REPORTS_DIR" ]]; then
        print_info "Detailed report: $TEST_REPORTS_DIR/index.html"
    fi
    
else
    echo ""
    print_error "Instrumented tests failed!"
    print_info "Execution time: ${execution_time}s"
    
    # Show failure details
    TEST_REPORTS_DIR="app/build/reports/androidTests/connected"
    if [[ -d "$TEST_REPORTS_DIR" ]]; then
        print_info "Detailed report: $TEST_REPORTS_DIR/index.html"
    fi
    
    echo ""
    echo "Common solutions:"
    echo "  1. Run with --verbose flag for detailed output"
    echo "  2. Check device screen is unlocked and app permissions granted"
    echo "  3. Verify device has sufficient storage space"
    echo "  4. Check test reports in app/build/reports/androidTests/"
    echo "  5. Run ./gradlew connectedDebugAndroidTest --info for full output"
    
    exit 1
fi

# Show next steps
echo ""
echo "Next steps:"
echo "  • Run lint checks: ./gradlew lint"
echo "  • Build release APK: ./scripts/assemble-release.sh"
echo "  • View test reports in app/build/reports/androidTests/"