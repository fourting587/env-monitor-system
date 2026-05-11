package com.soap.envmonitorsystem.bootstrap;

import com.soap.envmonitorsystem.entity.Role;
import com.soap.envmonitorsystem.entity.User;
import com.soap.envmonitorsystem.repository.UserRepository;
import com.soap.envmonitorsystem.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SensorService sensorService;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        log.info("初始化默认用户与演示传感器数据…");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setDisplayName("系统管理员");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        User u = new User();
        u.setUsername("demo");
        u.setPasswordHash(passwordEncoder.encode("demo123"));
        u.setDisplayName("演示账号");
        u.setRole(Role.USER);
        userRepository.save(u);

        for (int i = 0; i < 24; i++) {
            sensorService.appendDemoReading();
        }
    }
}
