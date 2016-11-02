FROM docker.us-east-1.aws.aol.com/metro/splicer-base:latest
MAINTAINER casey.doerschuk@teamaol.com

ENV       VERSION 0.1.5-2016011901
ENV       WORKDIR /usr/share/tsdb-splicer
ENV       DATADIR /data/tsdb-splicer
ENV       LOGDIR  /var/log/tsdb-splicer
RUN	      mkdir -p $WORKDIR/resources
RUN	      mkdir -p $DATADIR/cache
RUN       mkdir -p $LOGDIR

ENV	      CLASSPATH  $WORKDIR
ENV	      BASE       $WORKDIR
ENV	      SPLICER_PORT  4245
ENV       JMX_PORT 10245
ENV       JMX_HOSTNAME 0.0.0.0
ENV       LOGLEVEL INFO
EXPOSE    $SPLICER_PORT

ADD	      tsdb-splicer-all-$VERSION.jar $WORKDIR/
ADD	      startup.sh $WORKDIR/

ADD	      VERSION   $WORKDIR/resources/
ADD	      resources $WORKDIR/resources/

WORKDIR   $WORKDIR

ENTRYPOINT ["./startup.sh"]
HEALTHCHECK --interval=30s --timeout=2s \
  CMD curl -f http://localhost:$SPLICER_PORT/api/config || exit 1
