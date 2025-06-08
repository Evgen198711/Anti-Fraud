package antifraud.transaction.repository;

import antifraud.transaction.dto.Limits;
import antifraud.transaction.enums.Limit;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LimitsRepository {
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS limits (" +
            "card_number VARCHAR(256) PRIMARY KEY," +
            "allowed_limit INT DEFAULT 200," +
            "manual_limit INT DEFAULT 1500" +
            ");";
    private final JdbcTemplate jdbc;

    public LimitsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        jdbc.update(CREATE_TABLE);
    }

    public void addNewCard(String cardNumber) {
        String sql = "INSERT INTO limits (card_number) " +
                "VALUES (?);";
        jdbc.update(sql, cardNumber);
    }

    public Optional<Limits> findCardLimits(String cardNumber) {
        String sql = "SELECT * FROM limits WHERE card_number = ?";
        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, (r, i) -> new Limits(
                    r.getString("card_number"),
                    r.getInt("allowed_limit"),
                    r.getInt(("manual_limit"))
            ), cardNumber));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public void updateLimit(Limit limitType, String cardNumber, int limit) {
        String sqlAllowed = "UPDATE limits " +
                "SET allowed_limit = ? " +
                "WHERE card_number = ?";
        String sqlManual = "UPDATE limits " +
                "SET manual_limit = ? " +
                "WHERE card_number = ?";
        if (limitType == Limit.ALLOWED) {
            jdbc.update(sqlAllowed, limit, cardNumber);
        } else {
            jdbc.update(sqlManual, limit, cardNumber);
        }

    }
}
