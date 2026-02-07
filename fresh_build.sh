#!/bin/bash
# Fresh build script for School Canteen Management System

echo "Starting fresh application build..."

# Clean existing classes
echo "Cleaning compiled classes..."
rm -rf WebContent/WEB-INF/classes/com

# Create fresh classes directory
mkdir -p WebContent/WEB-INF/classes

# Compile all Java files
echo "Compiling Java source files..."
find src -name "*.java" > java_files.txt
javac -source 8 -target 8 -encoding UTF-8 -cp "WebContent/WEB-INF/lib/*:src" -d WebContent/WEB-INF/classes @java_files.txt
rm java_files.txt

# Copy configuration files
echo "Copying configuration files..."
cp src/*.properties WebContent/WEB-INF/classes/ 2>/dev/null || true
cp src/*.xml WebContent/WEB-INF/classes/ 2>/dev/null || true

# Create fresh WAR file
echo "Creating fresh WAR file..."
cd WebContent
jar -cf ../FreshBuild.war .
cd ..

# Verify the build
echo "Build completed!"
echo "WAR file: FreshBuild.war"
echo "Size: $(du -h FreshBuild.war | cut -f1)"

# Verify key components are included
echo "Verifying WAR contents:"
jar -tf FreshBuild.war | grep -E "(LoginAction|struts|config)" | head -5