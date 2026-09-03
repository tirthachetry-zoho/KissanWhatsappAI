'use client';

import { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { scenarios, type ChatMessage } from '@/data/scenarios';

function TypewriterText({ text, speed = 12, onComplete }: { text: string; speed?: number; onComplete?: () => void }) {
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
    }, speed);
    return () => clearInterval(interval);
  }, [text, speed, onComplete]);

  const renderText = (t: string) => {
    return t.split('\n').map((line, idx) => {
      const parts = line.split(/\*\*(.*?)\*\*/g);
      return (
        <div key={idx} className={line === '' ? 'h-2' : ''}>
          {parts.map((part, pidx) =>
            pidx % 2 === 1 ? (
              <strong key={pidx} className="text-[var(--color-foreground)] font-semibold">{part}</strong>
            ) : (
              <span key={pidx}>{part}</span>
            )
          )}
        </div>
      );
    });
  };

  return (
    <div className="text-sm leading-relaxed text-[var(--color-muted-foreground)]">
      {renderText(displayed)}
      {!done && <span className="typing-cursor" />}
    </div>
  );
}

export default function ChatDemo() {
  const [selectedScenario, setSelectedScenario] = useState(0);
  const [visibleMessages, setVisibleMessages] = useState<ChatMessage[]>([]);
  const [isTyping, setIsTyping] = useState(false);
  const [typingDone, setTypingDone] = useState(false);
  const chatRef = useRef<HTMLDivElement>(null);
  const scenario = scenarios[selectedScenario];

  useEffect(() => {
    setVisibleMessages([]);
    setIsTyping(false);
    setTypingDone(false);

    let msgIndex = 0;
    const showNext = () => {
      if (msgIndex >= scenario.messages.length) return;
      const msg = scenario.messages[msgIndex];

      if (msg.role === 'farmer') {
        setTimeout(() => {
          setVisibleMessages((prev) => [...prev, msg]);
          msgIndex++;
          showNext();
        }, 600);
      } else {
        setTimeout(() => {
          setIsTyping(true);
        }, 400);
      }
    };
    showNext();
  }, [selectedScenario, scenario]);

  const handleAIComplete = () => {
    const aiMsg = scenario.messages.find((m) => m.role === 'ai');
    if (aiMsg && !visibleMessages.find((m) => m.role === 'ai')) {
      setVisibleMessages((prev) => [...prev, aiMsg]);
      setIsTyping(false);
      setTypingDone(true);
    }
  };

  useEffect(() => {
    if (chatRef.current) {
      chatRef.current.scrollTop = chatRef.current.scrollHeight;
    }
  }, [visibleMessages, isTyping]);

  return (
    <section id="demo" className="relative py-32 bg-white">
      <div className="max-w-[1400px] mx-auto px-5 lg:px-6">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          transition={{ duration: 0.8 }}
          className="text-center mb-16"
        >
          <span className="text-sm font-medium text-[var(--color-primary)] uppercase tracking-widest mb-4 block">
            Live Demo
          </span>
          <h2 className="font-display text-4xl md:text-5xl font-semibold mb-6 text-[var(--color-midnight)]">
            Try It <span className="text-[var(--color-primary)]">Yourself</span>
          </h2>
          <p className="text-[var(--color-muted-foreground)] text-lg max-w-xl mx-auto">
            Pick a scenario and watch KissanAI respond in real-time — just like a real farmer would.
          </p>
        </motion.div>

        <div className="flex flex-col lg:flex-row gap-8 items-start max-w-5xl mx-auto">
          {/* Scenario Selector */}
          <motion.div
            initial={{ opacity: 0, x: -30 }}
            whileInView={{ opacity: 1, x: 0 }}
            viewport={{ once: true }}
            className="w-full lg:w-80 shrink-0 space-y-3"
          >
            <div className="text-sm font-semibold text-[var(--color-muted-foreground)] uppercase tracking-wider mb-4">
              Scenarios
            </div>
            {scenarios.map((s, i) => (
              <button
                key={s.id}
                onClick={() => setSelectedScenario(i)}
                className={`w-full text-left p-4 rounded-xl transition-all duration-300 border ${
                  i === selectedScenario
                    ? 'bg-white shadow-md border-[var(--color-primary)]/30'
                    : 'bg-[var(--color-mint)] border-[var(--color-border)] hover:bg-white hover:shadow-sm'
                }`}
              >
                <div className="flex items-center gap-3">
                  <span className="text-2xl">{s.icon}</span>
                  <div>
                    <div className="text-sm font-semibold text-[var(--color-foreground)]">{s.title}</div>
                    <div className="text-xs text-[var(--color-muted-foreground)] mt-0.5">
                      {s.subtitle} • {s.lang}
                    </div>
                  </div>
                </div>
              </button>
            ))}
          </motion.div>

          {/* Phone Frame */}
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="flex-1 flex justify-center"
          >
            <div className="w-full max-w-sm">
              <div className="rounded-[2rem] bg-[var(--color-midnight)] p-3 shadow-2xl shadow-black/20">
                {/* WhatsApp Header */}
                <div className="bg-[#075e54] rounded-t-2xl px-4 py-3 flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-[var(--color-primary)] flex items-center justify-center text-lg">
                    🌾
                  </div>
                  <div>
                    <div className="text-sm font-bold text-white">KissanAI</div>
                    <div className="text-xs text-white/60">online</div>
                  </div>
                </div>

                {/* Chat Area */}
                <div
                  ref={chatRef}
                  className="bg-[#e5ddd5] h-[400px] overflow-y-auto px-3 py-4 space-y-3"
                  style={{
                    backgroundImage:
                      'url("data:image/svg+xml,%3Csvg width=\'60\' height=\'60\' xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cpath d=\'M30 0L60 30L30 60L0 30Z\' fill=\'none\' stroke=\'rgba(0,0,0,0.03)\' stroke-width=\'1\'/%3E%3C/svg%3E")',
                  }}
                >
                  <div className="flex justify-center">
                    <div className="px-3 py-1.5 rounded-lg bg-[#fef3c7] text-xs text-[var(--color-foreground)]/60 text-center shadow-sm">
                      Messages to KissanAI are end-to-end encrypted
                    </div>
                  </div>

                  <AnimatePresence>
                    {visibleMessages.map((msg, i) => (
                      <motion.div
                        key={i}
                        initial={{ opacity: 0, y: 10, scale: 0.95 }}
                        animate={{ opacity: 1, y: 0, scale: 1 }}
                        transition={{ duration: 0.3, ease: [0.34, 1.56, 0.64, 1] }}
                        className={`flex ${msg.role === 'farmer' ? 'justify-end' : 'justify-start'}`}
                      >
                        <div
                          className={`max-w-[85%] px-3 py-2.5 ${
                            msg.role === 'farmer' ? 'wa-farmer' : 'wa-ai'
                          }`}
                        >
                          {msg.role === 'ai' && (
                            <div className="text-xs font-bold text-[var(--color-primary)] mb-1 flex items-center gap-1">
                              🌾 KissanAI
                            </div>
                          )}
                          {msg.role === 'ai' && i === visibleMessages.length - 1 && isTyping ? (
                            <TypewriterText text={msg.text} onComplete={handleAIComplete} />
                          ) : (
                            <div className="text-sm leading-relaxed whitespace-pre-line">
                              {msg.text.split(/\*\*(.*?)\*\*/g).map((part, pidx) =>
                                pidx % 2 === 1 ? (
                                  <strong key={pidx} className="font-semibold">{part}</strong>
                                ) : (
                                  <span key={pidx}>{part}</span>
                                )
                              )}
                            </div>
                          )}
                        </div>
                      </motion.div>
                    ))}
                  </AnimatePresence>

                  {/* Typing indicator */}
                  {isTyping && !typingDone && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="flex justify-start">
                      <div className="wa-ai px-4 py-3 flex items-center gap-1.5">
                        <div className="w-2 h-2 rounded-full bg-[var(--color-foreground)]/40 animate-bounce" style={{ animationDelay: '0ms' }} />
                        <div className="w-2 h-2 rounded-full bg-[var(--color-foreground)]/40 animate-bounce" style={{ animationDelay: '150ms' }} />
                        <div className="w-2 h-2 rounded-full bg-[var(--color-foreground)]/40 animate-bounce" style={{ animationDelay: '300ms' }} />
                      </div>
                    </motion.div>
                  )}

                  {isTyping && visibleMessages.length > 0 && visibleMessages[visibleMessages.length - 1].role === 'farmer' && (
                    <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="flex justify-start">
                      <div className="wa-ai px-3 py-2.5 max-w-[85%]">
                        <div className="text-xs font-bold text-[var(--color-primary)] mb-1">🌾 KissanAI</div>
                        <TypewriterText text={scenario.messages[scenario.messages.length - 1].text} onComplete={handleAIComplete} />
                      </div>
                    </motion.div>
                  )}
                </div>

                {/* Input Bar */}
                <div className="bg-[#f0f2f5] rounded-b-2xl px-3 py-3 flex items-center gap-2">
                  <div className="flex-1 bg-white rounded-full px-4 py-2 text-sm text-[var(--color-muted-foreground)]/50 border border-[var(--color-border)]">
                    Type a message...
                  </div>
                  <div className="w-10 h-10 rounded-full bg-[var(--color-primary)] flex items-center justify-center">
                    <svg className="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
                    </svg>
                  </div>
                </div>
              </div>

              <div className="flex justify-center mt-6">
                <button
                  onClick={() => setSelectedScenario(selectedScenario)}
                  className="px-6 py-2.5 rounded-full text-sm text-[var(--color-muted-foreground)] hover:text-[var(--color-foreground)] border border-[var(--color-border)] hover:border-[var(--color-primary)]/30 transition-all flex items-center gap-2 bg-white hover:shadow-sm"
                >
                  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                  </svg>
                  Replay Demo
                </button>
              </div>
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  );
}
