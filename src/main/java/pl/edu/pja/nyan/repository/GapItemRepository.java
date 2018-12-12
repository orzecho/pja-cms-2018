package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.GapItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GapItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GapItemRepository extends JpaRepository<GapItem, Long>, JpaSpecificationExecutor<GapItem> {

}
