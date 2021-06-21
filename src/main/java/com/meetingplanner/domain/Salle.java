package com.meetingplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Salle.
 */
@Entity
@Table(name = "salle")
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "capacite")
    private Integer capacite;

    @OneToMany(mappedBy = "salle")
    @JsonIgnoreProperties(value = { "salle" }, allowSetters = true)
    private Set<EquipementSalle> equipementSalles = new HashSet<>();

    @OneToMany(mappedBy = "salle")
    @JsonIgnoreProperties(value = { "salle" }, allowSetters = true)
    private Set<Reunion> reunions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salle id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Salle nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCapacite() {
        return this.capacite;
    }

    public Salle capacite(Integer capacite) {
        this.capacite = capacite;
        return this;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Set<EquipementSalle> getEquipementSalles() {
        return this.equipementSalles;
    }

    public Salle equipementSalles(Set<EquipementSalle> equipementSalles) {
        this.setEquipementSalles(equipementSalles);
        return this;
    }

    public Salle addEquipementSalle(EquipementSalle equipementSalle) {
        this.equipementSalles.add(equipementSalle);
        equipementSalle.setSalle(this);
        return this;
    }

    public Salle removeEquipementSalle(EquipementSalle equipementSalle) {
        this.equipementSalles.remove(equipementSalle);
        equipementSalle.setSalle(null);
        return this;
    }

    public void setEquipementSalles(Set<EquipementSalle> equipementSalles) {
        if (this.equipementSalles != null) {
            this.equipementSalles.forEach(i -> i.setSalle(null));
        }
        if (equipementSalles != null) {
            equipementSalles.forEach(i -> i.setSalle(this));
        }
        this.equipementSalles = equipementSalles;
    }

    public Set<Reunion> getReunions() {
        return this.reunions;
    }

    public Salle reunions(Set<Reunion> reunions) {
        this.setReunions(reunions);
        return this;
    }

    public Salle addReunion(Reunion reunion) {
        this.reunions.add(reunion);
        reunion.setSalle(this);
        return this;
    }

    public Salle removeReunion(Reunion reunion) {
        this.reunions.remove(reunion);
        reunion.setSalle(null);
        return this;
    }

    public void setReunions(Set<Reunion> reunions) {
        if (this.reunions != null) {
            this.reunions.forEach(i -> i.setSalle(null));
        }
        if (reunions != null) {
            reunions.forEach(i -> i.setSalle(this));
        }
        this.reunions = reunions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salle)) {
            return false;
        }
        return id != null && id.equals(((Salle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salle{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", capacite=" + getCapacite() +
            "}";
    }
}
