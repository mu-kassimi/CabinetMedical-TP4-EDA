package ma.fsr.eda.patientservice.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.patientservice.event.producer.PatientEventProducer;
import ma.fsr.eda.patientservice.model.Patient;
import ma.fsr.eda.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    
    private final PatientRepository repository;
    private final PatientEventProducer eventProducer;

    @Transactional
    public Patient createPatient(Patient patient) {
        patient.setDateCreation(LocalDateTime.now());
        Patient saved = repository.save(patient);
        
        // 🎯 Publication de l'événement après persistance
        eventProducer.publishPatientCreated(saved);
        
        return saved;
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    public Patient getPatientById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient introuvable : id = " + id));
    }
}