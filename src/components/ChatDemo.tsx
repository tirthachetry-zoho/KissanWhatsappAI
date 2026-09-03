'use client';

import { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useLang } from '@/i18n/context';
import { getResponse, quickReplies, greetings } from '@/data/chatbot';

interface Message {
  role: 'user' | 'bot';
  text: string;
  id: number;
}

function TypewriterText({ text, onComplete }: { text: string; onComplete?: () => void }) {
  const [displayed, setDisplayed] = useState('');
  const [done, setDone] = useState(false);

  useEffect(() => {
    setDisplayed('');
    setDone(false);
    let i = 0;
    const interval = setInterval(() => {
      if (i < text.length) {
        setDisplayed(text.slice(0, i + 1));
        i++;
      } else {
        clearInterval(interval);
        setDone(true);
        onComplete?.();
      }
    }, 12);
    return () => clearInterval(interval);
  }, [text]);

  const renderText = (t: string) =>
    t.split('\n').map((line, idx) => {
      const parts = line.split(/\*\*(.*?)\*\*/g);
      return (
        <div key={idx} className={line === '' ? 'h-2' : ''}>
          {parts.map((part, pidx) =>
            pidx % 2 === 1 ? (
              <strong key={pidx} className="font-semibold">{part}</strong>
            ) : (
              <span key={pidx}>{part}</span>
            )
          )}
        </div>
      );
    });

  return (
    <div className="text-sm leading-relaxed">
      {renderText(displayed)}
      {!done && <span className="typing-cursor" />}
    </div>
  );
}

