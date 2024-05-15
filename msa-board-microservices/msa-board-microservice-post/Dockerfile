FROM openjdk:8

COPY ./target/msa-board-microservice-post-0.0.1-SNAPSHOT.jar /app/application.jar

ENV TZ=Asia/Seoul
ENV LC_ALL=C.UTF-8

ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-common-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-kafka-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-redis-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-persist-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-web-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-validation-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-transaction-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-core-feign-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-domain-post-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-client-post-0.0.1-SNAPSHOT.jar
ENV LOADER_PATH=${LOADER_PATH},/app/libs/msa-board-client-member-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-cp", "/app/application.jar", "-Dloader.path=${LOADER_PATH}", "org.springframework.boot.loader.PropertiesLauncher" ]
