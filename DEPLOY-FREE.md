# Deploy Kissan-AI for Free 🆓

This guide deploys the **Kissan-AI** WhatsApp assistant (Java 17 + Quarkus, PostgreSQL) to a
**free tier** — no credit card required (or a small top-up that renews to $0).

The project already ships with a production `Dockerfile` and `docker-compose.yml`, so every
platform below just runs that container with a free PostgreSQL.

## Quick reference (pick one)

| Platform | Free compute | Free PostgreSQL | Sleeps after idle | Notes |
|---|---|---|---|---|
| **Render** | 512 MB RAM, 750 h/mo | 0.5 GB | 15 min idle → cold start | Easiest; 1-click from GitHub |
| **Google Cloud Run + Supabase** | 2M requests/mo, never sleeps | 500 MB | Never | Most reliable for "always on" |
| **Fly.io** | 3 × 256 MB VMs, 3 GB vol | 3 GB (Postgres add-on) | No | Run app + DB on free shared VMs |
| **Oracle Cloud Always Free** | 1–2 VMs (1 GB RAM each) | Self-managed Postgres | No | Most generous; most manual |

All options were validated against the production `Dockerfile` (JDK 17 build → runnable fast-jar).

> **Prerequisites everywhere:** push your code (incl. `Dockerfile`) to a GitHub/GitLab repo,
> and have a WhatsApp `VERIFY_TOKEN` + (later) `PHONE_NUMBER_ID`/`ACCESS_TOKEN` ready as secrets.

---

## Option 1 — Render (free, simplest)

Render auto-builds from the `Dockerfile` and gives a free PostgreSQL add-on.

### Deploying with OpenWA

Since Render doesn't support docker-compose (multi-container), you need to deploy OpenWA as a separate service:

