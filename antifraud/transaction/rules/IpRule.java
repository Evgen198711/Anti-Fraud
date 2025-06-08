package antifraud.transaction.rules;

import antifraud.IOC.service.SuspiciousIpService;
import antifraud.transaction.enums.Info;
import antifraud.transaction.enums.Result;
import antifraud.transaction.service.TransactionContext;
import org.springframework.stereotype.Component;

@Component
public class IpRule implements TransactionRule{
    private SuspiciousIpService service;

    public IpRule(SuspiciousIpService service) {
        this.service = service;
    }
    @Override
    public void check(TransactionContext context) {
        String ip = context.getRequest().ip();

        if (!service.isIpLegal(ip)) {
            context.apply(Result.PROHIBITED, Info.IP);
        }
    }
}
