package com.example.demo.rest;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.response.TokenDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtHelper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.userDetails.CustomUserDetails;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(Authentication authentication) {
       // Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByUsername(userDetails.getUsername()).get());
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(userDetails.getId());
        String refreshTokenString = jwtHelper.generateRefreshToken(userDetails.getId(), refreshToken);

        return ResponseEntity.ok(new TokenDto(userDetails.getId(), accessToken, refreshTokenString));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpDto dto) {
        User user = new User(dto.getUsername(),passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(user.getId());
        String refreshTokenString = jwtHelper.generateRefreshToken(user.getId(), refreshToken);

        return ResponseEntity.ok(new TokenDto(user.getId(), accessToken, refreshTokenString));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }
    @Transactional
    @PostMapping("logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDto dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteByUserId(Long.valueOf(jwtHelper.getUserIdFromRefreshToken(refreshTokenString)));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDto dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))){
            // valid and exists in db

            UserDto user = userService.findById(Long.valueOf(jwtHelper.getUserIdFromRefreshToken(refreshTokenString)));
            String accessToken = jwtHelper.generateAccessToken(user.getId());

            return ResponseEntity.ok(new TokenDto(user.getId(), accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDto dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            UserDto userDto = userService.findById(Long.valueOf(jwtHelper.getUserIdFromRefreshToken(refreshTokenString)));
            User user = UserMapper.INSTANCE.toEntity(userDto);
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(user.getId());
            String newRefreshTokenString = jwtHelper.generateRefreshToken(user.getId(), refreshToken);

            return ResponseEntity.ok(new TokenDto(user.getId(), accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }
}
