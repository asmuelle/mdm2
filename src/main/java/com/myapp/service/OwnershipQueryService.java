package com.myapp.service;

import com.myapp.domain.*; // for static metamodels
import com.myapp.domain.Ownership;
import com.myapp.repository.OwnershipRepository;
import com.myapp.service.criteria.OwnershipCriteria;
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
 * Service for executing complex queries for {@link Ownership} entities in the database.
 * The main input is a {@link OwnershipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ownership} or a {@link Page} of {@link Ownership} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OwnershipQueryService extends QueryService<Ownership> {

    private final Logger log = LoggerFactory.getLogger(OwnershipQueryService.class);

    private final OwnershipRepository ownershipRepository;

    public OwnershipQueryService(OwnershipRepository ownershipRepository) {
        this.ownershipRepository = ownershipRepository;
    }

    /**
     * Return a {@link List} of {@link Ownership} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ownership> findByCriteria(OwnershipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ownership> specification = createSpecification(criteria);
        return ownershipRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ownership} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ownership> findByCriteria(OwnershipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ownership> specification = createSpecification(criteria);
        return ownershipRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OwnershipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ownership> specification = createSpecification(criteria);
        return ownershipRepository.count(specification);
    }

    /**
     * Function to convert {@link OwnershipCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ownership> createSpecification(OwnershipCriteria criteria) {
        Specification<Ownership> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ownership_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ownership_.name));
            }
            if (criteria.getClientRef() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientRef(), Ownership_.clientRef));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Ownership_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Ownership_.endDate));
            }
            if (criteria.getOwnershipPropertyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOwnershipPropertyId(),
                            root -> root.join(Ownership_.ownershipProperties, JoinType.LEFT).get(OwnershipProperty_.id)
                        )
                    );
            }
            if (criteria.getMetersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMetersId(), root -> root.join(Ownership_.meters, JoinType.LEFT).get(Meter_.id))
                    );
            }
            if (criteria.getClassificationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassificationsId(),
                            root -> root.join(Ownership_.classifications, JoinType.LEFT).get(OwnershipClassification_.id)
                        )
                    );
            }
            if (criteria.getAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAddressId(), root -> root.join(Ownership_.address, JoinType.LEFT).get(Address_.id))
                    );
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Ownership_.owner, JoinType.LEFT).get(Owner_.id))
                    );
            }
        }
        return specification;
    }
}
