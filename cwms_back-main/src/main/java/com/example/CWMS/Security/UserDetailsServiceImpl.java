package com.example.CWMS.Security;

import com.example.CWMS.model.User;
import com.example.CWMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Recherche l'utilisateur par UserName (attention à la casse dans votre repo)
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // On force le format ROLE_NOM pour correspondre à .hasRole("ADMIN")
        String roleStr = user.getRole().getRoleName().toUpperCase();
        if (!roleStr.startsWith("ROLE_")) {
            roleStr = "ROLE_" + roleStr;
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority(roleStr))
        );
    }
}