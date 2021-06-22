package com.meetingplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meetingplanner.domain.enumeration.TypeEquipement;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A EquipementLibre.
 */
@Entity
@Table(name = "equipement_libre")
public class EquipementLibre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeEquipement type;

    @Column(name = "reserve")
    private Boolean reserve;

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipementLibres", "salle" }, allowSetters = true)
    private Reunion reunion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EquipementLibre id(Long id) {
        this.id = id;
        return this;
    }

    public TypeEquipement getType() {
        return this.type;
    }

    public EquipementLibre type(TypeEquipement type) {
        this.type = type;
        return this;
    }

    public void setType(TypeEquipement type) {
        this.type = type;
    }

    public Boolean getReserve() {
        return this.reserve;
    }

    public EquipementLibre reserve(Boolean reserve) {
        this.reserve = reserve;
        return this;
    }

    public void setReserve(Boolean reserve) {
        this.reserve = reserve;
    }

    public Reunion getReunion() {
        return this.reunion;
    }

    public EquipementLibre reunion(Reunion reunion) {
        this.setReunion(reunion);
        return this;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipementLibre)) {
            return false;
        }
        return id != null && id.equals(((EquipementLibre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementLibre{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", reserve='" + getReserve() + "'" +
            "}";
    }
}
