package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OwnershipProperty.
 */
@Entity
@Table(name = "ownership_property")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OwnershipProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ownershipProperties" }, allowSetters = true)
    private PropertyType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ownershipProperties", "meters", "classifications", "address", "owner" }, allowSetters = true)
    private Ownership ownership;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OwnershipProperty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public OwnershipProperty value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PropertyType getType() {
        return this.type;
    }

    public void setType(PropertyType propertyType) {
        this.type = propertyType;
    }

    public OwnershipProperty type(PropertyType propertyType) {
        this.setType(propertyType);
        return this;
    }

    public Ownership getOwnership() {
        return this.ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public OwnershipProperty ownership(Ownership ownership) {
        this.setOwnership(ownership);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OwnershipProperty)) {
            return false;
        }
        return id != null && id.equals(((OwnershipProperty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnershipProperty{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
