package com.example.prodajem_kupujem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProdajemKupujemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProdajemKupujemApplication.class, args);
    }


//    @Bean
//    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder encoder){
//        return args -> {
//            userRepository.save(new AppUser("milos@gmail.com",encoder.encode("aleksa111"),"Milos","NS","123",600,null));
//        };
//    }
}
