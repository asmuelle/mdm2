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
 * A PropertyType.
 */
@Entity
@Table(name = "property_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "pattern", nullable = false)
    private String pattern;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "ownership" }, allowSetters = true)
    private Set<OwnershipProperty> ownershipProperties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PropertyType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PropertyType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return this.pattern;
    }

    public PropertyType pattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<OwnershipProperty> getOwnershipProperties() {
        return this.ownershipProperties;
    }

    public void setOwnershipProperties(Set<OwnershipProperty> ownershipProperties) {
        if (this.ownershipProperties != null) {
            this.ownershipProperties.forEach(i -> i.setType(null));
        }
        if (ownershipProperties != null) {
            ownershipProperties.forEach(i -> i.setType(this));
        }
        this.ownershipProperties = ownershipProperties;
    }

    public PropertyType ownershipProperties(Set<OwnershipProperty> ownershipProperties) {
        this.setOwnershipProperties(ownershipProperties);
        return this;
    }

    public PropertyType addOwnershipProperty(OwnershipProperty ownershipProperty) {
        this.ownershipProperties.add(ownershipProperty);
        ownershipProperty.setType(this);
        return this;
    }

    public PropertyType removeOwnershipProperty(OwnershipProperty ownershipProperty) {
        this.ownershipProperties.remove(ownershipProperty);
        ownershipProperty.setType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyType)) {
            return false;
        }
        return id != null && id.equals(((PropertyType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pattern='" + getPattern() + "'" +
            "}";
    }
}
