package antifraud.transaction.repository;

import antifraud.transaction.domain.Transaction;
import antifraud.transaction.dto.NewTransactionRequestWithResult;
import antifraud.transaction.mappers.TransactionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {
    private JdbcTemplate jdbc;
    private String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS transactions (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "amount BIGINT," +
            "ip VARCHAR(256) NOT NULL," +
            "card_number VARCHAR(256) NOT NULL," +
            "region VARCHAR(256) NOT NULL," +
            "transaction_date TIMESTAMP," +
            "result VARCHAR(256)," +
            "feedback VARCHAR(256) DEFAULT ''" +
            ");";

    public TransactionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        jdbc.update(CREATE_TABLE);
    }

    public void addTransaction(NewTransactionRequestWithResult transaction) {
        String sql = "INSERT INTO transactions (" +
                "amount, ip, card_number, region, transaction_date, result) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        jdbc.update(sql,
                transaction.amount(),
                transaction.ip(),
                transaction.number(),
                transaction.region(),
                transaction.date(),
                transaction.result());
    }

    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions";
        return jdbc.query(sql, new TransactionRowMapper());
    }

    public List<Transaction> findTransactionsByCardNumber(String cardNumber) {
        String sql = "SELECT * FROM transactions WHERE card_number = ?";
        return jdbc.query(sql, new TransactionRowMapper(), cardNumber);
    }

    public Transaction findTransactionById(long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        return jdbc.queryForObject(sql, new TransactionRowMapper(), id);
    }

    public void updateTransaction(long id, String feedback) {
        String sql = "UPDATE transactions SET feedback = ? WHERE id = ?";
        jdbc.update(sql, feedback, id);
    }

    public int findTransactionsFromSpecifiedIpInTheLastHour(String cardNumber, String ip, LocalDateTime baseTime) {
        String sql = "SELECT COUNT (DISTINCT ip) FROM transactions WHERE card_number = ? AND ip <> ? AND " +
                "transaction_date BETWEEN DATEADD('HOUR', -1, CAST(? AS TIMESTAMP)) AND CAST(? AS TIMESTAMP)";
        return Optional.ofNullable(jdbc.queryForObject(sql, Integer.class, cardNumber, ip, baseTime, baseTime)).orElse(0);
    }

    public int findTransactionsFromSpecifiedRegionInTheLastHour(String cardNumber, String region, LocalDateTime baseTime) {
        String sql = "SELECT COUNT (DISTINCT region) FROM transactions WHERE card_number = ? AND  region <> ? AND " +
                "transaction_date BETWEEN DATEADD('HOUR', -1, CAST(? AS TIMESTAMP)) AND CAST(? AS TIMESTAMP)";
        return Optional.ofNullable(jdbc.queryForObject(sql, Integer.class, cardNumber, region, baseTime, baseTime)).orElse(0);
    }
}
