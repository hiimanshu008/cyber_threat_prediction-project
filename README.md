# AI-Driven Cyber Threat Predictor – Backend

- Java Spring Boot service that ingests network logs (Kafka or REST), enriches with VirusTotal + Gemini, scores risk, and persists results in MongoDB.

See also: [WORKFLOWS.md](./WORKFLOWS.md) for detailed workflows and tooling rationale.

## Prerequisites
- Java 17+
- Maven 3.9+
- MongoDB (local or remote)
- Apache Kafka broker (local or remote)
- VirusTotal API key (configured)
- Gemini API key (configured)

## Quick Start
- Configure via environment variables (recommended for local dev too). On Windows PowerShell:
  - Set required variables for your environment (examples):
    ```powershell
    $env:MONGODB_URI = "mongodb://localhost:27017"
    $env:MONGODB_DATABASE = "cybersec"
    $env:KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"
    $env:APP_KAFKA_TOPIC = "topic_0"
    $env:KAFKA_CONSUMER_GROUP_ID = "cybersec-analyzer"
    $env:KAFKA_SASL_USERNAME = "<if using SASL, else leave empty>"
    $env:KAFKA_SASL_PASSWORD = "<if using SASL, else leave empty>"
    $env:VIRUSTOTAL_API_KEY = "<your_vt_api_key>"
    $env:GEMINI_API_KEY = "<your_gemini_api_key>"
    # Optional
    $env:GEMINI_MODEL = "gemini-1.5-flash"
    $env:GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
    ```
- Build and run:
  - `mvn spring-boot:run`
- Open Swagger UI:
  - `http://localhost:8080/swagger-ui/index.html`

## Configuration Notes
- App reads configuration from environment variables with sensible defaults. See `src/main/resources/application.properties` for the full list of keys.
- Kafka topic used by the consumer defaults to `topic_0` (`APP_KAFKA_TOPIC`).
- Secrets like `VIRUSTOTAL_API_KEY` and `GEMINI_API_KEY` should be provided via environment variables (avoid committing secrets).

## Primary Endpoints
- `GET /api/logs` — list processed logs
- `POST /api/logs` — ingest a log and analyze immediately
- `GET /api/threats` — list threats
- `GET /api/threats/{id}` — threat by id
- `GET /api/summary/{id}` — latest AI explanation for a threat id
- `POST /api/analyze` — analyze a single log (no Kafka)

## Example: Analyze via REST
- Request
```bash
curl -X POST http://localhost:8080/api/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "sourceIP":"192.168.1.23",
    "destinationIP":"10.0.0.8",
    "port":443,
    "protocol":"TCP",
    "message":"Failed login attempt detected",
    "severity":"warning"
  }'
```

- Expected response (shape)
```json
{
  "id": "<logId>",
  "threatDetected": true,
  "threatScore": 0.0,
  "threatId": "<threatId>",
  "status": "processed",
  "createdAt": "<iso>",
  "updatedAt": "<iso>"
}
```

## Example: Ingest via Kafka
- Ensure topic exists: `topic_0`.
- Produce a JSON log message to Kafka (one JSON per line). For Confluent Cloud, use your SASL config file:
```bash
kafka-console-producer \
  --topic topic_0 \
  --bootstrap-server pkc-921jm.us-east-2.aws.confluent.cloud:9092 \
  --producer.config client.properties
{"sourceIP":"8.8.8.8","destinationIP":"10.0.0.5","port":22,"protocol":"TCP","message":"Multiple failed login attempts detected","severity":"critical"}
```

## Data Stores
- MongoDB collections:
  - `logs` — raw/processed logs + link to threat
  - `threats` — computed scores + threat type + link to explanation
  - `ai_explanations` — AI responses and token/cost metadata
  - `system_config` — not used in this hardcoded setup

## Observability
- Swagger/OpenAPI enabled.
- Logging defaults to INFO (set DEBUG selectively in `application.properties`).

## Common Issues
- Kafka auth/SSL: verify bootstrap server, API key/secret in properties, and topic `topic_0` exists.
- MongoDB Atlas: ensure your IP is allowlisted and user has access to DB `cybersec`.
- VirusTotal/Gemini: ensure API keys are active. If invalid, VT score returns 0 and AI content may be empty.
