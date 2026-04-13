package ma.fsr.eda.rendezvousservice.event.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousCreatedEvent {
    private String eventId;
    private String rendezVousId;
    private String patientId;
    private String medecinId;
    private LocalDateTime dateRendezVous;
    private String status;
}