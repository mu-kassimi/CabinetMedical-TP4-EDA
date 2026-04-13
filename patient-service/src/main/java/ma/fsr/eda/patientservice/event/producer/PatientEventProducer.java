package ma.fsr.eda.patientservice.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.PatientCreatedEvent;
import ma.fsr.eda.patientservice.model.Patient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientEventProducer {

    private final KafkaTemplate<String, PatientCreatedEvent> kafkaTemplate;

    public void publishPatientCreated(Patient patient) {
        PatientCreatedEvent event = new PatientCreatedEvent(
                UUID.randomUUID().toString(),
                patient.getId(),
                patient.getNom(),
                patient.getEmail(),
                patient.getDateCreation());

        log.info("📤 Publishing event: patient.created for patientId: {}", patient.getId());
        kafkaTemplate.send("patient.created", patient.getId(), event);
    }
}