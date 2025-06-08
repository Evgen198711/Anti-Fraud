package antifraud.transaction.rules;

import antifraud.transaction.service.TransactionContext;

public interface TransactionRule {

    void check(TransactionContext context);
}
