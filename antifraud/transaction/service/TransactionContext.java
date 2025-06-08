package antifraud.transaction.service;

import antifraud.transaction.dto.NewTransactionRequest;
import antifraud.transaction.dto.NewTransactionResponse;
import antifraud.transaction.enums.Info;
import antifraud.transaction.enums.Result;
import lombok.Getter;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TransactionContext {
    @Getter
    private final NewTransactionRequest request;
    private Result result;
    private final SortedSet<Info> infos;

    public TransactionContext(NewTransactionRequest request) {
        this.request = request;
        this.result = Result.ALLOWED;
        this.infos = new TreeSet<>(Comparator.comparing(Info::getReason));
    }

    public void apply(Result r, Info info) {
        if (r.ordinal() > result.ordinal()) {
            infos.clear();
            result = r;
            infos.add(info);
        } else if (r.ordinal() == result.ordinal()) {
            infos.add(info);
        }

    }

    public NewTransactionResponse formResponse() {
        String infoCsv = infos.isEmpty()
                ? Info.NONE.getReason()
                : infos.stream()
                .map(Info::getReason)
                .collect(Collectors.joining(", "));
        return new NewTransactionResponse(result.toString(), infoCsv);
    }

}
