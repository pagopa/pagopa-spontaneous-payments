FROM adoptopenjdk/openjdk11:alpine-jre as builder

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE}  application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk/openjdk11:alpine-jre

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --chown=spring:spring  --from=builder dependencies/ ./
COPY --chown=spring:spring  --from=builder snapshot-dependencies/ ./
# https://github.com/moby/moby/issues/37965#issuecomment-426853382
RUN true
COPY --chown=spring:spring  --from=builder spring-boot-loader/ ./
COPY --chown=spring:spring  --from=builder application/ ./

EXPOSE 8080

COPY --chown=spring:spring  docker/run.sh ./run.sh
ENTRYPOINT ["./run.sh"]
