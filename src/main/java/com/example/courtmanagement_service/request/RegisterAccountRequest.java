package com.example.courtmanagement_service.request;

import com.example.quanlysancau.entity.User;
import com.example.quanlysancau.util.Mappable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterAccountRequest implements Mappable<User> {
    String userName;
    String password;
    String email;
    String phone;
    Integer role;
    Integer status;

    @Override
    public User toEntity() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return User.builder()
                .username(this.userName)
                .passwordHash(passwordEncoder.encode(this.password))
                .email(this.email)
                .phone(this.phone)
                .role(this.role)
                .status(this.status)
                .build();
    }

    public String validate() {
        if (!StringUtils.hasLength(userName)) {
            return "Username is required";
        }
        if (!StringUtils.hasLength(password)) {
            return "Password is required";
        }
        if (!StringUtils.hasLength(phone)) {
            return "Phone number is required";
        }
        return null;
    }
}
