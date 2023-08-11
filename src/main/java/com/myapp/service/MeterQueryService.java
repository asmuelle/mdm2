package com.myapp.service;

import com.myapp.domain.*; // for static metamodels
import com.myapp.domain.Meter;
import com.myapp.repository.MeterRepository;
import com.myapp.service.criteria.MeterCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Meter} entities in the database.
 * The main input is a {@link MeterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Meter} or a {@link Page} of {@link Meter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeterQueryService extends QueryService<Meter> {

    private final Logger log = LoggerFactory.getLogger(MeterQueryService.class);

    private final MeterRepository meterRepository;

    public MeterQueryService(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    /**
     * Return a {@link List} of {@link Meter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Meter> findByCriteria(MeterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Meter> specification = createSpecification(criteria);
        return meterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Meter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Meter> findByCriteria(MeterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Meter> specification = createSpecification(criteria);
        return meterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Meter> specification = createSpecification(criteria);
        return meterRepository.count(specification);
    }

    /**
     * Function to convert {@link MeterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Meter> createSpecification(MeterCriteria criteria) {
        Specification<Meter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Meter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Meter_.name));
            }
            if (criteria.getAmrWeek() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmrWeek(), Meter_.amrWeek));
            }
            if (criteria.getAmrYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmrYear(), Meter_.amrYear));
            }
            if (criteria.getUtility() != null) {
                specification = specification.and(buildSpecification(criteria.getUtility(), Meter_.utility));
            }
            if (criteria.getLoadType() != null) {
                specification = specification.and(buildSpecification(criteria.getLoadType(), Meter_.loadType));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Meter_.price));
            }
            if (criteria.getLastReading() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastReading(), Meter_.lastReading));
            }
            if (criteria.getContactEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactEmail(), Meter_.contactEmail));
            }
            if (criteria.getParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParentId(), root -> root.join(Meter_.parent, JoinType.LEFT).get(Meter_.id))
                    );
            }
            if (criteria.getAlternativeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAlternativeId(), root -> root.join(Meter_.alternative, JoinType.LEFT).get(Meter_.id))
                    );
            }
            if (criteria.getPeerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPeerId(), root -> root.join(Meter_.peer, JoinType.LEFT).get(Peer_.id))
                    );
            }
            if (criteria.getProviderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProviderId(), root -> root.join(Meter_.provider, JoinType.LEFT).get(Provider_.id))
                    );
            }
            if (criteria.getNamespaceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNamespaceId(), root -> root.join(Meter_.namespace, JoinType.LEFT).get(Namespace_.id))
                    );
            }
            if (criteria.getOwnershipsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOwnershipsId(),
                            root -> root.join(Meter_.ownerships, JoinType.LEFT).get(Ownership_.id)
                        )
                    );
            }
            if (criteria.getMeterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMeterId(), root -> root.join(Meter_.meter, JoinType.LEFT).get(Meter_.id))
                    );
            }
            if (criteria.getMeterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMeterId(), root -> root.join(Meter_.meter, JoinType.LEFT).get(Meter_.id))
                    );
            }
        }
        return specification;
    }
}
