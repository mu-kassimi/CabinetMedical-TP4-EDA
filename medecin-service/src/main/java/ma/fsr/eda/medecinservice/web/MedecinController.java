package ma.fsr.eda.medecinservice.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.medecinservice.model.Medecin;
import ma.fsr.eda.medecinservice.service.MedecinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/v1/medecins")
@RequiredArgsConstructor
public class MedecinController {
    
    private final MedecinService medecinService;

    @GetMapping
    public ResponseEntity<List<Medecin>> getAllMedecins() {
        return ResponseEntity.ok(medecinService.getAllMedecins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medecin> getMedecinById(@PathVariable String id) {
        return ResponseEntity.ok(medecinService.getMedecinById(id));
    }

    @PostMapping
    public ResponseEntity<Medecin> createMedecin(@RequestBody Medecin medecin) {
        Medecin created = medecinService.createMedecin(medecin);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}