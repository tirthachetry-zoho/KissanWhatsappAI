import Navbar from '@/components/Navbar';
import Hero from '@/components/Hero';
import HowItWorks from '@/components/HowItWorks';
import ChatDemo from '@/components/ChatDemo';
import Features from '@/components/Features';
import Dashboard from '@/components/Dashboard';
import CTA from '@/components/CTA';
import Footer from '@/components/Footer';
import ParticlesBackground from '@/components/ParticlesBackground';
import LoadingScreen from '@/components/LoadingScreen';

export default function Home() {
  return (
    <>
      <LoadingScreen />
      <ParticlesBackground />
      <div className="relative z-10">
        <Navbar />
        <Hero />
        <HowItWorks />
        <ChatDemo />
        <Features />
        <Dashboard />
        <CTA />
        <Footer />
      </div>
    </>
  );
}
