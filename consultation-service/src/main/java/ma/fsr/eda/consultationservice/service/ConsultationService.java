package ma.fsr.eda.consultationservice.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.eventdtolib.event.dto.RendezVousCreatedEvent;
import ma.fsr.eda.consultationservice.event.producer.ConsultationEventProducer;
import ma.fsr.eda.consultationservice.model.Consultation;
import ma.fsr.eda.consultationservice.repository.ConsultationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository repository;
    private final ConsultationEventProducer eventProducer;

    @Transactional
    public void createConsultationFromRdv(RendezVousCreatedEvent event) {
        Consultation consultation = new Consultation();
        consultation.setRendezVousId(event.getRendezVousId());
        consultation.setPatientId(event.getPatientId());
        consultation.setMedecinId(event.getMedecinId());
        consultation.setDateConsultation(LocalDateTime.now());
        consultation.setStatut("CREATED");

        Consultation saved = repository.save(consultation);

        // Publication de l'événement
        eventProducer.publishConsultationCreated(saved);
    }

    public List<Consultation> getAllConsultations() {
        return repository.findAll();
    }

    public Consultation getConsultationById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation introuvable : id = " + id));
    }
}