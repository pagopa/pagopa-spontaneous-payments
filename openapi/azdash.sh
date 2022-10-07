#!/bin/bash

# 1 - retrive del openapi
echo "1 - retrive del openapi"

# 2 - create cfg file
echo "2 - create cfg file"
cat <<EOF > config.yaml
oa3_spec: https://raw.githubusercontent.com/pagopa/pagopa-spontaneous-payments/main/openapi/openapi.json
name: UAT GPS service
location: West Europe
timespan: 5m
resources:
  - /subscriptions/26abc801-0d8f-4a6e-ac5f-8e81bcc09112/resourceGroups/pagopa-u-vnet-rg/providers/Microsoft.Network/applicationGateways/pagopa-u-app-gw
EOF

# 3 - create dash
echo "3 - create dash"
docker run -v $(PWD):/home/nonroot/resources:Z ghcr.io/pagopa/opex-dashboard:latest generate --template-name azure-dashboard --config-file /home/nonroot/resources/config.yaml > uat_dash.json