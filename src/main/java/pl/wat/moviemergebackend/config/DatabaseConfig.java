package pl.wat.moviemergebackend.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;

@Slf4j
@AllArgsConstructor
@Configuration
public class DatabaseConfig {
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS pgcrypto");
            log.info("pgcrypto extension has been loaded.");
        } catch (Exception e) {
            log.info("Failed to load pgcrypto extension: " + e.getMessage());
        }
    }
}

