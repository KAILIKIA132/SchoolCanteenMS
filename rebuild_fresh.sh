#!/bin/bash

echo "=== Starting Complete Fresh Rebuild ==="

# Define paths
PROJECT_DIR="$(pwd)"
SRC_DIR="$PROJECT_DIR/src"
WEB_CONTENT_DIR="$PROJECT_DIR/WebContent"
CLASSES_DIR="$WEB_CONTENT_DIR/WEB-INF/classes"
LIB_DIR="$WEB_CONTENT_DIR/WEB-INF/lib"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
WAR_FILE="$PROJECT_DIR/SchoolCanteenMS_${TIMESTAMP}.war"

echo "Project Directory: $PROJECT_DIR"
echo "Target WAR File: $WAR_FILE"

# Clean all compiled classes completely
echo "Cleaning all compiled classes..."
rm -rf "$CLASSES_DIR/com"
rm -rf "$CLASSES_DIR"/*.properties
rm -rf "$CLASSES_DIR"/*.xml

# Verify clean state
echo "Classes directory after clean:"
ls -la "$CLASSES_DIR/"

# Create classes directory
echo "Creating classes directory..."
mkdir -p "$CLASSES_DIR"

# Compile Java source files
echo "Compiling Java source files..."
CLASSPATH="$LIB_DIR/*:$SRC_DIR"
find "$SRC_DIR" -name "*.java" > sources.txt
if [ -s sources.txt ]; then
    echo "Compiling $(wc -l < sources.txt) source files..."
    javac -source 8 -target 8 -encoding UTF-8 -cp "$CLASSPATH" -d "$CLASSES_DIR" @"sources.txt"
    
    if [ $? -eq 0 ]; then
        echo "✅ Compilation successful!"
    else
        echo "❌ Compilation failed!"
        rm sources.txt
        exit 1
    fi
else
    echo "❌ No source files found to compile!"
    exit 1
fi
rm sources.txt

# Copy resource files
echo "Copying resource files..."
cp "$SRC_DIR"/*.properties "$CLASSES_DIR/" 2>/dev/null || true
cp "$SRC_DIR"/*.xml "$CLASSES_DIR/" 2>/dev/null || true

# Verify classes were created
echo "Verifying compiled classes..."
if [ -d "$CLASSES_DIR/com/zk/action" ]; then
    echo "✅ Classes compiled successfully"
    echo "Action classes count: $(ls "$CLASSES_DIR/com/zk/action/" | wc -l)"
else
    echo "❌ Classes not found in expected location"
    exit 1
fi

# Create WAR file
echo "Creating fresh WAR file: $WAR_FILE"
cd "$WEB_CONTENT_DIR"
TEMP_WAR_DIR=$(mktemp -d)
echo "Temp directory: $TEMP_WAR_DIR"

# Copy all WebContent to temp directory
cp -r . "$TEMP_WAR_DIR/"
echo "Copied WebContent to temp directory"

# Copy compiled classes to temp directory
cp -r "$CLASSES_DIR" "$TEMP_WAR_DIR/WEB-INF/"
echo "Copied compiled classes to temp directory"

# Change to temp directory to create WAR
cd "$TEMP_WAR_DIR"

# Create WAR file
jar -cf "$WAR_FILE" .
if [ $? -eq 0 ]; then
    echo "✅ Fresh WAR file created successfully: $WAR_FILE"
    echo "Size: $(du -h "$WAR_FILE" | cut -f1)"
else
    echo "❌ Failed to create WAR file!"
    cd "$PROJECT_DIR"
    rm -rf "$TEMP_WAR_DIR"
    exit 1
fi

# Verify WAR contents
echo "Verifying WAR file contents..."
if jar -tf "$WAR_FILE" | grep -q "WEB-INF/classes/com/zk/action/LoginAction.class"; then
    echo "✅ LoginAction class found in WAR"
else
    echo "❌ LoginAction class NOT found in WAR"
fi

if jar -tf "$WAR_FILE" | grep -q "WEB-INF/classes/struts.xml"; then
    echo "✅ struts.xml found in WAR"
else
    echo "❌ struts.xml NOT found in WAR"
fi

if jar -tf "$WAR_FILE" | grep -q "WEB-INF/classes/config.xml"; then
    echo "✅ config.xml found in WAR"
else
    echo "❌ config.xml NOT found in WAR"
fi

# Cleanup
cd "$PROJECT_DIR"
rm -rf "$TEMP_WAR_DIR"

echo "=== Complete Fresh Rebuild Finished Successfully ==="
echo "New WAR file: $WAR_FILE"