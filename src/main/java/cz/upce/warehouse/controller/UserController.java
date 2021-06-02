package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.UserDto;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createOrUpdateUser(@RequestBody UserDto dto){
        return userService.createOrUpdateUser(dto);
    }

    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }
}
