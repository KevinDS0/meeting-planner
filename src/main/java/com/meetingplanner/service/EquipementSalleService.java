package com.meetingplanner.service;

import com.meetingplanner.domain.EquipementSalle;
import com.meetingplanner.repository.EquipementSalleRepository;
import com.meetingplanner.service.dto.EquipementSalleDTO;
import com.meetingplanner.service.mapper.EquipementSalleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EquipementSalle}.
 */
@Service
@Transactional
public class EquipementSalleService {

    private final Logger log = LoggerFactory.getLogger(EquipementSalleService.class);

    private final EquipementSalleRepository equipementSalleRepository;

    private final EquipementSalleMapper equipementSalleMapper;

    public EquipementSalleService(EquipementSalleRepository equipementSalleRepository, EquipementSalleMapper equipementSalleMapper) {
        this.equipementSalleRepository = equipementSalleRepository;
        this.equipementSalleMapper = equipementSalleMapper;
    }

    /**
     * Save a equipementSalle.
     *
     * @param equipementSalleDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipementSalleDTO save(EquipementSalleDTO equipementSalleDTO) {
        log.debug("Request to save EquipementSalle : {}", equipementSalleDTO);
        EquipementSalle equipementSalle = equipementSalleMapper.toEntity(equipementSalleDTO);
        equipementSalle = equipementSalleRepository.save(equipementSalle);
        return equipementSalleMapper.toDto(equipementSalle);
    }

    /**
     * Partially update a equipementSalle.
     *
     * @param equipementSalleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EquipementSalleDTO> partialUpdate(EquipementSalleDTO equipementSalleDTO) {
        log.debug("Request to partially update EquipementSalle : {}", equipementSalleDTO);

        return equipementSalleRepository
            .findById(equipementSalleDTO.getId())
            .map(
                existingEquipementSalle -> {
                    equipementSalleMapper.partialUpdate(existingEquipementSalle, equipementSalleDTO);

                    return existingEquipementSalle;
                }
            )
            .map(equipementSalleRepository::save)
            .map(equipementSalleMapper::toDto);
    }

    /**
     * Get all the equipementSalles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EquipementSalleDTO> findAll() {
        log.debug("Request to get all EquipementSalles");
        return equipementSalleRepository
            .findAll()
            .stream()
            .map(equipementSalleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one equipementSalle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipementSalleDTO> findOne(Long id) {
        log.debug("Request to get EquipementSalle : {}", id);
        return equipementSalleRepository.findById(id).map(equipementSalleMapper::toDto);
    }

    /**
     * Delete the equipementSalle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipementSalle : {}", id);
        equipementSalleRepository.deleteById(id);
    }
}
