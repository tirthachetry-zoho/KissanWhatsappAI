'use client';

import { useRef, useEffect, useState } from 'react';
import { motion, useInView } from 'framer-motion';
import { dashboardData } from '@/data/scenarios';
import { useLang } from '@/i18n/context';

function AnimatedCounter({ target, suffix = '' }: { target: number; suffix?: string }) {
  const [count, setCount] = useState(0);
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true });
  useEffect(() => {
    if (!isInView) return;
    const duration = 2000, steps = 60, increment = target / steps;
    let current = 0;
    const timer = setInterval(() => { current += increment; if (current >= target) { setCount(target); clearInterval(timer); } else setCount(Math.floor(current)); }, duration / steps);
    return () => clearInterval(timer);
  }, [isInView, target]);
  return <span ref={ref} className="tabular-nums">{count.toLocaleString('en-IN')}{suffix}</span>;
}

export default function Dashboard() {
  const { t } = useLang();
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: '-100px' });
  const maxCount = Math.max(...dashboardData.topQueries.map((q) => q.count));

  return (
    <section id="dashboard" className="relative py-32 bg-white">
      <div className="max-w-[1400px] mx-auto px-5 lg:px-6" ref={ref}>
        <motion.div initial={{ opacity: 0, y: 30 }} animate={isInView ? { opacity: 1, y: 0 } : {}} transition={{ duration: 0.8 }} className="text-center mb-16">
          <span className="text-sm font-medium text-[var(--color-primary)] uppercase tracking-widest mb-4 block">{t.dashboard.badge}</span>
          <h2 className="font-display text-4xl md:text-5xl font-semibold mb-6 text-[var(--color-midnight)]">
            {t.dashboard.title.split(t.dashboard.titleHighlight)[0]}<span className="text-[var(--color-primary)]">{t.dashboard.titleHighlight}</span>
          </h2>
          <p className="text-[var(--color-muted-foreground)] text-lg max-w-xl mx-auto">{t.dashboard.desc}</p>
        </motion.div>

        <motion.div initial={{ opacity: 0, y: 20 }} animate={isInView ? { opacity: 1, y: 0 } : {}} transition={{ duration: 0.6, delay: 0.2 }} className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 mb-12">
          {[
            { value: dashboardData.stats.totalQueries, label: t.dashboard.queries, suffix: '+', icon: '📊' },
            { value: dashboardData.stats.farmersServed, label: t.dashboard.farmers, suffix: '+', icon: '👨‍🌾' },
            { value: dashboardData.stats.languagesSupported, label: t.dashboard.languages, icon: '🌐' },
            { value: dashboardData.stats.statesCovered, label: t.dashboard.states, icon: '🗺️' },
          ].map((stat, i) => (
            <div key={i} className="glass-card rounded-xl p-4 text-center">
              <div className="text-xl mb-2">{stat.icon}</div>
              <div className="text-2xl md:text-3xl font-bold text-[var(--color-primary)]"><AnimatedCounter target={stat.value} suffix={stat.suffix || ''} /></div>
              <div className="text-xs text-[var(--color-muted-foreground)] mt-1">{stat.label}</div>
            </div>
          ))}
          <div className="glass-card rounded-xl p-4 text-center">
            <div className="text-xl mb-2">⚡</div>
            <div className="text-2xl md:text-3xl font-bold text-[var(--color-primary)]">{dashboardData.stats.avgResponseTime}</div>
            <div className="text-xs text-[var(--color-muted-foreground)] mt-1">{t.dashboard.response}</div>
          </div>
          <div className="glass-card rounded-xl p-4 text-center">
            <div className="text-xl mb-2">🎯</div>
            <div className="text-2xl md:text-3xl font-bold text-[var(--color-primary)]">{dashboardData.stats.accuracy}</div>
            <div className="text-xs text-[var(--color-muted-foreground)] mt-1">{t.dashboard.accuracy}</div>
          </div>
        </motion.div>

        <div className="grid lg:grid-cols-2 gap-8">
          <motion.div initial={{ opacity: 0, x: -30 }} animate={isInView ? { opacity: 1, x: 0 } : {}} transition={{ duration: 0.6, delay: 0.3 }} className="glass-card rounded-2xl p-6">
            <h3 className="text-lg font-bold text-[var(--color-foreground)] mb-6">{t.dashboard.topQueries}</h3>
            <div className="space-y-4">
              {dashboardData.topQueries.map((query, i) => (
                <div key={i}>
                  <div className="flex items-center justify-between mb-1.5">
                    <span className="text-sm text-[var(--color-muted-foreground)]">{query.category}</span>
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-semibold text-[var(--color-foreground)]">{(query.count / 1000).toFixed(1)}K</span>
                      <span className="text-xs text-[var(--color-primary)]">{query.trend}</span>
                    </div>
                  </div>
                  <div className="h-2.5 bg-[var(--color-muted)] rounded-full overflow-hidden">
                    <div className="bar-fill h-full rounded-full bg-gradient-to-r from-[var(--color-primary)] to-emerald-400" style={{ width: isInView ? `${(query.count / maxCount) * 100}%` : '0%', transitionDelay: `${0.5 + i * 0.1}s` }} />
                  </div>
                </div>
              ))}
            </div>
          </motion.div>

          <motion.div initial={{ opacity: 0, x: 30 }} animate={isInView ? { opacity: 1, x: 0 } : {}} transition={{ duration: 0.6, delay: 0.4 }} className="glass-card rounded-2xl p-6">
            <h3 className="text-lg font-bold text-[var(--color-foreground)] mb-6">{t.dashboard.regional}</h3>
            <div className="grid grid-cols-2 gap-3">
              {dashboardData.regions.map((region, i) => {
                const maxQ = Math.max(...dashboardData.regions.map((r) => r.queries));
                const intensity = region.queries / maxQ;
                return (
                  <div key={i} className="rounded-xl p-3 border border-[var(--color-border)] transition-all hover:border-[var(--color-primary)]/30 hover:shadow-sm" style={{ background: `rgba(36, 143, 71, ${intensity * 0.08})` }}>
                    <div className="text-sm font-semibold text-[var(--color-foreground)]">{region.name}</div>
                    <div className="text-lg font-bold text-[var(--color-primary)]">{(region.queries / 1000).toFixed(1)}K</div>
                    <div className="flex flex-wrap gap-1 mt-1">
                      {region.crops.map((crop, ci) => (
                        <span key={ci} className="text-[10px] px-1.5 py-0.5 rounded bg-[var(--color-muted)] text-[var(--color-muted-foreground)]">{crop}</span>
                      ))}
                    </div>
                  </div>
                );
              })}
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  );
}
