package com.kishan.service;

import com.kishan.entity.Conversation;
import com.kishan.entity.Farmer;
import com.kishan.entity.Message;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Locale;

/**
 * Offline, rule-based implementation of {@link AIService}.
 * Detects intents (crop help, weather, market price, language switch)
 * and answers in the farmer's preferred language (en/kn/hi).
 * No external API key is required, so the MVP works out of the box.
 */
@ApplicationScoped
public class SimpleAIService implements AIService {

    @Override
    public String generateResponse(Farmer farmer, Conversation conversation, Message incomingMessage) {
        String text = incomingMessage.getContent() == null ? "" : incomingMessage.getContent().toLowerCase(Locale.ROOT);
        String lang = farmer.getLanguage() == null ? "en" : farmer.getLanguage();

        if (text.contains("price") || text.contains("market") || text.contains("mandi")
                || text.contains("bhav") || text.contains("daam") || text.contains("rate")) {
            return marketResponse(lang);
        }
        if (text.contains("rain") || text.contains("weather") || text.contains("spray")
                || text.contains("mausam") || text.contains("barsaat") || text.contains("hava")) {
            return weatherResponse(lang);
        }
        if (text.contains("fertilizer") || text.contains("pest") || text.contains("disease")
                || text.contains("leaf") || text.contains("crop") || text.contains("roga")
                || text.contains("kita") || text.contains("khad") || text.contains("beej")) {
            return cropResponse(lang);
        }
        if (text.contains("kannada")) {
            farmer.setLanguage("kn");
            return "ನಮಸ್ಕಾರ! ನಿಮ್ಮ ಭಾಷೆಯನ್ನು ಕನ್ನಡಕ್ಕೆ ಬದಲಾಯಿಸಲಾಗಿದೆ. ನಾನು ಕಿಶನ್-ಎ ಸಹಾಯಕ. ನಿಮ್ಮ ಬೆಳೆ, ಹವಾಮಾನ ಅಥವಾ ಮಾರುಕಟ್ಟೆ ಬಗ್ಗೆ ಕೇಳಿ.";
        }
        if (text.contains("hindi")) {
            farmer.setLanguage("hi");
            return "नमस्ते! आपकी भाषा हिंदी में बदल दी गई है। मैं किशन-ए सहायक हूँ। अपनी फसल, मौसम या बाज़ार के बारे में पूछें।";
        }
        return defaultResponse(lang);
    }

    private String marketResponse(String lang) {
        return switch (lang) {
            case "kn" -> "ಮಾರುಕಟ್ಟೆ ಬೆಲೆಗಳು ದಿನದಲ್ಲಿ ಬದಲಾಗಬಹುದು. ನಿಮ್ಮ ಸ್ಥಳೀಯ ಮಂಡಿಯ ಇಂದಿನ ಬೆಲೆಯನ್ನು ಪರಿಶೀಲಿಸಲು ನಿಮ್ಮ ಬೆಳೆ ಹೆಸರು ಕಳುಹಿಸಿ.";
            case "hi" -> "बाज़ार भाव दिन में बदल सकते हैं। आज के स्थानीय मंडी भाव जानने के लिए अपनी फसल का नाम भेजें।";
            default -> "Market prices can change during the day. Send your crop name to check today's local mandi price.";
        };
    }

    private String weatherResponse(String lang) {
        return switch (lang) {
            case "kn" -> "ಹವಾಮಾನ ಮುನ್ಸೂಚನೆಗಾಗಿ ನಿಮ್ಮ ಸ್ಥಳವನ್ನು ಹಂಚಿಕೊಳ್ಳಿ. ಸಿಂಪಡಿಸುವ ಮೊದಲು ಮಳೆಯ ಸಾಧ್ಯತೆ ಮತ್ತು ಉತ್ಪನ್ನದ ಲೇಬಲ್ ಸೂಚನೆಗಳನ್ನು ಪರಿಶೀಲಿಸಿ.";
            case "hi" -> "मौसम पूर्वानुमान के लिए अपना स्थान साझा करें। छिड़काव से पहले बारिश की संभावना और उत्पाद लेबल निर्देश जांचें।";
            default -> "Share your location for a weather forecast. Before spraying, check rain chances and follow the product label instructions.";
        };
    }

    private String cropResponse(String lang) {
        return switch (lang) {
            case "kn" -> "ನಿಮ್ಮ ಬೆಳೆ ಸಮಸ್ಯೆಯನ್ನು ವಿವರಿಸಿ ಅಥವಾ ಫೋಟೋ ಕಳುಹಿಸಿ. ಖಚಿತ ರೋಗನಿರ್ಣಯಕ್ಕಾಗಿ ನಾವು ಹೆಚ್ಚಿನ ಮಾಹಿತಿ ಕೇಳಬಹುದು. ರಾಸಾಯನಿಕ ಶಿಫಾರಸುಗಳಿಗೆ ಯಾವಾಗಲೂ ಲೇಬಲ್ ಅನುಸರಿಸಿ.";
            case "hi" -> "अपनी फसल की समस्या बताएं या फोटो भेजें। निश्चित निदान के लिए हम अधिक जानकारी मांग सकते हैं। रासायनिक सिफारिशों के लिए हमेशा लेबल का पालन करें।";
            default -> "Describe your crop problem or send a photo. We may ask more questions for a confident diagnosis. Always follow the label for chemical recommendations.";
        };
    }

    private String defaultResponse(String lang) {
        return switch (lang) {
            case "kn" -> "ನಮಸ್ಕಾರ! ನಾನು ಕಿಶನ್-ಎ ಸಹಾಯಕ. ಬೆಳೆ ಸಮಸ್ಯೆ, ಹವಾಮಾನ, ಮಾರುಕಟ್ಟೆ ಬೆಲೆ ಅಥವಾ ಗೊಬ್ಬರದ ಬಗ್ಗೆ ಕೇಳಿ. ನಾನು ಕನ್ನಡ, ಹಿಂದಿ ಮತ್ತು ಇಂಗ್ಲಿಷ್ ಬೆಂಬಲಿಸುತ್ತೇನೆ.";
            case "hi" -> "नमस्ते! मैं किशन-ए सहायक हूँ। फसल समस्या, मौसम, बाज़ार भाव या खाद के बारे में पूछें। मैं हिंदी, कन्नड़ और अंग्रेज़ी का समर्थन करता हूँ।";
            default -> "Hello! I am Kishan-A, your farming assistant. Ask about crop problems, weather, market prices, or fertilizer. I support English, Kannada, and Hindi.";
        };
    }
}