FROM anapsix/alpine-java:8u202b08_jdk

MAINTAINER fanzhongwei fanzhongwei@thunisoft.com

EXPOSE 8080

WORKDIR /opt

COPY ./target/log-parser.jar log-parser.jar

RUN pwd &&\
    chmod 777 ./log-parser.jar

ENTRYPOINT ["java", "-jar", "log-parser.jar"]


