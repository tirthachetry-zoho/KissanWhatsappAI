# Product Requirements Document
## WhatsApp AI Assistant for Farmers — MVP

**Version:** 1.0  
**Status:** Draft  
**Primary Goal:** Build a low-cost WhatsApp-based agricultural assistant that helps farmers get timely, understandable, actionable information using text, voice, and images.

---

# 1. Product Vision

Create a simple agricultural assistant that farmers can access through WhatsApp without installing a new application.

The assistant should help farmers answer questions such as:

- “What is wrong with my crop?”
- “Can I spray today?”
- “It is going to rain. What should I do?”
- “What fertilizer should I use?”
- “What is today's market price?”
- “When should I irrigate?”
- “What should I do this week?”

The product should support **regional languages, voice messages, images, and simple conversational interactions**.

### Vision

> Give every farmer access to a knowledgeable agricultural assistant through the communication channel they already use.

---

# 2. MVP Philosophy

The first version should **not attempt to solve everything**.

We will optimize for:

1. Low infrastructure cost
2. Fast development
3. Simple farmer experience
4. Reliable information
5. Human escalation when AI is uncertain
6. Learning from real farmer conversations

The MVP should validate:

> Will farmers repeatedly use a WhatsApp assistant for agricultural problems?

---

# 3. Target Users

## Primary User

Small and medium-sized farmers who:

- Already use WhatsApp
- Have access to a smartphone
- Prefer local languages
- Need practical agricultural information
- May not have easy access to agricultural experts

## Secondary Users

- Farmer producer organizations (FPOs)
- Agricultural NGOs
- Agricultural officers
- Rural community organizations
- Agri-input organizations
- Agricultural consultants

---

# 4. Initial Geography

### MVP recommendation

Start with **one state / region and 1–3 major crops**, rather than launching nationally.

Example:

**India → Karnataka → Tomato / Cotton / Maize**

This allows the knowledge base, language support, weather information, market data, and recommendations to be localized.

The geography and crops should be configurable rather than hard-coded.

---

# 5. User Problems

Farmers commonly face:

### Information access

Agricultural information is scattered across:

- Government websites
- YouTube
- WhatsApp groups
- Local dealers
- Agricultural officers
- Other farmers

### Timing

Information is often received after the optimal action window.

### Language

Much agricultural content is not written in the farmer's preferred language.

### Complexity

Technical agricultural recommendations may be difficult to understand.

### Diagnosis

Farmers may not know whether a problem is:

- Pest
- Disease
- Nutrient deficiency
- Water stress
- Weather-related
- Soil-related

### Trust

Incorrect recommendations can cause:

- Crop loss
- Unnecessary input costs
- Pesticide misuse
- Financial loss

Therefore, **trust and safety are core product requirements.**

---

# 6. MVP Scope

## P0 — Must Have

### 6.1 WhatsApp Conversation

Farmer can:

- Send text
- Send voice notes
- Send images
- Receive text responses

The system remembers the conversation context.

---

### 6.2 Farmer Profile

Create a lightweight farmer profile.

```text
Farmer
├── Phone number
├── Name
├── Preferred language
├── Location
├── Crops
├── Farm size
└── Optional farm information
```

The farmer should not need to provide all information upfront.

The profile can be built progressively.

---

### 6.3 Language Detection

The system should detect the farmer's language automatically.

MVP languages:

- English
- Kannada
- Hindi

Additional languages can be added later.

The farmer should also be able to say:

> “Respond in Kannada”

and change their preference.

---

### 6.4 Crop Help

Example:

> Farmer: Tomato leaves are curling.

Workflow:

```text
Identify crop
      ↓
Understand symptom
      ↓
Ask relevant questions
      ↓
Request photo if useful
      ↓
Analyze available information
      ↓
Retrieve trusted agricultural information
      ↓
Generate response
      ↓
Safety check
      ↓
Answer farmer
```

The system should distinguish between:

- High confidence
- Possible causes
- Insufficient information

It should not present uncertain diagnoses as facts.

---

### 6.5 Image-Based Crop Problem Identification

Farmer can send a crop image.

Example:

> “What is this?”

The system:

1. Receives image
2. Determines crop/problem category
3. Extracts visible symptoms
4. Combines image with conversation context
5. Produces possible causes
6. Asks follow-up questions where necessary
7. Provides next steps

The MVP should focus on **assistance**, not claiming perfect image diagnosis.

