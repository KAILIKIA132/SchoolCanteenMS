#!/bin/bash

# Script to build the School Canteen Management System WAR file

echo "Building School Canteen Management System WAR file..."

# Define paths
PROJECT_DIR="$(pwd)"
SRC_DIR="$PROJECT_DIR/src"
WEB_CONTENT_DIR="$PROJECT_DIR/WebContent"
CLASSES_DIR="$WEB_CONTENT_DIR/WEB-INF/classes"
LIB_DIR="$WEB_CONTENT_DIR/WEB-INF/lib"
WAR_FILE="$PROJECT_DIR/SchoolCanteenMS.war"

# Clean previous compiled classes (optional)
echo "Cleaning previous compiled classes..."
rm -rf "$CLASSES_DIR"/com

# Create classes directory if it doesn't exist
mkdir -p "$CLASSES_DIR"

# Compile Java source files
echo "Compiling Java source files..."

# Build classpath with all JAR files
CLASSPATH="$LIB_DIR/*:$SRC_DIR"

# Find all Java files and compile them
find "$SRC_DIR" -name "*.java" > sources.txt
if [ -s sources.txt ]; then
    echo "Compiling $(wc -l < sources.txt) source files..."
    javac -source 8 -target 8 -encoding UTF-8 -cp "$CLASSPATH" -d "$CLASSES_DIR" @"sources.txt"
    
    if [ $? -eq 0 ]; then
        echo "Compilation successful!"
    else
        echo "Compilation failed!"
        rm sources.txt
        exit 1
    fi
else
    echo "No source files found to compile!"
fi

# Clean up temporary file
rm sources.txt

# Copy resource files (properties, xml) to classes directory
echo "Copying resource files..."
find "$SRC_DIR" -name "*.properties" -exec cp {} "$CLASSES_DIR"/ \; 2>/dev/null || true
find "$SRC_DIR" -name "*.xml" -exec cp {} "$CLASSES_DIR"/ \; 2>/dev/null || true

# Create WAR file
echo "Creating WAR file: $WAR_FILE"
cd "$WEB_CONTENT_DIR"

# Create temporary directory for WAR creation
TEMP_WAR_DIR=$(mktemp -d)

# Copy all WebContent to temp directory
cp -r . "$TEMP_WAR_DIR/"

# Copy compiled classes to temp directory
cp -r "$CLASSES_DIR" "$TEMP_WAR_DIR/WEB-INF/"

# Change to temp directory to create WAR with proper structure
cd "$TEMP_WAR_DIR"

# Create WAR file
jar -cf "$WAR_FILE" .

if [ $? -eq 0 ]; then
    echo "WAR file created successfully: $WAR_FILE"
    echo "Size: $(du -h "$WAR_FILE" | cut -f1)"
else
    echo "Failed to create WAR file!"
    cd "$PROJECT_DIR"
    rm -rf "$TEMP_WAR_DIR"
    exit 1
fi

# Cleanup
cd "$PROJECT_DIR"
rm -rf "$TEMP_WAR_DIR"

echo "Build completed successfully!"
echo "WAR file location: $WAR_FILE"