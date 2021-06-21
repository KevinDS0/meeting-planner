package com.meetingplanner.service;

import com.meetingplanner.domain.EquipementLibre;
import com.meetingplanner.repository.EquipementLibreRepository;
import com.meetingplanner.service.dto.EquipementLibreDTO;
import com.meetingplanner.service.mapper.EquipementLibreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EquipementLibre}.
 */
@Service
@Transactional
public class EquipementLibreService {

    private final Logger log = LoggerFactory.getLogger(EquipementLibreService.class);

    private final EquipementLibreRepository equipementLibreRepository;

    private final EquipementLibreMapper equipementLibreMapper;

    public EquipementLibreService(EquipementLibreRepository equipementLibreRepository, EquipementLibreMapper equipementLibreMapper) {
        this.equipementLibreRepository = equipementLibreRepository;
        this.equipementLibreMapper = equipementLibreMapper;
    }

    /**
     * Save a equipementLibre.
     *
     * @param equipementLibreDTO the entity to save.
     * @return the persisted entity.
     */
    public EquipementLibreDTO save(EquipementLibreDTO equipementLibreDTO) {
        log.debug("Request to save EquipementLibre : {}", equipementLibreDTO);
        EquipementLibre equipementLibre = equipementLibreMapper.toEntity(equipementLibreDTO);
        equipementLibre = equipementLibreRepository.save(equipementLibre);
        return equipementLibreMapper.toDto(equipementLibre);
    }

    /**
     * Partially update a equipementLibre.
     *
     * @param equipementLibreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EquipementLibreDTO> partialUpdate(EquipementLibreDTO equipementLibreDTO) {
        log.debug("Request to partially update EquipementLibre : {}", equipementLibreDTO);

        return equipementLibreRepository
            .findById(equipementLibreDTO.getId())
            .map(
                existingEquipementLibre -> {
                    equipementLibreMapper.partialUpdate(existingEquipementLibre, equipementLibreDTO);

                    return existingEquipementLibre;
                }
            )
            .map(equipementLibreRepository::save)
            .map(equipementLibreMapper::toDto);
    }

    /**
     * Get all the equipementLibres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EquipementLibreDTO> findAll() {
        log.debug("Request to get all EquipementLibres");
        return equipementLibreRepository
            .findAll()
            .stream()
            .map(equipementLibreMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one equipementLibre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EquipementLibreDTO> findOne(Long id) {
        log.debug("Request to get EquipementLibre : {}", id);
        return equipementLibreRepository.findById(id).map(equipementLibreMapper::toDto);
    }

    /**
     * Delete the equipementLibre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EquipementLibre : {}", id);
        equipementLibreRepository.deleteById(id);
    }
}
