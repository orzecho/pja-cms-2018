package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.GapItem;
import pl.edu.pja.nyan.repository.GapItemRepository;
import pl.edu.pja.nyan.service.dto.GapItemDTO;
import pl.edu.pja.nyan.service.mapper.GapItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing GapItem.
 */
@Service
@Transactional
public class GapItemService {

    private final Logger log = LoggerFactory.getLogger(GapItemService.class);

    private final GapItemRepository gapItemRepository;

    private final GapItemMapper gapItemMapper;

    public GapItemService(GapItemRepository gapItemRepository, GapItemMapper gapItemMapper) {
        this.gapItemRepository = gapItemRepository;
        this.gapItemMapper = gapItemMapper;
    }

    /**
     * Save a gapItem.
     *
     * @param gapItemDTO the entity to save
     * @return the persisted entity
     */
    public GapItemDTO save(GapItemDTO gapItemDTO) {
        log.debug("Request to save GapItem : {}", gapItemDTO);
        GapItem gapItem = gapItemMapper.toEntity(gapItemDTO);
        gapItem = gapItemRepository.save(gapItem);
        return gapItemMapper.toDto(gapItem);
    }

    /**
     * Get all the gapItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GapItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GapItems");
        return gapItemRepository.findAll(pageable)
            .map(gapItemMapper::toDto);
    }


    /**
     * Get one gapItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<GapItemDTO> findOne(Long id) {
        log.debug("Request to get GapItem : {}", id);
        return gapItemRepository.findById(id)
            .map(gapItemMapper::toDto);
    }

    /**
     * Delete the gapItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GapItem : {}", id);
        gapItemRepository.deleteById(id);
    }
}
