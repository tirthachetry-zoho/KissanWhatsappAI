'use client';

import { motion, useInView } from 'framer-motion';
import { useRef } from 'react';

export default function CTA() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: '-100px' });

  return (
    <section id="cta" className="relative py-32">
      <div className="max-w-4xl mx-auto px-5 lg:px-6" ref={ref}>
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.8 }}
          className="relative overflow-hidden rounded-3xl bg-brand-night"
        >
          <div className="absolute inset-0 os-scrim" />

          {/* Content */}
          <div className="relative z-10 p-12 md:p-16 text-center">
            <div className="text-5xl mb-6">🌾</div>
            <h2 className="font-display text-3xl md:text-5xl font-semibold mb-4 text-white">
              Start Farming <span className="text-[var(--color-voice)]">Smarter</span>
            </h2>
            <p className="text-white/50 text-lg max-w-lg mx-auto mb-8">
              Five applications, one platform. Save our number, send a message in your language, and get instant AI-powered agricultural guidance.
            </p>

            {/* Phone Number */}
            <div className="os-glass rounded-2xl p-6 max-w-sm mx-auto mb-8">
              <div className="text-sm text-white/40 mb-2">WhatsApp us at</div>
              <div className="text-3xl font-bold text-[var(--color-voice)] tracking-wide">
                +91 98765 43210
              </div>
              <div className="text-sm text-white/40 mt-2">Available 24/7 • Free forever</div>
            </div>

            {/* Language Badges */}
            <div className="flex flex-wrap justify-center gap-2 mb-8">
              {['हिन्दी', 'मराठी', 'தமிழ்', 'తెలుగు', 'ಕನ್ನಡ', 'ગુજરાતી', 'ਪੰਜਾਬੀ', 'বাংলা', 'മലയാളം', 'ଓଡ଼ିଆ', 'অসমীয়া', 'English'].map(
                (lang) => (
                  <span
                    key={lang}
                    className="px-3 py-1.5 rounded-full bg-white/5 border border-white/10 text-xs text-white/60"
                  >
                    {lang}
                  </span>
                )
              )}
            </div>

            {/* CTA Buttons */}
            <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
              <button className="px-8 py-4 bg-[var(--color-voice)] text-[var(--color-voice-ink)] rounded-2xl font-bold text-lg hover:bg-[var(--color-voice)]/90 transition-all hover:scale-105 flex items-center gap-2 shadow-lg shadow-[var(--color-voice)]/20">
                <svg className="w-6 h-6" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413z" />
                </svg>
                Open WhatsApp
              </button>
              <button className="os-glass px-8 py-4 rounded-2xl font-semibold text-white/80 hover:text-white transition-all">
                Partner With Us →
              </button>
            </div>
          </div>
        </motion.div>
      </div>
    </section>
  );
}
