Certificats for server: server_keystore.keystore
1. Valid from 2014/8/1 to 2034/7/27. 20 yeas.
2. Put server_keystore.keystore in "${TOMCAT_HOME}\conf" directory.
3. Configure "${TOMCAT_HOME}\conf\server.xml".You can refer to the .\server.xml.

	<Connector URIEncoding="UTF-8" port="8089" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
               
<!-- Add the https connector configure to server.xml-->
<Connector URIEncoding="UTF-8" port="8092" protocol="org.apache.coyote.http11.Http11NioProtocol" 
		SSLEnabled="true" maxThreads="150" scheme="https"
		secure="true" clientAuth="false" sslProtocol="SSLv3" 
		ciphers="TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,TLS_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_256_CBC_SHA" 
		keystoreFile="./conf/server_keystore.keystore" keystorePass="123456" 
		truststoreFile="./conf/server_keystore.keystore" truststorePass="123456" /> 