# Sử dụng JDK 21 làm base image
FROM eclipse-temurin:21-jdk-alpine

# Tạo thư mục trong container
WORKDIR /app

# Copy file JAR vào container (giả sử tên là jfix-be-0.0.1-SNAPSHOT.jar)
COPY target/jfix-be-0.0.1-SNAPSHOT.jar app.jar

# Mở port 8080
EXPOSE 8080

# Lệnh khởi chạy
ENTRYPOINT ["java", "-jar", "app.jar"]
