package ma.fsr.eda.rendezvousservice.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.rendezvousservice.model.RendezVous;
import ma.fsr.eda.rendezvousservice.service.RendezVousService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/v1/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {
    
    private final RendezVousService rendezVousService;

    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getRendezVousById(@PathVariable String id) {
        return ResponseEntity.ok(rendezVousService.getRendezVousById(id));
    }

    @PostMapping
    public ResponseEntity<RendezVous> createRendezVous(@RequestBody RendezVous rendezVous) {
        RendezVous created = rendezVousService.createRendezVous(rendezVous);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}