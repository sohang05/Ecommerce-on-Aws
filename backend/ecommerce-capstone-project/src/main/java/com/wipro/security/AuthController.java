package com.wipro.security;

import com.wipro.model.Customer;
import com.wipro.model.LoginResponse;
import com.wipro.repository.CustomerRepository;
import com.wipro.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * JSON-based auth endpoints for SPA.
 * POST /api/auth/login   -> returns { accessToken: "..." }
 * POST /api/auth/register -> creates a customer (201) 
 * GET  /api/auth/me      -> returns current user info (requires Authorization: Bearer <token>)
 */


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerRepository customerRepository;

    // NOTE: You already have LoginRequest and JwtResponse classes in com.wipro.security
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getEmail(),
//                            loginRequest.getPassword()
//                    )
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = jwtTokenProvider.generateToken(authentication);
//            return ResponseEntity.ok(new JwtResponse(jwt));
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error authenticating user");
//        }
//    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Customer user = customerRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new LoginResponse(token, user.getRole()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Register with JSON body { "name": "...", "email": "...", "password": "..." }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Customer created = customerService.registerCustomer(request.getName(), request.getEmail(), request.getPassword());
            // Do not return password
            CreatedUserDto dto = new CreatedUserDto(created.getId(), created.getName(), created.getEmail(), created.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Returns current user data based on JWT authentication
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        SimpleUserDto dto = new SimpleUserDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getRole());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        String[] categories = {"Electronics", "Footwear", "Fashion", "Clothing"};
        return ResponseEntity.ok(categories);
    }

    // DTOs
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class SimpleUserDto {
        private Long id;
        private String name;
        private String email;
        private String role;
        public SimpleUserDto(Long id, String name, String email, String role) {
            this.id = id; this.name = name; this.email = email; this.role = role;
        }
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }

    public static class CreatedUserDto extends SimpleUserDto {
        public CreatedUserDto(Long id, String name, String email, String role) { super(id, name, email, role); }
    }
}
