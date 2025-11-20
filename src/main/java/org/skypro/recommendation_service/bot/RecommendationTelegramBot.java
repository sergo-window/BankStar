package org.skypro.recommendation_service.bot;

import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.skypro.recommendation_service.model.dto.RecommendationResponse;
import org.skypro.recommendation_service.service.RecommendationService;
import org.skypro.recommendation_service.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.UUID;

@Component
public class RecommendationTelegramBot extends TelegramLongPollingBot {

    private final RecommendationService recommendationService;
    private final UserService userService;

    @Value("${telegram.bot.username:}")
    private String botUsername;

    @Value("${telegram.bot.token:}")
    private String botToken;

    public RecommendationTelegramBot(RecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String text = message.getText();

            processMessage(chatId, text);
        }
    }

    private void processMessage(String chatId, String text) {
        if (text.equals("/start")) {
            sendHelpMessage(chatId);
        } else if (text.startsWith("/recommend ")) {
            processRecommendCommand(chatId, text);
        } else {
            sendHelpMessage(chatId);
        }
    }

    private void processRecommendCommand(String chatId, String text) {
        try {
            String[] parts = text.split(" ", 2);
            if (parts.length < 2) {
                sendMessage(chatId, "❌ Использование: /recommend username");
                return;
            }

            String username = parts[1].trim();
            UUID userId = userService.findUserIdByName(username);

            if (userId == null) {
                sendMessage(chatId, "❌ Пользователь не найден");
                return;
            }

            RecommendationResponse response = recommendationService.getRecommendationsForUser(userId);
            String recommendationsText = formatRecommendations(response);

            sendMessage(chatId, recommendationsText);

        } catch (Exception e) {
            sendMessage(chatId, "❌ Произошла ошибка при обработке запроса");
        }
    }

    private String formatRecommendations(RecommendationResponse response) {
        StringBuilder sb = new StringBuilder();

        String userName = userService.getUserNameById(response.getUserId());
        sb.append("👋 Здравствуйте, ").append(userName).append("\n\n");

        List<RecommendationDTO> recommendations = response.getRecommendations();
        if (recommendations.isEmpty()) {
            sb.append("📭 На данный момент для вас нет рекомендаций");
        } else {
            sb.append("🎯 Новые продукты для вас:\n\n");
            for (int i = 0; i < recommendations.size(); i++) {
                RecommendationDTO rec = recommendations.get(i);
                sb.append(i + 1).append(". ").append(rec.getName()).append("\n");
                sb.append("   ").append(rec.getText()).append("\n\n");
            }
        }

        return sb.toString();
    }

    private void sendHelpMessage(String chatId) {
        String helpText = """
                🤖 Бот рекомендаций банковских продуктов
                
                Доступные команды:
                /start - показать это сообщение
                /recommend [имя] - получить рекомендации для пользователя
                
                Пример:
                /recommend Иван Тестовый
                """;
        sendMessage(chatId, helpText);
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.enableMarkdown(true);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}
