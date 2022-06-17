#!/bin/bash

# wsdl spec
#
ID_PA=$1 ID_BROKER_PA=$2 ID_STATION=$3 node base64.js | xargs bash chunked.sh