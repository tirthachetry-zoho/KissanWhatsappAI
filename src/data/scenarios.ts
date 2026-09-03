export interface ChatMessage {
  role: 'farmer' | 'ai';
  text: string;
  lang?: string;
}

export interface Scenario {
  id: number;
  title: string;
  subtitle: string;
  icon: string;
  lang: string;
  messages: ChatMessage[];
}

export const scenarios: Scenario[] = [
  {
    id: 1,
    title: 'Leaf Blight Diagnosis',
    subtitle: 'Maharashtra — Soybean',
    icon: '🔬',
    lang: 'Marathi',
    messages: [
      { role: 'farmer', text: 'माझ्या सोयाबीन पिकाला पाने काळे पडत आहेत. काय करावे?', lang: 'mr' },
      { role: 'ai', text: 'Your soybean crop is showing signs of **Alternaria Leaf Blight**. Here is what to do:\n\n1. **Spray Carbendazim 50% WP** — 2g per litre of water\n2. Remove and destroy infected leaves immediately\n3. Avoid overhead irrigation\n4. Spray during early morning (6–8 AM)\n\nIf the infection is severe (>30% leaf area), repeat spray after 10 days. Consult your nearest KVK for soil testing.' },
    ],
  },
  {
    id: 2,
    title: 'Mandi Prices',
    subtitle: 'Punjab — Wheat',
    icon: '💰',
    lang: 'Hindi',
    messages: [
      { role: 'farmer', text: 'Gehu ki aaj ki mandi kya chal rahi hai? Mujhe bechna hai.', lang: 'hi' },
      { role: 'ai', text: 'Here are today\'s wheat (gehú) mandi prices:\n\n📍 **Ludhiana**: ₹2,285/quintal\n📍 **Amritsar**: ₹2,260/quintal\n📍 **Jalandhar**: ₹2,270/quintal\n📍 **Moga**: ₹2,250/quintal\n\n**MSP (Govt.)**: ₹2,275/quintal\n\n💡 **Tip**: Prices are 3% higher than last week. Ludhiana has the best rate today. Consider selling within 2–3 days as prices may dip with new arrivals.' },
    ],
  },
  {
    id: 3,
    title: 'Irrigation Schedule',
    subtitle: 'Gujarat — Cotton',
    icon: '💧',
    lang: 'Gujarati',
    messages: [
      { role: 'farmer', text: 'મારા કપાસને પાણી ક્યારે આપવું? ગરમી વધુ છે.', lang: 'gu' },
      { role: 'ai', text: 'Based on your location (Gujarat) and current weather:\n\n🌡️ **Forecast**: 42°C for next 3 days\n🌧️ **Rain expected**: Day 5 (light showers)\n\n**Irrigation Plan**:\n- **Today**: Full irrigation (critical — cotton at squaring stage)\n- **Day 3**: Light irrigation (25mm)\n- **Day 5-6**: Skip — rain expected\n\n**Drip irrigation preferred** if available. Saves 40% water vs flood method.\n\n⚠️ Avoid irrigation during afternoon hours. Best time: 5–7 AM.' },
    ],
  },
  {
    id: 4,
    title: 'Government Schemes',
    subtitle: 'Karnataka — Paddy',
    icon: '🏛️',
    lang: 'Kannada',
    messages: [
      { role: 'farmer', text: 'ಸರ್ಕಾರದ ಯಾವುದಾದರೂ ಸಹಾಯ ಯೋಜನೆ ಇದೆಯೇ? ನನಗೆ ಸಹಾಯ ಬೇಕು.', lang: 'kn' },
      { role: 'ai', text: 'Here are government schemes you may be eligible for:\n\n🏛️ **PM-KISAN**: ₹6,000/year direct transfer (if not yet enrolled, visit your Patwari)\n\n🏛️ **PM Fasal Bima Yojana**: Crop insurance at 1.5% premium for paddy. Deadline: July 15\n\n🏛️ **Karnataka Raitha Samparka Yojana**: Subsidized drip irrigation (55% subsidy for small farmers)\n\n🏛️ **Soil Health Card Scheme**: Free soil testing at your nearest KVK\n\n**Documents needed**: Aadhaar, land records (7/12), bank passbook\n\nWant me to help you apply for any of these?' },
    ],
  },
  {
    id: 5,
    title: 'Soil Health Check',
    subtitle: 'Tamil Nadu — Rice',
    icon: '🌱',
    lang: 'Tamil',
    messages: [
      { role: 'farmer', text: 'என் மண்ணில் என்ன குறைபாடு இருக்கிறது? நெல் சரியாக வளரவில்லை.', lang: 'ta' },
      { role: 'ai', text: 'Based on your Soil Health Card (Nagapattinam district):\n\n📊 **Soil Analysis**:\n- pH: 5.2 (⚠️ Too acidic — ideal: 6.0–7.0)\n- Nitrogen: Low\n- Phosphorus: Medium\n- Potassium: High\n- Organic Carbon: 0.35% (Low)\n\n**Recommendations**:\n1. Apply **Dolomite lime** — 2 tonnes/acre to raise pH\n2. Use **Urea** at 45kg/acre split into 3 doses\n3. Reduce Potassium — skip K-fertilizer this season\n4. Add **Green manure** (Dhaincha) before next crop\n\n🧪 Get your soil retested after 60 days.' },
    ],
  },
  {
    id: 6,
    title: 'Voice Query — Hindi',
    subtitle: 'Rajasthan — Mustard',
    icon: '🎙️',
    lang: 'Hindi (Voice)',
    messages: [
      { role: 'farmer', text: '🎙️ [Voice Note — 0:08]\n"Meri sarson mein keede lag gaye hain, kya karoon?"', lang: 'hi' },
      { role: 'ai', text: 'Voice transcribed & understood! 🎙️➡️📝\n\n**Problem**: Mustard Aphid infestation detected\n\n**Treatment Plan**:\n1. Spray **Dimethoate 30% EC** — 1.5ml/L water\n2. Add 5ml soap per litre for better sticking\n3. Spray in evening (not in sun — reduces effectiveness)\n4. If >40% plants affected, repeat after 7 days\n\n**Prevention for next season**:\n- Sow **Neem-coated seeds**\n- Install yellow sticky traps (20/acre)\n- Avoid excess nitrogen fertilizer\n\n📞 Contact your nearest KVK Jaipur: 0141-2711347' },
    ],
  },
];

