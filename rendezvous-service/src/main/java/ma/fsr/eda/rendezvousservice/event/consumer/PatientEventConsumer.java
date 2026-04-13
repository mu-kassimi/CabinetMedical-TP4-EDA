package ma.fsr.eda.rendezvousservice.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.PatientCreatedEvent;
import ma.fsr.eda.rendezvousservice.model.projection.PatientProjection;
import ma.fsr.eda.rendezvousservice.repository.PatientProjectionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientEventConsumer {

    private final PatientProjectionRepository repository;

    @KafkaListener(topics = "patient.created", groupId = "rendezvous-group")
    public void consume(PatientCreatedEvent event) {
        log.info("📥 Received event: patient.created for patientId: {}", event.getPatientId());

        PatientProjection projection = new PatientProjection();
        projection.setPatientId(event.getPatientId());
        projection.setNom(event.getNom());
        projection.setEmail(event.getEmail());

        repository.save(projection);
        log.info("✅ Patient projection saved: {}", event.getPatientId());
    }
}