package com.smart;

import com.smart.service.ColisService;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SmrtApplicationTests {

    @Autowired
    private ColisService colisService;

    @Test
    void contextLoads() {
        // Vérifie que le contexte Spring se charge sans problème
    }

    @Test
    void testColisServiceIsLoaded() {
        // Vérifie que le service est bien injecté par Spring
        assertThat(colisService).isNotNull();
    }
}
