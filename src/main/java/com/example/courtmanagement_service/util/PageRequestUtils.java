package com.example.courtmanagement_service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestUtils {
    public static PageRequest buildPageRequest(int start, int limit) {
        int page = start / limit;
        Sort sort = Sort.by(
                Sort.Order.asc("id")
        );
        return PageRequest.of(page, limit, sort);
    }
}
