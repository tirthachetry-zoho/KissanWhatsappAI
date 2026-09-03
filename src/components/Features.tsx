'use client';

import { useRef, useState } from 'react';
import { motion, useInView } from 'framer-motion';
import { useLang } from '@/i18n/context';

const featuresData = [
  { icon: '🔬', titleKey: 'Crop Disease Detection', stat: '94% accuracy', color: 'vision', bg: 'bg-[var(--color-vision)]/5', border: 'border-[var(--color-vision)]/20', statBg: 'bg-[var(--color-vision)]/10', statBorder: 'border-[var(--color-vision)]/20', statText: 'text-[var(--color-vision-ink)]', desc: { en: 'Upload a photo of your crop. Our AI identifies diseases, pests, and nutrient deficiencies from low-quality smartphone photos in real-time.', hi: 'अपनी फसल की फोटो अपलोड करें। हमारा AI कम गुणवत्ता वाली तस्वीरों से रोग, कीट और पोषक तत्वों की कमी की पहचान करता है।' } },
  { icon: '🎙️', titleKey: 'Voice-First Design', stat: '22+ languages', color: 'voice', bg: 'bg-[var(--color-voice)]/5', border: 'border-[var(--color-voice)]/20', statBg: 'bg-[var(--color-voice)]/10', statBorder: 'border-[var(--color-voice)]/20', statText: 'text-[var(--color-voice-ink)]', desc: { en: 'Ask questions in your language, in your voice. No typing needed. Supports 22+ Indian languages with natural voice responses.', hi: 'अपनी भाषा में, अपनी आवाज़ में सवाल पूछें। टाइपिंग की ज़रूरत नहीं। 22+ भारतीय भाषाओं में प्राकृतिक वॉइस प्रतिक्रियाएं।' } },
  { icon: '💧', titleKey: 'Smart Irrigation', stat: '40% water saved', color: 'engage', bg: 'bg-[var(--color-engage)]/5', border: 'border-[var(--color-engage)]/20', statBg: 'bg-[var(--color-engage)]/10', statBorder: 'border-[var(--color-engage)]/20', statText: 'text-[var(--color-engage-ink)]', desc: { en: 'Get personalized watering schedules based on your location, weather forecasts, soil type, and crop growth stage.', hi: 'अपनी स्थिति, मौसम, मिट्टी के प्रकार और फसल के चरण के आधार पर व्यक्तिगत सिंचाई शेड्यूल पाएं।' } },
  { icon: '💰', titleKey: 'Live Mandi Prices', stat: '7,000+ mandis', color: 'agents', bg: 'bg-[var(--color-agents)]/5', border: 'border-[var(--color-agents)]/20', statBg: 'bg-[var(--color-agents)]/10', statBorder: 'border-[var(--color-agents)]/20', statText: 'text-[var(--color-agents-ink)]', desc: { en: 'Real-time prices from 7,000+ mandis across India. Get alerts when prices peak for your crop in your nearest market.', hi: 'भारत भर की 7,000+ मंडियों से रीयल-टाइम भाव। जब आपकी फसल का भाव चरम पर हो तो अलर्ट पाएं।' } },
  { icon: '🏛️', titleKey: 'Govt. Schemes', stat: '50+ schemes', color: 'knowledge', bg: 'bg-[var(--color-knowledge)]/5', border: 'border-[var(--color-knowledge)]/20', statBg: 'bg-[var(--color-knowledge)]/10', statBorder: 'border-[var(--color-knowledge)]/20', statText: 'text-[var(--color-knowledge-ink)]', desc: { en: 'Instant eligibility check for PM-KISAN, PMFBY, and 50+ state schemes. Get step-by-step application guidance in your language.', hi: 'पीएम-किसान, पीएमएफबीवाई और 50+ राज्य योजनाओं की तत्काल पात्रता जांच। अपनी भाषा में चरण-दर-चरण मार्गदर्शन।' } },
  { icon: '🌱', titleKey: 'Soil Health Analysis', stat: '22 soil params', color: 'primary', bg: 'bg-[var(--color-primary)]/5', border: 'border-[var(--color-primary)]/20', statBg: 'bg-[var(--color-primary)]/10', statBorder: 'border-[var(--color-primary)]/20', statText: 'text-[var(--color-primary)]', desc: { en: 'Connect your Soil Health Card data for customized fertilizer recommendations and soil improvement plans.', hi: 'अनुकूलित उर्वरक सिफारिशों और मिट्टी सुधार योजनाओं के लिए अपना मृदा स्वास्थ्य कार्ड जोड़ें।' } },
];

function TiltCard({ children, className }: { children: React.ReactNode; className?: string }) {
  const [tilt, setTilt] = useState({ x: 0, y: 0 });
  const [hovering, setHovering] = useState(false);
  return (
    <div
      className={className}
      onMouseMove={(e) => {
        const rect = e.currentTarget.getBoundingClientRect();
        setTilt({ x: ((e.clientY - rect.top) / rect.height - 0.5) * -8, y: ((e.clientX - rect.left) / rect.width - 0.5) * 8 });
      }}
      onMouseEnter={() => setHovering(true)}
      onMouseLeave={() => { setHovering(false); setTilt({ x: 0, y: 0 }); }}
      style={{ transform: `perspective(800px) rotateX(${tilt.x}deg) rotateY(${tilt.y}deg)`, transition: hovering ? 'transform 0.1s ease-out' : 'transform 0.5s ease-out' }}
    >
      {children}
    </div>
  );
}

export default function Features() {
  const { lang, t } = useLang();
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: '-100px' });

  return (
    <section id="features" className="relative py-32 bg-[var(--color-mint)]">
      <div className="max-w-[1400px] mx-auto px-5 lg:px-6" ref={ref}>
        <motion.div initial={{ opacity: 0, y: 30 }} animate={isInView ? { opacity: 1, y: 0 } : {}} transition={{ duration: 0.8 }} className="text-center mb-16">
          <span className="text-sm font-medium text-[var(--color-primary)] uppercase tracking-widest mb-4 block">{t.features.badge}</span>
          <h2 className="font-display text-4xl md:text-5xl font-semibold mb-6 text-[var(--color-midnight)]">
            {t.features.title} <span className="text-[var(--color-primary)]">{t.features.titleHighlight}</span>
          </h2>
          <p className="text-[var(--color-muted-foreground)] text-lg max-w-xl mx-auto">{t.features.desc}</p>
        </motion.div>
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {featuresData.map((feature, i) => (
            <motion.div key={i} initial={{ opacity: 0, y: 30 }} animate={isInView ? { opacity: 1, y: 0 } : {}} transition={{ duration: 0.6, delay: i * 0.08 }}>
              <TiltCard className="glass-card rounded-2xl p-6 h-full cursor-default group">
                <div className={`w-12 h-12 rounded-xl ${feature.bg} border ${feature.border} flex items-center justify-center text-2xl mb-4 group-hover:scale-110 transition-transform`}>{feature.icon}</div>
                <h3 className="text-lg font-bold text-[var(--color-foreground)] mb-2">{feature.titleKey}</h3>
                <p className="text-sm text-[var(--color-muted-foreground)] leading-relaxed mb-4">{feature.desc[lang as keyof typeof feature.desc] || feature.desc.en}</p>
                <div className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full ${feature.statBg} border ${feature.statBorder}`}>
                  <span className={`w-1.5 h-1.5 rounded-full bg-current ${feature.statText}`} />
                  <span className={`text-xs font-semibold ${feature.statText}`}>{feature.stat}</span>
                </div>
              </TiltCard>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
