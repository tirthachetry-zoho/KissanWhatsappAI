'use client';

import { useEffect, useState, useRef } from 'react';
import { motion } from 'framer-motion';

const headlines = [
  'Your AI Agronomist',
  'Crop Advisor in Your Pocket',
  'Vernacular Farming AI',
  '22 Languages. One Platform.',
];

export default function Hero() {
  const [headlineIndex, setHeadlineIndex] = useState(0);
  const [displayText, setDisplayText] = useState('');
  const [isDeleting, setIsDeleting] = useState(false);
  const timeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    const current = headlines[headlineIndex];
    if (!isDeleting && displayText === current) {
      timeoutRef.current = setTimeout(() => setIsDeleting(true), 2500);
      return () => clearTimeout(timeoutRef.current!);
    }
    if (isDeleting && displayText === '') {
      setIsDeleting(false);
      setHeadlineIndex((prev) => (prev + 1) % headlines.length);
      return;
    }
    const speed = isDeleting ? 30 : 60;
    timeoutRef.current = setTimeout(() => {
      setDisplayText(
        isDeleting
          ? current.slice(0, displayText.length - 1)
          : current.slice(0, displayText.length + 1)
      );
    }, speed);
    return () => clearTimeout(timeoutRef.current!);
  }, [displayText, isDeleting, headlineIndex]);

  return (
    <section className="relative min-h-screen flex items-center justify-center bg-brand-night overflow-hidden">
      {/* Scrim overlay */}
      <div className="absolute inset-0 os-scrim" />

      {/* Floating accent orbs */}
      <div className="absolute top-1/4 left-[15%] w-80 h-80 bg-[var(--color-primary)]/10 rounded-full blur-3xl os-float" />
      <div className="absolute bottom-1/3 right-[10%] w-64 h-64 bg-[var(--color-voice)]/5 rounded-full blur-3xl os-float-slow" />

      <div className="relative z-10 max-w-6xl mx-auto px-5 pt-24 pb-16 text-center">
        {/* Badge */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="inline-flex items-center gap-2 px-4 py-2 rounded-full os-glass mb-8"
        >
          <span className="w-2 h-2 rounded-full bg-[var(--color-voice)] os-live" />
          <span className="text-sm text-white/70">
            Powered by Bhashini + AgriStack • ICAR-validated data
          </span>
        </motion.div>

        {/* Main Heading — Serif Display */}
        <motion.h1
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.2 }}
          className="font-display text-5xl md:text-[4.5rem] lg:text-[5.5rem] font-semibold tracking-tight leading-[1.1] mb-6 text-white"
        >
          India&apos;s <span className="text-[var(--color-voice)]">WhatsApp</span>
          <br />
          <span className="text-white/60 text-3xl md:text-4xl lg:text-5xl font-normal font-sans">
            {displayText}
            <span className="typing-cursor" />
          </span>
        </motion.h1>

        {/* Subtitle */}
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.4 }}
          className="text-lg md:text-xl text-white/50 max-w-2xl mx-auto mb-12 leading-relaxed"
        >
          Send a voice note in your language. Get research-backed advice on crop diseases,
          mandi prices, irrigation, and government schemes — in under 3 seconds.
        </motion.p>

        {/* CTA Buttons */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.6 }}
          className="flex flex-col sm:flex-row items-center justify-center gap-4 mb-16"
        >
          <a
            href="#demo"
            className="group relative px-8 py-4 bg-[var(--color-voice)] text-[var(--color-voice-ink)] rounded-2xl font-bold text-lg hover:bg-[var(--color-voice)]/90 transition-all hover:scale-105 shadow-lg shadow-[var(--color-voice)]/20"
          >
            <span className="relative z-10 flex items-center gap-2">
              Try the Demo
              <svg className="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 8l4 4m0 0l-4 4m4-4H3" />
              </svg>
            </span>
          </a>
          <a
            href="#how-it-works"
            className="os-glass px-8 py-4 rounded-2xl font-semibold text-white/80 hover:text-white transition-all"
          >
            See How It Works →
          </a>
        </motion.div>

        {/* 5 Application Windows — OS-like dock */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 0.8 }}
          className="flex flex-wrap justify-center gap-4 mb-16"
        >
          {[
            { icon: '🔬', name: 'Disease Detection', color: 'vision', desc: 'AI-powered crop disease diagnosis' },
            { icon: '🎙️', name: 'Voice Advisory', color: 'voice', desc: '22+ languages, voice-first design' },
            { icon: '💧', name: 'Irrigation', color: 'engage', desc: 'Weather-aware smart watering' },
            { icon: '💰', name: 'Mandi Prices', color: 'agents', desc: '7,000+ mandis, real-time prices' },
            { icon: '🏛️', name: 'Govt. Schemes', color: 'knowledge', desc: '50+ schemes, eligibility check' },
          ].map((app, i) => (
            <div
              key={app.name}
              className="os-window rounded-2xl p-5 w-44 text-center os-dock-icon hover:scale-105 cursor-default group"
            >
              <div className="text-3xl mb-2">{app.icon}</div>
              <div className="text-sm font-semibold text-white mb-1">{app.name}</div>
              <div className="text-xs text-white/40 leading-snug">{app.desc}</div>
              <div className={`mt-3 w-full h-1 rounded-full bg-[var(--color-${app.color})]/30`}>
                <div className={`h-full rounded-full bg-[var(--color-${app.color})] w-0 group-hover:w-full transition-all duration-500`} />
              </div>
            </div>
          ))}
        </motion.div>

        {/* Stats Row */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 1 }}
          className="flex flex-wrap justify-center gap-8 md:gap-16"
        >
          {[
            { value: '22+', label: 'Languages' },
            { value: '50K+', label: 'Queries Served' },
            { value: '2.3s', label: 'Avg Response' },
            { value: '94%', label: 'Accuracy' },
          ].map((stat, i) => (
            <div key={i} className="text-center">
              <div className="text-3xl md:text-4xl font-bold text-white mb-1">{stat.value}</div>
              <div className="text-sm text-white/40">{stat.label}</div>
            </div>
          ))}
        </motion.div>
      </div>

      {/* Scroll indicator */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1.5 }}
        className="absolute bottom-8 left-1/2 -translate-x-1/2"
      >
        <div className="w-6 h-10 rounded-full border-2 border-white/20 flex items-start justify-center p-1.5">
          <motion.div
            animate={{ y: [0, 12, 0] }}
            transition={{ duration: 1.5, repeat: Infinity }}
            className="w-1.5 h-1.5 rounded-full bg-[var(--color-voice)]"
          />
        </div>
      </motion.div>
    </section>
  );
}
