package com.brasilburger.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    
    public static String get(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = dotenv.get(key);
        }
        return value;
    }
    
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }
}