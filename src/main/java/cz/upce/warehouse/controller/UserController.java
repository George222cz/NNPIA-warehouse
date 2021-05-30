package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.UserDto;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.dto.RoleEnum;
import cz.upce.warehouse.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createOrUpdateUser(@RequestBody UserDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(RoleEnum.valueOf(dto.getRole()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> deleteUser(@PathVariable Long userId){
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            userRepository.deleteById(userId);
            return userRepository.findAll();
        } else {
            throw new NoSuchElementException("User with ID: " + userId + " was not found!");
        }
    }
}
