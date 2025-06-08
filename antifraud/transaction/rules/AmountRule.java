package antifraud.transaction.rules;

import antifraud.transaction.dto.Limits;
import antifraud.transaction.enums.Info;
import antifraud.transaction.enums.Result;
import antifraud.transaction.exceptions.WrongFormatException;
import antifraud.transaction.service.LimitsService;
import antifraud.transaction.service.TransactionContext;
import org.springframework.stereotype.Component;

@Component
public class AmountRule implements TransactionRule{
    private final LimitsService service;

    AmountRule(LimitsService service) {
        this.service = service;
    }

    @Override
    public void check(TransactionContext context) {
        Limits limits = service.getLimits(context.getRequest().number());
        int manualLimit = limits.manualLimit();
        int allowedLimit = limits.allowedLimit();
        long amount = context.getRequest().amount();
        if (amount <= 0) {
            throw new WrongFormatException("Amount must be positive number!");
        }
        if (amount > manualLimit) {
            context.apply(Result.PROHIBITED, Info.AMOUNT);
        } else if (amount > allowedLimit) {
            context.apply(Result.MANUAL_PROCESSING, Info.AMOUNT);
        }
    }
}
