#!/usr/bin/env python3
"""
Simple HTTP proxy to forward requests from Docker container to API server
Forwards requests from localhost:8002 to 10.244.175.45:8001
"""
import http.server
import socketserver
import urllib.request
import urllib.parse
import json
import sys

API_TARGET = "http://10.244.175.45:8001"
PROXY_PORT = 8002

class ProxyHandler(http.server.BaseHTTPRequestHandler):
    def do_POST(self):
        try:
            # Read request body
            content_length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_length)
            
            # Forward request to API server
            target_url = API_TARGET + self.path
            req = urllib.request.Request(target_url, data=body, method='POST')
            
            # Copy headers
            for header, value in self.headers.items():
                if header.lower() not in ['host', 'connection', 'content-length']:
                    req.add_header(header, value)
            
            # Make request
            with urllib.request.urlopen(req, timeout=30) as response:
                # Send response back to client
                self.send_response(response.getcode())
                for header, value in response.headers.items():
                    if header.lower() not in ['connection', 'transfer-encoding']:
                        self.send_header(header, value)
                self.end_headers()
                self.wfile.write(response.read())
                
        except Exception as e:
            print(f"Error: {e}", file=sys.stderr)
            self.send_error(500, f"Proxy error: {str(e)}")
    
    def do_GET(self):
        self.do_POST()  # Handle GET same as POST for simplicity
    
    def log_message(self, format, *args):
        # Suppress default logging
        pass

if __name__ == "__main__":
    with socketserver.TCPServer(("", PROXY_PORT), ProxyHandler) as httpd:
        print(f"Proxy server running on port {PROXY_PORT}, forwarding to {API_TARGET}")
        print("Press Ctrl+C to stop")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nShutting down proxy server...")
            httpd.shutdown()



