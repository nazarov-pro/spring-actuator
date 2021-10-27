KIBANA_HOST=localhost
KIBANA_PORT=5601
ELASTIC_HOST=localhost
ELASTIC_PORT=9200
ELASTIC_USER=elastic
ELASTIC_PASSWORD=changeme

if [ -n "${ELASTIC_USER}" ]; then
  CURL_UESR_OPT="-u ${ELASTIC_USER}:${ELASTIC_PASSWORD}"
fi

timeoutSeconds=60
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' ${KIBANA_HOST}:${KIBANA_PORT}/api)" -ne "404" && timeoutSeconds -gt 0 ]]; do
  echo "Check health timeout $timeoutSeconds seconds"
  sleep 5
  timeoutSeconds=$((timeoutSeconds-5))
done

curl ${CURL_UESR_OPT} -X PUT -H "kbn-xsrf: true" -H "Content-Type: application/json" "http://${ELASTIC_HOST}:${ELASTIC_PORT}/_cluster/settings" -d '{ "transient": { "cluster.routing.allocation.disk.threshold_enabled": false } }'
curl ${CURL_UESR_OPT} -X PUT -H "kbn-xsrf: true" -H "Content-Type: application/json" "http://${ELASTIC_HOST}:${ELASTIC_PORT}/_all/_settings" -d '{"index.blocks.read_only_allow_delete": null}'
curl ${CURL_UESR_OPT} -X POST "http://${KIBANA_HOST}:${KIBANA_PORT}/api/saved_objects/_import" -H "kbn-xsrf: true" --form file=@configs/elk/kibana-index.ndjson
curl ${CURL_UESR_OPT} -X POST "http://${KIBANA_HOST}:${KIBANA_PORT}/api/saved_objects/_import" -H "kbn-xsrf: true" --form file=@configs/elk/kibana-visualize.ndjson
curl ${CURL_UESR_OPT} -X POST "http://${KIBANA_HOST}:${KIBANA_PORT}/api/saved_objects/_import" -H "kbn-xsrf: true" --form file=@configs/elk/kibana-dashboard.ndjson
