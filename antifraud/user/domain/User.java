package antifraud.user.domain;

import antifraud.user.enums.LockState;
import antifraud.user.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LockState isLocked;


}
