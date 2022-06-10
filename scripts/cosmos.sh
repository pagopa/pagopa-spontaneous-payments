#!/bin/bash

# set -e

# force delete image and container of MS AZ Cosmos DB Emulator
docker container rm -f test-linux-emulator
docker image rm -f mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator:latest

PORT=$1

# Main ...
if [ -z "$PORT" ]
then
  PORT=8081
  echo "CosmosDB starting on DEFAULT port $PORT"
else
  echo "CosmosDB starting on specific port $PORT"
fi


# Azure Cosmos DB Emulator
URL="https://localhost:$PORT/_explorer/index.html"


ipaddr="`ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}' | head -n 1`" && echo $ipaddr

docker pull mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator


docker run \
    --detach \
    --publish $PORT:8081 \
    --publish 10251-10254:10251-10254 \
    --memory 3g --cpus=2.0 \
    --name=test-linux-emulator \
    --env AZURE_COSMOS_EMULATOR_PARTITION_COUNT=10 \
    --env AZURE_COSMOS_EMULATOR_ENABLE_DATA_PERSISTENCE=true \
    --env AZURE_COSMOS_EMULATOR_IP_ADDRESS_OVERRIDE=$ipaddr \
    --interactive \
    --tty \
    mcr.microsoft.com/cosmosdb/linux/azure-cosmos-emulator

echo -n "Cosmos starting..."
cosmos_started="`docker logs test-linux-emulator | grep -wc Started`"
echo -n $cosmos_started
# check cosmos is UP
while [ "$cosmos_started" != "12" ]
do
    sleep 3
    echo -n "."
    cosmos_started=`docker logs test-linux-emulator | grep -wc Started`
    echo -n $cosmos_started
done

echo "!!! STARTED !!!"

curl -k https://$ipaddr:$PORT/_explorer/emulator.pem > emulatorcert.crt

# add keychain accesss
sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain emulatorcert.crt

# add jvm trust-store
sudo keytool -trustcacerts -keystore "${JAVA_HOME}/lib/security/cacerts" -storepass changeit -importcert -alias testalias -file emulatorcert.crt

open $URL
