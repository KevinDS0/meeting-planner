package com.meetingplanner.service.mapper;

import com.meetingplanner.domain.*;
import com.meetingplanner.service.dto.ReunionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reunion} and its DTO {@link ReunionDTO}.
 */
@Mapper(componentModel = "spring", uses = { SalleMapper.class })
public interface ReunionMapper extends EntityMapper<ReunionDTO, Reunion> {
    @Mapping(target = "salle", source = "salle", qualifiedByName = "id")
    ReunionDTO toDto(Reunion s);
}
