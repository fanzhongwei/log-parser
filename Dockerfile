FROM anapsix/alpine-java:8u202b08_jdk

MAINTAINER fanzhongwei fanzhongwei@thunisoft.com

ENV ELASTICSEARCH=http://localhost:9200 \
    Xms=1024m \
    Xmx=2048m

LABEL acloud.description="处理filbeat发送到ES的日志" \
      acloud.env.ELASTICSEARCH="http://localhost:9200" \
      acloud.env.Xms="1024m" \
      acloud.env.Xmx="2048m"

EXPOSE 8080

WORKDIR /opt

COPY ./target/log-parser.jar log-parser.jar

RUN pwd &&\
    chmod 777 ./log-parser.jar

ENTRYPOINT java -jar -Xms$Xms -Xmx$Xmx -Delasticsearch=$ELASTICSEARCH log-parser.jar
