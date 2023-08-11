package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Namespace.
 */
@Entity
@Table(name = "namespace")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Namespace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "namespace")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "parent", "alternative", "peer", "provider", "namespace", "ownerships", "meter", "meter" },
        allowSetters = true
    )
    private Set<Meter> meters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Namespace id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Namespace name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Meter> getMeters() {
        return this.meters;
    }

    public void setMeters(Set<Meter> meters) {
        if (this.meters != null) {
            this.meters.forEach(i -> i.setNamespace(null));
        }
        if (meters != null) {
            meters.forEach(i -> i.setNamespace(this));
        }
        this.meters = meters;
    }

    public Namespace meters(Set<Meter> meters) {
        this.setMeters(meters);
        return this;
    }

    public Namespace addMeter(Meter meter) {
        this.meters.add(meter);
        meter.setNamespace(this);
        return this;
    }

    public Namespace removeMeter(Meter meter) {
        this.meters.remove(meter);
        meter.setNamespace(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Namespace)) {
            return false;
        }
        return id != null && id.equals(((Namespace) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Namespace{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
