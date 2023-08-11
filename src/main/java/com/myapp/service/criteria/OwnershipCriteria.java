package com.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myapp.domain.Ownership} entity. This class is used
 * in {@link com.myapp.web.rest.OwnershipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ownerships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OwnershipCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter clientRef;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LongFilter ownershipPropertyId;

    private LongFilter metersId;

    private LongFilter classificationsId;

    private LongFilter addressId;

    private LongFilter ownerId;

    private Boolean distinct;

    public OwnershipCriteria() {}

    public OwnershipCriteria(OwnershipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.clientRef = other.clientRef == null ? null : other.clientRef.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.ownershipPropertyId = other.ownershipPropertyId == null ? null : other.ownershipPropertyId.copy();
        this.metersId = other.metersId == null ? null : other.metersId.copy();
        this.classificationsId = other.classificationsId == null ? null : other.classificationsId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OwnershipCriteria copy() {
        return new OwnershipCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getClientRef() {
        return clientRef;
    }

    public StringFilter clientRef() {
        if (clientRef == null) {
            clientRef = new StringFilter();
        }
        return clientRef;
    }

    public void setClientRef(StringFilter clientRef) {
        this.clientRef = clientRef;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getOwnershipPropertyId() {
        return ownershipPropertyId;
    }

    public LongFilter ownershipPropertyId() {
        if (ownershipPropertyId == null) {
            ownershipPropertyId = new LongFilter();
        }
        return ownershipPropertyId;
    }

    public void setOwnershipPropertyId(LongFilter ownershipPropertyId) {
        this.ownershipPropertyId = ownershipPropertyId;
    }

    public LongFilter getMetersId() {
        return metersId;
    }

    public LongFilter metersId() {
        if (metersId == null) {
            metersId = new LongFilter();
        }
        return metersId;
    }

    public void setMetersId(LongFilter metersId) {
        this.metersId = metersId;
    }

    public LongFilter getClassificationsId() {
        return classificationsId;
    }

    public LongFilter classificationsId() {
        if (classificationsId == null) {
            classificationsId = new LongFilter();
        }
        return classificationsId;
    }

    public void setClassificationsId(LongFilter classificationsId) {
        this.classificationsId = classificationsId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OwnershipCriteria that = (OwnershipCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(clientRef, that.clientRef) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(ownershipPropertyId, that.ownershipPropertyId) &&
            Objects.equals(metersId, that.metersId) &&
            Objects.equals(classificationsId, that.classificationsId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            clientRef,
            startDate,
            endDate,
            ownershipPropertyId,
            metersId,
            classificationsId,
            addressId,
            ownerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnershipCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (clientRef != null ? "clientRef=" + clientRef + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (ownershipPropertyId != null ? "ownershipPropertyId=" + ownershipPropertyId + ", " : "") +
            (metersId != null ? "metersId=" + metersId + ", " : "") +
            (classificationsId != null ? "classificationsId=" + classificationsId + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
