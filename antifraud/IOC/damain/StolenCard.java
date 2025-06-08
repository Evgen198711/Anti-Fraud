package antifraud.IOC.damain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StolenCard {
    private long id;
    private String number;

}
