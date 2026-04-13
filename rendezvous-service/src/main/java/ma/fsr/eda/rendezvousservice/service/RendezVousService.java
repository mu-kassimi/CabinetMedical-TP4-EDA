package ma.fsr.eda.rendezvousservice.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.rendezvousservice.event.producer.RendezVousEventProducer;
import ma.fsr.eda.rendezvousservice.model.RendezVous;
import ma.fsr.eda.rendezvousservice.repository.MedecinProjectionRepository;
import ma.fsr.eda.rendezvousservice.repository.PatientProjectionRepository;
import ma.fsr.eda.rendezvousservice.repository.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RendezVousService {
    
    private final RendezVousRepository repository;
    private final PatientProjectionRepository patientRepo;
    private final MedecinProjectionRepository medecinRepo;
    private final RendezVousEventProducer eventProducer;

    @Transactional
    public RendezVous createRendezVous(RendezVous rdv) {
        // Vérification locale (pas d'appel REST !)
        if (!patientRepo.existsById(rdv.getPatientId())) {
            throw new RuntimeException("Patient inexistant dans les projections locales");
        }
        
        if (!medecinRepo.existsById(rdv.getMedecinId())) {
            throw new RuntimeException("Médecin inexistant dans les projections locales");
        }
        
        rdv.setStatus("CREATED");
        RendezVous saved = repository.save(rdv);
        
        // Publication de l'événement
        eventProducer.publishRendezVousCreated(saved);
        
        return saved;
    }

    public List<RendezVous> getAllRendezVous() {
        return repository.findAll();
    }

    public RendezVous getRendezVousById(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable : id = " + id));
    }
}