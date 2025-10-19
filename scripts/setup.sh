#!/bin/bash

# MetalInspect Developer Setup Script
# This script checks prerequisites and sets up the development environment

set -e

echo "🚀 MetalInspect Developer Setup"
echo "==============================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Helper functions
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

# Check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check Java version
check_java() {
    echo "Checking Java installation..."
    
    if command_exists java; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1-2)
        MAJOR_VERSION=$(echo $JAVA_VERSION | cut -d'.' -f1)
        
        # Handle Java 9+ versioning (e.g., "11.0.1" -> "11")
        if [[ $MAJOR_VERSION -eq 1 ]]; then
            MAJOR_VERSION=$(echo $JAVA_VERSION | cut -d'.' -f2)
        fi
        
        if [[ $MAJOR_VERSION -ge 11 ]]; then
            print_success "Java $JAVA_VERSION detected"
        else
            print_error "Java 11+ required, found Java $JAVA_VERSION"
            echo "Please install Java 11 or higher (recommended: Java 17)"
            exit 1
        fi
    else
        print_error "Java not found"
        echo "Please install Java 11+ (recommended: Java 17)"
        echo "Download from: https://adoptium.net/"
        exit 1
    fi
}

# Check Android SDK
check_android_sdk() {
    echo "Checking Android SDK..."
    
    if [[ -n "$ANDROID_HOME" ]] && [[ -d "$ANDROID_HOME" ]]; then
        print_success "Android SDK found at: $ANDROID_HOME"
        
        # Check for required SDK components
        SDK_MANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager"
        if [[ -f "$SDK_MANAGER" ]]; then
            print_success "SDK Manager found"
        else
            print_warning "SDK Manager not found - install Android Command Line Tools"
        fi
    else
        print_error "ANDROID_HOME not set or directory doesn't exist"
        echo "Please install Android SDK and set ANDROID_HOME environment variable"
        echo "Download from: https://developer.android.com/studio"
        exit 1
    fi
}

# Check Gradle
check_gradle() {
    echo "Checking Gradle..."
    
    if [[ -f "./gradlew" ]]; then
        print_success "Gradle Wrapper found"
        echo "Testing Gradle..."
        ./gradlew --version > /dev/null 2>&1
        if [[ $? -eq 0 ]]; then
            print_success "Gradle working correctly"
        else
            print_error "Gradle test failed"
            exit 1
        fi
    else
        print_error "Gradle Wrapper not found"
        echo "Make sure you're in the project root directory"
        exit 1
    fi
}

# Check Git
check_git() {
    echo "Checking Git..."
    
    if command_exists git; then
        GIT_VERSION=$(git --version | cut -d' ' -f3)
        print_success "Git $GIT_VERSION detected"
        
        # Check if we're in a git repository
        if git rev-parse --git-dir > /dev/null 2>&1; then
            print_success "Git repository detected"
        else
            print_warning "Not in a git repository"
        fi
    else
        print_error "Git not found"
        echo "Please install Git"
        exit 1
    fi
}

# Setup Gradle cache optimization
setup_gradle_cache() {
    echo "Setting up Gradle cache optimization..."
    
    GRADLE_PROPERTIES_FILE="$HOME/.gradle/gradle.properties"
    
    # Create .gradle directory if it doesn't exist
    mkdir -p "$HOME/.gradle"
    
    # Add optimization properties if they don't exist
    if [[ ! -f "$GRADLE_PROPERTIES_FILE" ]]; then
        cat > "$GRADLE_PROPERTIES_FILE" << EOF
# Gradle optimization properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.configureondemand=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx4g -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8

# Android optimization
android.useAndroidX=true
android.enableJetifier=true
EOF
        print_success "Created optimized ~/.gradle/gradle.properties"
    else
        print_info "Gradle properties file already exists"
    fi
}

# Warm up Gradle cache
warmup_gradle_cache() {
    echo "Warming up Gradle cache (this may take a few minutes)..."
    
    ./gradlew dependencies --quiet > /dev/null 2>&1 &
    PID=$!
    
    # Show spinner while warming up
    spin='-\|/'
    i=0
    while kill -0 $PID 2>/dev/null; do
        i=$(( (i+1) %4 ))
        printf "\r${spin:$i:1} Downloading dependencies..."
        sleep 0.1
    done
    
    wait $PID
    if [[ $? -eq 0 ]]; then
        print_success "Gradle cache warmed up successfully"
    else
        print_warning "Gradle cache warmup completed with warnings"
    fi
}

# Create local.properties if needed
setup_local_properties() {
    echo "Setting up local.properties..."
    
    if [[ ! -f "local.properties" ]]; then
        if [[ -n "$ANDROID_HOME" ]]; then
            echo "sdk.dir=$ANDROID_HOME" > local.properties
            print_success "Created local.properties with Android SDK path"
        else
            print_warning "ANDROID_HOME not set, skipping local.properties creation"
        fi
    else
        print_info "local.properties already exists"
    fi
}

# Verify project structure
verify_project_structure() {
    echo "Verifying project structure..."
    
    REQUIRED_FILES=(
        "app/build.gradle"
        "app/src/main/AndroidManifest.xml"
        "app/src/main/java/com/metalinspect/app/MetalInspectApplication.kt"
    )
    
    for file in "${REQUIRED_FILES[@]}"; do
        if [[ -f "$file" ]]; then
            print_success "$file found"
        else
            print_error "$file missing"
            exit 1
        fi
    done
}

# Run a test build
test_build() {
    echo "Testing debug build..."
    
    print_info "Running assembleDebug (this may take several minutes on first run)..."
    ./gradlew assembleDebug --quiet
    
    if [[ $? -eq 0 ]]; then
        print_success "Debug build successful! 🎉"
        
        # Show APK location
        APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
        if [[ -f "$APK_PATH" ]]; then
            APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
            print_info "Debug APK created: $APK_PATH ($APK_SIZE)"
        fi
    else
        print_error "Debug build failed"
        echo "Check the error messages above and run './gradlew assembleDebug' for detailed output"
        exit 1
    fi
}

# Show next steps
show_next_steps() {
    echo ""
    echo "🎉 Setup Complete!"
    echo "=================="
    echo ""
    echo "Next steps:"
    echo "1. Import project in Android Studio"
    echo "2. Run unit tests: ./scripts/run-unit.sh"
    echo "3. Run instrumented tests: ./scripts/run-instrumented.sh"
    echo "4. Start developing! Check CONTRIBUTING.md for guidelines"
    echo ""
    echo "Useful commands:"
    echo "• ./gradlew assembleDebug - Build debug APK"
    echo "• ./gradlew test - Run unit tests"
    echo "• ./gradlew lint - Run code analysis"
    echo "• ./gradlew clean - Clean build cache"
    echo ""
    echo "Need help? Check docs/ directory or create an issue on GitHub"
}

# Main execution
main() {
    echo "Starting development environment setup..."
    echo ""
    
    check_java
    check_android_sdk
    check_gradle
    check_git
    setup_gradle_cache
    setup_local_properties
    verify_project_structure
    warmup_gradle_cache
    test_build
    show_next_steps
}

# Run main function
main