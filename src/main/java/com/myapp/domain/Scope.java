package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Scope.
 */
@Entity
@Table(name = "scope")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Scope implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "meter_description")
    private String meterDescription;

    @Column(name = "meter_name")
    private String meterName;

    @Column(name = "meter_utility")
    private String meterUtility;

    @Column(name = "price_per_month")
    private Integer pricePerMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "scopes", "customer" }, allowSetters = true)
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "scopes" }, allowSetters = true)
    private Service service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Scope id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterDescription() {
        return this.meterDescription;
    }

    public Scope meterDescription(String meterDescription) {
        this.setMeterDescription(meterDescription);
        return this;
    }

    public void setMeterDescription(String meterDescription) {
        this.meterDescription = meterDescription;
    }

    public String getMeterName() {
        return this.meterName;
    }

    public Scope meterName(String meterName) {
        this.setMeterName(meterName);
        return this;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getMeterUtility() {
        return this.meterUtility;
    }

    public Scope meterUtility(String meterUtility) {
        this.setMeterUtility(meterUtility);
        return this;
    }

    public void setMeterUtility(String meterUtility) {
        this.meterUtility = meterUtility;
    }

    public Integer getPricePerMonth() {
        return this.pricePerMonth;
    }

    public Scope pricePerMonth(Integer pricePerMonth) {
        this.setPricePerMonth(pricePerMonth);
        return this;
    }

    public void setPricePerMonth(Integer pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Scope contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public Service getService() {
        return this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Scope service(Service service) {
        this.setService(service);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scope)) {
            return false;
        }
        return id != null && id.equals(((Scope) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Scope{" +
            "id=" + getId() +
            ", meterDescription='" + getMeterDescription() + "'" +
            ", meterName='" + getMeterName() + "'" +
            ", meterUtility='" + getMeterUtility() + "'" +
            ", pricePerMonth=" + getPricePerMonth() +
            "}";
    }
}
