package com.kishan.service;

import com.kishan.entity.Conversation;
import com.kishan.entity.Farmer;
import com.kishan.entity.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pure unit tests for the offline rule-based {@link SimpleAIService} —
 * no Quarkus/DB required.
 */
public class SimpleAIServiceTest {

    private final SimpleAIService service = new SimpleAIService();

    private Farmer farmer(String language) {
        Farmer farmer = new Farmer();
        farmer.setPhoneNumber("919000000001");
        farmer.setLanguage(language);
        return farmer;
    }

    private Message message(String content) {
        Message message = new Message();
        message.setContent(content);
        return message;
    }

    @Test
    void marketPriceQuestionIsRecognized() {
        String response = service.generateResponse(farmer("en"), new Conversation(), message("What is tomato price today?"));
        assertTrue(response.contains("Market prices"), "Expected market response but got: " + response);
    }

    @Test
    void sprayQuestionRoutesToWeather() {
        String response = service.generateResponse(farmer("en"), new Conversation(), message("Can I spray today?"));
        assertTrue(response.toLowerCase().contains("weather"), "Expected weather response but got: " + response);
    }

    @Test
    void cropSymptomRoutesToCropHelp() {
        String response = service.generateResponse(farmer("en"), new Conversation(), message("Tomato leaves are curling"));
        assertTrue(response.contains("crop problem"), "Expected crop response but got: " + response);
    }

    @Test
    void defaultGreetingIsReturnedForUnknownInput() {
        String response = service.generateResponse(farmer("en"), new Conversation(), message("Hello there"));
        assertTrue(response.contains("farming assistant"), "Expected greeting but got: " + response);
    }

    @Test
    void switchToKannadaChangesFarmerLanguage() {
        Farmer farmer = farmer("en");
        String response = service.generateResponse(farmer, new Conversation(), message("Please respond in Kannada"));
        assertEquals("kn", farmer.getLanguage());
        assertTrue(response.contains("ನಮಸ್ಕಾರ"), "Expected Kannada greeting but got: " + response);
    }

    @Test
    void switchToHindiChangesFarmerLanguage() {
        Farmer farmer = farmer("en");
        String response = service.generateResponse(farmer, new Conversation(), message("Please respond in Hindi"));
        assertEquals("hi", farmer.getLanguage());
        assertTrue(response.contains("नमस्ते"), "Expected Hindi greeting but got: " + response);
    }

    @Test
    void respondsInKannadaForMarketQuestion() {
        String response = service.generateResponse(farmer("kn"), new Conversation(), message("ಮಾರುಕಟ್ಟೆ ಬೆಲೆ"));
        assertTrue(response.contains("ಮಾರುಕಟ್ಟೆ"), "Expected Kannada response but got: " + response);
    }

    @Test
    void respondsInHindiForWeatherQuestion() {
        String response = service.generateResponse(farmer("hi"), new Conversation(), message("मौसम कैसा है?"));
        assertTrue(response.contains("मौसम"), "Expected Hindi response but got: " + response);
    }

    @Test
    void nullContentIsHandledGracefully() {
        String response = service.generateResponse(farmer("en"), new Conversation(), message(null));
        assertTrue(response.contains("farming assistant"), "Expected greeting but got: " + response);
    }
}