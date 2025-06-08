package antifraud.user.mappers;

import antifraud.user.domain.User;
import antifraud.user.enums.LockState;
import antifraud.user.enums.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString(("password")));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setIsLocked(LockState.is(rs.getBoolean("locked")));

        return user;
    }
}
