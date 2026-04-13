package ma.fsr.eda.rendezvousservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class RendezVousServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RendezVousServiceApplication.class, args);
    }
}