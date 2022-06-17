# https://everything.curl.dev/http/post/chunked

FILENAME=$1
HOST=localhost
PORT=8080
curl  -H "Transfer-Encoding: chunked" --location --request POST -d @${FILENAME} "https://${HOST}:${PORT}" \
--header 'SOAPAction: paDemandPaymentNotice' \
--header 'Content-Type: application/xml'