export const pipelineSteps = [
  {
    step: 1,
    title: 'Ingestion',
    desc: 'Farmer sends voice/image via WhatsApp',
    icon: '📨',
    detail: 'WhatsApp Business API receives audio, text, or images',
  },
  {
    step: 2,
    title: 'ASR & Translation',
    desc: 'Voice → Text → English',
    icon: '🗣️',
    detail: 'Bhashini API transcribes regional dialect, translates to English',
  },
  {
    step: 3,
    title: 'Vision Analysis',
    desc: 'Image → Diagnosis',
    icon: '👁️',
    detail: 'Gemini Vision identifies diseases, pests, nutrient deficiencies',
  },
  {
    step: 4,
    title: 'RAG Retrieval',
    desc: 'Query → Verified Knowledge',
    icon: '📚',
    detail: 'Vector search across ICAR data, weather, mandi prices, schemes',
  },
  {
    step: 5,
    title: 'AI Reasoning',
    desc: 'Synthesize → Actionable Advice',
    icon: '🧠',
    detail: 'LLM generates localized, context-aware recommendations',
  },
  {
    step: 6,
    title: 'Voice Response',
    desc: 'Answer → Voice Note',
    icon: '🔊',
    detail: 'TTS converts response back to farmer\'s language',
  },
];

export const features = [
  {
    icon: '🔬',
    title: 'Crop Disease Detection',
    desc: 'Upload a photo of your crop. Our AI identifies diseases, pests, and nutrient deficiencies from low-quality smartphone photos in real-time.',
    stat: '94% accuracy',
  },
  {
    icon: '💧',
    title: 'Smart Irrigation',
    desc: 'Get personalized watering schedules based on your location, weather forecasts, soil type, and crop growth stage.',
    stat: '40% water saved',
  },
  {
    icon: '💰',
    title: 'Live Mandi Prices',
    desc: 'Real-time prices from 7,000+ mandis across India. Get alerts when prices peak for your crop in your nearest market.',
    stat: '7,000+ mandis',
  },
  {
    icon: '🏛️',
    title: 'Govt. Schemes',
    desc: 'Instant eligibility check for PM-KISAN, PMFBY, and 50+ state schemes. Get step-by-step application guidance in your language.',
    stat: '50+ schemes',
  },
  {
    icon: '🌱',
    title: 'Soil Health Analysis',
    desc: 'Connect your Soil Health Card data for customized fertilizer recommendations and soil improvement plans.',
    stat: '22 soil params',
  },
  {
    icon: '🎙️',
    title: 'Voice-First Design',
    desc: 'Ask questions in your language, in your voice. No typing needed. Supports 12+ Indian languages with natural voice responses.',
    stat: '12+ languages',
  },
];

export const dashboardData = {
  regions: [
    { name: 'Punjab', queries: 12400, crops: ['Wheat', 'Rice', 'Cotton'] },
    { name: 'Maharashtra', queries: 9800, crops: ['Soybean', 'Cotton', 'Sugarcane'] },
    { name: 'Tamil Nadu', queries: 8200, crops: ['Rice', 'Sugarcane', 'Cotton'] },
    { name: 'Gujarat', queries: 7600, crops: ['Cotton', 'Groundnut', 'Bajra'] },
    { name: 'Uttar Pradesh', queries: 15200, crops: ['Wheat', 'Sugarcane', 'Potato'] },
    { name: 'Karnataka', queries: 6400, crops: ['Rice', 'Maize', 'Coffee'] },
    { name: 'Rajasthan', queries: 5100, crops: ['Mustard', 'Wheat', 'Bajra'] },
    { name: 'Madhya Pradesh', queries: 8900, crops: ['Soybean', 'Wheat', 'Gram'] },
  ],
  topQueries: [
    { category: 'Disease Diagnosis', count: 34200, trend: '+12%' },
    { category: 'Mandi Prices', count: 28100, trend: '+8%' },
    { category: 'Irrigation', count: 22400, trend: '+15%' },
    { category: 'Govt Schemes', count: 18900, trend: '+22%' },
    { category: 'Soil Health', count: 12600, trend: '+18%' },
    { category: 'Pest Control', count: 9800, trend: '+5%' },
  ],
  stats: {
    totalQueries: 145000,
    farmersServed: 52000,
    languagesSupported: 12,
    avgResponseTime: '2.3s',
    accuracy: '94%',
    statesCovered: 22,
  },
};
