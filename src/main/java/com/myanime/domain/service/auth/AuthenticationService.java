package com.myanime.domain.service.auth;

import com.myanime.application.rest.requests.authen.AuthenticationRequest;
import com.myanime.application.rest.requests.authen.IntrospectRequest;
import com.myanime.application.rest.requests.authen.RefreshTokenRequest;
import com.myanime.application.rest.responses.AuthenticationResponse;
import com.myanime.application.rest.responses.IntrospectResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.dtos.JwtDTO;
import com.myanime.domain.exceptions.LoginException;
import com.myanime.infrastructure.configurations.securities.utils.CustomUserDetailService;
import com.myanime.infrastructure.configurations.securities.utils.CustomUserDetails;
import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements AuthenticationServiceInterface {

    AuthenticationManager authenticationManager;
    JwtUtil jwtUtil;
    CustomUserDetailService customUserDetailService;

    public IntrospectResponse introspect(IntrospectRequest request) {
        boolean valid = jwtUtil.validateToken(request.getToken());
        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws LoginException {
        try {
            String username = request.getUsername();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            JwtDTO jwtDTO = jwtUtil.generateToken(username);

            return AuthenticationResponse.builder()
                    .token(jwtDTO.getJwt())
                    .refreshToken(jwtDTO.getRefreshToken())
                    .authenticated(true)
                    .expireTime(jwtDTO.getExpireTime())
                    .expireAt(jwtDTO.getExpireAt())
                    .build();
        } catch (Exception e) {
            throw new LoginException("Tài khoản hoặc mật khẩu không chính xác");
        }
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws BadRequestException {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BadRequestException("Refresh token không hợp lệ");
        }

        String username = jwtUtil.extractUsername(refreshToken);

        CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        if (userDetails == null || !username.equals(userDetails.getUsername())) {
            throw new BadRequestException("Người dùng không tồn tại");
        }

        JwtDTO jwtDTO = jwtUtil.generateToken(username);

        return AuthenticationResponse.builder()
                .token(jwtDTO.getJwt())
                .refreshToken(jwtDTO.getRefreshToken())
                .authenticated(true)
                .expireTime(jwtDTO.getExpireTime())
                .expireAt(jwtDTO.getExpireAt())
                .build();
    }
}

//    private String buildScope(User user) {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//
//        if (!CollectionUtils.isEmpty(user.getRoles())) {
//            user.getRoles().forEach(role -> {
//                stringJoiner.add("ROLE_" + role.getName());
//                if (!CollectionUtils.isEmpty(role.getPermissions())) {
//                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
//                }
//            });
//        }
//
//        return stringJoiner.toString();
//    }

