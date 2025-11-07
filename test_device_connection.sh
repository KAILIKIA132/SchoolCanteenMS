#!/bin/bash
echo "═══════════════════════════════════════════════════════════"
echo "🔍 Device Connection Diagnostic Tool"
echo "═══════════════════════════════════════════════════════════"
echo ""

echo "1. Checking server status..."
docker-compose ps | grep -E "(tomcat|mysql)"
echo ""

echo "2. Testing server endpoint..."
curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" http://localhost:8080/pushdemo/iclock/cdata
echo ""

echo "3. Checking for devices in database..."
docker-compose exec -T mysql mysql -u root -proot -e "USE pushdemo; SELECT COUNT(*) as device_count FROM device_info;" 2>/dev/null | grep -v "Warning"
echo ""

echo "4. Recent connection attempts (last 20 lines)..."
docker-compose logs tomcat 2>&1 | grep -i -E "(cdata|device|push)" | tail -5
echo ""

echo "5. Server IP address:"
ifconfig | grep "inet " | grep -v 127.0.0.1 | head -1 | awk '{print "   " $2}'
echo ""

echo "6. Port 8080 status:"
lsof -i :8080 2>/dev/null | head -2 || echo "   Port check requires sudo"
echo ""

echo "═══════════════════════════════════════════════════════════"
echo "✅ Diagnostic complete"
echo "═══════════════════════════════════════════════════════════"
