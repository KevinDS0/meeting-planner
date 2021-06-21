package com.meetingplanner.service.dto;

import com.meetingplanner.domain.enumeration.TypeEquipement;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.meetingplanner.domain.EquipementLibre} entity.
 */
public class EquipementLibreDTO implements Serializable {

    private Long id;

    private TypeEquipement type;

    private Boolean reserve;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeEquipement getType() {
        return type;
    }

    public void setType(TypeEquipement type) {
        this.type = type;
    }

    public Boolean getReserve() {
        return reserve;
    }

    public void setReserve(Boolean reserve) {
        this.reserve = reserve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipementLibreDTO)) {
            return false;
        }

        EquipementLibreDTO equipementLibreDTO = (EquipementLibreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipementLibreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementLibreDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", reserve='" + getReserve() + "'" +
            "}";
    }
}
