FROM openjdk:8-jdk-alpine
RUN addgroup -S basf && adduser -S basf -G basf
USER basf:basf
COPY build/dependency/BOOT-INF/lib /app/lib
COPY build/dependency/META-INF /app/META-INF
COPY build/dependency/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","hello.Application"]