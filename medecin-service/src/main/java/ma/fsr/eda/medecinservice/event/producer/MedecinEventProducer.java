package ma.fsr.eda.medecinservice.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.MedecinCreatedEvent;
import ma.fsr.eda.medecinservice.model.Medecin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MedecinEventProducer {

    private final KafkaTemplate<String, MedecinCreatedEvent> kafkaTemplate;

    public void publishMedecinCreated(Medecin medecin) {
        MedecinCreatedEvent event = new MedecinCreatedEvent(
                UUID.randomUUID().toString(),
                medecin.getId(),
                medecin.getNom(),
                medecin.getSpecialite(),
                medecin.getEmail(),
                medecin.getDateCreation());

        log.info("📤 Publishing event: medecin.created for medecinId: {}", medecin.getId());
        kafkaTemplate.send("medecin.created", medecin.getId(), event);
    }
}