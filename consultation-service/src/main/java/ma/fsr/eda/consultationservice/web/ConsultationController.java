package ma.fsr.eda.consultationservice.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.consultationservice.model.Consultation;
import ma.fsr.eda.consultationservice.service.ConsultationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/v1/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    
    private final ConsultationService consultationService;

    @GetMapping
    public ResponseEntity<List<Consultation>> getAllConsultations() {
        return ResponseEntity.ok(consultationService.getAllConsultations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable String id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }
}