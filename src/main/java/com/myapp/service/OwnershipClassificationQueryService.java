package com.myapp.service;

import com.myapp.domain.*; // for static metamodels
import com.myapp.domain.OwnershipClassification;
import com.myapp.repository.OwnershipClassificationRepository;
import com.myapp.service.criteria.OwnershipClassificationCriteria;
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
 * Service for executing complex queries for {@link OwnershipClassification} entities in the database.
 * The main input is a {@link OwnershipClassificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OwnershipClassification} or a {@link Page} of {@link OwnershipClassification} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OwnershipClassificationQueryService extends QueryService<OwnershipClassification> {

    private final Logger log = LoggerFactory.getLogger(OwnershipClassificationQueryService.class);

    private final OwnershipClassificationRepository ownershipClassificationRepository;

    public OwnershipClassificationQueryService(OwnershipClassificationRepository ownershipClassificationRepository) {
        this.ownershipClassificationRepository = ownershipClassificationRepository;
    }

    /**
     * Return a {@link List} of {@link OwnershipClassification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OwnershipClassification> findByCriteria(OwnershipClassificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OwnershipClassification> specification = createSpecification(criteria);
        return ownershipClassificationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OwnershipClassification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OwnershipClassification> findByCriteria(OwnershipClassificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OwnershipClassification> specification = createSpecification(criteria);
        return ownershipClassificationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OwnershipClassificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OwnershipClassification> specification = createSpecification(criteria);
        return ownershipClassificationRepository.count(specification);
    }

    /**
     * Function to convert {@link OwnershipClassificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OwnershipClassification> createSpecification(OwnershipClassificationCriteria criteria) {
        Specification<OwnershipClassification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OwnershipClassification_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OwnershipClassification_.name));
            }
            if (criteria.getOwnershipsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOwnershipsId(),
                            root -> root.join(OwnershipClassification_.ownerships, JoinType.LEFT).get(Ownership_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
