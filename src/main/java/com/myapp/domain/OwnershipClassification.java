package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OwnershipClassification.
 */
@Entity
@Table(name = "ownership_classification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OwnershipClassification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "classifications")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ownershipProperties", "meters", "classifications", "address", "owner" }, allowSetters = true)
    private Set<Ownership> ownerships = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OwnershipClassification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public OwnershipClassification name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Ownership> getOwnerships() {
        return this.ownerships;
    }

    public void setOwnerships(Set<Ownership> ownerships) {
        if (this.ownerships != null) {
            this.ownerships.forEach(i -> i.removeClassifications(this));
        }
        if (ownerships != null) {
            ownerships.forEach(i -> i.addClassifications(this));
        }
        this.ownerships = ownerships;
    }

    public OwnershipClassification ownerships(Set<Ownership> ownerships) {
        this.setOwnerships(ownerships);
        return this;
    }

    public OwnershipClassification addOwnerships(Ownership ownership) {
        this.ownerships.add(ownership);
        ownership.getClassifications().add(this);
        return this;
    }

    public OwnershipClassification removeOwnerships(Ownership ownership) {
        this.ownerships.remove(ownership);
        ownership.getClassifications().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OwnershipClassification)) {
            return false;
        }
        return id != null && id.equals(((OwnershipClassification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnershipClassification{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
