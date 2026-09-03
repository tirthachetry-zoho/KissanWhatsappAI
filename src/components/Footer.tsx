export default function Footer() {
  return (
    <footer className="relative border-t border-[var(--color-border)] bg-[var(--color-mint)]">
      <div className="max-w-[1400px] mx-auto px-5 lg:px-6 py-16">
        <div className="grid md:grid-cols-4 gap-10 mb-12">
          {/* Brand */}
          <div className="md:col-span-2">
            <div className="flex items-center gap-2.5 mb-4">
              <div className="w-8 h-8 rounded-lg bg-[var(--color-primary)] flex items-center justify-center text-base font-bold text-white">
                🌾
              </div>
              <span className="text-lg font-bold">
                <span className="font-display text-[var(--color-midnight)]">Kissan</span>
                <span className="text-[var(--color-primary)]">AI</span>
              </span>
            </div>
            <p className="text-sm text-[var(--color-muted-foreground)] leading-relaxed max-w-sm mb-6">
              India's vernacular AI agronomist on WhatsApp. Built with Bhashini, AgriStack, and
              ICAR-validated data to empower 140M+ farmers with real-time, research-backed
              agricultural guidance.
            </p>
            <div className="flex items-center gap-2 text-xs text-[var(--color-muted-foreground)]">
              <span className="w-2 h-2 rounded-full bg-[var(--color-primary)] os-live" />
              All systems operational
            </div>
          </div>

          {/* Links */}
          <div>
            <h4 className="text-sm font-semibold text-[var(--color-foreground)] uppercase tracking-wider mb-4">Features</h4>
            <ul className="space-y-2.5">
              {['Crop Disease Detection', 'Voice-First Design', 'Smart Irrigation', 'Live Mandi Prices', 'Govt. Schemes'].map((item) => (
                <li key={item}>
                  <a href="#" className="text-sm text-[var(--color-muted-foreground)] hover:text-[var(--color-primary)] transition-colors">
                    {item}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h4 className="text-sm font-semibold text-[var(--color-foreground)] uppercase tracking-wider mb-4">Company</h4>
            <ul className="space-y-2.5">
              {['About', 'Blog', 'Careers', 'Contact', 'Press Kit'].map((item) => (
                <li key={item}>
                  <a href="#" className="text-sm text-[var(--color-muted-foreground)] hover:text-[var(--color-primary)] transition-colors">
                    {item}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        </div>

        {/* Tech Stack */}
        <div className="border-t border-[var(--color-border)] pt-8 mb-8">
          <div className="text-xs text-[var(--color-muted-foreground)] text-center mb-4">Built with</div>
          <div className="flex flex-wrap justify-center gap-3">
            {[
              'WhatsApp Business API', 'Bhashini ASR/TTS', 'Gemini Vision',
              'AgriStack DPI', 'ICAR Knowledge Base', 'Pinecone RAG',
              'Next.js', 'Vercel',
            ].map((tech) => (
              <span
                key={tech}
                className="px-3 py-1.5 rounded-lg bg-white border border-[var(--color-border)] text-xs text-[var(--color-muted-foreground)]"
              >
                {tech}
              </span>
            ))}
          </div>
        </div>

        {/* Bottom */}
        <div className="flex flex-col md:flex-row items-center justify-between gap-4 text-xs text-[var(--color-muted-foreground)]">
          <div>© 2026 KissanAI. All rights reserved.</div>
          <div className="flex items-center gap-4">
            <a href="#" className="hover:text-[var(--color-primary)] transition-colors">Privacy</a>
            <a href="#" className="hover:text-[var(--color-primary)] transition-colors">Terms</a>
            <a href="#" className="hover:text-[var(--color-primary)] transition-colors">Security</a>
          </div>
        </div>
      </div>
    </footer>
  );
}
