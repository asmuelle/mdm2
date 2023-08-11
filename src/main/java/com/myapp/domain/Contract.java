package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "customer_contact_name")
    private String customerContactName;

    @Column(name = "customer_contact_addresslines")
    private String customerContactAddresslines;

    @Column(name = "customer_purchase_number")
    private String customerPurchaseNumber;

    @Column(name = "kwiqly_order_number")
    private String kwiqlyOrderNumber;

    @Column(name = "base_price_per_month")
    private Integer basePricePerMonth;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "service" }, allowSetters = true)
    private Set<Scope> scopes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contracts" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Contract name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerContactName() {
        return this.customerContactName;
    }

    public Contract customerContactName(String customerContactName) {
        this.setCustomerContactName(customerContactName);
        return this;
    }

    public void setCustomerContactName(String customerContactName) {
        this.customerContactName = customerContactName;
    }

    public String getCustomerContactAddresslines() {
        return this.customerContactAddresslines;
    }

    public Contract customerContactAddresslines(String customerContactAddresslines) {
        this.setCustomerContactAddresslines(customerContactAddresslines);
        return this;
    }

    public void setCustomerContactAddresslines(String customerContactAddresslines) {
        this.customerContactAddresslines = customerContactAddresslines;
    }

    public String getCustomerPurchaseNumber() {
        return this.customerPurchaseNumber;
    }

    public Contract customerPurchaseNumber(String customerPurchaseNumber) {
        this.setCustomerPurchaseNumber(customerPurchaseNumber);
        return this;
    }

    public void setCustomerPurchaseNumber(String customerPurchaseNumber) {
        this.customerPurchaseNumber = customerPurchaseNumber;
    }

    public String getKwiqlyOrderNumber() {
        return this.kwiqlyOrderNumber;
    }

    public Contract kwiqlyOrderNumber(String kwiqlyOrderNumber) {
        this.setKwiqlyOrderNumber(kwiqlyOrderNumber);
        return this;
    }

    public void setKwiqlyOrderNumber(String kwiqlyOrderNumber) {
        this.kwiqlyOrderNumber = kwiqlyOrderNumber;
    }

    public Integer getBasePricePerMonth() {
        return this.basePricePerMonth;
    }

    public Contract basePricePerMonth(Integer basePricePerMonth) {
        this.setBasePricePerMonth(basePricePerMonth);
        return this;
    }

    public void setBasePricePerMonth(Integer basePricePerMonth) {
        this.basePricePerMonth = basePricePerMonth;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Contract startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Contract endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Set<Scope> getScopes() {
        return this.scopes;
    }

    public void setScopes(Set<Scope> scopes) {
        if (this.scopes != null) {
            this.scopes.forEach(i -> i.setContract(null));
        }
        if (scopes != null) {
            scopes.forEach(i -> i.setContract(this));
        }
        this.scopes = scopes;
    }

    public Contract scopes(Set<Scope> scopes) {
        this.setScopes(scopes);
        return this;
    }

    public Contract addScope(Scope scope) {
        this.scopes.add(scope);
        scope.setContract(this);
        return this;
    }

    public Contract removeScope(Scope scope) {
        this.scopes.remove(scope);
        scope.setContract(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Contract customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return id != null && id.equals(((Contract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", customerContactName='" + getCustomerContactName() + "'" +
            ", customerContactAddresslines='" + getCustomerContactAddresslines() + "'" +
            ", customerPurchaseNumber='" + getCustomerPurchaseNumber() + "'" +
            ", kwiqlyOrderNumber='" + getKwiqlyOrderNumber() + "'" +
            ", basePricePerMonth=" + getBasePricePerMonth() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
