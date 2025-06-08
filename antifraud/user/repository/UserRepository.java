package antifraud.user.repository;

import antifraud.user.domain.User;
import antifraud.user.enums.Role;
import antifraud.user.mappers.UserRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR(256)," +
            "username VARCHAR_IGNORECASE(256) NOT NULL UNIQUE," +
            "password VARCHAR(256)," +
            "role ENUM('ADMINISTRATOR', 'MERCHANT', 'SUPPORT')," +
            "locked BOOL" +
            ");";

    UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        jdbc.update(CREATE_TABLE);
    }

    public void addUser(String name, String username, String password, String role, boolean lockState) {
        String sql = "INSERT INTO users (name, username, password, role, locked)" +
                " VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, name, username, password, role, lockState);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbc.query(sql, new UserRowMapper());
    }

    public Optional<User> findUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, new UserRowMapper(), username));
        } catch (EmptyResultDataAccessException ex){
            return Optional.empty();
        }

    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        jdbc.update(sql, username);
    }

    public void updateRole(String username, Role role) {
        String sql = "UPDATE users SET role = ? WHERE username = ?";
        jdbc.update(sql, role.name(), username);
    }

    public void updateLockState(String username, boolean isLocked) {
        String sql = "UPDATE users SET locked = ? WHERE username = ?";
        jdbc.update(sql, isLocked, username);
    }
}
