FROM java:8-jdk

WORKDIR /home/apps/
ADD target/worktime-tool-1.0-SNAPSHOT.jar .
ADD target/lib ./lib
ADD start.sh .

ENTRYPOINT ["sh", "/home/apps/start.sh"]