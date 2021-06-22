package com.meetingplanner.service.mapper;

import com.meetingplanner.domain.*;
import com.meetingplanner.service.dto.EquipementLibreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipementLibre} and its DTO {@link EquipementLibreDTO}.
 */
@Mapper(componentModel = "spring", uses = { ReunionMapper.class })
public interface EquipementLibreMapper extends EntityMapper<EquipementLibreDTO, EquipementLibre> {
    @Mapping(target = "reunion", source = "reunion", qualifiedByName = "id")
    EquipementLibreDTO toDto(EquipementLibre s);
}
