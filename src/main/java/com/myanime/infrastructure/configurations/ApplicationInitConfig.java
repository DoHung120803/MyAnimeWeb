package com.myanime.infrastructure.configurations;

import com.myanime.infrastructure.entities.jpa.Role;
import com.myanime.infrastructure.entities.jpa.User;
import com.myanime.infrastructure.jparepos.jpa.RoleRepository;
import com.myanime.infrastructure.jparepos.jpa.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    @NonFinal
    private String adminUsernameDefault;

    @Value("${admin.password}")
    @NonFinal
    private String adminPasswordDefault;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .build();

                Role userRole = Role.builder()
                        .name("USER")
                        .description("User role")
                        .build();

                roleRepository.save(adminRole);
                roleRepository.save(userRole);

                Set<Role> adminRoleSet = new HashSet<>();
                adminRoleSet.add(adminRole);

                User admin = User.builder()
                        .username(adminUsernameDefault)
                        .password(passwordEncoder.encode(adminPasswordDefault))
                        .roles(adminRoleSet)
                        .build();

                userRepository.save(admin);
                log.warn("Admin user has been created with default password: *****, please change it");
            }
        };
    }
}
