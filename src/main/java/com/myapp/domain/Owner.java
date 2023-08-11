package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.Stage;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Owner.
 */
@Entity
@Table(name = "owner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Owner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "owner_key")
    private String ownerKey;

    @Column(name = "owner_group")
    private String ownerGroup;

    @Column(name = "meters")
    private Integer meters;

    @Column(name = "last_week")
    private Integer lastWeek;

    @Column(name = "before_last_week")
    private Integer beforeLastWeek;

    @Column(name = "amr")
    private Integer amr;

    @Column(name = "last_year")
    private Integer lastYear;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "electricity_price")
    private Float electricityPrice;

    @Column(name = "gas_price")
    private Float gasPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "gas_stage")
    private Stage gasStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "electricity_stage")
    private Stage electricityStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "water_stage")
    private Stage waterStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "heat_stage")
    private Stage heatStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "solar_heat")
    private Stage solarHeat;

    @Enumerated(EnumType.STRING)
    @Column(name = "solar_power_stage")
    private Stage solarPowerStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "wind_stage")
    private Stage windStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "cogen_power_stage")
    private Stage cogenPowerStage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "meters", "owner" }, allowSetters = true)
    private Set<Peer> peers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ownershipProperties", "meters", "classifications", "address", "owner" }, allowSetters = true)
    private Set<Ownership> ownerships = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Owner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Owner name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Owner fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOwnerKey() {
        return this.ownerKey;
    }

    public Owner ownerKey(String ownerKey) {
        this.setOwnerKey(ownerKey);
        return this;
    }

    public void setOwnerKey(String ownerKey) {
        this.ownerKey = ownerKey;
    }

    public String getOwnerGroup() {
        return this.ownerGroup;
    }

    public Owner ownerGroup(String ownerGroup) {
        this.setOwnerGroup(ownerGroup);
        return this;
    }

    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    public Integer getMeters() {
        return this.meters;
    }

    public Owner meters(Integer meters) {
        this.setMeters(meters);
        return this;
    }

    public void setMeters(Integer meters) {
        this.meters = meters;
    }

    public Integer getLastWeek() {
        return this.lastWeek;
    }

    public Owner lastWeek(Integer lastWeek) {
        this.setLastWeek(lastWeek);
        return this;
    }

    public void setLastWeek(Integer lastWeek) {
        this.lastWeek = lastWeek;
    }

    public Integer getBeforeLastWeek() {
        return this.beforeLastWeek;
    }

    public Owner beforeLastWeek(Integer beforeLastWeek) {
        this.setBeforeLastWeek(beforeLastWeek);
        return this;
    }

    public void setBeforeLastWeek(Integer beforeLastWeek) {
        this.beforeLastWeek = beforeLastWeek;
    }

    public Integer getAmr() {
        return this.amr;
    }

    public Owner amr(Integer amr) {
        this.setAmr(amr);
        return this;
    }

    public void setAmr(Integer amr) {
        this.amr = amr;
    }

    public Integer getLastYear() {
        return this.lastYear;
    }

    public Owner lastYear(Integer lastYear) {
        this.setLastYear(lastYear);
        return this;
    }

    public void setLastYear(Integer lastYear) {
        this.lastYear = lastYear;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public Owner contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Float getElectricityPrice() {
        return this.electricityPrice;
    }

    public Owner electricityPrice(Float electricityPrice) {
        this.setElectricityPrice(electricityPrice);
        return this;
    }

    public void setElectricityPrice(Float electricityPrice) {
        this.electricityPrice = electricityPrice;
    }

    public Float getGasPrice() {
        return this.gasPrice;
    }

    public Owner gasPrice(Float gasPrice) {
        this.setGasPrice(gasPrice);
        return this;
    }

    public void setGasPrice(Float gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Stage getGasStage() {
        return this.gasStage;
    }

    public Owner gasStage(Stage gasStage) {
        this.setGasStage(gasStage);
        return this;
    }

    public void setGasStage(Stage gasStage) {
        this.gasStage = gasStage;
    }

    public Stage getElectricityStage() {
        return this.electricityStage;
    }

    public Owner electricityStage(Stage electricityStage) {
        this.setElectricityStage(electricityStage);
        return this;
    }

    public void setElectricityStage(Stage electricityStage) {
        this.electricityStage = electricityStage;
    }

    public Stage getWaterStage() {
        return this.waterStage;
    }

    public Owner waterStage(Stage waterStage) {
        this.setWaterStage(waterStage);
        return this;
    }

    public void setWaterStage(Stage waterStage) {
        this.waterStage = waterStage;
    }

    public Stage getHeatStage() {
        return this.heatStage;
    }

    public Owner heatStage(Stage heatStage) {
        this.setHeatStage(heatStage);
        return this;
    }

    public void setHeatStage(Stage heatStage) {
        this.heatStage = heatStage;
    }

    public Stage getSolarHeat() {
        return this.solarHeat;
    }

    public Owner solarHeat(Stage solarHeat) {
        this.setSolarHeat(solarHeat);
        return this;
    }

    public void setSolarHeat(Stage solarHeat) {
        this.solarHeat = solarHeat;
    }

    public Stage getSolarPowerStage() {
        return this.solarPowerStage;
    }

    public Owner solarPowerStage(Stage solarPowerStage) {
        this.setSolarPowerStage(solarPowerStage);
        return this;
    }

    public void setSolarPowerStage(Stage solarPowerStage) {
        this.solarPowerStage = solarPowerStage;
    }

    public Stage getWindStage() {
        return this.windStage;
    }

    public Owner windStage(Stage windStage) {
        this.setWindStage(windStage);
        return this;
    }

    public void setWindStage(Stage windStage) {
        this.windStage = windStage;
    }

    public Stage getCogenPowerStage() {
        return this.cogenPowerStage;
    }

    public Owner cogenPowerStage(Stage cogenPowerStage) {
        this.setCogenPowerStage(cogenPowerStage);
        return this;
    }

    public void setCogenPowerStage(Stage cogenPowerStage) {
        this.cogenPowerStage = cogenPowerStage;
    }

    public Set<Peer> getPeers() {
        return this.peers;
    }

    public void setPeers(Set<Peer> peers) {
        if (this.peers != null) {
            this.peers.forEach(i -> i.setOwner(null));
        }
        if (peers != null) {
            peers.forEach(i -> i.setOwner(this));
        }
        this.peers = peers;
    }

    public Owner peers(Set<Peer> peers) {
        this.setPeers(peers);
        return this;
    }

    public Owner addPeer(Peer peer) {
        this.peers.add(peer);
        peer.setOwner(this);
        return this;
    }

    public Owner removePeer(Peer peer) {
        this.peers.remove(peer);
        peer.setOwner(null);
        return this;
    }

    public Set<Ownership> getOwnerships() {
        return this.ownerships;
    }

    public void setOwnerships(Set<Ownership> ownerships) {
        if (this.ownerships != null) {
            this.ownerships.forEach(i -> i.setOwner(null));
        }
        if (ownerships != null) {
            ownerships.forEach(i -> i.setOwner(this));
        }
        this.ownerships = ownerships;
    }

    public Owner ownerships(Set<Ownership> ownerships) {
        this.setOwnerships(ownerships);
        return this;
    }

    public Owner addOwnership(Ownership ownership) {
        this.ownerships.add(ownership);
        ownership.setOwner(this);
        return this;
    }

    public Owner removeOwnership(Ownership ownership) {
        this.ownerships.remove(ownership);
        ownership.setOwner(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Owner)) {
            return false;
        }
        return id != null && id.equals(((Owner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Owner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", ownerKey='" + getOwnerKey() + "'" +
            ", ownerGroup='" + getOwnerGroup() + "'" +
            ", meters=" + getMeters() +
            ", lastWeek=" + getLastWeek() +
            ", beforeLastWeek=" + getBeforeLastWeek() +
            ", amr=" + getAmr() +
            ", lastYear=" + getLastYear() +
            ", contactEmail='" + getContactEmail() + "'" +
            ", electricityPrice=" + getElectricityPrice() +
            ", gasPrice=" + getGasPrice() +
            ", gasStage='" + getGasStage() + "'" +
            ", electricityStage='" + getElectricityStage() + "'" +
            ", waterStage='" + getWaterStage() + "'" +
            ", heatStage='" + getHeatStage() + "'" +
            ", solarHeat='" + getSolarHeat() + "'" +
            ", solarPowerStage='" + getSolarPowerStage() + "'" +
            ", windStage='" + getWindStage() + "'" +
            ", cogenPowerStage='" + getCogenPowerStage() + "'" +
            "}";
    }
}
