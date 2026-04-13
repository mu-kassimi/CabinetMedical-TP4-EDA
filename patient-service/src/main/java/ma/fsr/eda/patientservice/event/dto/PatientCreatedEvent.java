package ma.fsr.eda.patientservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreatedEvent {
    private String eventId;
    private String patientId;
    private String nom;
    private String email;
    private LocalDateTime dateCreation;
}