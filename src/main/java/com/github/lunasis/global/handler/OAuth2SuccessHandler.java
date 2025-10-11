package com.github.lunasis.global.handler;

import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByGoogleId((String) oAuth2User.getAttributes().get("sub"));

        User user = optionalUser.orElseGet(() -> from(oAuth2User.getAttributes()));

        boolean firstLogin = user.getNickname() == null;

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        String exchangeTokenId = jwtUtil.generateExchangeToken(accessToken, refreshToken, firstLogin, (String) oAuth2User.getAttributes().get("name"), user.getPrivateChat());

        String redirectUrl = String.format("http://localhost:3000/oauth/callback?code=%s", exchangeTokenId);
        response.sendRedirect(redirectUrl);
    }


    private User from(Map<String, Object> attributes) {

        return userRepository.save(
                User.builder()
                        .nickname(null)
                        .googleId((String) attributes.get("sub"))
                        .profile((String) attributes.get("picture"))
                        .build()
        );
    }

}
