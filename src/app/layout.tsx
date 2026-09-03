import type { Metadata } from "next";
import { Inter, Fraunces } from "next/font/google";
import "./globals.css";
import { LangProvider } from "@/i18n/context";

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
});

const fraunces = Fraunces({
  subsets: ["latin"],
  variable: "--font-fraunces",
  display: "swap",
});

export const metadata: Metadata = {
  title: "KissanAI — Vernacular AI Agronomist on WhatsApp",
  description:
    "India's AI-powered agricultural advisor on WhatsApp. Get crop disease diagnosis, mandi prices, irrigation schedules, and govt scheme guidance in 22+ Indian languages.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${inter.variable} ${fraunces.variable} bg-white text-[var(--color-foreground)] antialiased`}
      >
        <LangProvider>{children}</LangProvider>
      </body>
    </html>
  );
}
