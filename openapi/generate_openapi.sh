#!/bin/bash
curl http://localhost:9090/v3/api-docs | python3 -m json.tool > ./openapi.json
