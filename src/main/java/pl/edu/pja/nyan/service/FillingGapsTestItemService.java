package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.FillingGapsTestItem;
import pl.edu.pja.nyan.repository.FillingGapsTestItemRepository;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;
import pl.edu.pja.nyan.service.mapper.FillingGapsTestItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing FillingGapsTestItem.
 */
@Service
@Transactional
public class FillingGapsTestItemService {

    private final Logger log = LoggerFactory.getLogger(FillingGapsTestItemService.class);

    private final FillingGapsTestItemRepository fillingGapsTestItemRepository;

    private final FillingGapsTestItemMapper fillingGapsTestItemMapper;

    public FillingGapsTestItemService(FillingGapsTestItemRepository fillingGapsTestItemRepository, FillingGapsTestItemMapper fillingGapsTestItemMapper) {
        this.fillingGapsTestItemRepository = fillingGapsTestItemRepository;
        this.fillingGapsTestItemMapper = fillingGapsTestItemMapper;
    }

    /**
     * Save a fillingGapsTestItem.
     *
     * @param fillingGapsTestItemDTO the entity to save
     * @return the persisted entity
     */
    public FillingGapsTestItemDTO save(FillingGapsTestItemDTO fillingGapsTestItemDTO) {
        log.debug("Request to save FillingGapsTestItem : {}", fillingGapsTestItemDTO);
        FillingGapsTestItem fillingGapsTestItem = fillingGapsTestItemMapper.toEntity(fillingGapsTestItemDTO);
        fillingGapsTestItem = fillingGapsTestItemRepository.save(fillingGapsTestItem);
        return fillingGapsTestItemMapper.toDto(fillingGapsTestItem);
    }

    /**
     * Get all the fillingGapsTestItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FillingGapsTestItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FillingGapsTestItems");
        return fillingGapsTestItemRepository.findAll(pageable)
            .map(fillingGapsTestItemMapper::toDto);
    }


    /**
     * Get one fillingGapsTestItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FillingGapsTestItemDTO> findOne(Long id) {
        log.debug("Request to get FillingGapsTestItem : {}", id);
        return fillingGapsTestItemRepository.findById(id)
            .map(fillingGapsTestItemMapper::toDto);
    }

    /**
     * Delete the fillingGapsTestItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FillingGapsTestItem : {}", id);
        fillingGapsTestItemRepository.deleteById(id);
    }
}
