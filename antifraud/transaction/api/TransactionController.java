package antifraud.transaction.api;

import antifraud.transaction.domain.Transaction;
import antifraud.transaction.dto.Feedback;
import antifraud.transaction.dto.NewTransactionRequest;
import antifraud.transaction.dto.NewTransactionResponse;
import antifraud.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transaction")
    public ResponseEntity<NewTransactionResponse> addTransaction(@RequestBody @Valid NewTransactionRequest request) {
        return ResponseEntity.ok(service.addNewTransaction(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory() {
        return ResponseEntity.ok(service.findAllTransactions());
    }

    @GetMapping("/history/{cardNumber}")
    public ResponseEntity<List<Transaction>> getHistoryByCardNumber(@PathVariable("cardNumber") String cardNumber) {
        return ResponseEntity.ok(service.findAllTransactionsByCardNumber(cardNumber));
    }

    @PutMapping("/transaction")
    public ResponseEntity<Transaction> addFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(service.addFeedback(feedback));
    }
}
