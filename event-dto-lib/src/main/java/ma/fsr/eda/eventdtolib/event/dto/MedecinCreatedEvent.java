package ma.fsr.eda.eventdtolib.event.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedecinCreatedEvent {
    private String eventId;
    private String medecinId;
    private String nom;
    private String specialite;
    private String email;
    private LocalDateTime dateCreation;
}
