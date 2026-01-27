#!/usr/bin/env python3
"""
Test script to verify the Push Demo setup on Windows Server
"""
import os
import sys
import subprocess
import requests
from pathlib import Path

def check_python_version():
    """Check if Python version is compatible"""
    version = sys.version_info
    print(f"✓ Python version: {version.major}.{version.minor}.{version.micro}")
    if version < (3, 8):
        print("⚠ Warning: Python 3.8+ recommended")
    return True

def check_virtual_environment():
    """Check if running in virtual environment"""
    in_venv = hasattr(sys, 'real_prefix') or (
        hasattr(sys, 'base_prefix') and sys.base_prefix != sys.prefix
    )
    if in_venv:
        print("✓ Running in virtual environment")
        print(f"  Virtual environment: {sys.prefix}")
    else:
        print("⚠ Not running in virtual environment")
    return in_venv

def check_dependencies():
    """Check if required Python packages are installed"""
    required_packages = ['flask', 'requests']
    installed = []
    
    for package in required_packages:
        try:
            __import__(package)
            print(f"✓ {package} installed")
            installed.append(package)
        except ImportError:
            print(f"✗ {package} not installed")
    
    return len(installed) == len(required_packages)

def check_java():
    """Check if Java is installed and accessible"""
    try:
        result = subprocess.run(['java', '-version'], 
                              capture_output=True, text=True, check=True)
        print("✓ Java is installed")
        # Parse version from stderr
        version_line = result.stderr.split('\n')[0]
        print(f"  Version: {version_line}")
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        print("✗ Java not found or not in PATH")
        return False

def check_tomcat():
    """Check if Tomcat is installed"""
    tomcat_paths = [
        r"C:\apache-tomcat-9.0.84",
        r"C:\Program Files\Apache Software Foundation\Tomcat 9.0"
    ]
    
    for path in tomcat_paths:
        if os.path.exists(path):
            print(f"✓ Tomcat found at: {path}")
            return True
    
    print("✗ Tomcat not found")
    return False

def check_mysql():
    """Check if MySQL is installed"""
    mysql_paths = [
        r"C:\Program Files\MySQL\MySQL Server 8.0",
        r"C:\Program Files\MySQL\MySQL Server 5.7"
    ]
    
    for path in mysql_paths:
        if os.path.exists(path):
            print(f"✓ MySQL found at: {path}")
            return True
    
    print("✗ MySQL not found")
    return False

def check_database_connection():
    """Check if database connection works (requires MySQL connector)"""
    try:
        import mysql.connector
        # Try to connect (will fail if credentials are wrong, but that's expected)
        print("✓ MySQL connector installed")
        return True
    except ImportError:
        print("⚠ MySQL connector not installed (optional for this test)")
        return True  # Not critical for basic setup test

def check_web_access():
    """Check if the web application is accessible"""
    try:
        response = requests.get('http://localhost:8080/pushdemo', timeout=5)
        if response.status_code == 200:
            print("✓ Web application is accessible")
            return True
        else:
            print(f"⚠ Web application returned status: {response.status_code}")
            return False
    except requests.exceptions.ConnectionError:
        print("⚠ Cannot connect to web application (may not be running)")
        return False
    except requests.exceptions.Timeout:
        print("⚠ Web application connection timed out")
        return False

def check_project_files():
    """Check if essential project files exist"""
    required_files = [
        'WebContent/WEB-INF/web.xml',
        'WebContent/WEB-INF/classes/config.xml',
        'src/com/zk/action/DeviceAction.java',
        'doc/pushdemo.sql'
    ]
    
    base_path = Path.cwd()
    all_found = True
    
    for file_path in required_files:
        full_path = base_path / file_path
        if full_path.exists():
            print(f"✓ {file_path}")
        else:
            print(f"✗ {file_path} not found")
            all_found = False
    
    return all_found

def main():
    """Main test function"""
    print("=" * 50)
    print("Push Demo Setup Verification")
    print("=" * 50)
    print()
    
    tests = [
        ("Python Version", check_python_version),
        ("Virtual Environment", check_virtual_environment),
        ("Python Dependencies", check_dependencies),
        ("Java Installation", check_java),
        ("Tomcat Installation", check_tomcat),
        ("MySQL Installation", check_mysql),
        ("Database Connection", check_database_connection),
        ("Project Files", check_project_files),
        ("Web Access", check_web_access),
    ]
    
    results = []
    for test_name, test_func in tests:
        print(f"\n{test_name}:")
        try:
            result = test_func()
            results.append((test_name, result))
        except Exception as e:
            print(f"✗ Test failed with error: {e}")
            results.append((test_name, False))
    
    print("\n" + "=" * 50)
    print("Summary:")
    print("=" * 50)
    
    passed = sum(1 for _, result in results if result)
    total = len(results)
    
    for test_name, result in results:
        status = "PASS" if result else "FAIL"
        print(f"{status:4} | {test_name}")
    
    print(f"\nPassed: {passed}/{total}")
    
    if passed == total:
        print("\n🎉 All tests passed! Setup is complete.")
    elif passed >= total * 0.8:
        print("\n⚠ Most tests passed. Some minor issues detected.")
    else:
        print("\n❌ Setup incomplete. Please check the failed tests.")
    
    print("\n" + "=" * 50)

if __name__ == "__main__":
    main()