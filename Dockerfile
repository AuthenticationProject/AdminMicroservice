# Usa un'immagine leggera di runtime Java (senza tutto il JDK di compilazione)
FROM eclipse-temurin:17-jre-alpine

# Imposta la cartella di lavoro interna
WORKDIR /app

# Copia il JAR generato da Maven dalla cartella target della CI dentro al container
COPY target/*.jar app.jar

# Espone la porta di Spring Boot
EXPOSE 8080

# Avvia l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]