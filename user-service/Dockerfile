FROM openjdk:17-oracle
COPY gitlab.crt /usr/local/share/ca-certificates/gitlab.crt
RUN keytool -import -alias gitlab -keystore $JAVA_HOME/lib/security/cacerts -file /usr/local/share/ca-certificates/gitlab.crt -storepass changeit -noprompt
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Djavax.net.debug=ssl", "-jar", "/app.jar"]
