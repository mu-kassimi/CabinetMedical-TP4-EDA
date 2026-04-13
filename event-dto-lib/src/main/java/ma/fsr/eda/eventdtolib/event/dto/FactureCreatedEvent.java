package ma.fsr.eda.eventdtolib.event.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureCreatedEvent {
    private String eventId;
    private String factureId;
    private String consultationId;
    private String patientId;
    private Double montant;
    private String statut;
    private LocalDateTime dateCreation;
}
