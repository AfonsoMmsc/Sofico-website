# Usar a imagem do Maven para construir o projeto
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar arquivos do projeto e fazer o build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Usar a imagem do JDK para rodar a aplicação
FROM eclipse-temurin:17
WORKDIR /app

# Copiar o JAR gerado
COPY --from=build /app/target/sofico-backend-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta
EXPOSE 8080

# Comando para rodar o backend
ENTRYPOINT ["java", "-jar", "app.jar"]