export default function ChatDemo() {
  const { lang, t } = useLang();
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const [showQuickReplies, setShowQuickReplies] = useState(true);
  const chatRef = useRef<HTMLDivElement>(null);
  const idRef = useRef(0);
  const initialized = useRef(false);

  // Show greeting on first load
  useEffect(() => {
    if (initialized.current) return;
    initialized.current = true;
    const greeting = greetings[lang] || greetings.en;
    setMessages([{ role: 'bot', text: greeting, id: idRef.current++ }]);
  }, [lang]);

  const handleSend = (text: string) => {
    if (!text.trim()) return;

    // Add user message
    const userMsg: Message = { role: 'user', text: text.trim(), id: idRef.current++ };
    setMessages((prev) => [...prev, userMsg]);
    setInput('');
    setShowQuickReplies(false);
    setIsTyping(true);

    // Simulate typing delay
    setTimeout(() => {
      const response = getResponse(text);
      const botMsg: Message = { role: 'bot', text: response.text, id: idRef.current++ };
      setMessages((prev) => [...prev, botMsg]);
      setIsTyping(false);
      setTimeout(() => setShowQuickReplies(true), 300);
    }, 800 + Math.random() * 1200);
  };

  useEffect(() => {
    if (chatRef.current) {
      chatRef.current.scrollTop = chatRef.current.scrollHeight;
    }
  }, [messages, isTyping]);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend(input);
    }
  };

  return (
    <section id="demo" className="relative py-32 bg-white">
      <div className="max-w-[1400px] mx-auto px-5 lg:px-6">
        {/* Header */}
        <motion.div initial={{ opacity: 0, y: 30 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true }} transition={{ duration: 0.8 }} className="text-center mb-16">
          <span className="text-sm font-medium text-[var(--color-primary)] uppercase tracking-widest mb-4 block">{t.demo.badge}</span>
          <h2 className="font-display text-4xl md:text-5xl font-semibold mb-6 text-[var(--color-midnight)]">
            {t.demo.title.split(t.demo.titleHighlight)[0]}<span className="text-[var(--color-primary)]">{t.demo.titleHighlight}</span>
          </h2>
          <p className="text-[var(--color-muted-foreground)] text-lg max-w-xl mx-auto">{t.demo.desc}</p>
        </motion.div>

        <div className="flex flex-col lg:flex-row gap-8 items-start max-w-5xl mx-auto">
          {/* Quick Replies Sidebar */}
          <motion.div initial={{ opacity: 0, x: -30 }} whileInView={{ opacity: 1, x: 0 }} viewport={{ once: true }} className="w-full lg:w-72 shrink-0 space-y-3">
            <div className="text-sm font-semibold text-[var(--color-muted-foreground)] uppercase tracking-wider mb-4">Quick Questions</div>
            {quickReplies.map((qr, i) => (
              <button
                key={i}
                onClick={() => handleSend(qr.keywords)}
                className="w-full text-left p-3 rounded-xl border border-[var(--color-border)] bg-[var(--color-mint)] hover:bg-white hover:shadow-sm hover:border-[var(--color-primary)]/30 transition-all duration-200 flex items-center gap-3"
              >
                <span className="text-xl">{qr.emoji}</span>
                <span className="text-sm font-medium text-[var(--color-foreground)]">{qr.label}</span>
              </button>
            ))}
            <div className="mt-6 p-4 rounded-xl bg-[var(--color-primary)]/5 border border-[var(--color-primary)]/10">
              <div className="text-xs font-semibold text-[var(--color-primary)] mb-2">💡 Try asking in any language:</div>
              <div className="text-xs text-[var(--color-muted-foreground)] space-y-1">
                <div>• "मेरी फसल में रोग है"</div>
                <div>• "விவசாய உதவி வேண்டும்"</div>
                <div>• "गहूं का भाव आज"</div>
                <div>• "my crop has disease"</div>
              </div>
            </div>
          </motion.div>

          {/* Phone Frame */}
          <motion.div initial={{ opacity: 0, y: 30 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true }} className="flex-1 flex justify-center">
            <div className="w-full max-w-sm">
              <div className="rounded-[2rem] bg-[var(--color-midnight)] p-3 shadow-2xl shadow-black/20">
                {/* WhatsApp Header */}
                <div className="bg-[#075e54] rounded-t-2xl px-4 py-3 flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-[var(--color-primary)] flex items-center justify-center text-lg">🌾</div>
                  <div>
                    <div className="text-sm font-bold text-white">KissanAI</div>
                    <div className="text-xs text-white/60">{t.demo.online}</div>
                  </div>
                </div>

                {/* Chat Area */}
                <div
                  ref={chatRef}
                  className="bg-[#e5ddd5] h-[450px] overflow-y-auto px-3 py-4 space-y-3"
                  style={{ backgroundImage: 'url("data:image/svg+xml,%3Csvg width=\'60\' height=\'60\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M30 0L60 30L30 60L0 30Z\' fill=\'none\' stroke=\'rgba(0,0,0,0.03)\' stroke-width=\'1\'/%3E%3C/svg%3E")' }}
                >
                  <div className="flex justify-center">
                    <div className="px-3 py-1.5 rounded-lg bg-[#fef3c7] text-xs text-[var(--color-foreground)]/60 text-center shadow-sm">{t.demo.encrypted}</div>
                  </div>

                  <AnimatePresence>
                    {messages.map((msg) => (
                      <motion.div
                        key={msg.id}
                        initial={{ opacity: 0, y: 10, scale: 0.95 }}
                        animate={{ opacity: 1, y: 0, scale: 1 }}
                        transition={{ duration: 0.3, ease: [0.34, 1.56, 0.64, 1] }}
                        className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}
                      >
                        <div className={`max-w-[85%] px-3 py-2.5 ${msg.role === 'user' ? 'wa-farmer' : 'wa-ai'}`}>
                          {msg.role === 'bot' && (
                            <div className="text-xs font-bold text-[var(--color-primary)] mb-1">🌾 KissanAI</div>
                          )}
                          <TypewriterText text={msg.text} />
                        </div>
                      </motion.div>
                    ))}
                  </AnimatePresence>

                  {/* Typing indicator */}
                  {isTyping && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="flex justify-start">
                      <div className="wa-ai px-4 py-3 flex items-center gap-1.5">
                        <div className="w-2 h-2 rounded-full bg-[var(--color-foreground)]/40 animate-bounce" style={{ animationDelay: '0ms' }} />
                        <div className="w-2 h-2 rounded-full bg-[var(--color-foreground)]/40 animate-bounce" style={{ animationDelay: '150ms' }} />
                        <div className="w-2 h-2 rounded-full bg-[var(--color-foreground)]/40 animate-bounce" style={{ animationDelay: '300ms' }} />
                      </div>
                    </motion.div>
                  )}
                </div>

                {/* Quick Reply Chips */}
                {showQuickReplies && !isTyping && (
                  <div className="bg-[#f0f2f5] px-3 pt-2 pb-0 flex gap-1.5 overflow-x-auto no-scrollbar">
                    {quickReplies.map((qr, i) => (
                      <button
                        key={i}
                        onClick={() => handleSend(qr.keywords)}
                        className="shrink-0 px-3 py-1.5 rounded-full bg-white border border-[var(--color-border)] text-xs font-medium text-[var(--color-foreground)] hover:bg-[var(--color-primary)]/5 hover:border-[var(--color-primary)]/30 transition-colors flex items-center gap-1"
                      >
                        <span>{qr.emoji}</span>
                        <span>{qr.label}</span>
                      </button>
                    ))}
                  </div>
                )}

                {/* Input Bar */}
                <div className="bg-[#f0f2f5] rounded-b-2xl px-3 py-3 flex items-center gap-2">
                  <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder={t.demo.placeholder}
                    className="flex-1 bg-white rounded-full px-4 py-2.5 text-sm text-[var(--color-foreground)] border border-[var(--color-border)] focus:outline-none focus:border-[var(--color-primary)]/50 placeholder:text-[var(--color-muted-foreground)]/40"
                  />
                  <button
                    onClick={() => handleSend(input)}
                    disabled={!input.trim()}
                    className="w-10 h-10 rounded-full bg-[var(--color-primary)] flex items-center justify-center disabled:opacity-50 hover:bg-[var(--color-primary)]/90 transition-colors"
                  >
                    <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  );
}
