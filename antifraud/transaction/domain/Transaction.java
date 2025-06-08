package antifraud.transaction.domain;

import antifraud.transaction.enums.Region;
import antifraud.transaction.enums.Result;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private long transactionId;
    private long amount;
    private String ip;
    private String number;
    private Region region;
    private String date;
    private Result result;
    private String feedback;


}
