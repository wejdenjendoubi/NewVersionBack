package com.example.CWMS.controller;

import com.example.CWMS.payload.*;
import com.example.CWMS.Security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // --- DEBUG : Affiche le hash dans la console à chaque tentative ---
        System.out.println("DEBUG: Mot de passe reçu: " + loginRequest.getPassword());
        System.out.println("DEBUG: Hash attendu en base pour ce password: " + encoder.encode(loginRequest.getPassword()));
        // -----------------------------------------------------------------

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                             loginRequest.getPassword()
                    )
            );

            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password incorrect");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}