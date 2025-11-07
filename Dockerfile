FROM tomcat:9.0-jdk8

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# MySQL connector will be mounted from host via docker-compose volumes
# The JAR file is already in WebContent/WEB-INF/lib/ and will be available at runtime

# Set working directory
WORKDIR /usr/local/tomcat

# Expose port
EXPOSE 8080

# Start Tomcat
ENV CATALINA_OPTS="-Dorg.apache.catalina.startup.ContextConfig.jarsToSkip=* -Dtomcat.util.scan.StandardJarScanFilter.jarsToSkip=* -Dorg.apache.catalina.startup.ContextConfig.processAnnotations=false"
CMD ["catalina.sh", "run"]



