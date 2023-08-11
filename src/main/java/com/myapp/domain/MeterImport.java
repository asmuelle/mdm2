package com.myapp.domain;

import com.myapp.domain.enumeration.Utility;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MeterImport.
 */
@Entity
@Table(name = "meter_import")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MeterImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "provider")
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "utility")
    private Utility utility;

    @Column(name = "namespace")
    private String namespace;

    @Column(name = "client_ref")
    private String clientRef;

    @Column(name = "meter_name")
    private String meterName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "ownership")
    private String ownership;

    @Column(name = "owner")
    private String owner;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "addresslines")
    private String addresslines;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @Column(name = "classifications")
    private String classifications;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MeterImport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider() {
        return this.provider;
    }

    public MeterImport provider(String provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Utility getUtility() {
        return this.utility;
    }

    public MeterImport utility(Utility utility) {
        this.setUtility(utility);
        return this;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public MeterImport namespace(String namespace) {
        this.setNamespace(namespace);
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClientRef() {
        return this.clientRef;
    }

    public MeterImport clientRef(String clientRef) {
        this.setClientRef(clientRef);
        return this;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public String getMeterName() {
        return this.meterName;
    }

    public MeterImport meterName(String meterName) {
        this.setMeterName(meterName);
        return this;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public MeterImport contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getOwnership() {
        return this.ownership;
    }

    public MeterImport ownership(String ownership) {
        this.setOwnership(ownership);
        return this;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getOwner() {
        return this.owner;
    }

    public MeterImport owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public MeterImport postcode(String postcode) {
        this.setPostcode(postcode);
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddresslines() {
        return this.addresslines;
    }

    public MeterImport addresslines(String addresslines) {
        this.setAddresslines(addresslines);
        return this;
    }

    public void setAddresslines(String addresslines) {
        this.addresslines = addresslines;
    }

    public Float getLat() {
        return this.lat;
    }

    public MeterImport lat(Float lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return this.lon;
    }

    public MeterImport lon(Float lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public String getClassifications() {
        return this.classifications;
    }

    public MeterImport classifications(String classifications) {
        this.setClassifications(classifications);
        return this;
    }

    public void setClassifications(String classifications) {
        this.classifications = classifications;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeterImport)) {
            return false;
        }
        return id != null && id.equals(((MeterImport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeterImport{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", utility='" + getUtility() + "'" +
            ", namespace='" + getNamespace() + "'" +
            ", clientRef='" + getClientRef() + "'" +
            ", meterName='" + getMeterName() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", ownership='" + getOwnership() + "'" +
            ", owner='" + getOwner() + "'" +
            ", postcode='" + getPostcode() + "'" +
            ", addresslines='" + getAddresslines() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", classifications='" + getClassifications() + "'" +
            "}";
    }
}
