package ma.fsr.eda.rendezvousservice.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.rendezvousservice.model.projection.MedecinProjection;
import ma.fsr.eda.rendezvousservice.model.projection.PatientProjection;
import ma.fsr.eda.rendezvousservice.repository.MedecinProjectionRepository;
import ma.fsr.eda.rendezvousservice.repository.PatientProjectionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/api/v1/rendezvous/projections")
@RequiredArgsConstructor
public class ProjectionController {
    
    private final PatientProjectionRepository patientRepository;
    private final MedecinProjectionRepository medecinRepository;

    @PostMapping("/patient")
    public ResponseEntity<PatientProjection> createPatientProjection(@RequestBody PatientProjection projection) {
        PatientProjection saved = patientRepository.save(projection);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/medecin")
    public ResponseEntity<MedecinProjection> createMedecinProjection(@RequestBody MedecinProjection projection) {
        MedecinProjection saved = medecinRepository.save(projection);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<PatientProjection> getPatientProjection(@PathVariable String id) {
        return patientRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/medecin/{id}")
    public ResponseEntity<MedecinProjection> getMedecinProjection(@PathVariable String id) {
        return medecinRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
