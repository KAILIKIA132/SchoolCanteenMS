#!/bin/bash

echo "=========================================="
echo "   Building pushdemo.war"
echo "=========================================="

# Define paths
SRC_DIR="src"
BUILD_DIR="build_temp"
WEB_CONTENT="WebContent"
WAR_FILE="pushdemo.war"
LIB_DIR="$WEB_CONTENT/WEB-INF/lib"
CLASSES_DIR="$WEB_CONTENT/WEB-INF/classes"

# Clean previous build
echo "Cleaning up..."
rm -rf "$BUILD_DIR"
rm -f "$WAR_FILE"
mkdir -p "$BUILD_DIR/WEB-INF/classes"

echo "Copying WebContent..."
cp -r "$WEB_CONTENT/"* "$BUILD_DIR/"

echo "Compiling Java sources..."
# Compile all java files in src to build_dir/WEB-INF/classes
# We need to include the libraries in the classpath
CLASSPATH=".:$LIB_DIR/*"
find "$SRC_DIR" -name "*.java" > sources.txt

javac -d "$BUILD_DIR/WEB-INF/classes" -cp "$CLASSPATH" @sources.txt
if [ $? -ne 0 ]; then
    echo "Error: Compilation failed!"
    rm sources.txt
    exit 1
fi
rm sources.txt

echo "Copying resources (non-java files) from src..."
# Copy non-java resources (xml, properties, etc) to classes
rsync -av --exclude="*.java" "$SRC_DIR/" "$BUILD_DIR/WEB-INF/classes/"

echo "Creating WAR file..."
cd "$BUILD_DIR"
jar -cvf "../$WAR_FILE" * > /dev/null
cd ..

echo "Cleaning up temp files..."
rm -rf "$BUILD_DIR"

echo "=========================================="
echo "   Build Success: $WAR_FILE created!"
echo "=========================================="
