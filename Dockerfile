FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD worktime-tool-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 5555
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
