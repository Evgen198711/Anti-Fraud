package antifraud.transaction.rules;

import antifraud.transaction.enums.Info;
import antifraud.transaction.enums.Result;
import antifraud.transaction.repository.TransactionRepository;
import antifraud.transaction.service.TransactionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class IpCorrelationRule implements TransactionRule{
    private final TransactionRepository repository;

    public IpCorrelationRule(TransactionRepository repository) {
        this.repository = repository;
    }
    @Override
    public void check(TransactionContext context) {
        int manualCountLimit = 2;
        String cardNumber = context.getRequest().number();
        String ip = context.getRequest().ip();
        LocalDateTime dateTime = LocalDateTime.parse(
                context.getRequest().date(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        int numberOfTransactions = repository.findTransactionsFromSpecifiedIpInTheLastHour(cardNumber, ip, dateTime);

        if (numberOfTransactions > manualCountLimit) {
            context.apply(Result.PROHIBITED, Info.IP_CORRELATION);
        } else if (numberOfTransactions == manualCountLimit) {
            context.apply(Result.MANUAL_PROCESSING, Info.IP_CORRELATION);
        }
    }

}
