package cz.upce.warehouse.controller;

import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.dto.LoginResponse;
import cz.upce.warehouse.dto.LoginRequest;
import cz.upce.warehouse.dto.RoleEnum;
import cz.upce.warehouse.dto.UserDto;
import cz.upce.warehouse.repository.UserRepository;
import cz.upce.warehouse.security.JwtTokenUtil;
import cz.upce.warehouse.service.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new LoginResponse(token,userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),userDetails.getPhone(),roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setRole(RoleEnum.valueOf(userDto.getRole()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully! Redirecting...");
    }
}
