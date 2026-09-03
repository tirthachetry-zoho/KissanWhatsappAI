'use client';

import { useRef, useEffect, useState } from 'react';
import { motion, useInView } from 'framer-motion';
import { pipelineSteps } from '@/data/scenarios';
import { useLang } from '@/i18n/context';

export default function HowItWorks() {
  const { t } = useLang();
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: '-100px' });
  const [activeStep, setActiveStep] = useState(0);

  useEffect(() => {
    if (!isInView) return;
    const interval = setInterval(() => {
      setActiveStep((prev) => (prev + 1) % pipelineSteps.length);
    }, 2500);
    return () => clearInterval(interval);
  }, [isInView]);

  return (
    <section id="how-it-works" className="relative py-32 bg-[var(--color-mint)]">
      <div className="max-w-[1400px] mx-auto px-5 lg:px-6" ref={ref}>
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.8 }}
          className="text-center mb-20"
        >
          <span className="text-sm font-medium text-[var(--color-primary)] uppercase tracking-widest mb-4 block">
            {t.howItWorks.badge}
          </span>
          <h2 className="font-display text-4xl md:text-5xl font-semibold mb-6 text-[var(--color-midnight)]">
            {t.howItWorks.title} <span className="text-[var(--color-primary)]">{t.howItWorks.titleHighlight}</span>
          </h2>
          <p className="text-[var(--color-muted-foreground)] text-lg max-w-xl mx-auto">
            {t.howItWorks.desc}
          </p>
        </motion.div>

        {/* Horizontal Pipeline */}
        <div className="hidden lg:block mb-16">
          <div className="flex items-center justify-between relative">
            <div className="absolute top-14 left-[8%] right-[8%] h-px bg-[var(--color-border)]" />
            <div
              className="absolute top-14 left-[8%] h-px bg-[var(--color-primary)] transition-all duration-1000"
              style={{ width: `${(activeStep / (pipelineSteps.length - 1)) * 84}%` }}
            />
            {pipelineSteps.map((step, i) => (
              <motion.div
                key={step.step}
                initial={{ opacity: 0, y: 30 }}
                animate={isInView ? { opacity: 1, y: 0 } : {}}
                transition={{ duration: 0.6, delay: i * 0.1 }}
                className="flex flex-col items-center relative z-10 w-1/6"
              >
                <div className={`w-28 h-28 rounded-2xl flex items-center justify-center text-3xl transition-all duration-500 ${
                  i <= activeStep ? 'bg-[var(--color-primary)]/10 border-2 border-[var(--color-primary)]/30 scale-110 shadow-lg shadow-[var(--color-primary)]/10' : 'bg-white border border-[var(--color-border)]'
                }`}>
                  {step.icon}
                </div>
                <div className="mt-4 text-center">
                  <div className={`text-xs font-bold uppercase tracking-wider mb-1 transition-colors ${i <= activeStep ? 'text-[var(--color-primary)]' : 'text-[var(--color-muted-foreground)]'}`}>
                    Step {step.step}
                  </div>
                  <div className="text-sm font-semibold text-[var(--color-foreground)] mb-1">{step.title}</div>
                  <div className="text-xs text-[var(--color-muted-foreground)]">{step.desc}</div>
                </div>
              </motion.div>
            ))}
          </div>
        </div>

        {/* Mobile Pipeline */}
        <div className="lg:hidden space-y-4">
          {pipelineSteps.map((step, i) => (
            <motion.div
              key={step.step}
              initial={{ opacity: 0, x: -30 }}
              animate={isInView ? { opacity: 1, x: 0 } : {}}
              transition={{ duration: 0.5, delay: i * 0.1 }}
              className={`flex items-start gap-4 p-4 rounded-2xl transition-all duration-500 ${i <= activeStep ? 'bg-white shadow-md border border-[var(--color-primary)]/20' : 'bg-white/50'}`}
            >
              <div className={`w-14 h-14 rounded-xl flex items-center justify-center text-2xl shrink-0 transition-all ${i <= activeStep ? 'bg-[var(--color-primary)]/10 border border-[var(--color-primary)]/30' : 'bg-[var(--color-muted)] border border-[var(--color-border)]'}`}>
                {step.icon}
              </div>
              <div>
                <div className="text-xs font-bold text-[var(--color-primary)] mb-0.5">Step {step.step}</div>
                <div className="text-sm font-semibold text-[var(--color-foreground)]">{step.title}</div>
                <div className="text-xs text-[var(--color-muted-foreground)] mt-0.5">{step.detail}</div>
              </div>
            </motion.div>
          ))}
        </div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.8, delay: 0.5 }}
          className="glass-card rounded-2xl p-6 max-w-2xl mx-auto text-center mt-8"
        >
          <div className="text-4xl mb-3">{pipelineSteps[activeStep].icon}</div>
          <div className="text-lg font-bold text-[var(--color-foreground)] mb-2">
            Step {pipelineSteps[activeStep].step}: {pipelineSteps[activeStep].title}
          </div>
          <div className="text-[var(--color-muted-foreground)] text-sm">{pipelineSteps[activeStep].detail}</div>
        </motion.div>
      </div>
    </section>
  );
}
