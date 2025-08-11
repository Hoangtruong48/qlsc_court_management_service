package com.example.courtmanagement_service.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleConfig {
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_STAFF = 2;
    public static final int ROLE_CUSTOMER = 3;

    public static final Set<Integer> allRoles =
            new HashSet<>(Arrays.asList(ROLE_ADMIN, ROLE_STAFF, ROLE_CUSTOMER));
}
