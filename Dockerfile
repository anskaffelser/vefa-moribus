FROM maven:3.3.9-jdk-8 AS mvn

ADD . $MAVEN_HOME

RUN cd $MAVEN_HOME \
 && mvn -B clean package \
 && mv $MAVEN_HOME/moribus-publisher/target/$(ls $MAVEN_HOME/moribus-publisher/target | grep full$ | head -1)/vefa-moribus /vefa-moribus



FROM java:8-jre-alpine

COPY --from=mvn /vefa-moribus /vefa-moribus

VOLUME /src /target

WORKDIR /src
ENTRYPOINT ["sh", "/vefa-moribus/bin/run.sh"]
CMD ["-target", "/target"]