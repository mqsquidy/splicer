#FROM docker.turn.com/tops-base/jdk7:latest
FROM java:openjdk-7-jre
MAINTAINER instrumentation@turn.com

ENV       VERSION 0.1.5-2016011901
ENV       WORKDIR /usr/share/tsdb-splicer
ENV       DATADIR /data/tsdb-splicer
ENV       LOGDIR  /var/log/tsdb-splicer
ENV       REDIS_HOSTS "192.168.99.100:6379"
RUN	      mkdir -p $WORKDIR/resources
RUN	      mkdir -p $DATADIR/cache
RUN       mkdir -p $LOGDIR

ENV	      CLASSPATH  $WORKDIR
ENV	      BASE       $WORKDIR
ENV	      SPLICER_PORT  4245
ENV       JMX_PORT 10245
ENV       JMX_HOSTNAME 192.168.99.100
ENV       LOGLEVEL INFO
EXPOSE    $SPLICER_PORT

ADD	      tsdb-splicer-all-$VERSION.jar $WORKDIR/
ADD	      startup.sh $WORKDIR/

ADD	      VERSION   $WORKDIR/resources/
ADD	      resources $WORKDIR/resources/

WORKDIR   $WORKDIR

#ENTRYPOINT /bin/bash -c "java -server -classpath $WORKDIR/resources:$WORKDIR:$WORKDIR/tsdb-splicer-all-$VERSION.jar com.turn.splicer.SplicerMain --port $SPLICER_PORT --config=$WORKDIR/resources/splicer.conf"
ENTRYPOINT ["./startup.sh"]
