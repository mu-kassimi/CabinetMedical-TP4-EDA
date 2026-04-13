package ma.fsr.eda.rendezvousservice.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.FactureFailedEvent;
import ma.fsr.eda.rendezvousservice.model.RendezVous;
import ma.fsr.eda.rendezvousservice.repository.RendezVousRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FactureEventConsumer {

    private final RendezVousRepository rendezVousRepository;

    @KafkaListener(topics = "facture.failed", groupId = "rendezvous-group")
    @Transactional
    public void handleFactureFailed(FactureFailedEvent event) {
        log.warn("📥 Received event: facture.failed for consultationId: {} - Reason: {}",
                event.getConsultationId(), event.getReason());

        // 🔄 COMPENSATION : Annuler tous les rendez-vous liés
        // (Dans une vraie app, il faudrait lier consultation → rendezVous via les
        // événements)
        log.warn("⚠️ COMPENSATION TRIGGERED: Cancelling related rendez-vous");

        // Pour la démo, on annule tous les rendez-vous CREATED
        rendezVousRepository.findAll().stream()
                .filter(rdv -> "CREATED".equals(rdv.getStatus()))
                .forEach(rdv -> {
                    rdv.setStatus("CANCELLED");
                    rendezVousRepository.save(rdv);
                    log.warn("❌ Rendez-vous {} cancelled due to billing failure", rdv.getId());
                });
    }
}