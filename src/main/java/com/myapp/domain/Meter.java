package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.LoadType;
import com.myapp.domain.enumeration.Utility;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Meter.
 */
@Entity
@Table(name = "meter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Meter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amr_week")
    private Integer amrWeek;

    @Column(name = "amr_year")
    private Integer amrYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "utility")
    private Utility utility;

    @Enumerated(EnumType.STRING)
    @Column(name = "load_type")
    private LoadType loadType;

    @Column(name = "price")
    private Float price;

    @Column(name = "last_reading")
    private LocalDate lastReading;

    @Column(name = "contact_email")
    private String contactEmail;

    @JsonIgnoreProperties(
        value = { "parent", "alternative", "peer", "provider", "namespace", "ownerships", "meter", "meter" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Meter parent;

    @JsonIgnoreProperties(
        value = { "parent", "alternative", "peer", "provider", "namespace", "ownerships", "meter", "meter" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Meter alternative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "meters", "owner" }, allowSetters = true)
    private Peer peer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "meters" }, allowSetters = true)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "meters" }, allowSetters = true)
    private Namespace namespace;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "meters")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ownershipProperties", "meters", "classifications", "address", "owner" }, allowSetters = true)
    private Set<Ownership> ownerships = new HashSet<>();

    @JsonIgnoreProperties(
        value = { "parent", "alternative", "peer", "provider", "namespace", "ownerships", "meter", "meter" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "alternative")
    private Meter meter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Meter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Meter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmrWeek() {
        return this.amrWeek;
    }

    public Meter amrWeek(Integer amrWeek) {
        this.setAmrWeek(amrWeek);
        return this;
    }

    public void setAmrWeek(Integer amrWeek) {
        this.amrWeek = amrWeek;
    }

    public Integer getAmrYear() {
        return this.amrYear;
    }

    public Meter amrYear(Integer amrYear) {
        this.setAmrYear(amrYear);
        return this;
    }

    public void setAmrYear(Integer amrYear) {
        this.amrYear = amrYear;
    }

    public Utility getUtility() {
        return this.utility;
    }

    public Meter utility(Utility utility) {
        this.setUtility(utility);
        return this;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public LoadType getLoadType() {
        return this.loadType;
    }

    public Meter loadType(LoadType loadType) {
        this.setLoadType(loadType);
        return this;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public Float getPrice() {
        return this.price;
    }

    public Meter price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getLastReading() {
        return this.lastReading;
    }

    public Meter lastReading(LocalDate lastReading) {
        this.setLastReading(lastReading);
        return this;
    }

    public void setLastReading(LocalDate lastReading) {
        this.lastReading = lastReading;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public Meter contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Meter getParent() {
        return this.parent;
    }

    public void setParent(Meter meter) {
        this.parent = meter;
    }

    public Meter parent(Meter meter) {
        this.setParent(meter);
        return this;
    }

    public Meter getAlternative() {
        return this.alternative;
    }

    public void setAlternative(Meter meter) {
        this.alternative = meter;
    }

    public Meter alternative(Meter meter) {
        this.setAlternative(meter);
        return this;
    }

    public Peer getPeer() {
        return this.peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public Meter peer(Peer peer) {
        this.setPeer(peer);
        return this;
    }

    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Meter provider(Provider provider) {
        this.setProvider(provider);
        return this;
    }

    public Namespace getNamespace() {
        return this.namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public Meter namespace(Namespace namespace) {
        this.setNamespace(namespace);
        return this;
    }

    public Set<Ownership> getOwnerships() {
        return this.ownerships;
    }

    public void setOwnerships(Set<Ownership> ownerships) {
        if (this.ownerships != null) {
            this.ownerships.forEach(i -> i.removeMeters(this));
        }
        if (ownerships != null) {
            ownerships.forEach(i -> i.addMeters(this));
        }
        this.ownerships = ownerships;
    }

    public Meter ownerships(Set<Ownership> ownerships) {
        this.setOwnerships(ownerships);
        return this;
    }

    public Meter addOwnerships(Ownership ownership) {
        this.ownerships.add(ownership);
        ownership.getMeters().add(this);
        return this;
    }

    public Meter removeOwnerships(Ownership ownership) {
        this.ownerships.remove(ownership);
        ownership.getMeters().remove(this);
        return this;
    }

    public Meter getMeter() {
        return this.meter;
    }

    public void setMeter(Meter meter) {
        if (this.meter != null) {
            this.meter.setAlternative(null);
        }
        if (meter != null) {
            meter.setAlternative(this);
        }
        this.meter = meter;
    }

    public Meter meter(Meter meter) {
        this.setMeter(meter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Meter)) {
            return false;
        }
        return id != null && id.equals(((Meter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Meter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amrWeek=" + getAmrWeek() +
            ", amrYear=" + getAmrYear() +
            ", utility='" + getUtility() + "'" +
            ", loadType='" + getLoadType() + "'" +
            ", price=" + getPrice() +
            ", lastReading='" + getLastReading() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            "}";
    }
}
