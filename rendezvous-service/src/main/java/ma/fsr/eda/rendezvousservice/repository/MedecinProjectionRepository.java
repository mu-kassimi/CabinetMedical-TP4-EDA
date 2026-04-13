package ma.fsr.eda.rendezvousservice.repository;

import ma.fsr.eda.rendezvousservice.model.projection.MedecinProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedecinProjectionRepository extends JpaRepository<MedecinProjection, String> {
}