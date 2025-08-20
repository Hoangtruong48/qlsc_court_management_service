package com.example.courtmanagement_service.request.base_request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseDeleteRequest {
    List<Integer> ids;

    Integer status;

    public String validate() {
        if (ids == null || ids.isEmpty()) {
            return "ids is empty";
        }
        return null;
    }
}
