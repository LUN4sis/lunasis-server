package com.github.lunasis.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.global.dto.ApiResponse;
import com.github.lunasis.global.exception.ApiException;
import com.github.lunasis.global.security.authentication.UserAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.extractToken(request);

        try {
            if (jwtUtil.validateToken(accessToken)) {

                UUID userId = jwtUtil.extractUUID(accessToken);

                User user = userRepository.findById(userId).orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

                UserAuthentication authentication = new UserAuthentication(user);
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ApiException e) {

            ApiResponse<?> apiResponse = ApiResponse.error(e);

            String body = objectMapper.writeValueAsString(apiResponse);
            response.setStatus(e.getCode());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(body);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
