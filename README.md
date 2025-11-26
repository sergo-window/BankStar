# BankStar Recommendation Service

[![Java 17](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-orange.svg)](https://maven.apache.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> Микросервис для интеллектуальной рекомендации банковских продуктов на основе динамических правил и Telegram бота.

## 🚀 Возможности

- **Динамические правила** - создание и управление бизнес-правилами через REST API
- **Telegram бот** - взаимодействие с системой через мессенджер
- **Кэширование** - повышение производительности через кэш рекомендаций
- **Статистика** - отслеживание эффективности правил
- **Модульная архитектура** - легко расширяемая система команд и правил

## 📋 Технологии

- **Java 17** - основная платформа
- **Spring Boot 3** - фреймворк приложения
- **Maven** - управление зависимостями
- **Telegram Bot API** - интеграция с мессенджером
- **Spring Data JPA** - работа с базой данных
- **Cache Abstraction** - кэширование результатов

## 🏗 Архитектура
recommendation-service/
├── src/main/java/org/skypro/recommendation_service/
│ ├── bot/ # Telegram бот
│ ├── configuration/ # Конфигурации Spring
│ ├── controller/ # REST контроллеры
│ ├── mapper/ # Мапперы DTO
│ ├── model/ # Модели данных
│ │ ├── dto/ # Data Transfer Objects
│ │ ├── entity/ # JPA сущности
│ │ └── enums/ # Перечисления
│ └── service/ # Бизнес-логика
│ ├── cache/ # Сервис кэширования
│ ├── rule/ # Обработка правил
│ │ ├── command/ # Паттерн Command
│ │ └── impl/ # Реализации правил
│ └── repository/ # Data Access Layer

## ⚡ Быстрый старт

### Предварительные требования

- Java 17 или выше
- Maven 3.8+
- PostgreSQL (или другая БД)
- Telegram Bot Token (для бота)

### Установка и запуск

1. **Клонируйте репозиторий:**
git clone https://github.com/sergo-window/BankStar.git
cd BankStar/recommendation-service
2. Соберите проект:
mvn clean compile
3. Настройте базу данных в application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/recommendation_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Настройки Telegram бота
telegram.bot.token=your_bot_token
telegram.bot.name=your_bot_name
4. Запустите приложение:
mvn spring-boot:run

Или соберите JAR:
mvn clean package
java -jar target/recommendation-service-1.0.0.jar

### Telegram бот
Доступные команды:
/start - начать работу
/recommend - получить рекомендации
/stats - статистика правил

👥 Команда разработки
Сергей - ведущий разработчик (@sergo-window)
