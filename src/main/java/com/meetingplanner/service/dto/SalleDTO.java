package com.meetingplanner.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.meetingplanner.domain.Salle} entity.
 */
public class SalleDTO implements Serializable {

    private Long id;

    private String nom;

    private Integer capacite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalleDTO)) {
            return false;
        }

        SalleDTO salleDTO = (SalleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalleDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", capacite=" + getCapacite() +
            "}";
    }
}
