package antifraud.user.api;

import antifraud.user.domain.User;
import antifraud.user.dto.*;
import antifraud.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody @Valid SaveNewUserRequest request) {
        User user = service.saveNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        DeleteResponse response = service.deleteUser(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/access")
    public ResponseEntity<ChangeLockStateResponse> changeLockState(@RequestBody @Valid ChangeLockStateRequest request) {
        ChangeLockStateResponse response = service.changeLockState(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/role")
    public ResponseEntity<User> changeRole(@RequestBody @Valid ChangeRoleRequest request) {
        User user = service.changeRole(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = service.findAll();
        return ResponseEntity.ok(users);
    }
}
