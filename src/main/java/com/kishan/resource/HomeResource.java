package com.kishan.resource;

import com.kishan.config.AppConfig;
import com.kishan.config.OpenWAConfig;
import com.kishan.config.WhatsAppConfig;
import com.kishan.entity.Conversation;
import com.kishan.entity.Farmer;
import com.kishan.entity.Message;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;

/**
 * Lightweight HTML landing page served at "/" so people hitting the service
 * root get a useful overview instead of a 404: status, live counters,
 * integration flags and the endpoints they can call to test the API.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HomeResource {

    @Inject
    AppConfig appConfig;

    @Inject
    WhatsAppConfig whatsappConfig;

    @Inject
    OpenWAConfig openwaConfig;

    @GET
    public String home() {
        long farmers = Farmer.count();
        long conversations = Conversation.count();
        long messages = Message.count();

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Kissan-AI — WhatsApp Assistant for Farmers</title>
                  <style>
                    :root { color-scheme: light dark; }
                    body { font-family: system-ui, -apple-system, "Segoe UI", sans-serif; margin: 0; padding: 2rem 1rem; }
                    main  { max-width: 860px; margin: 0 auto; }
                    .hero { background: linear-gradient(135deg, #1d6e2c, #2e8b57); color: #fff; border-radius: 14px; padding: 1.4rem 1.6rem; }
                    .hero h1 { margin: 0; font-size: 1.7rem; }
                    .hero p  { margin: .4rem 0 0; }
                    .badge-ok   { background: #2f9e44; color: #fff; padding: .15rem .6rem; border-radius: 999px; font-size: .75rem; }
                    .badge-info { background: #1971c2; color: #fff; padding: .15rem .6rem; border-radius: 999px; font-size: .75rem; }
                    h2 { margin-top: 1.6rem; }
                    .cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(190px, 1fr)); gap: .8rem; }
                    .card { border: 1px solid #ccc; border-radius: 10px; padding: .8rem 1rem; }
                    .card .num { font-size: 1.6rem; font-weight: 700; }
                    .card .lbl { font-size: .8rem; color: #555; }
                    table { width: 100%%; border-collapse: collapse; font-size: .9rem; }
                    th, td { text-align: left; padding: .45rem .6rem; border-bottom: 1px solid #ddd; }
                    code, pre { background: #f2f4f7; padding: .1rem .35rem; border-radius: 5px; font-size: .8rem; }
                    pre { display: block; padding: .7rem .9rem; overflow-x: auto; }
                    footer { margin-top: 1.6rem; color: #777; font-size: .8rem; }
                  </style>
                </head>
                <body>
                <main>
                  <div class="hero">
                    <h1>🌾 Kissan-AI</h1>
                    <p>WhatsApp AI assistant for farmers — deployed and <span class="badge-ok">healthy</span></p>
                  </div>

                  <h2>Live status</h2>
                  <div class="cards">
                    <div class="card"><div class="num">%d</div><div class="lbl">Farmers onboarded</div></div>
                    <div class="card"><div class="num">%d</div><div class="lbl">Conversations</div></div>
                    <div class="card"><div class="num">%d</div><div class="lbl">Messages stored</div></div>
                  </div>

                  <h2>API endpoints</h2>
                  <table>
                    <tr><th>Endpoint</th><th>Purpose</th></tr>
                    <tr><td><code>GET /health</code></td><td>Health check</td></tr>
                    <tr><td><code>GET /webhook</code></td><td>Meta webhook verification</td></tr>
                    <tr><td><code>POST /webhook</code></td><td>Meta webhook receive</td></tr>
                    <tr><td><code>POST /webhook/openwa</code></td><td>OpenWA gateway webhook</td></tr>
                    <tr><td><code>GET /api/farmers</code></td><td>List farmers</td></tr>
                    <tr><td><code>GET /api/conversations</code></td><td>List conversations</td></tr>
                    <tr><td><code>GET /api/messages</code></td><td>List messages</td></tr>
                    <tr><td><code>GET /api/farms</code> · <code>/api/crops</code> · <code>/api/farm-events</code></td><td>Farm domain data</td></tr>
                  </table>

                  <h2>Integration status</h2>
                  <table>
                    <tr><th>Channel</th><th>Status</th></tr>
                    <tr><td>Meta WhatsApp Business API</td><td>%s</td></tr>
                    <tr><td>OpenWA self-hosted gateway</td><td>%s</td></tr>
                    <tr><td>Reply delivery over OpenWA (<code>openwa.send-replies</code>)</td><td>%s</td></tr>
                  </table>

                  <h2>Try it yourself</h2>
                  <pre># 1. Health
                curl https://&lt;your-host&gt;/health

                # 2. Simulate a WhatsApp message (creates a farmer + conversation + AI reply)
                curl -X POST https://&lt;your-host&gt;/webhook \\
                     -H 'Content-Type: application/json' \\
                     --data '{"object":"whatsapp_business_account","entry":[{"id":"WABA_ID","changes":[{"field":"messages","value":{"messaging_product":"whatsapp","metadata":{"display_phone_number":"15550000000","phone_number_id":"PHONE_ID"},"contacts":[{"profile":{"name":"Ravi"},"wa_id":"919000000001"}],"messages":[{"from":"919000000001","id":"wamid.1","timestamp":"1700000000","type":"text","text":{"body":"Tomato price today"}}]}]}}]}'

                # 3. See what got stored
                curl https://&lt;your-host&gt;/api/farmers</pre>

                  <footer>Kissan-AI 1.0.0 · default language %s (%s) · default region %s</footer>
                </main>
                </body>
                </html>
                """.formatted(
                        farmers,
                        conversations,
                        messages,
                        metaStatus(),
                        openwaStatus(),
                        openwaConfig.sendReplies() ? "enabled" : "disabled",
                        appConfig.defaultLanguage(),
                        String.join(", ", appConfig.supportedLanguages()),
                        appConfig.defaultState()
                );
    }

    private String metaStatus() {
        boolean configured = whatsappConfig.phoneNumberId().isPresent()
                && whatsappConfig.accessToken().isPresent();
        return configured
                ? "<span class=\"badge-ok\">connected</span> (phone number ID + access token set)"
                : "<span class=\"badge-info\">receive-only</span> — phone number ID / access token not configured; webhook still processes inbound messages";
    }

    private String openwaStatus() {
        Optional<String> key = openwaConfig.apiKey();
        if (key.isPresent() && !key.get().isBlank()) {
            return "<span class=\"badge-ok\">configured</span> → " + esc(openwaConfig.apiBaseUrl());
        }
        return "<span class=\"badge-info\">not configured</span> — replies are stored but not delivered; set OPENWA_API_KEY to enable";
    }

    private static String esc(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}