package com.gildas.springBaseProjet.config;

import com.gildas.springBaseProjet.entity.JwtEntity;
import com.gildas.springBaseProjet.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService  jwtService;
    public JwtFilter(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String token = null;
        JwtEntity tokenInDB = null;
        Boolean isTokenExpired = true;
       final String authorization = request.getHeader("Authorization");
       if (authorization != null && authorization.startsWith("Bearer ")) {
           token = authorization.substring(7);

           tokenInDB = this.jwtService.tokenByValue(token);

           isTokenExpired = jwtService.isTokenExpired(token);
           username = jwtService.readUserNameOnDB(token);
       }

       if (!isTokenExpired
               && SecurityContextHolder.getContext().getAuthentication() == null
               && Objects.equals(tokenInDB.getUsers().getEmail(), username)
       ) {
           UserDetails userDetails = userService.loadUserByUsername(username);
           UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(authentication);
       }

       filterChain.doFilter(request, response);
    }
}
