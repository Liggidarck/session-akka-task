package com.example.testtask.service;

import com.example.testtask.dto.ErrorMessage;
import com.example.testtask.dto.LoginDto;
import com.example.testtask.dto.RegisterDto;
import com.example.testtask.model.User;
import com.example.testtask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Setter
public class AuthenticationService {

    @Value(value = "${custom.max.session}")
    private int maxSession;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    SecurityContextRepository securityContextRepository;


    SecurityContextHolderStrategy securityContextHolderStrategy;

    AuthenticationManager authManager;

    RedisIndexedSessionRepository redisIndexedSessionRepository;

    SessionRegistry sessionRegistry;

    public AuthenticationService(AuthenticationManager authManager, RedisIndexedSessionRepository redisIndexedSessionRepository,
                                 SessionRegistry sessionRegistry, SecurityContextRepository securityContextRepository) {
        this.authManager = authManager;
        this.redisIndexedSessionRepository = redisIndexedSessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.securityContextRepository = securityContextRepository;
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    }

    public ErrorMessage registerUser(RegisterDto dto) {
        String email = dto.email().trim();
        Optional<User> exists = userRepository.findByPrincipal(email);

        if (exists.isPresent()) {
            return new ErrorMessage("session.errors.emailAlreadyRegistered");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setName(dto.name());
        user.setCreatedOn(String.valueOf(LocalDateTime.now(ZoneId.of("UTC+3"))));
        user.setLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        userRepository.save(user);
        return new ErrorMessage("OK");
    }

    public String login(LoginDto dto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
                        dto.email().trim(), dto.password()));
        validateMaxSession(authentication);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);

        return "done!";
    }

    private void validateMaxSession(Authentication authentication) {
        if (maxSession <= 0) {
            return;
        }

        var principal = (UserDetails) authentication.getPrincipal();
        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(principal, false);

        if (sessions.size() >= maxSession) {
            sessions.stream()
                    .min(Comparator.comparing(SessionInformation::getLastRequest))
                    .ifPresent(sessionInfo -> this.redisIndexedSessionRepository.deleteById(sessionInfo.getSessionId()));
        }
    }
}
