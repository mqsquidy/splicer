#!/bin/bash

sed -ie 's/---REDIS_HOSTS---/'$REDIS_HOSTS'/g' $WORKDIR/resources/splicer.conf
echo "------ config -------"
cat $WORKDIR/resources/splicer.conf | grep caching.hosts
echo "------ config end-------"
java -server -classpath $WORKDIR/resources:$WORKDIR:$WORKDIR/tsdb-splicer-all-$VERSION.jar com.turn.splicer.SplicerMain --port $SPLICER_PORT --config=$WORKDIR/resources/splicer.conf | tee $LOGDIR/splicer.log