package ma.fsr.eda.rendezvousservice.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.RendezVousCreatedEvent;
import ma.fsr.eda.rendezvousservice.model.RendezVous;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RendezVousEventProducer {

    private final KafkaTemplate<String, RendezVousCreatedEvent> kafkaTemplate;

    public void publishRendezVousCreated(RendezVous rdv) {
        RendezVousCreatedEvent event = new RendezVousCreatedEvent(
                UUID.randomUUID().toString(),
                rdv.getId(),
                rdv.getPatientId(),
                rdv.getMedecinId(),
                rdv.getDateRendezVous(),
                rdv.getStatus());

        log.info("📤 Publishing event: rendezvous.created for rendezVousId: {}", rdv.getId());
        kafkaTemplate.send("rendezvous.created", rdv.getId(), event);
    }
}