package com.wooricard.approval;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 카드 승인 서비스 애플리케이션
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
public class WooricardApprovalServiceApplication {
    public static void main(String[] args) {
        // .env 파일을 읽어서 시스템 프로퍼티로 복사
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(WooricardApprovalServiceApplication.class, args);
    }
}

