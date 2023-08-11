package com.myapp.service.criteria;

import com.myapp.domain.enumeration.LoadType;
import com.myapp.domain.enumeration.Utility;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myapp.domain.Peer} entity. This class is used
 * in {@link com.myapp.web.rest.PeerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /peers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeerCriteria implements Serializable, Criteria {

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

    private UtilityFilter utility;

    private LoadTypeFilter loadType;

    private LongFilter meterId;

    private LongFilter ownerId;

    private Boolean distinct;

    public PeerCriteria() {}

    public PeerCriteria(PeerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.utility = other.utility == null ? null : other.utility.copy();
        this.loadType = other.loadType == null ? null : other.loadType.copy();
        this.meterId = other.meterId == null ? null : other.meterId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PeerCriteria copy() {
        return new PeerCriteria(this);
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
        final PeerCriteria that = (PeerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(utility, that.utility) &&
            Objects.equals(loadType, that.loadType) &&
            Objects.equals(meterId, that.meterId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, utility, loadType, meterId, ownerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (utility != null ? "utility=" + utility + ", " : "") +
            (loadType != null ? "loadType=" + loadType + ", " : "") +
            (meterId != null ? "meterId=" + meterId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
