curl -i \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '
    {
    "entities": [
        {
            "type": "agent05",
            "isPattern": "false",
            "id": "agent05_id_001"
        }
    ]
}' \
  http://<iotdiscovery>:8002/ngsi9/discoverContextAvailability
