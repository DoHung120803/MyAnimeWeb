package com.myanime.domain.service.auth;

import com.myanime.infrastructure.configurations.securities.utils.JwtUtil;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.application.rest.requests.authen.AuthenticationRequest;
import com.myanime.application.rest.requests.authen.IntrospectRequest;
import com.myanime.application.rest.responses.AuthenticationResponse;
import com.myanime.application.rest.responses.IntrospectResponse;
import com.myanime.infrastructure.jparepos.UserJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements AuthenticationServiceInterface {

    UserJpaRepository userJpaRepository;
    JwtUtil jwtUtil;

    public IntrospectResponse introspect(IntrospectRequest request) {
        boolean valid = jwtUtil.validateToken(request.getToken());
        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userJpaRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var jwtDTO = jwtUtil.generateToken(user.getId());

        return AuthenticationResponse.builder()
                .token(jwtDTO.getJwt())
                .authenticated(true)
                .build();
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
}
