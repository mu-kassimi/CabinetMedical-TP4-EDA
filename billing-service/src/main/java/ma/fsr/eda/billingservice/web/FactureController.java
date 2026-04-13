package ma.fsr.eda.billingservice.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.eda.billingservice.model.Facture;
import ma.fsr.eda.billingservice.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/v1/factures")
@RequiredArgsConstructor
public class FactureController {
    
    private final BillingService billingService;

    @GetMapping
    public ResponseEntity<List<Facture>> getAllFactures() {
        return ResponseEntity.ok(billingService.getAllFactures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable String id) {
        return ResponseEntity.ok(billingService.getFactureById(id));
    }
}