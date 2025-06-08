package antifraud.transaction.service;

import antifraud.transaction.dto.NewTransactionRequest;
import antifraud.transaction.dto.NewTransactionResponse;
import antifraud.transaction.rules.TransactionRule;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionProcessor {
    private final List<TransactionRule> rules;

    public TransactionProcessor(List<TransactionRule> rules) {
        this.rules =rules;
    }

    public NewTransactionResponse evaluate(NewTransactionRequest request) {
        TransactionContext context = new TransactionContext(request);

        for (TransactionRule rule : rules) {
            rule.check(context);
        }

        return context.formResponse();
    }
}
