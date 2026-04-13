package ma.fsr.eda.billingservice.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.ConsultationCreatedEvent;
import ma.fsr.eda.billingservice.service.BillingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsultationEventConsumer {

    private final BillingService billingService;

    @KafkaListener(topics = "consultation.created", groupId = "billing-group")
    public void consume(ConsultationCreatedEvent event) {
        log.info("📥 Received event: consultation.created for consultationId: {}", event.getConsultationId());

        billingService.generateFacture(event);

        log.info("✅ Facture generation attempted for consultation: {}", event.getConsultationId());
    }
}