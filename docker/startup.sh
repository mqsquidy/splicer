#!/bin/bash

sed -i 's/~~LOGLEVEL~~/'$LOGLEVEL'/g' $WORKDIR/resources/logback.xml
echo "------ config -------"
cat $WORKDIR/resources/logback.xml | grep "root level"
echo "------ config end-------"
exec java -server -Dcom.sun.management.jmxremote= -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=${JMX_HOSTNAME} -Dconfig.file=/splicer_conf/splicer.conf -classpath $WORKDIR/resources:$WORKDIR:$WORKDIR/tsdb-splicer-all-$VERSION.jar com.turn.splicer.SplicerMain | tee $LOGDIR/splicer.log