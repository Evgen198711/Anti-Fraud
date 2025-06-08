package antifraud.IOC.api;

import antifraud.IOC.damain.IP;
import antifraud.IOC.dto.DeleteResponse;
import antifraud.IOC.dto.IpRequest;
import antifraud.IOC.service.SuspiciousIpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class SuspiciousIpController {

    private SuspiciousIpService service;

    public SuspiciousIpController(SuspiciousIpService service) {
        this.service = service;
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<IP> addSuspiciousIp(@RequestBody @Valid IpRequest request) {
        return ResponseEntity.ok(service.saveNewSuspiciousIp(request));
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<DeleteResponse> deleteSuspiciousIp(@PathVariable("ip") String ip) {
        return ResponseEntity.ok(service.deleteIP(ip));
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<IP>> findAllSuspiciousIps() {
        return ResponseEntity.ok(service.findAllIps());
    }
}
