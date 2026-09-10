# Kishan-A — WhatsApp AI Assistant for Farmers

A low-cost, phone-first agricultural assistant that farmers can reach through
WhatsApp. Built in **Java 17 + Quarkus** (converted from the original Python/FastAPI
prototype described in `PRD — WhatsApp AI Assistant for Farmers.md`).

Farmers can ask about crop problems, weather, market prices and fertilizer,
send voice notes and images, and get answers in **English, Kannada or Hindi** —
in the language they prefer.

> The MVP keeps the AI abstraction behind the `AIService` interface (PRD §14).
> It ships with an **offline, rule-based `SimpleAIService`** so the app runs with
> zero configuration and no API keys. Swapping in an LLM later does not require
> touching the rest of the application.

---

## Features (MVP)

| Area            | Details |
|-----------------|---------|
| WhatsApp webhook | `GET /webhook` verification + `POST /webhook` receive (text, image, audio, video, document) |
| Farmer profile   | Register by phone number, language, location; progressive profile |
| Farm & crops     | Register farms, crops, planting dates and growth stages |
| Farm log         | Log fertilizer/pesticide/harvest events with quantity and cost |
| Conversations    | Automatic conversation & turn memory per farmer |
| Language         | Auto-respond in farmer's language; `respond in Kannada/Hindi` switches preference |
| Safety            | Answers default to *possible causes + ask for more info + follow product labels* for risky topics |
| Health check     | `GET /health` |

---

## Tech Stack

| Layer      | Choice |
|------------|--------|
| Language   | Java 17 |
| Framework  | Quarkus (RESTEasy Reactive, Hibernate ORM with Panache, Hibernate Validator, SmallRye Config) |
| Database   | PostgreSQL (production); H2 in-memory for `dev` / `test` — **no database needed locally** |
| AI         | `AIService` interface — offline `SimpleAIService` by default; OpenAI-config ready |
| Build      | Maven |
| Testing    | Quarkus Test (RestAssured) integration tests + JUnit 5 unit tests |

---

## Getting Started

Prerequisites: **JDK 17+** and **Maven 3.8+**.

```bash
# 1. Run in dev mode (H2 in-memory DB, zero configuration)
mvn quarkus:dev

# 2. Verify it is up
curl http://localhost:8080/health
# {"service":"kishan-a","timestamp":"...","status":"ok"}

# 3. Run the full test suite
mvn test
```

The dev profile uses an in-memory H2 database, so **no PostgreSQL is required
to start experimenting**.

### PostgreSQL (production)

Set these environment variables (see [`.env.example`](.env.example)):

```bash
export DB_URL=jdbc:postgresql://localhost:5432/kishan_a
export DB_USER=postgres
export DB_PASSWORD=postgres
export WHATSAPP_VERIFY_TOKEN=replace-me
```

Tables are created automatically at startup (`quarkus.hibernate-orm.database.generation=update`).

---

## Environment Variables

| Variable                  | Default              | Purpose |
|---------------------------|----------------------|---------|
| `DB_URL`                  | `jdbc:postgresql://localhost:5432/kishan_a` | Production JDBC URL |
| `DB_USER`                 | `postgres`           | Database user |
| `DB_PASSWORD`             | `postgres`           | Database password |
| `WHATSAPP_API_URL`        | `https://graph.facebook.com/v18.0` | Meta Graph API base |
| `WHATSAPP_PHONE_NUMBER_ID`| *(empty)*            | WhatsApp Business phone id |
| `WHATSAPP_ACCESS_TOKEN`   | *(empty)*            | WhatsApp API token |
| `WHATSAPP_VERIFY_TOKEN`   | `dev-verify-token`   | Webhook verification token |
| `OPENAI_API_KEY`          | *(empty)*            | Optional — used by future LLM-backed `AIService` |

---

## API Overview

| Method | Path                        | Description |
|--------|-----------------------------|-------------|
| GET    | `/health`                   | Health check |
| GET    | `/webhook`                  | WhatsApp verification |
| POST   | `/webhook`                  | Receive WhatsApp messages |
| GET/POST | `/api/farmers`            | List / create farmers |
| GET/PUT/DELETE | `/api/farmers/{id}` | Read / update / delete farmer |
| GET/POST | `/api/farms`              | List / create farms |
| GET    | `/api/farms/by-farmer/{id}` | Farms of a farmer |
| GET/POST | `/api/crops`             | List / create crops |
| GET    | `/api/crops/by-farm/{id}`  | Crops of a farm |
| GET/POST | `/api/conversations`     | List / create conversations |
| GET    | `/api/conversations/by-farmer/{id}` | Conversations of a farmer |
| GET/POST | `/api/messages`          | List / create messages |
| GET    | `/api/messages/by-conversation/{id}` | Messages of a conversation |
| GET/POST | `/api/farm-events`       | List / create farm log events |
| GET    | `/api/farm-events/by-farm/{id}` | Events of a farm |

### Example: simulate a WhatsApp message

```bash
curl -X POST http://localhost:8080/webhook \
  -H 'Content-Type: application/json' \
  -d '{
    "object": "whatsapp_business_account",
    "entry": [{
      "changes": [{
        "value": {
          "messaging_product": "whatsapp",
          "contacts": [{"profile": {"name": "Ravi"}, "wa_id": "919000000001"}],
          "messages": [{"from": "919000000001", "id": "wamid.1", "timestamp": "1700000000", "type": "text", "text": {"body": "Tomato price today"}}]
        }
      }]
    }]
  }'
```

---

## Connecting the real WhatsApp Business Platform

1. Set `WHATSAPP_PHONE_NUMBER_ID`, `WHATSAPP_ACCESS_TOKEN` and a private
   `WHATSAPP_VERIFY_TOKEN`.
2. Expose the app to the internet (e.g. `ngrok http 8080`).
3. In the WhatsApp Business dashboard, register the webhook
   `https://<your-host>/webhook` with the same verify token.
4. Done — inbound farmer messages flow through the same `POST /webhook` handler
   that the tests exercise.

---

## Project Structure

```
src/main/java/com/kishan
├── config/          # @ConfigMapping interfaces (app, whatsapp, openai)
├── dto/             # Request/response DTOs (+ whatsapp/ webhook payloads)
├── entity/          # Panache entities (Farmer, Farm, Crop, Conversation, Message, FarmEvent)
├── resource/        # REST resources incl. WhatsAppWebhookResource
└── service/         # AIService interface + SimpleAIService (offline rule-based)

src/test/java/com/kishan
├── resource/        # Integration tests (RestAssured + QuarkusTest)
└── service/         # Unit tests for SimpleAIService
```

## Architecture

The code follows the PRD's MVP architecture principle (§28):
**separate WhatsApp, AI, agricultural knowledge and business logic.**

```
WhatsAppWebhookResource (channel adapter)
        ↓
  Conversation, Message entities (memory)
        ↓
        AIService (intent routing + response generation)
        ↓
  SimpleAIService ── offline rules / future LLM provider
```

Because the channel logic and the AI logic are decoupled, WhatsApp can later be
replaced with Telegram, IVR, a web app or an FPO dashboard without rebuilding
the intelligence layer.

---

## Roadmap (from the PRD)

- [x] Phase 1 — Foundation (backend, DB, farmer profile, WhatsApp webhook)
- [x] Phase 2 — AI (intent routing, conversation memory, language detection, response generation)
- [ ] Phase 3 — Crop assistance with image analysis (AI provider)
- [ ] Phase 4 — Weather & market data integrations
- [ ] Phase 5 — Pilot with human expert escalation

See `PRD — WhatsApp AI Assistant for Farmers.md` for the full product
requirements.