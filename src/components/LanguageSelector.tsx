'use client';

import { useState, useRef, useEffect } from 'react';
import { useLang, languages } from '@/i18n/context';

export default function LanguageSelector() {
  const { lang, setLang } = useLang();
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClick = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClick);
    return () => document.removeEventListener('mousedown', handleClick);
  }, []);

  const current = languages.find((l) => l.code === lang)!;

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setOpen(!open)}
        className="flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm font-medium text-[var(--color-muted-foreground)] hover:text-[var(--color-foreground)] hover:bg-[var(--color-muted)] transition-colors border border-[var(--color-border)]"
      >
        🌐 {current.native}
        <svg className={`w-3 h-3 transition-transform ${open ? 'rotate-180' : ''}`} fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      </button>

      {open && (
        <div className="absolute right-0 top-full mt-2 w-48 bg-white rounded-xl border border-[var(--color-border)] shadow-lg py-2 z-50 max-h-80 overflow-y-auto">
          {languages.map((l) => (
            <button
              key={l.code}
              onClick={() => { setLang(l.code); setOpen(false); }}
              className={`w-full text-left px-4 py-2 text-sm flex items-center gap-2 transition-colors ${
                lang === l.code
                  ? 'bg-[var(--color-primary)]/5 text-[var(--color-primary)] font-medium'
                  : 'text-[var(--color-foreground)] hover:bg-[var(--color-muted)]'
              }`}
            >
              <span className="text-xs text-[var(--color-muted-foreground)] w-16">{l.name}</span>
              <span>{l.native}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
