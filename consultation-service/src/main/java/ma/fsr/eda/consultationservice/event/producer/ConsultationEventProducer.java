package ma.fsr.eda.consultationservice.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.ConsultationCreatedEvent;
import ma.fsr.eda.consultationservice.model.Consultation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsultationEventProducer {

    private final KafkaTemplate<String, ConsultationCreatedEvent> kafkaTemplate;

    public void publishConsultationCreated(Consultation consultation) {
        ConsultationCreatedEvent event = new ConsultationCreatedEvent(
                UUID.randomUUID().toString(),
                consultation.getId(),
                consultation.getRendezVousId(),
                consultation.getPatientId(),
                consultation.getMedecinId(),
                consultation.getDateConsultation(),
                consultation.getStatut());

        log.info("📤 Publishing event: consultation.created for consultationId: {}", consultation.getId());
        kafkaTemplate.send("consultation.created", consultation.getId(), event);
    }
}