package org.skypro.recommendation_service.configuration;

import org.skypro.recommendation_service.bot.RecommendationTelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Value("${telegram.bot.token:}")
    private String botToken;

    @Value("${telegram.bot.username:}")
    private String botUsername;

    @Bean
    public TelegramBotsApi telegramBotsApi(RecommendationTelegramBot bot) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            if (!botToken.isEmpty() && !botUsername.isEmpty()) {
                botsApi.registerBot(bot);
            }
            return botsApi;
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to register Telegram bot", e);
        }
    }
}