1. **Deploy OpenWA service first:**
   - OpenWA is now in a separate repository: https://github.com/tirthachetry-zoho/openwa
   - Sign up at [render.com](https://render.com) (GitHub auth, no card).
   - Click **New → Web Service** → connect the OpenWA repository.
   - Runtime: `Docker`.
   - Region: pick nearest.
   - Click **Create Web Service**.
   - On the OpenWA service → **Environment**, add:
     ```
     PORT = 3000
     WHATSAPP_HOOK_URL = https://<your-kissan-ai-service>.onrender.com/openwa/webhook
     WHATSAPP_HOOK_SECRET = <your-webhook-secret>
     WHATSAPP_API_KEY = <your-api-key>
     SESSION_NAME = kissan-assistant
     ```
   - Note the OpenWA service URL (e.g., `https://kissan-ai-openwa.onrender.com`).

2. **Deploy Kissan-AI app:**
   - Click **New → Web Service** → connect your repo.
   - Runtime: leave auto-detected (`Docker`).
   - Region: pick nearest (same as OpenWA for lower latency).
   - Click **Create Web Service** → Render builds your image and deploys it.
3. Add a free database: **New → PostgreSQL** → choose **Free** (0.5 GB) → name it e.g. `kissan-ai-db`.
4. On your Kissan-AI service → **Environment → Secrets**, add:
   ```
   DB_URL            = <jdbc url from the database page, e.g. jdbc:postgresql://<host>:5432/<db>>
   DB_USER           = <db user>
   DB_PASSWORD       = <db password>
   WHATSAPP_VERIFY_TOKEN = your-secret-token
   WHATSAPP_PHONE_NUMBER_ID = (from Meta dashboard)
   WHATSAPP_ACCESS_TOKEN    = (from Meta dashboard)
   OPENWA_API_BASE_URL = https://<your-openwa-service>.onrender.com
   OPENWA_API_KEY = <same-api-key-as-openwa-service>
   OPENWA_SESSION_NAME = kissan-assistant
   OPENWA_WEBHOOK_SECRET = <same-webhook-secret-as-openwa-service>
   JAVA_OPTS = -Dquarkus.http.port=10000   # Render expects port in $PORT; the Quarkus build picks it up
   ```
   Render injects `$PORT`; Quarkus honors it via `quarkus.http.port=${PORT:8080}` only if you
   add it to `application.properties` — so set `JAVA_OPTS` (or app port) accordingly, or
   set **Environment → Add Environment Variable** `QUARKUS_HTTP_PORT` = `$PORT` is not possible;
   instead use the **Autoscale / Port** setting "Port" = **8080** and let Render map it.
5. Visit `https://<service>.onrender.com/health` → `{"status":"ok",...}`.
6. Scan the QR code from the OpenWA service logs to connect your WhatsApp number:
   - Go to your OpenWA service on Render → **Logs**.
   - Look for the QR code URL or connect to the service endpoint to scan.

Free tier limits: 512 MB RAM, 750 h/mo (≈ 1 month of uptime), PostgreSQL 0.5 GB / 100 k rows.
The web service **sleeps after 15 min idle** (first request is slow) — fine for an MVP.

### Alternative: Use docker-compose locally or on other platforms

For local development or platforms that support docker-compose (Fly.io, Oracle Cloud, etc.),
use the updated `docker-compose.yml` which includes both OpenWA and the app in one setup:

```bash
cp .env.example .env
# Edit .env with your configuration
docker compose up -d --build
```

## Option 2 — Google Cloud Run + Supabase (free, never sleeps)

Best "set and forget" free tier: Cloud Run never sleeps and has a generous always-free quota.

1. **Supabase** (free Postgres): sign up at [supabase.com](https://supabase.com) → New project →
   copy the connection string (e.g. `postgresql://postgres:...@db.<ref>.supabase.co:5432/postgres`).
   Convert to JDBC form for the app:
   ```
   DB_URL        = jdbc:postgresql://db.<ref>.supabase.co:5432/postgres
   DB_USER       = postgres
   DB_PASSWORD   = <your db password>
   ```
2. **Cloud Run** (requires a Google account; always-free, no card for the tier):
   - Enable **Cloud Run** and **Artifact Registry** APIs.
   - Deploy from the `Dockerfile`:
     ```bash
     gcloud run deploy kissan-ai \
       --source . \
       --region us-central1 \
       --set-env-vars=DB_URL=jdbc:postgresql://db.<ref>.supabase.co:5432/postgres,DB_USER=postgres,DB_PASSWORD=<pw>,WHATSAPP_VERIFY_TOKEN=<token> \
       --allow-unauthenticated \
       --port 8080
     ```
     `--source` builds the Dockerfile and deploys in one step.
   - Cloud Run prints a URL like `https://kissan-ai-xxxx-uc.a.run.app`.
3. Verify: `curl https://<url>/health`.
4. Register `https://<url>/webhook` in the Meta WhatsApp dashboard (HTTPS required — Cloud Run provides it).

Free limits: 2 M requests / month, 360 000 GB·s + 180 000 vCPU·s, 5 GB egress, 1 GB Artifact
Registry storage. Enough for a small farm-focused MVP.

## Option 3 — Fly.io (free shared-VMs)

Fly gives 3 shared-CPU (256 MB) VMs + 3 GB volume free per month — enough for a small Quarkus app.

1. Install [`flyctl`](https://fly.io/docs/hands-on/install-flyctl/) and `fly auth login`.
2. `fly launch` → choose a name, accept the free shared-cpu-1x (256 MB) instance, **deploy now**.
3. Add a free Postgres: `fly postgres create --name kissan-ai-db --initial-cluster-size 1`
   (pick the free tier when prompted).
4. Mount it as a secret on your app:
   ```bash
   fly secrets set \
     DB_URL="jdbc:postgresql://<fly-postgres-host>:5432/kissan_ai" \
     DB_USER=postgres \
     DB_PASSWORD=<password> \
     WHATSAPP_VERIFY_TOKEN=<token>
   ```
5. `fly deploy` to apply, then `fly status` / `https://<app>.fly.dev/health`.

Free tier: 3 shared VMs (256 MB each) + 3 GB storage + 160 GB outbound/month.
Note: keep the JVM small — the `Dockerfile` already sets `MaxRAMPercentage=75.0`; on 256 MB that
leaves ~190 MB heap, which is enough for this lightweight app.

## Option 4 — Oracle Cloud Always Free (most generous)

Oracle's truly-free tier is the most generous: **2 AMD VMs (1 GB RAM each)**, 2 × 200 GB block
volumes, and always-free networking.

1. Sign up at [cloud.oracle.com](https://cloud.oracle.com/) → **Always Free** (no card for the
   VM tier itself, though OCI may verify identity).
2. Create **2x Ampere A1 (1/8 OCPU, 1 GB RAM)** → "Always Free" → create a **Boot Volume** (50 GB).
3. SSH in and install Docker:
   ```bash
   # Ubuntu
   sudo apt-get update && sudo apt-get install -y docker.io
   # (or follow https://docs.docker.com/engine/install/ubuntu)
   ```
4. Run the app + Postgres from the bundled compose file:
   ```bash
   # On your dev machine, build only the app image and push to a free registry
   # (or scp the project to the VM and build there):
   scp -r . opc@<vm-ip>:~/kissan-ai
   ssh opc@<vm-ip>
   cd ~/kissan-ai
   # Edit .env with your secrets, then:
   docker compose -f docker-compose.yml up -d --build
   ```
   Expose port 8080 in the **Security List** (Ingress TCP 8080 from `0.0.0.0/0`).
5. Get the VM's public IP → `curl http://<public-ip>:8080/health`.
6. (Optional) Add nginx on a 2nd tiny VM for TLS termination, or use **Cloudflare Tunnel**
   (free) to expose `https://kissan-ai.your-domain.com` without opening ports:
   ```bash
   cloudflared tunnel --url http://localhost:8080
   ```

Oracle free tier: 2 VMs × 1 GB RAM, 2 × 200 GB block storage, 10 TB egress/mo — effectively
unlimited for an MVP. The only catch is the initial identity verification.

---

## Common post-deploy verification

Once any option is live:

```bash
# 1. Health
curl https://<your-host>/health
# → {"service":"kissan-ai","timestamp":"...","status":"ok"}

# 2. Webhook verification (use your real token)
curl "https://<your-host>/webhook?hub.mode=subscribe&hub.verify_token=<your-token>&hub.challenge=1234567890"
# → should echo 1234567890

# 3. Simulate a WhatsApp message
curl -X POST https://<your-host>/webhook \
  -H 'Content-Type: application/json' \
  -d '{"object":"whatsapp_business_account","entry":[{"id":"WABA_ID","changes":[{"field":"messages","value":{"messaging_product":"whatsapp","contacts":[{"profile":{"name":"Ravi"},"wa_id":"919000000001"}],"messages":[{"from":"919000000001","id":"wamid.1","timestamp":"1700000000","type":"text","text":{"body":"Tomato price today"}}]}}]}]}'
# → 200; then query your DB:
#   SELECT phonenumber,name,language FROM farmers;        -- 919000000001 | Ravi | en
#   SELECT direction,content FROM messages ORDER BY id;   -- INCOMING "Tomato price today", OUTGOING <AI reply>
```

## Important free-tier notes

- **JDK version:** the `Dockerfile` builds with **JDK 17** (matches `pom.xml`) and sidesteps the
  ByteBuddy/JDK-22+ build error. If you ever build the native image or the jar on JDK 22+, add
  `-Dnet.bytebuddy.experimental=true` to the build.
- **WhatsApp requires HTTPS + a public URL.** Free platforms that give you a HTTPS endpoint
  (Render, Cloud Run, Fly) work directly; Oracle VMs need a tunnel (Cloudflare Tunnel / ngrok)
  in front.
- **Don't commit secrets.** Keep `.env` local and add it to git: the `.gitignore` already
  ignores `target/`; add `.env` if you use it locally.
- **Free ≠ SLA.** These tiers are great for a pilot, but for a production rollout behind real
  farmer traffic, move to a paid plan / VPS (the `Dockerfile` + `kissan-ai.service` transfer
  over unchanged).
