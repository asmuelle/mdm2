package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.LoadType;
import com.myapp.domain.enumeration.Utility;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Peer.
 */
@Entity
@Table(name = "peer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Peer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "utility")
    private Utility utility;

    @Enumerated(EnumType.STRING)
    @Column(name = "load_type")
    private LoadType loadType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "parent", "alternative", "peer", "provider", "namespace", "ownerships", "meter", "meter" },
        allowSetters = true
    )
    private Set<Meter> meters = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "peers", "ownerships" }, allowSetters = true)
    private Owner owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Peer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Peer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Utility getUtility() {
        return this.utility;
    }

    public Peer utility(Utility utility) {
        this.setUtility(utility);
        return this;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public LoadType getLoadType() {
        return this.loadType;
    }

    public Peer loadType(LoadType loadType) {
        this.setLoadType(loadType);
        return this;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public Set<Meter> getMeters() {
        return this.meters;
    }

    public void setMeters(Set<Meter> meters) {
        if (this.meters != null) {
            this.meters.forEach(i -> i.setPeer(null));
        }
        if (meters != null) {
            meters.forEach(i -> i.setPeer(this));
        }
        this.meters = meters;
    }

    public Peer meters(Set<Meter> meters) {
        this.setMeters(meters);
        return this;
    }

    public Peer addMeter(Meter meter) {
        this.meters.add(meter);
        meter.setPeer(this);
        return this;
    }

    public Peer removeMeter(Meter meter) {
        this.meters.remove(meter);
        meter.setPeer(null);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Peer owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Peer)) {
            return false;
        }
        return id != null && id.equals(((Peer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Peer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", utility='" + getUtility() + "'" +
            ", loadType='" + getLoadType() + "'" +
            "}";
    }
}
