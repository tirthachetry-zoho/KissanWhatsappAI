'use client';

import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useLang } from '@/i18n/context';
import LanguageSelector from './LanguageSelector';

const navLinks = [
  { key: 'howItWorks', href: '#how-it-works' },
  { key: 'demo', href: '#demo' },
  { key: 'features', href: '#features' },
  { key: 'dashboard', href: '#dashboard' },
];

export default function Navbar() {
  const { t } = useLang();
  const [scrolled, setScrolled] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);
  const [activeSection, setActiveSection] = useState('');

  useEffect(() => {
    const onScroll = () => {
      setScrolled(window.scrollY > 50);
      const sections = navLinks.map((l) => l.href.slice(1));
      for (let i = sections.length - 1; i >= 0; i--) {
        const el = document.getElementById(sections[i]);
        if (el && el.getBoundingClientRect().top < 200) {
          setActiveSection(sections[i]);
          break;
        }
      }
    };
    window.addEventListener('scroll', onScroll, { passive: true });
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  return (
    <>
      <motion.nav
        initial={{ y: -100 }}
        animate={{ y: 0 }}
        transition={{ duration: 0.6, ease: [0.4, 0, 0.2, 1] }}
        className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ${
          scrolled
            ? 'bg-white/80 backdrop-blur-xl border-b border-[var(--color-border)] shadow-sm'
            : 'bg-transparent'
        }`}
      >
        <div className="max-w-[1400px] mx-auto px-5 lg:px-6 h-16 flex items-center justify-between">
          {/* Logo */}
          <a href="#" className="flex items-center gap-2.5 group">
            <div className="w-8 h-8 rounded-lg bg-[var(--color-primary)] flex items-center justify-center text-base font-bold text-white group-hover:scale-110 transition-transform">
              🌾
            </div>
            <span className="text-lg font-bold tracking-tight">
              <span className="font-display text-[var(--color-midnight)]">Kissan</span>
              <span className="text-[var(--color-primary)]">AI</span>
            </span>
          </a>

          {/* Desktop Nav */}
          <div className="hidden md:flex items-center gap-6">
            {navLinks.map((link) => (
              <a
                key={link.href}
                href={link.href}
                className={`nav-link text-sm font-medium transition-colors ${
                  activeSection === link.href.slice(1)
                    ? 'text-[var(--color-primary)] active'
                    : 'text-[var(--color-muted-foreground)] hover:text-[var(--color-foreground)]'
                }`}
              >
                {t.nav[link.key as keyof typeof t.nav]}
              </a>
            ))}
            <LanguageSelector />
            <a
              href="#cta"
              className="px-5 py-2 bg-[var(--color-midnight)] text-white rounded-full text-sm font-semibold hover:bg-[var(--color-plum)] transition-colors"
            >
              {t.nav.cta}
            </a>
          </div>

          {/* Mobile Hamburger */}
          <div className="md:hidden flex items-center gap-2">
            <LanguageSelector />
            <button
              onClick={() => setMobileOpen(!mobileOpen)}
              className="w-10 h-10 flex items-center justify-center text-[var(--color-foreground)]"
            >
              <div className="space-y-1.5">
                <div className={`w-6 h-0.5 bg-current transition-all ${mobileOpen ? 'rotate-45 translate-y-2' : ''}`} />
                <div className={`w-6 h-0.5 bg-current transition-all ${mobileOpen ? 'opacity-0' : ''}`} />
                <div className={`w-6 h-0.5 bg-current transition-all ${mobileOpen ? '-rotate-45 -translate-y-2' : ''}`} />
              </div>
            </button>
          </div>
        </div>
      </motion.nav>

      {/* Mobile Menu */}
      <AnimatePresence>
        {mobileOpen && (
          <motion.div
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            className="fixed inset-x-0 top-16 z-40 bg-white/95 backdrop-blur-xl border-b border-[var(--color-border)] p-6 md:hidden"
          >
            <div className="flex flex-col gap-4">
              {navLinks.map((link) => (
                <a
                  key={link.href}
                  href={link.href}
                  onClick={() => setMobileOpen(false)}
                  className="text-[var(--color-muted-foreground)] hover:text-[var(--color-foreground)] text-lg font-medium py-2"
                >
                  {t.nav[link.key as keyof typeof t.nav]}
                </a>
              ))}
              <a
                href="#cta"
                onClick={() => setMobileOpen(false)}
                className="mt-2 px-5 py-3 bg-[var(--color-midnight)] text-white rounded-full text-center font-semibold"
              >
                {t.nav.cta}
              </a>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  );
}
