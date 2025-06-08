package antifraud.transaction.service;

import antifraud.IOC.service.StolenCardService;
import antifraud.transaction.domain.Transaction;
import antifraud.transaction.dto.Feedback;
import antifraud.transaction.dto.NewTransactionRequest;
import antifraud.transaction.dto.NewTransactionRequestWithResult;
import antifraud.transaction.dto.NewTransactionResponse;
import antifraud.transaction.enums.Limit;
import antifraud.transaction.enums.Result;
import antifraud.transaction.exceptions.DataAlreadySpecifiedException;
import antifraud.transaction.exceptions.DataNotFoundException;
import antifraud.transaction.exceptions.FeedbackEqualToResultException;
import antifraud.transaction.exceptions.WrongFormatException;
import antifraud.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository repository;
    private StolenCardService stolenCardService;
    private LimitsService limitsService;
    private TransactionProcessor processor;

    public TransactionService(TransactionRepository repository,
                              StolenCardService stolenCardService,
                              LimitsService limitsService,
                              TransactionProcessor processor) {
        this.repository = repository;
        this.stolenCardService = stolenCardService;
        this.limitsService = limitsService;
        this.processor = processor;
    }

    public NewTransactionResponse addNewTransaction(NewTransactionRequest request) {
        limitsService.addCard(request.number());
        NewTransactionResponse response = processor.evaluate(request);
        LocalDateTime dateTime = LocalDateTime.parse(
                request.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        NewTransactionRequestWithResult toSave = new NewTransactionRequestWithResult(
                request.amount(),
                request.ip(),
                request.number(),
                request.region(),
                dateTime,
                response.result()
        );

        repository.addTransaction(toSave);

        return response;
    }

    public Transaction addFeedback(Feedback feedback) {
        Result feed;
        try {
            feed = Result.of(feedback.feedback());
        } catch (IllegalArgumentException ex) {
            throw new WrongFormatException("Specified feedback is inappropriate!");
        }

        Transaction transaction = repository.findTransactionById(feedback.transactionId());

        if (!transaction.getFeedback().isEmpty()) {
            throw new DataAlreadySpecifiedException("Feedback is already specified!");
        }

        if (feed == transaction.getResult()) {
            throw new FeedbackEqualToResultException("Feedback is equal to result!");
        }

        updateLimits(feed, transaction);
        repository.updateTransaction(feedback.transactionId(), feedback.feedback());

        return repository.findTransactionById(feedback.transactionId());
    }

    public List<Transaction> findAllTransactions() {
        return repository.findAll();
    }

    public List<Transaction> findAllTransactionsByCardNumber(String cardNumber) {
        stolenCardService.isCardLegal(cardNumber);

        List<Transaction> transactions =  repository.findTransactionsByCardNumber(cardNumber);

        if (transactions.isEmpty()) {
            throw new DataNotFoundException("Card number not found!");
        }

        return transactions;
    }

    private void updateLimits(Result feedback, Transaction transaction) {
        long amount = transaction.getAmount();
        String cardNumber = transaction.getNumber();
        Result result = transaction.getResult();

        switch(result) {
            case ALLOWED:
                if (feedback == Result.MANUAL_PROCESSING) {
                    limitsService.decreaseLimits(Limit.ALLOWED, cardNumber, amount);
                } else if (feedback == Result.PROHIBITED) {
                    limitsService.decreaseLimits(Limit.ALLOWED, cardNumber, amount);
                    limitsService.decreaseLimits(Limit.MANUAL, cardNumber, amount);
                }
                break;
            case MANUAL_PROCESSING:
                if (feedback == Result.ALLOWED) {
                    limitsService.increaseLimits(Limit.ALLOWED, cardNumber, amount);
                } else if (feedback == Result.PROHIBITED) {
                    limitsService.decreaseLimits(Limit.MANUAL, cardNumber, amount);
                }
                break;
            case PROHIBITED:
                if (feedback == Result.ALLOWED) {
                    limitsService.increaseLimits(Limit.ALLOWED, cardNumber, amount);
                    limitsService.increaseLimits(Limit.MANUAL, cardNumber, amount);
                } else if (feedback == Result.MANUAL_PROCESSING) {
                    limitsService.increaseLimits(Limit.MANUAL, cardNumber, amount);
                }
                break;
        }
    }
}
