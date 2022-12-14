package com.example.demo.security.filter;

import com.example.demo.security.provider.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
    @Autowired
    AuthenticationManager authenticationManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
        Optional<String> accessToken = parseAccessToken(request);
        if (accessToken.isPresent()){
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(accessToken.get());
            JwtAuthenticationToken authenticate = (JwtAuthenticationToken) authenticationManager.authenticate(jwtAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }}catch (AuthenticationException e) {
            log.error("Authentication failed");
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }
        return Optional.empty();
    }
}


/*        try {
            Optional<String> accessToken = parseAccessToken(request);
            if(accessToken.isPresent() && jwtHelper.validateAccessToken(accessToken.get())) {
                String userId = jwtHelper.getUserIdFromAccessToken(accessToken.get());
                User user = userService.findById(userId);
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        } catch (Exception e) {
            log.error("cannot set authentication", e);
        }*/