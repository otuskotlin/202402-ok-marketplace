#!/bin/bash

export LC_ALL=C
TOPIC=ok-mkpl-logs

/opt/bitnami/kafka/bin/kafka-topics.sh \
  --bootstrap-server kafka:9092 \
  --topic $TOPIC --create --partitions 3 --replication-factor 1

while true
do
  TIME=$(date +"%Y-%m-%dT%H:%M:%S.%N%z")
  echo "{\"@timestamp\":\"$TIME\",\"log\":\"ok\"}" | \
  /opt/bitnami/kafka/bin/kafka-console-producer.sh --bootstrap-server kafka:9092 --topic $TOPIC \
  && echo "sent at $TIME"
  sleep 10
done
