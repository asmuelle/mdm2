package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ownership.
 */
@Entity
@Table(name = "ownership")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ownership implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "client_ref")
    private String clientRef;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ownership")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "ownership" }, allowSetters = true)
    private Set<OwnershipProperty> ownershipProperties = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_ownership__meters",
        joinColumns = @JoinColumn(name = "ownership_id"),
        inverseJoinColumns = @JoinColumn(name = "meters_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "parent", "alternative", "peer", "provider", "namespace", "ownerships", "meter", "meter" },
        allowSetters = true
    )
    private Set<Meter> meters = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_ownership__classifications",
        joinColumns = @JoinColumn(name = "ownership_id"),
        inverseJoinColumns = @JoinColumn(name = "classifications_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ownerships" }, allowSetters = true)
    private Set<OwnershipClassification> classifications = new HashSet<>();

    @JsonIgnoreProperties(value = { "ownership", "country" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "ownership")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "peers", "ownerships" }, allowSetters = true)
    private Owner owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ownership id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Ownership name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientRef() {
        return this.clientRef;
    }

    public Ownership clientRef(String clientRef) {
        this.setClientRef(clientRef);
        return this;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Ownership startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Ownership endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<OwnershipProperty> getOwnershipProperties() {
        return this.ownershipProperties;
    }

    public void setOwnershipProperties(Set<OwnershipProperty> ownershipProperties) {
        if (this.ownershipProperties != null) {
            this.ownershipProperties.forEach(i -> i.setOwnership(null));
        }
        if (ownershipProperties != null) {
            ownershipProperties.forEach(i -> i.setOwnership(this));
        }
        this.ownershipProperties = ownershipProperties;
    }

    public Ownership ownershipProperties(Set<OwnershipProperty> ownershipProperties) {
        this.setOwnershipProperties(ownershipProperties);
        return this;
    }

    public Ownership addOwnershipProperty(OwnershipProperty ownershipProperty) {
        this.ownershipProperties.add(ownershipProperty);
        ownershipProperty.setOwnership(this);
        return this;
    }

    public Ownership removeOwnershipProperty(OwnershipProperty ownershipProperty) {
        this.ownershipProperties.remove(ownershipProperty);
        ownershipProperty.setOwnership(null);
        return this;
    }

    public Set<Meter> getMeters() {
        return this.meters;
    }

    public void setMeters(Set<Meter> meters) {
        this.meters = meters;
    }

    public Ownership meters(Set<Meter> meters) {
        this.setMeters(meters);
        return this;
    }

    public Ownership addMeters(Meter meter) {
        this.meters.add(meter);
        meter.getOwnerships().add(this);
        return this;
    }

    public Ownership removeMeters(Meter meter) {
        this.meters.remove(meter);
        meter.getOwnerships().remove(this);
        return this;
    }

    public Set<OwnershipClassification> getClassifications() {
        return this.classifications;
    }

    public void setClassifications(Set<OwnershipClassification> ownershipClassifications) {
        this.classifications = ownershipClassifications;
    }

    public Ownership classifications(Set<OwnershipClassification> ownershipClassifications) {
        this.setClassifications(ownershipClassifications);
        return this;
    }

    public Ownership addClassifications(OwnershipClassification ownershipClassification) {
        this.classifications.add(ownershipClassification);
        ownershipClassification.getOwnerships().add(this);
        return this;
    }

    public Ownership removeClassifications(OwnershipClassification ownershipClassification) {
        this.classifications.remove(ownershipClassification);
        ownershipClassification.getOwnerships().remove(this);
        return this;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        if (this.address != null) {
            this.address.setOwnership(null);
        }
        if (address != null) {
            address.setOwnership(this);
        }
        this.address = address;
    }

    public Ownership address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Ownership owner(Owner owner) {
        this.setOwner(owner);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ownership)) {
            return false;
        }
        return id != null && id.equals(((Ownership) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ownership{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", clientRef='" + getClientRef() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
