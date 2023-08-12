package com.myapp.service.criteria;

import com.myapp.domain.enumeration.LoadType;
import com.myapp.domain.enumeration.Utility;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myapp.domain.Meter} entity. This class is used
 * in {@link com.myapp.web.rest.MeterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /meters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MeterCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Utility
     */
    public static class UtilityFilter extends Filter<Utility> {

        public UtilityFilter() {}

        public UtilityFilter(UtilityFilter filter) {
            super(filter);
        }

        @Override
        public UtilityFilter copy() {
            return new UtilityFilter(this);
        }
    }

    /**
     * Class for filtering LoadType
     */
    public static class LoadTypeFilter extends Filter<LoadType> {

        public LoadTypeFilter() {}

        public LoadTypeFilter(LoadTypeFilter filter) {
            super(filter);
        }

        @Override
        public LoadTypeFilter copy() {
            return new LoadTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter amrWeek;

    private IntegerFilter amrYear;

    private UtilityFilter utility;

    private LoadTypeFilter loadType;

    private FloatFilter price;

    private LocalDateFilter lastReading;

    private StringFilter contactEmail;

    private LongFilter parentId;

    private LongFilter alternativeId;

    private LongFilter peerId;

    private LongFilter providerId;

    private LongFilter namespaceId;

    private LongFilter ownershipsId;

    private LongFilter meterId;

    private Boolean distinct;

    public MeterCriteria() {}

    public MeterCriteria(MeterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.amrWeek = other.amrWeek == null ? null : other.amrWeek.copy();
        this.amrYear = other.amrYear == null ? null : other.amrYear.copy();
        this.utility = other.utility == null ? null : other.utility.copy();
        this.loadType = other.loadType == null ? null : other.loadType.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.lastReading = other.lastReading == null ? null : other.lastReading.copy();
        this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.alternativeId = other.alternativeId == null ? null : other.alternativeId.copy();
        this.peerId = other.peerId == null ? null : other.peerId.copy();
        this.providerId = other.providerId == null ? null : other.providerId.copy();
        this.namespaceId = other.namespaceId == null ? null : other.namespaceId.copy();
        this.ownershipsId = other.ownershipsId == null ? null : other.ownershipsId.copy();
        this.meterId = other.meterId == null ? null : other.meterId.copy();
        this.meterId = other.meterId == null ? null : other.meterId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MeterCriteria copy() {
        return new MeterCriteria(this);
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

    public IntegerFilter getAmrWeek() {
        return amrWeek;
    }

    public IntegerFilter amrWeek() {
        if (amrWeek == null) {
            amrWeek = new IntegerFilter();
        }
        return amrWeek;
    }

    public void setAmrWeek(IntegerFilter amrWeek) {
        this.amrWeek = amrWeek;
    }

    public IntegerFilter getAmrYear() {
        return amrYear;
    }

    public IntegerFilter amrYear() {
        if (amrYear == null) {
            amrYear = new IntegerFilter();
        }
        return amrYear;
    }

    public void setAmrYear(IntegerFilter amrYear) {
        this.amrYear = amrYear;
    }

    public UtilityFilter getUtility() {
        return utility;
    }

    public UtilityFilter utility() {
        if (utility == null) {
            utility = new UtilityFilter();
        }
        return utility;
    }

    public void setUtility(UtilityFilter utility) {
        this.utility = utility;
    }

    public LoadTypeFilter getLoadType() {
        return loadType;
    }

    public LoadTypeFilter loadType() {
        if (loadType == null) {
            loadType = new LoadTypeFilter();
        }
        return loadType;
    }

    public void setLoadType(LoadTypeFilter loadType) {
        this.loadType = loadType;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public LocalDateFilter getLastReading() {
        return lastReading;
    }

    public LocalDateFilter lastReading() {
        if (lastReading == null) {
            lastReading = new LocalDateFilter();
        }
        return lastReading;
    }

    public void setLastReading(LocalDateFilter lastReading) {
        this.lastReading = lastReading;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public StringFilter contactEmail() {
        if (contactEmail == null) {
            contactEmail = new StringFilter();
        }
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getAlternativeId() {
        return alternativeId;
    }

    public LongFilter alternativeId() {
        if (alternativeId == null) {
            alternativeId = new LongFilter();
        }
        return alternativeId;
    }

    public void setAlternativeId(LongFilter alternativeId) {
        this.alternativeId = alternativeId;
    }

    public LongFilter getPeerId() {
        return peerId;
    }

    public LongFilter peerId() {
        if (peerId == null) {
            peerId = new LongFilter();
        }
        return peerId;
    }

    public void setPeerId(LongFilter peerId) {
        this.peerId = peerId;
    }

    public LongFilter getProviderId() {
        return providerId;
    }

    public LongFilter providerId() {
        if (providerId == null) {
            providerId = new LongFilter();
        }
        return providerId;
    }

    public void setProviderId(LongFilter providerId) {
        this.providerId = providerId;
    }

    public LongFilter getNamespaceId() {
        return namespaceId;
    }

    public LongFilter namespaceId() {
        if (namespaceId == null) {
            namespaceId = new LongFilter();
        }
        return namespaceId;
    }

    public void setNamespaceId(LongFilter namespaceId) {
        this.namespaceId = namespaceId;
    }

    public LongFilter getOwnershipsId() {
        return ownershipsId;
    }

    public LongFilter ownershipsId() {
        if (ownershipsId == null) {
            ownershipsId = new LongFilter();
        }
        return ownershipsId;
    }

    public void setOwnershipsId(LongFilter ownershipsId) {
        this.ownershipsId = ownershipsId;
    }

    public LongFilter getMeterId() {
        return meterId;
    }

    public LongFilter meterId() {
        if (meterId == null) {
            meterId = new LongFilter();
        }
        return meterId;
    }

    public void setMeterId(LongFilter meterId) {
        this.meterId = meterId;
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
        final MeterCriteria that = (MeterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(amrWeek, that.amrWeek) &&
            Objects.equals(amrYear, that.amrYear) &&
            Objects.equals(utility, that.utility) &&
            Objects.equals(loadType, that.loadType) &&
            Objects.equals(price, that.price) &&
            Objects.equals(lastReading, that.lastReading) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(alternativeId, that.alternativeId) &&
            Objects.equals(peerId, that.peerId) &&
            Objects.equals(providerId, that.providerId) &&
            Objects.equals(namespaceId, that.namespaceId) &&
            Objects.equals(ownershipsId, that.ownershipsId) &&
            Objects.equals(meterId, that.meterId) &&
            Objects.equals(meterId, that.meterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            amrWeek,
            amrYear,
            utility,
            loadType,
            price,
            lastReading,
            contactEmail,
            parentId,
            alternativeId,
            peerId,
            providerId,
            namespaceId,
            ownershipsId,
            meterId,
            meterId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (amrWeek != null ? "amrWeek=" + amrWeek + ", " : "") +
            (amrYear != null ? "amrYear=" + amrYear + ", " : "") +
            (utility != null ? "utility=" + utility + ", " : "") +
            (loadType != null ? "loadType=" + loadType + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (lastReading != null ? "lastReading=" + lastReading + ", " : "") +
            (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (alternativeId != null ? "alternativeId=" + alternativeId + ", " : "") +
            (peerId != null ? "peerId=" + peerId + ", " : "") +
            (providerId != null ? "providerId=" + providerId + ", " : "") +
            (namespaceId != null ? "namespaceId=" + namespaceId + ", " : "") +
            (ownershipsId != null ? "ownershipsId=" + ownershipsId + ", " : "") +
            (meterId != null ? "meterId=" + meterId + ", " : "") +
            (meterId != null ? "meterId=" + meterId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
