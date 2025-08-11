package com.example.courtmanagement_service.service;

import com.example.quanlysancau.entity.User;
import com.example.quanlysancau.repo.UserRepoJpa;
import com.example.quanlysancau.request.IntrospectRequest;
import com.example.quanlysancau.request.RegisterAccountRequest;
import com.example.quanlysancau.request.UserLoginRequest;
import com.example.quanlysancau.response.ApiResponse;
import com.example.quanlysancau.response.IntrospectResponse;
import com.example.quanlysancau.response.UserLoginResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    Logger LOG = LoggerFactory.getLogger(UserService.class);
    UserRepoJpa userRepoJpa;

    @NonFinal
    @Value("${jwt.signer_key}")
    private String signerKey;

    public ApiResponse<?> registerAccount(RegisterAccountRequest request) {
        ApiResponse<?> response = new ApiResponse<>();

        String messageError = request.validate();
        if (StringUtils.hasLength(messageError)) {
            response.setMessageFailed(messageError);
            return response;
        }

        boolean isExistUser = userRepoJpa.existsByUsername(request.getUserName());
        if (isExistUser) {
            response.setMessageFailed("Username is already taken");
            return response;
        }
        User user = request.toEntity();
        userRepoJpa.save(user);
        return ApiResponse.builder()
                .message("Registered Successfully")
                .statusCode(0)
                .data(user)
                .build();
    }

    public ApiResponse<?> authenticate(UserLoginRequest request) {
        ApiResponse<?> response = new ApiResponse<>();
        var messageError = request.validate();
        if (StringUtils.hasLength(messageError)) {
            response.setMessageFailed(messageError);
            return response;
        }

        var user = userRepoJpa.findByUsername(request.getUserName()).orElseThrow();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var token = generateToken(request.getUserName(), user.getRole());
        boolean isMatched = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        return ApiResponse.builder()
                .statusCode(isMatched ? 0 : 1)
                .message(isMatched ? "Successfully logged in" : "Invalid username or password")
                .data(new UserLoginResponse(isMatched ? token : "", isMatched))
                .build();
    }

    private String generateToken(String userName, int role) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        String roles;
        if (role == User.UserRole.ADMIN.getCode()) {
            roles = "ADMIN";
        } else if (role == User.UserRole.STAFF.getCode()) {
            roles = ("STAFF");
        } else if (role == User.UserRole.CUSTOMER.getCode()) {
            roles = ("CUSTOMER");
        } else {
            roles = "GUEST"; // Hoặc để trống nếu không xác định
        }
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .issuer("quanlysancau")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("roles", roles)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        LOG.info("Signer key = {}", signerKey);
        try {
            jwsObject.sign(new MACSigner(signerKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }

    public ApiResponse<?> introSpectToken(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        var valid = verified && expiryTime.after(new Date());

        return ApiResponse.builder()
                .statusCode(valid ? 0 : 1)
                .data(IntrospectResponse.builder()
                        .valid(valid))
                .build();
    }

    public ApiResponse<?> getUserByUserName(String username) {
        var user = userRepoJpa.findByUsername(username).orElse(null);
        return ApiResponse.builder()
                .data(user)
                .statusCode(user != null ? 0 : 1)
                .build();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> findAllUser() {
        LOG.info("---> Finding all users : ");
        return ApiResponse.builder()
                .statusCode(0)
                .data(userRepoJpa.findAll())
                .build();
    }
}
