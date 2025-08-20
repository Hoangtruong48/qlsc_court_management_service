package com.example.courtmanagement_service.specification;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchCriteria {

    // ===== Getters & Setters =====
    private String key;                // field name
    private SearchOperation operation; // EQUAL, LIKE, IN, BETWEEN, ...
    private Object value;              // value đơn
    private Object valueTo;            // value cho BETWEEN
    private List<?> values;            // list value cho IN/NOT_IN
    private Class<?> type;             // type thực tế của field (Integer, Long, String,...)

    // Constructor cho value đơn
    public SearchCriteria(String key, SearchOperation operation, Object value, Class<?> type) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.type = type;
    }

    // Constructor cho BETWEEN
    public SearchCriteria(String key, SearchOperation operation, Object value, Object valueTo, Class<?> type) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.valueTo = valueTo;
        this.type = type;
    }

    // Constructor cho IN / NOT_IN
    public SearchCriteria(String key, SearchOperation operation, List<?> values, Class<?> type) {
        this.key = key;
        this.operation = operation;
        this.values = values;
        this.type = type;
    }

}