---

### 6.6 Weather

Farmer can ask:

> “Will it rain tomorrow?”

or:

> “Should I spray today?”

The system can combine weather data with the farmer's location.

Example:

> Rain is expected tomorrow afternoon. If you're planning a foliar spray, consider the product label and local agricultural guidance and avoid spraying when rain is likely to wash the application off.

Weather-based alerts can be added after the conversational feature is validated.

---

### 6.7 Market Price Lookup

Farmer can ask:

> “Tomato price today”

The system returns available market information.

Example:

```text
Tomato — Today's available market prices

Market A: ₹X/kg
Market B: ₹Y/kg
Market C: ₹Z/kg

Prices can change during the day.
```

Market data must display the **source and date/time** where available.

---

### 6.8 Farm Log

Farmers can send messages such as:

> Bought 2 bags fertilizer for ₹1,800.

The system converts this into structured information.

```text
Date: 10 Sep
Category: Fertilizer
Quantity: 2 bags
Cost: ₹1,800
```

This is useful but can remain lightweight in the first MVP.

---

### 6.9 Human Escalation

If the system is uncertain or the issue is high-risk:

```text
AI
 ↓
Low confidence / safety trigger
 ↓
Human expert
 ↓
Farmer
```

Example:

> “I’m not confident enough to recommend a treatment from this photo. I can send this to an agricultural expert for review.”

---

# 7. P1 Features

These should be built after initial validation.

### Proactive alerts

- Weather alerts
- Irrigation reminders
- Pest monitoring reminders
- Harvest reminders

### Crop calendar

```text
Crop
 ↓
Planting date
 ↓
Growth stage
 ↓
Recommended activities
 ↓
WhatsApp reminders
```

### Government schemes

Farmer asks:

> “What government schemes can I apply for?”

System provides relevant schemes with source links and eligibility information.

---

# 8. P2 Features

Future possibilities:

- Voice-first farmer experience
- IVR integration
- Soil-test interpretation
- Satellite/remote-sensing data
- Farm economics
- Input price comparison
- Crop yield estimation
- Personalized crop planning
- FPO dashboards
- Expert marketplace
- Agricultural commerce

These are explicitly **out of scope for MVP**.

---

# 9. Core User Journey

## First Interaction

```text
Farmer sends WhatsApp message
        ↓
Welcome
        ↓
Detect language
        ↓
Ask name
        ↓
Ask village/location
        ↓
Ask primary crop
        ↓
Create farmer profile
        ↓
Ready
```

Don't force the farmer through a long registration form.

---

# 10. Conversation Example

### Farmer

> Tomato leaves are becoming yellow.

### Assistant

> I can help you check this.
>
> Please send me a clear photo of the affected leaves.

### Farmer

> [Image]

### Assistant

> Thank you. Yellowing leaves can have several causes, including nutrient problems, watering issues, root problems, or disease.
>
> To narrow it down:
>
> 1. How old is the crop?
> 2. Are the older leaves or newer leaves turning yellow?
> 3. Is the soil currently very wet?

### Farmer

> 45 days. Older leaves. Soil is wet.

### Assistant

> Based on what you've described, water/root stress is one possibility. I would avoid adding more fertilizer until the cause is clearer.
>
> Please check whether water is standing around the plants and whether the roots/stem base show any discoloration.
>
> If you send me a photo of the whole plant and the soil around its base, I can help narrow it down.

This style is preferable to:

> “Your tomato plants have disease X. Spray chemical Y.”

---

# 11. Conversation Architecture

Every message should pass through an intent router.

```text
Incoming WhatsApp message
          ↓
Language detection
          ↓
Intent classification
          ↓
Conversation state
          ↓
Relevant workflow
```

Possible intents:

```text
CROP_PROBLEM
PEST_DISEASE
WEATHER
MARKET_PRICE
FERTILIZER
IRRIGATION
CROP_PLANNING
FARM_LOG
GOVERNMENT_SCHEME
GENERAL_AGRICULTURE
HUMAN_HELP
UNKNOWN
```

---

# 12. Recommended Technical Architecture

The MVP should use a **modular architecture**, but keep infrastructure extremely simple.

```text
                WhatsApp
                   │
                   ▼
            WhatsApp API
                   │
                   ▼
             Webhook API
                   │
                   ▼
          Workflow / Backend
                   │
       ┌───────────┼────────────┐
       ▼           ▼            ▼
   PostgreSQL    AI Model    External APIs
       │           │        ├── Weather
       │           │        └── Market
       │           │
       └───────────┼────────────┘
                   ▼
             Safety Layer
                   │
                   ▼
                WhatsApp
```

