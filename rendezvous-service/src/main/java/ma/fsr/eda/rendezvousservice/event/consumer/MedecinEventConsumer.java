package ma.fsr.eda.rendezvousservice.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.fsr.eda.eventdtolib.event.dto.MedecinCreatedEvent;
import ma.fsr.eda.rendezvousservice.model.projection.MedecinProjection;
import ma.fsr.eda.rendezvousservice.repository.MedecinProjectionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MedecinEventConsumer {

    private final MedecinProjectionRepository repository;

    @KafkaListener(topics = "medecin.created", groupId = "rendezvous-group")
    public void consume(MedecinCreatedEvent event) {
        log.info("📥 Received event: medecin.created for medecinId: {}", event.getMedecinId());

        MedecinProjection projection = new MedecinProjection();
        projection.setMedecinId(event.getMedecinId());
        projection.setNom(event.getNom());
        projection.setSpecialite(event.getSpecialite());

        repository.save(projection);
        log.info("✅ Medecin projection saved: {}", event.getMedecinId());
    }
}