package com.wooricard.approval;

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
        SpringApplication.run(WooricardApprovalServiceApplication.class, args);
    }
}
