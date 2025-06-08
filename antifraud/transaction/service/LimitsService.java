package antifraud.transaction.service;

import antifraud.transaction.dto.Limits;
import antifraud.transaction.enums.Limit;
import antifraud.transaction.exceptions.DataNotFoundException;
import antifraud.transaction.repository.LimitsRepository;
import org.springframework.stereotype.Service;

@Service
public class LimitsService {
    private final LimitsRepository repository;

    public LimitsService(LimitsRepository repository) {
        this.repository = repository;
    }

    public void addCard(String cardNumber) {
        if (repository.findCardLimits(cardNumber).isEmpty()) {
            repository.addNewCard(cardNumber);
        }
    }

    public Limits getLimits(String cardNumber) {
        return repository.findCardLimits(cardNumber).orElseThrow(() -> new DataNotFoundException("Card number not found!"));
    }

    public void increaseLimits(Limit limitsType, String cardNumber, long amount) {
        Limits cardsLimits = getLimits(cardNumber);
        if (limitsType == Limit.ALLOWED) {
            int allowedLimit = cardsLimits.allowedLimit();
            allowedLimit = (int)Math.ceil(0.8 * allowedLimit + 0.2 * amount);
            repository.updateLimit(Limit.ALLOWED, cardNumber, allowedLimit);
        } else {
            int manualLimit = cardsLimits.manualLimit();
            manualLimit = (int)Math.ceil(0.8 * manualLimit + 0.2 * amount);
            repository.updateLimit(Limit.MANUAL, cardNumber, manualLimit);
        }
    }

    public void decreaseLimits(Limit limitsType, String cardNumber, long amount) {
        Limits cardsLimits = getLimits(cardNumber);
        if (limitsType == Limit.ALLOWED) {
            int allowedLimit = cardsLimits.allowedLimit();
            allowedLimit = (int)Math.ceil(0.8 * allowedLimit - 0.2 * amount);
            repository.updateLimit(Limit.ALLOWED, cardNumber, allowedLimit);
        } else {
            int manualLimit = cardsLimits.manualLimit();
            manualLimit = (int)Math.ceil(0.8 * manualLimit - 0.2 * amount);
            repository.updateLimit(Limit.MANUAL, cardNumber, manualLimit);
        }
    }
}
