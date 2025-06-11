# 🧊 Base image nhẹ, chuẩn Java 17
FROM eclipse-temurin:17-jdk-alpine

# 📂 Tạo thư mục app bên trong container
WORKDIR /app

# 📦 Copy file JAR từ target vào container
COPY target/lakesidehotel-0.0.1-SNAPSHOT.jar app.jar

# 🚀 Command để chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
