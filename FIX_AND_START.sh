#!/bin/bash
cd /Users/aaron/pushdemoNew
rm -f WebContent/WEB-INF/lib/mysql-connector-java*.jar
docker-compose build tomcat
docker-compose up -d
sleep 30
docker-compose ps
echo ""
echo "Application: http://localhost:8080/pushdemo"



