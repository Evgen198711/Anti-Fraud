package antifraud.IOC.service;

import antifraud.IOC.damain.StolenCard;
import antifraud.IOC.dto.CardNumberRequest;
import antifraud.IOC.dto.DeleteResponse;
import antifraud.IOC.exceptions.DataNotFoundException;
import antifraud.IOC.exceptions.DataIsAlreadyInDbException;
import antifraud.IOC.exceptions.WrongFormatException;
import antifraud.IOC.repository.StolenCardRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StolenCardService {
    private StolenCardRepository repository;

    public StolenCardService(StolenCardRepository repository) {
        this.repository = repository;
    }

    public StolenCard saveNewStolenCard(CardNumberRequest number) {
        checkCardNumber(number.number());

        try {
            repository.addStolenCard(number.number());
        } catch (DataIntegrityViolationException ex) {
            throw new DataIsAlreadyInDbException("Card is already in DB!");
        }

        return repository.findCardByCardNumber(number.number()).orElseThrow(() -> new DataNotFoundException("Card not found in DB!"));
    }

    public List<StolenCard> findAllStolenCards() {
        return repository.findAll();
    }

    public DeleteResponse deleteCard(String cardNumber) {
        checkCardNumber(cardNumber);

        repository.findCardByCardNumber(cardNumber).orElseThrow(() -> new DataNotFoundException("Card not found in DB!"));

        repository.deleteCard(cardNumber);

        return new DeleteResponse(String.format("Card %s successfully removed!", cardNumber));
    }


    public void checkCardNumber(String number) {
        int[] array = convertNumberToArray(number);

        int sum = 0;
        int checkDigit = array[array.length - 1];

        for (int i = array.length - 2; i >= 0; i--) {
            int numberToAdd = i % 2 == 0 ? array[i] * 2 : array[i];
            numberToAdd = numberToAdd > 9 ? numberToAdd - 9 : numberToAdd;
            sum += numberToAdd;
        }

       if (number.length() != 16 || (10 - (sum % 10)) % 10 != checkDigit) {
           throw new WrongFormatException("Wrong card format!");
       }
    }

    private int[] convertNumberToArray(String number) {
        int[] result = new int[number.length()];
        char[] chars = number.toCharArray();
        for (int i = 0; i < result.length; i++) {
            result[i] = chars[i] - '0';
        }
        return result;
    }

    public boolean isCardLegal(String cardNumber) {
        checkCardNumber(cardNumber);
        return repository.findCardByCardNumber(cardNumber).isEmpty();
    }
}