---

# 13. Low-Cost Tech Stack

The goal should be **$0–$20/month during early testing**, excluding unavoidable WhatsApp messaging charges.

Exact pricing should be verified before production because provider pricing changes.

## Backend

### Option A — Java / Quarkus ✅ *implemented (MVP)*

**Java 17 + Quarkus** (RESTEasy Reactive, Hibernate ORM + Panache, SmallRye Config, Hibernate Validator)

Why:

- Free
- Open source
- GraalVM native-image support (low cost, fast cold start, tiny footprint)
- Reactive + imperative in one framework
- Strong test harness (`@QuarkusTest` + RestAssured)
- Panache removes most boilerplate for CRUD + repositories

---

## Database

### Supabase / PostgreSQL

Use PostgreSQL for:

- Farmers
- Farms
- Crops
- Conversations
- Messages
- Tasks
- Feedback
- Expert escalations

Supabase is attractive for MVP because it can provide database + authentication + storage in one platform.

---

## Hosting

Start with a free-tier hosting provider where practical.

Possible options:

- Fly.io
- Render
- Railway
- Scalingo
- Supabase (database)
- Any provider serving a JAR or GraalVM native binary (Ferry, Cloud Run, etc.)

The exact choice should depend on the backend architecture and current free-tier limits.

**Recommendation:** keep the backend portable so the project can move providers later.
Quarkus makes this easy — the same codebase produces a plain `java -jar` runnable
JAR **and** a self-contained GraalVM native executable.

---

# 14. AI Layer

The AI should be abstracted behind an internal interface.

```text
AIService
├── generateResponse()
├── classifyIntent()
├── summarizeConversation()
├── analyzeImage()
└── translate()
```

This allows us to change AI providers without rewriting the application.

For development:

- Use free/local models where practical.
- Use API-based models selectively for higher-quality tasks.
- Cache repeated responses.
- Don't send unnecessary conversation history to paid models.

### Long-term

Consider running open-source models locally once usage becomes large enough to justify it.

---

# 15. Knowledge Base

AI should not rely entirely on general model knowledge.

Create a curated agricultural knowledge base.

Sources may include:

- Government agriculture departments
- Agricultural universities
- ICAR resources
- Extension materials
- Crop-specific research
- Approved pesticide/product labels
- Weather information
- Market data

Structure:

```text
Knowledge Base
│
├── Crop
│   ├── Growth stages
│   ├── Diseases
│   ├── Pests
│   ├── Nutrients
│   └── Management
│
├── Weather
│
├── Market
│
└── Government programs
```

Use retrieval/RAG so the model receives relevant source material before generating an answer.

---

# 16. WhatsApp Integration

WhatsApp integration is the main unavoidable cost consideration.

For MVP:

1. Use the official WhatsApp Business Platform/API.
2. Build around webhooks.
3. Keep message volume low during the pilot.
4. Avoid unnecessary outbound messages.
5. Use WhatsApp templates only where required.
6. Track messaging costs from day one.

We should **not build the production system around unofficial WhatsApp automation**, even if it appears free, because account restrictions and reliability risks can undermine the product.

---

# 17. Data Model

### Farmer

```text
id
phone_hash / identifier
name
language
location
created_at
updated_at
```

### Farm

```text
id
farmer_id
location
area
area_unit
soil_type
created_at
```

### Crop

```text
id
farm_id
crop
variety
planting_date
area
growth_stage
```

### Conversation

```text
id
farmer_id
intent
status
created_at
updated_at
```

### Message

```text
id
conversation_id
direction
message_type
content
media_url
timestamp
```

### Farm Event

```text
id
farm_id
event_type
date
quantity
cost
notes
```

---

# 18. Safety Requirements

This is a critical component.

The assistant must avoid confidently giving dangerous or unsupported agricultural recommendations.

## High-risk categories

- Pesticides
- Herbicides
- Fungicides
- Fertilizer dosage
- Livestock medication
- Human health
- Food safety
- Toxic chemicals

### Safety workflow

```text
AI recommendation
       ↓
Risk classifier
       ↓
Low risk ───────→ Answer
       │
       ▼
Medium risk ───→ Add warning + trusted source
       │
       ▼
High risk ─────→ Human expert / authoritative source
```

