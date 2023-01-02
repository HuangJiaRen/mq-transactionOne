FROM openjdk:8-jdk-alpine
RUN adduser spring -D && \
passwd spring -d 2Ql^cKF0nxAhGRNS && \
chown spring:spring -R /opt /var/app/ && \
ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
echo 'Asia/Shanghai' >/etc/timezone
ADD *.jar /opt/
USER spring

# 设置系统环境变量
export APPLICATION_NAME=distributredTransaction
export JVM_PARAMS='-Xmx:2048m -Xms:2048m -Xmn:768m -Xss:256KB -XX:+UseConcMarkSweepGC XX:+UseCompressedClassPointers -XX:+PrintGCDetails -XX:+PrintGC:PrintGCTimeStamps -Xloggc:/var/app/logs/gc/gc.log -verbose:gc'
export JVM_SYS_PARAMS=
export SPRING.PROFILES.ACTIVE=dev

# 设置skywalking环境变量
export SW_AGENT_NAME=$APPLICATION_NAME
export SW_AGENT_COLLECTOR_BACKEND_SERVICES=skywalking-oap.ops-dev.svc.cluster.local:11800
export SW_AGENT_SPAN_LIMIT=2000
export JAVA_AGENT=-javaagent:/opt/skywalking-agent/skywalking-agent.jar
export SW_MYSQL_TRACE_SQL_PARAMETERS=true


ADD target/*.jar /
EXPOSE 8080
CMD java -jar -Dspring.profiles.active=$SPRING.PROFILES.ACTIVE $JVM_PARAMS $JVM_SYS_PARAMS $JAVA_AGENT /*.jar