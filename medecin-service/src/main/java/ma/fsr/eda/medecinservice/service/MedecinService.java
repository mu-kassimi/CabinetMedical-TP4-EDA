package ma.fsr.eda.medecinservice.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.medecinservice.event.producer.MedecinEventProducer;
import ma.fsr.eda.medecinservice.model.Medecin;
import ma.fsr.eda.medecinservice.repository.MedecinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedecinService {
    
    private final MedecinRepository repository;
    private final MedecinEventProducer eventProducer;

    @Transactional
    public Medecin createMedecin(Medecin medecin) {
        medecin.setDateCreation(LocalDateTime.now());
        Medecin saved = repository.save(medecin);
        
        // Publication de l'événement
        eventProducer.publishMedecinCreated(saved);
        
        return saved;
    }

    public List<Medecin> getAllMedecins() {
        return repository.findAll();
    }

    public Medecin getMedecinById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Médecin introuvable : id = " + id));
    }
}