package com.withNoa;

import com.withNoa.entity.Client;
import com.withNoa.entity.enums.Role;
import com.withNoa.entity.AppUser;
import com.withNoa.repository.ClientRepository;
import com.withNoa.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InvoiceGeneratorApplication implements CommandLineRunner {

	@Value("${seed.admin.username}")
	private String adminUsername;

	@Value("${seed.admin.password}")
	private String adminPassword; // Already encrypted in YAML

	@Value("${seed.user.username}")
	private String userUsername;

	@Value("${seed.user.password}")
	private String userPassword; // Already encrypted in YAML

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	AppUserRepository appUserRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(InvoiceGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("🔐 SSL password from env: " + System.getenv("SSL_KEYSTORE_PASSWORD"));

		// 🧾 Seed a test client
		clientRepository.save(new Client(null, "Test Client", "test@withnoa.com"));

		// 👑 Seed admin user
		if (appUserRepository.findByUsername(adminUsername).isEmpty()) {
			appUserRepository.save(new AppUser(null, userUsername, userPassword, Role.USER));
			System.out.println("👑 Admin user created: " + adminUsername + " / [encrypted]");
		}

		// 🌸 Seed Noa's account
		if (appUserRepository.findByUsername(userUsername).isEmpty()) {
			appUserRepository.save(new AppUser(null, userUsername, userPassword, Role.USER));
			System.out.println("🌸 Noa's account created: " + userUsername + " / [encrypted]");
		} else {
			System.out.println("🔐 Admin user already exists.");
		}

		System.out.println("✅ PostgreSQL connection successful.");
	}
}