For chemical recommendations, the system should prioritize **locally applicable, authoritative guidance and product-label instructions** rather than inventing dosages.

---

# 19. Human Expert Dashboard

MVP version can be extremely simple.

Experts should see:

```text
Open cases
───────────────
Farmer
Location
Crop
Problem
Image
AI assessment
Conversation
```

Expert can respond:

> Approved answer

or:

> Edit answer

or:

> Request more information

The farmer receives the response on WhatsApp.

---

# 20. Feedback Loop

Every useful interaction should create product learning.

At the end of important conversations:

> Was this helpful?

**👍 Yes**

**👎 No**

If no:

> What went wrong?

Options:

- Wrong answer
- Didn't understand
- Need expert
- Need more information
- Other

This data becomes critical for improving the system.

---

# 21. Analytics

Track:

### Acquisition

- Number of farmers
- New farmers/day
- Referral source

### Engagement

- Messages/farmer
- Active farmers/week
- Returning farmers
- Conversations/farmer

### Use cases

```text
Crop diagnosis       35%
Weather              20%
Market prices        15%
Fertilizer           10%
General questions    20%
```

### Quality

- Helpful %
- Unhelpful %
- Human escalation %
- AI confidence
- Expert correction rate

### Business

- Cost/farmer
- Cost/conversation
- WhatsApp cost
- AI cost
- Infrastructure cost

---

# 22. MVP Success Metrics

The first milestone is **not revenue**.

We need evidence of farmer value.

### Target metrics for pilot

- 100+ registered farmers
- 50+ weekly active farmers
- 30%+ returning users
- 60%+ conversations rated helpful
- <20% conversations requiring human intervention
- <10% materially incorrect AI responses after expert review

Targets should be adjusted after the first pilot.

---

# 23. MVP Development Plan

## Phase 1 — Foundation

**Week 1**

- Backend
- Database
- Farmer profile
- WhatsApp webhook
- Basic message handling

## Phase 2 — AI

**Week 2**

- Intent classification
- Conversation memory
- Language detection
- Basic agricultural knowledge base
- Response generation

## Phase 3 — Crop Assistance

**Week 3**

- Crop problem workflow
- Image upload
- Image analysis
- Follow-up questions
- Safety checks

## Phase 4 — Data Integrations

**Week 4**

- Weather
- Market information
- Source attribution

## Phase 5 — Pilot

**Weeks 5–6**

- 20–50 farmers
- Monitor conversations
- Human review
- Fix failure modes
- Improve prompts/knowledge base

---

# 24. Cost Strategy

## Development

Aim for:

**₹0**

Use:

- Open-source frameworks
- GitHub
- Local development
- Free-tier database
- Free-tier hosting
- Local/open-source models where practical

## Pilot

Target:

**As close to ₹0 as possible**

Expected unavoidable costs:

- WhatsApp messaging
- Potential AI API usage
- Domain, if desired
- Paid external data APIs, if free alternatives are insufficient

## Scaling

Only introduce paid infrastructure when:

```text
Usage
  ↓
Free-tier limit
  ↓
Measure cost
  ↓
Optimize
  ↓
Pay only for valuable usage
```

---

# 25. Cost Optimization Techniques

### 1. Don't use the largest AI model for everything.

Use smaller/cheaper models for:

- Intent classification
- Language detection
- Simple translation
- Message routing

Use stronger models for:

- Complex agricultural reasoning
- Image analysis
- Difficult cases

### 2. Cache

Cache:

- Weather queries
- Market queries
- Frequently requested agricultural information

### 3. Summarize conversations

Instead of sending the entire conversation to the model every time:

```text
100 messages
     ↓
Conversation summary
     ↓
Relevant recent messages
```

### 4. RAG instead of huge prompts

Don't send the entire agriculture knowledge base to the AI.

Retrieve only relevant documents.

### 5. Human escalation

Don't spend expensive AI inference endlessly trying to solve uncertain problems.

---

# 26. Non-Functional Requirements

### Reliability

System should gracefully handle:

- WhatsApp API failures
- AI API failures
- Missing images
- Poor-quality images
- External API downtime

### Performance

Target:

**<10 seconds** for normal text responses where external services permit.

### Availability

MVP target:

**99%+**

### Privacy

Store only information required for the service.

Sensitive information should not be unnecessarily retained.

