FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="김홍태 소프트웍스"
LABEL description="Spring Boot User Rest API"

# Define build-time arguments
ARG APP_JAR=target/user_rest-0.0.1-SNAPSHOT.jar
#ARG TIMEZONE=Asia/Seoul

# Set environment variables
# ENV TZ=$TIMEZONE

# Set the working directory
# WORKDIR /app

# Install tzdata for timezone configuration and clean up
# RUN apk add --no-cache tzdata \
#     && cp /usr/share/zoneinfo/$TZ /etc/localtime \
#     && echo $TZ > /etc/timezone \
#     && apk del tzdata \
#     && addgroup -S spring && adduser -S spring -G spring \
#     && chown -R spring:spring /app

# Copy the jar file into the container
COPY ${APP_JAR} app.jar

# Expose the port the app runs on
EXPOSE 8080

# Switch to non-root user
# USER spring:spring

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
