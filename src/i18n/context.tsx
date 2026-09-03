'use client';

import { createContext, useContext, useState, ReactNode } from 'react';
import { en } from './en';

type LangCode = 'en' | 'hi' | 'mr' | 'ta' | 'te' | 'kn' | 'gu' | 'bn' | 'ml' | 'pa' | 'or' | 'as';

interface LangContextType {
  lang: LangCode;
  setLang: (l: LangCode) => void;
  t: typeof en;
}

const LangContext = createContext<LangContextType>({
  lang: 'en',
  setLang: () => {},
  t: en,
});

export function LangProvider({ children }: { children: ReactNode }) {
  const [lang, setLang] = useState<LangCode>('en');

  // Dynamic import translations
  const [translations, setTranslations] = useState<typeof en>(en);

  const handleSetLang = async (newLang: LangCode) => {
    setLang(newLang);
    if (newLang === 'en') {
      setTranslations(en);
    } else {
      try {
        const mod = await import(`./${newLang}`);
        setTranslations(mod.default || mod[Object.keys(mod)[0]]);
      } catch {
        setTranslations(en);
      }
    }
  };

  return (
    <LangContext.Provider value={{ lang, setLang: handleSetLang, t: translations }}>
      {children}
    </LangContext.Provider>
  );
}

export function useLang() {
  return useContext(LangContext);
}

export const languages: { code: LangCode; name: string; native: string }[] = [
  { code: 'en', name: 'English', native: 'English' },
  { code: 'hi', name: 'Hindi', native: 'हिन्दी' },
  { code: 'mr', name: 'Marathi', native: 'मराठी' },
  { code: 'ta', name: 'Tamil', native: 'தமிழ்' },
  { code: 'te', name: 'Telugu', native: 'తెలుగు' },
  { code: 'kn', name: 'Kannada', native: 'ಕನ್ನಡ' },
  { code: 'gu', name: 'Gujarati', native: 'ગુજરાતી' },
  { code: 'bn', name: 'Bengali', native: 'বাংলা' },
  { code: 'ml', name: 'Malayalam', native: 'മലയാളം' },
  { code: 'pa', name: 'Punjabi', native: 'ਪੰਜਾਬੀ' },
  { code: 'or', name: 'Odia', native: 'ଓଡ଼ିଆ' },
  { code: 'as', name: 'Assamese', native: 'অসমীয়া' },
];