Farmers should understand:

- What information is collected
- Why it is collected
- How it is used

---

# 27. What We Will NOT Build Initially

To prevent scope creep, MVP will not include:

- Native Android/iOS app
- Full farm-management ERP
- Payments
- E-commerce
- Drone integration
- Satellite analytics
- Complex recommendation engine
- Automated pesticide purchasing
- Credit/insurance
- Large-scale government integration
- Multi-country support

---

# 28. MVP Architecture Principle

The most important architectural decision is:

> **Separate WhatsApp, AI, agricultural knowledge, and business logic.**

For example:

```text
WhatsApp Adapter
      ↓
Conversation Engine
      ↓
Intent Router
      ↓
Workflow Engine
      ↓
 ┌──────────┬──────────┬───────────┐
 │ Crop     │ Weather  │ Market    │
 │ Workflow │ Workflow │ Workflow  │
 └──────────┴──────────┴───────────┘
      ↓
AI Service
      ↓
Knowledge Retrieval
      ↓
Safety Layer
      ↓
Response
```

This means we can eventually replace WhatsApp with:

- Android app
- Telegram
- IVR
- Web
- FPO dashboard

without rebuilding the agricultural intelligence layer.

---

# 29. Definition of Done — MVP

The MVP is considered complete when a farmer can:

1. Start a WhatsApp conversation.
2. Communicate in a supported language.
3. Register basic farm information.
4. Ask an agricultural question.
5. Receive a contextual response.
6. Send a crop image.
7. Receive an image-assisted assessment.
8. Ask for weather information.
9. Ask for market information.
10. Get a human expert when AI confidence is insufficient.
11. Rate the response.

The team can:

1. View conversations.
2. Review AI responses.
3. Correct AI answers.
4. Add knowledge-base documents.
5. View basic usage/quality metrics.
6. Measure cost per farmer/conversation.

---

# 30. North Star Metric

### Weekly Farmers Receiving Useful Agricultural Assistance

Not:

- Messages sent
- AI responses generated
- Number of registered users

The product succeeds when farmers **come back because the assistant actually helped them make a better farming decision.**

---

# 31. Recommended MVP

If we want to be extremely disciplined about cost and development time, I would launch with only:

### Version 0.1

**WhatsApp + AI + Crop Questions + Images + Human Escalation**

Then:

### Version 0.2

Add:

**Weather + Market Prices**

Then:

### Version 0.3

Add:

**Farm Memory + Crop Calendar + Proactive Alerts**

This gives us a useful product without spending months building a large agricultural platform before we know what farmers actually need.

---

# 32. Implementation Status — Java MVP ✅

The MVP backend has been **implemented and tested** in **Java 17 + Quarkus**
(`pom.xml`, `src/main/java/com/kishan`). The Python/FastAPI prototype files have
been removed; the PRD's intent, scope and architecture remain unchanged.

## What is built

| Component | Status |
|-----------|--------|
| WhatsApp webhook (`GET` verify + `POST` receive) | ✅ |
| Farmer registration & profile (phone, name, language, location) | ✅ |
| Farm, Crop, Conversation, Message, FarmEvent entities + CRUD APIs | ✅ |
| Conversation memory (one active conversation per farmer, message history) | ✅ |
| Language detection & preference switch (English / Kannada / Hindi) | ✅ |
| `AIService` abstraction + offline rule-based `SimpleAIService` | ✅ |
| Health endpoint | ✅ |
| Full automated test suite (`mvn test`) | ✅ |

## Deliberate MVP simplification

- §14 of this PRD describes richer AI primitives (`classifyIntent`, `analyzeImage`,
  `summarizeConversation`, `translate`). The current `AIService` surface is
  intentionally minimal (`generateResponse`) and works offline with zero cost;
  the additional primitives can be added to the same interface without touching
  the channel/resource layer.
- Weather and market data integration (PRD §6.6 / §6.7): in the MVP the responses
  are static, rule-based informational answers, so the feature works end-to-end
  with zero external API cost. Live data sources plug into the `SimpleAIService`
  without webhook changes.
- Image *diagnosis* (PRD §6.5) is stubbed to store the media reference; turning
  it on requires wiring an image-capable AI provider behind `AIService`.

## Run it

```bash
mvn quarkus:dev   # H2 in-memory, zero configuration
mvn test          # 30+ automated tests
```

See `README.md` for endpoint reference and WhatsApp configuration steps.