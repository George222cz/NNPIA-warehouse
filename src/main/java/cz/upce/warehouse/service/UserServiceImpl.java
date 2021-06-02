package cz.upce.warehouse.service;

import cz.upce.warehouse.dto.RoleEnum;
import cz.upce.warehouse.dto.UserDto;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public ResponseEntity<Object> createOrUpdateUser(UserDto dto){
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

    public List<User> deleteUser(Long userId){
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            userRepository.deleteById(userId);
            return userRepository.findAll();
        } else {
            throw new NoSuchElementException("User with ID: " + userId + " was not found!");
        }
    }
}
