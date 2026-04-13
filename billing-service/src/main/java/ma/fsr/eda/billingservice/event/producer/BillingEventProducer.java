package ma.fsr.eda.billingservice.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.FactureCreatedEvent;
import ma.fsr.eda.eventdtolib.event.dto.FactureFailedEvent;
import ma.fsr.eda.billingservice.model.Facture;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishFactureCreated(Facture facture) {
        FactureCreatedEvent event = new FactureCreatedEvent(
                UUID.randomUUID().toString(),
                facture.getId(),
                facture.getConsultationId(),
                facture.getPatientId(),
                facture.getMontant(),
                facture.getStatut(),
                facture.getDateCreation());
        kafkaTemplate.send("facture.created", event);
        log.info("✅ Published FactureCreated event for consultation: {}", facture.getConsultationId());
    }

    public void publishFactureFailed(String consultationId, String errorMessage) {
        FactureFailedEvent event = new FactureFailedEvent(
                UUID.randomUUID().toString(),
                consultationId,
                errorMessage,
                LocalDateTime.now());
        kafkaTemplate.send("facture.failed", event);
        log.info("❌ Published FactureFailed event for consultation: {} - Reason: {}", consultationId, errorMessage);
    }
}