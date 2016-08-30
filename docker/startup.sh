#!/bin/bash

sed -i 's/~~LOGLEVEL~~/'$LOGLEVEL'/g' $WORKDIR/resources/logback.xml
sed -i 's/---REDIS_HOSTS---/'$REDIS_HOSTS'/g' $WORKDIR/resources/splicer.conf
sed -i 's/---TSD_HOSTS---/'$TSD_HOSTS'/g' $WORKDIR/resources/splicer.conf
echo "------ config -------"
cat $WORKDIR/resources/splicer.conf | grep caching.hosts
cat $WORKDIR/resources/splicer.conf | grep tsd.hosts
cat $WORKDIR/resources/logback.xml | grep "root level"
echo "------ config end-------"
exec java -server -Dcom.sun.management.jmxremote= -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=${JMX_HOSTNAME} -classpath $WORKDIR/resources:$WORKDIR:$WORKDIR/tsdb-splicer-all-$VERSION.jar com.turn.splicer.SplicerMain --port $SPLICER_PORT --config=$WORKDIR/resources/splicer.conf | tee $LOGDIR/splicer.log