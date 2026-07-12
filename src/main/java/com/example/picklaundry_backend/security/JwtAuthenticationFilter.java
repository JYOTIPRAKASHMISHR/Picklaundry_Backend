package com.example.picklaundry_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n========== JWT FILTER ==========");

        String authHeader = request.getHeader("Authorization");

        System.out.println("HEADER : " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("No Token");

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {

            System.out.println("Checking Token...");

            if (!jwtService.isTokenValid(jwt)) {

                System.out.println("Token Invalid");

                filterChain.doFilter(request, response);
                return;
            }

            String subject = jwtService.extractUserId(jwt);

            System.out.println("SUBJECT : " + subject);

            if ("SUPER_ADMIN".equals(subject)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                "SUPER_ADMIN",
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("SUPER ADMIN LOGIN");

            } else {

                Long userId = Long.parseLong(subject);

                System.out.println("Loading User : " + userId);

                CustomUserDetails userDetails =
                        (CustomUserDetails) customUserDetailsService.loadUserById(userId);

                System.out.println("User Found : " + userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("AUTH SUCCESS");
            }

        } catch (Exception e) {

            System.out.println("JWT FILTER ERROR");
            e.printStackTrace();

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}