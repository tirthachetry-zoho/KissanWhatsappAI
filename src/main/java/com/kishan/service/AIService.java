package com.kishan.service;

import com.kishan.entity.Conversation;
import com.kishan.entity.Farmer;
import com.kishan.entity.Message;

/**
 * Abstraction for the AI layer (see PRD section 14).
 * Implementations can swap between local/rule-based logic and external
 * LLM providers without changing the rest of the application.
 */
public interface AIService {

    String generateResponse(Farmer farmer, Conversation conversation, Message incomingMessage);
}