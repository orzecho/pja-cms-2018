package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.FillingGapsTestItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FillingGapsTestItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FillingGapsTestItemRepository extends JpaRepository<FillingGapsTestItem, Long>, JpaSpecificationExecutor<FillingGapsTestItem> {

}
