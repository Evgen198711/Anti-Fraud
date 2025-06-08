package antifraud.user.service;

import antifraud.user.domain.User;
import antifraud.user.dto.*;
import antifraud.user.enums.LockState;
import antifraud.user.enums.Role;
import antifraud.user.exceptions.*;
import antifraud.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repository = repo;
        this.encoder = encoder;
    }

    public User saveNewUser(SaveNewUserRequest saveNewUserRequest) {
        String name = saveNewUserRequest.name();
        String username = saveNewUserRequest.username().toLowerCase();
        String encodedPassword = encoder.encode(saveNewUserRequest.password());

        if (repository.findAll().isEmpty()) {
            repository.addUser(name, username, encodedPassword, Role.ADMINISTRATOR.name(), LockState.UNLOCKED.isLocked());
        } else {
            try {
                repository.addUser(name, username, encodedPassword, Role.MERCHANT.name(), LockState.LOCKED.isLocked());
            } catch (DataIntegrityViolationException ex) {
                throw new UsernameIsNotUniqueException();
            }
        }

        return repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public ChangeLockStateResponse changeLockState(ChangeLockStateRequest lockStateRequest) {
        String username = lockStateRequest.username();
        LockState newLockState = defineNewLockState(lockStateRequest.operation());

        User user = repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (user.getRole() == Role.ADMINISTRATOR) {
            throw new AdminBlockException();
        }

        LockState currentLockState = user.getIsLocked();

        if (newLockState == currentLockState) {
            throw new IdenticalDataException();
        }

        repository.updateLockState(username, newLockState.isLocked());

        return new ChangeLockStateResponse(String.format("User %s %s!", username, newLockState.name().toLowerCase()));
    }

    private LockState defineNewLockState (String operation) {
        String operationInUpperCase = operation.toUpperCase();
        return switch (operationInUpperCase) {
            case "LOCK" -> LockState.LOCKED;
            case "UNLOCK" -> LockState.UNLOCKED;
            default -> throw new WrongOperationException();
        };
    }

    public User changeRole(ChangeRoleRequest roleRequest) {
        String username = roleRequest.username();
        Role newRole = defineNewRole(roleRequest.role());

        User user = repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Role currentRole = user.getRole();

        if (currentRole == Role.ADMINISTRATOR) {
            throw new WrongRoleException();
        }

        if (newRole == currentRole) {
            throw new IdenticalDataException();
        }

        repository.updateRole(username, newRole);

        return repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private Role defineNewRole (String role) {
        String roleToUpperCase = role.toUpperCase();

        return switch (roleToUpperCase) {
            case "SUPPORT" -> Role.SUPPORT;
            case "MERCHANT" -> Role.MERCHANT;
            default -> throw new WrongRoleException();
        };
    }

    public DeleteResponse deleteUser (String username) {
        User user = repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (user.getRole() == Role.ADMINISTRATOR) {
            throw new AdminDeleteException();
        }

        repository.deleteUser(username);

        return new DeleteResponse(username, "Deleted successfully!");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        String password = user.getPassword();
        String role = user.getRole().name();
        boolean isLocked = user.getIsLocked().isLocked();

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .roles(role)
                .accountLocked(isLocked)
                .build();
    }
}
