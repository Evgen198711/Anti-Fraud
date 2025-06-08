package antifraud.IOC.repository;

import antifraud.IOC.damain.StolenCard;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class StolenCardRepository {
    private String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS stolen_cards (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "number VARCHAR(256) NOT NULL UNIQUE" +
            ");";
    private JdbcTemplate jdbc;

    public StolenCardRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        jdbc.update(CREATE_TABLE);
    }

    public void addStolenCard(String cardNumber) {
        String sql = "INSERT INTO stolen_cards (number) VALUES(?);";
        jdbc.update(sql, cardNumber);
    }

    public Optional<StolenCard> findCardByCardNumber(String cardNumber) {
        String sql = "SELECT * FROM stolen_cards WHERE number = ?;";

        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, (r, i) -> new StolenCard(
                    r.getLong("id"),
                    r.getString("number")
            ), cardNumber));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<StolenCard> findAll() {
        String sql = "SELECT * FROM stolen_cards";

        return jdbc.query(sql, (r, i) -> new StolenCard(
                r.getLong("id"),
                r.getString("number")
        ));
    }

    public void deleteCard(String cardNumber) {
        String sql = "DELETE FROM stolen_cards WHERE number = ?;";
        jdbc.update(sql, cardNumber);
    }

}
