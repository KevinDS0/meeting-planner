package com.meetingplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meetingplanner.domain.enumeration.TypeEquipement;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A EquipementSalle.
 */
@Entity
@Table(name = "equipement_salle")
public class EquipementSalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeEquipement type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipementSalles", "reunions" }, allowSetters = true)
    private Salle salle;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EquipementSalle id(Long id) {
        this.id = id;
        return this;
    }

    public TypeEquipement getType() {
        return this.type;
    }

    public EquipementSalle type(TypeEquipement type) {
        this.type = type;
        return this;
    }

    public void setType(TypeEquipement type) {
        this.type = type;
    }

    public Salle getSalle() {
        return this.salle;
    }

    public EquipementSalle salle(Salle salle) {
        this.setSalle(salle);
        return this;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipementSalle)) {
            return false;
        }
        return id != null && id.equals(((EquipementSalle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementSalle{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
