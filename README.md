# cloud_api_gw
Spring Cloud Gateway

Check this url if the gateway is running: 

http://localhost:8080/actuator/health




docker build -t cloud-gateway .
docker run -d -p 8080:8080 --name cloud-gateway cloud-gateway