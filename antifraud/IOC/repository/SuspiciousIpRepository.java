package antifraud.IOC.repository;

import antifraud.IOC.damain.IP;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SuspiciousIpRepository {
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ips (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "ip VARCHAR(256) NOT NULL UNIQUE" +
            ");";
    private final JdbcTemplate jdbc;
    public SuspiciousIpRepository(JdbcTemplate jdbc) {
        this.jdbc =jdbc;
        jdbc.update(CREATE_TABLE);
    }

    public void addSuspiciousIp(String ip) {
        String sql = "INSERT INTO ips (ip) VALUES (?);";
        jdbc.update(sql, ip);
    }

    public Optional<IP> findIpByIp(String ip) {
        String sql = "SELECT * FROM ips WHERE ip = ?";

        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, (r, i) -> new IP(
                    r.getLong("id"),
                    r.getString("ip")
            ), ip));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<IP> findAll() {
        String sql = "SELECT * FROM ips";

        return jdbc.query(sql, (r, i) -> new IP(
                r.getLong("id"),
                r.getString("ip")
        ));
    }

    public void deleteIp(String ip) {
        String sql = "DELETE FROM ips WHERE ip = ?";
        jdbc.update(sql, ip);
    }

}
