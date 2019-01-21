#!/bin/bash
# Script to run multiple instances of FastRTPs performance test

ruta="./HelloWorldExample"
#duration=1500
#users=200
#jump=1200
#between=6

duration=21600
users=20
jump=120
between=6
til=$(( duration - jump ))


threadNumber=1
for(( i=$duration ; i>$til ; i-=$between))
do
  echo "inicio $i con hilo $threadNumber"
  $ruta/HelloWorldExample publisher $i 100 $threadNumber > /dev/null &
  threadNumber=$((threadNumber+1))
  sleep $between
done


sleep $til

for ((i=1; i<=users ; i++))
do
  cat /opt/tmp/logFastRTPS_$i.log >> logFinal.log
done
