package antifraud.IOC.service;

import antifraud.IOC.damain.IP;
import antifraud.IOC.dto.DeleteResponse;
import antifraud.IOC.dto.IpRequest;
import antifraud.IOC.exceptions.DataIsAlreadyInDbException;
import antifraud.IOC.exceptions.DataNotFoundException;
import antifraud.IOC.exceptions.WrongFormatException;
import antifraud.IOC.repository.SuspiciousIpRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SuspiciousIpService {

    private SuspiciousIpRepository repository;

    public SuspiciousIpService(SuspiciousIpRepository repository) {
        this.repository = repository;
    }

    public IP saveNewSuspiciousIp(IpRequest request) {
        checkIpFormat(request.ip());

        try {
            repository.addSuspiciousIp(request.ip());
        } catch (DataIntegrityViolationException ex) {
            throw new DataIsAlreadyInDbException("IP is already in DB!");
        }
        return repository.findIpByIp(request.ip()).orElseThrow(() -> new DataNotFoundException("IP not found in DB!"));
    }

    public List<IP> findAllIps() {
        return repository.findAll();
    }

    public DeleteResponse deleteIP(String ip) {
        checkIpFormat(ip);

        repository.findIpByIp(ip).orElseThrow(() -> new DataNotFoundException("IP not found in DB!"));
        repository.deleteIp(ip);

        return new DeleteResponse(String.format("IP %s successfully removed!", ip));
    }

    private void checkIpFormat(String ip) {
        Pattern pattern = Pattern.compile("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
        Matcher matcher = pattern.matcher(ip);

        if (!matcher.matches()) {
            throw new WrongFormatException("Wrong IP format!");
        }
    }

    public boolean isIpLegal(String ip) {
        checkIpFormat(ip);
        return repository.findIpByIp(ip).isEmpty();
    }
}
