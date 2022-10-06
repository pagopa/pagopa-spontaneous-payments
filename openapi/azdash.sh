#!/bin/bash

# 1 - retivere del openapi

# 2 - crea il file cfg
cat <<EOF > config.yaml
oa3_spec: https://raw.githubusercontent.com/pagopa/pagopa-gps-donation-service/pasqualespica-patch-opex/openapi/openapi.json
name: Dash GPS Donation service
location: West Europe
timespan: 5m
resources:
  - /subscriptions/bbe47ad4-08b3-4925-94c5-1278e5819b86/resourceGroups/pagopa-d-vnet-rg/providers/Microsoft.Network/applicationGateways/pagopa-d-app-gw
EOF


# 3 - crea la dash
docker run -v $(pwd):/home/nonroot/resources:Z \
  ghcr.io/pagopa/opex-dashboard:latest generate \
  --template-name azure-dashboard \
  --config-file /home/nonroot/resources/config.yaml