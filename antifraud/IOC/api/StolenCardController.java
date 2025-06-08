package antifraud.IOC.api;

import antifraud.IOC.damain.StolenCard;
import antifraud.IOC.dto.CardNumberRequest;
import antifraud.IOC.dto.DeleteResponse;
import antifraud.IOC.service.StolenCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class StolenCardController {

    private final StolenCardService service;

    public StolenCardController(StolenCardService service) {
        this.service = service;
    }

    @PostMapping("/stolencard")
    public ResponseEntity<StolenCard> addStolenCard(@RequestBody @Valid CardNumberRequest number) {
        return ResponseEntity.ok(service.saveNewStolenCard(number));
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<DeleteResponse> deleteCardNumber(@PathVariable("number") String number) {
        return ResponseEntity.ok(service.deleteCard(number));
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<StolenCard>> findAllStolenCards() {
        return ResponseEntity.ok(service.findAllStolenCards());
    }
}
