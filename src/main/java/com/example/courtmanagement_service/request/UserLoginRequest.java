package com.example.courtmanagement_service.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginRequest {
    String userName;
    String password;

    public String validate() {
        if (!StringUtils.hasLength(userName)) {
            return "Username is required";
        }
        if (!StringUtils.hasLength(password)) {
            return "Password is required";
        }
        return null;
    }
}
