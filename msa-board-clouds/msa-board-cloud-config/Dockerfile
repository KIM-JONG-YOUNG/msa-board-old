FROM openjdk:8

COPY ./target/msa-board-cloud-config-0.0.1-SNAPSHOT.jar /app/application.jar

ENV TZ=Asia/Seoul
ENV LC_ALL=C.UTF-8

ENTRYPOINT [ "java", "-jar", "/app/application.jar"]
