package antifraud.transaction.mappers;

import antifraud.transaction.domain.Transaction;
import antifraud.transaction.enums.Region;
import antifraud.transaction.enums.Result;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction tr = new Transaction();
        tr.setTransactionId(rs.getLong("id"));
        tr.setAmount(rs.getLong("amount"));
        tr.setIp(rs.getString("ip"));
        tr.setNumber(rs.getString("card_number"));
        tr.setRegion(Region.valueOf(rs.getString("region")));
        tr.setDate(rs.getObject("transaction_date", LocalDateTime.class).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        tr.setResult(Result.of(rs.getString("result")));
        tr.setFeedback(rs.getString("feedback"));

        return tr;
    }
}
