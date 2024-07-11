# Utiliza una imagen base de Maven para compilar el proyecto
FROM maven:3.8.4-openjdk-17-slim AS build

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo pom.xml y los archivos de configuración
COPY pom.xml .

# Copia los archivos de la aplicación al directorio de trabajo
COPY src ./src

# Compila el proyecto utilizando Maven
RUN mvn clean package -DskipTests

# Utiliza una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR desde la etapa de compilación
COPY --from=build /app/target/cloud-gateway-0.0.1-SNAPSHOT.jar cloud-gateway.jar

# Expone el puerto 8080 para acceder al API Gateway
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "cloud-gateway.jar"]
