package com.example.internintelligence_movieapidevelopment.config.enums;

public enum SecurityUrls {
    PERMIT_ALL("/api/public/**"),
    ANY_AUTHENTICATED("/api/user/**"),
    USER("/api/user/**"),
    ADMIN("/api/admin/**"),
    ADMIN_AND_USER("/api/user/**", "/api/admin/**");

    private final String[] patterns;

    SecurityUrls(String... patterns) {
        this.patterns = patterns;
    }

    public String[] getUrls() {
        return patterns;
    }
}
