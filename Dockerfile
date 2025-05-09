#FROM maven:3-jdk-11 as builder
#WORKDIR /data
#COPY . .
#RUN mvn clean package -B -DskipTests
#
#
#FROM openjdk:11-jre
#ENV TZ Asia/Shanghai
#WORKDIR /data
##RUN apk add fontconfig && apk add --update ttf-dejavu && fc-cache --force
#COPY --from=builder /data/target/ROOT.jar .
#CMD ["java","-jar","-Djava.security.egd=file:/dev/./urandom","ROOT.jar"]

#FROM amazoncorretto:17-alpine
#COPY target/ROOT.jar /data/
#WORKDIR /data
#CMD ["java","-jar","ROOT.jar"]

FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /data
COPY . .
RUN mvn clean package -pl . -am -B -DskipTests


FROM amazoncorretto:17-alpine
ENV TZ Asia/Shanghai
WORKDIR /data
COPY --from=builder /data/target/ROOT.jar .
# 默认开启用JVM快速堆栈抛出特性，优化JVM性能但如果出问题不容易排查堆栈
# CMD ["java", "--enable-preview", "-jar","-Djava.security.egd=file:/dev/./urandom","BrokerExchange.jar"]
# 禁用JVM快速堆栈抛出特性，将打印所有的错误堆栈方便排查问题，但需要牺牲性能为代价
CMD ["java", "--enable-preview", "-jar","-Djava.security.egd=file:/dev/./urandom","ROOT.jar", "-XX:-OmitStackTraceInFastThrow"]
