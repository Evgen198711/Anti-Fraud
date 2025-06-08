package antifraud.transaction.rules;

import antifraud.IOC.service.StolenCardService;
import antifraud.transaction.enums.Info;
import antifraud.transaction.enums.Result;
import antifraud.transaction.service.TransactionContext;
import org.springframework.stereotype.Component;

@Component
public class CardRule implements TransactionRule{
    private final StolenCardService service;

    public CardRule(StolenCardService service) {
        this.service = service;
    }
    @Override
    public void check(TransactionContext context) {
        String cardNumber = context.getRequest().number();
        if (!service.isCardLegal(cardNumber)) {
            context.apply(Result.PROHIBITED, Info.CARD_NUMBER);
        }
    }
}
