package com.meetingplanner.service;

import com.meetingplanner.domain.Reunion;
import com.meetingplanner.repository.ReunionRepository;
import com.meetingplanner.service.dto.ReunionDTO;
import com.meetingplanner.service.mapper.ReunionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reunion}.
 */
@Service
@Transactional
public class ReunionService {

    private final Logger log = LoggerFactory.getLogger(ReunionService.class);

    private final ReunionRepository reunionRepository;

    private final ReunionMapper reunionMapper;

    public ReunionService(ReunionRepository reunionRepository, ReunionMapper reunionMapper) {
        this.reunionRepository = reunionRepository;
        this.reunionMapper = reunionMapper;
    }

    /**
     * Save a reunion.
     *
     * @param reunionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReunionDTO save(ReunionDTO reunionDTO) {
        log.debug("Request to save Reunion : {}", reunionDTO);
        Reunion reunion = reunionMapper.toEntity(reunionDTO);
        reunion = reunionRepository.save(reunion);
        return reunionMapper.toDto(reunion);
    }

    /**
     * Partially update a reunion.
     *
     * @param reunionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReunionDTO> partialUpdate(ReunionDTO reunionDTO) {
        log.debug("Request to partially update Reunion : {}", reunionDTO);

        return reunionRepository
            .findById(reunionDTO.getId())
            .map(
                existingReunion -> {
                    reunionMapper.partialUpdate(existingReunion, reunionDTO);

                    return existingReunion;
                }
            )
            .map(reunionRepository::save)
            .map(reunionMapper::toDto);
    }

    /**
     * Get all the reunions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReunionDTO> findAll() {
        log.debug("Request to get all Reunions");
        return reunionRepository.findAll().stream().map(reunionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reunion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReunionDTO> findOne(Long id) {
        log.debug("Request to get Reunion : {}", id);
        return reunionRepository.findById(id).map(reunionMapper::toDto);
    }

    /**
     * Delete the reunion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reunion : {}", id);
        reunionRepository.deleteById(id);
    }
}
