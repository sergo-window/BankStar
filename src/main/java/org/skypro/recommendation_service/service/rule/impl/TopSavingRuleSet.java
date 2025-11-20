package org.skypro.recommendation_service.service.rule.impl;

import org.skypro.recommendation_service.model.dto.RecommendationDTO;
import org.skypro.recommendation_service.repository.UserDataRepository;
import org.skypro.recommendation_service.service.rule.RecommendationRuleSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_DESCRIPTION = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\nПреимущества «Копилки»: Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final UserDataRepository userDataRepository;

    public TopSavingRuleSet(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public Optional<RecommendationDTO> checkUser(UUID userId) {

        /// Правило 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean rule1 = userDataRepository.hasProductType(userId, "DEBIT");

        /// Правило 2: Сумма пополнений по всем продуктам типа DEBIT >= 50,000 ₽ ИЛИ
        /// сумма пополнений по всем продуктам типа SAVING >= 50,000 ₽
        BigDecimal debitDeposits = userDataRepository.getTotalDepositsAmount(userId, "DEBIT");
        BigDecimal savingDeposits = userDataRepository.getTotalDepositsAmount(userId, "SAVING");
        boolean rule2 = debitDeposits.compareTo(new BigDecimal("50000")) >= 0 ||
                savingDeposits.compareTo(new BigDecimal("50000")) >= 0;

        /// Правило 3: Сумма пополнений по всем продуктам типа DEBIT больше, чем сумма трат
        boolean rule3 = userDataRepository.isDepositsGreaterThanSpends(userId, "DEBIT");

        if (rule1 && rule2 && rule3) {
            return Optional.of(new RecommendationDTO(PRODUCT_NAME, PRODUCT_ID, PRODUCT_DESCRIPTION));
        }

        return Optional.empty();
    }
}
