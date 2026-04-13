package ma.fsr.eda.billingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.ConsultationCreatedEvent;
import ma.fsr.eda.billingservice.event.producer.BillingEventProducer;
import ma.fsr.eda.billingservice.model.Facture;
import ma.fsr.eda.billingservice.repository.FactureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final FactureRepository repository;
    private final BillingEventProducer eventProducer;
    private final Random random = new Random();

    @Transactional
    public void generateFacture(ConsultationCreatedEvent event) {
        try {
            // 🎲 Simulation d'un échec aléatoire (30% de chance d'échec pour démo)
            if (random.nextInt(100) < 30) {
                log.warn("⚠️ Simulating billing failure for consultation: {}", event.getConsultationId());
                throw new RuntimeException("Erreur de facturation simulée");
            }

            Facture facture = new Facture();
            facture.setConsultationId(event.getConsultationId());
            facture.setPatientId(event.getPatientId());
            facture.setMontant(200.0); // Montant fixe pour la démo
            facture.setStatut("CREATED");
            facture.setDateCreation(LocalDateTime.now());

            Facture saved = repository.save(facture);

            log.info("✅ Facture created successfully: {}", saved.getId());

            // ✅ Publication événement de succès
            eventProducer.publishFactureCreated(saved);

        } catch (Exception e) {
            log.error("❌ Billing failed for consultation: {} - Error: {}",
                    event.getConsultationId(), e.getMessage());

            // ❌ Publication événement d'échec (déclenche compensation)
            eventProducer.publishFactureFailed(
                    event.getConsultationId(),
                    "Erreur génération facture: " + e.getMessage());
        }
    }

    public List<Facture> getAllFactures() {
        return repository.findAll();
    }

    public Facture getFactureById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable : id = " + id));
    }
}