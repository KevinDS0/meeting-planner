package com.meetingplanner.service.mapper;

import com.meetingplanner.domain.*;
import com.meetingplanner.service.dto.EquipementSalleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipementSalle} and its DTO {@link EquipementSalleDTO}.
 */
@Mapper(componentModel = "spring", uses = { SalleMapper.class })
public interface EquipementSalleMapper extends EntityMapper<EquipementSalleDTO, EquipementSalle> {
    @Mapping(target = "salle", source = "salle", qualifiedByName = "id")
    EquipementSalleDTO toDto(EquipementSalle s);
}
