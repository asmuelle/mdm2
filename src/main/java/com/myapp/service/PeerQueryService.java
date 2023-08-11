package com.myapp.service;

import com.myapp.domain.*; // for static metamodels
import com.myapp.domain.Peer;
import com.myapp.repository.PeerRepository;
import com.myapp.service.criteria.PeerCriteria;
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
 * Service for executing complex queries for {@link Peer} entities in the database.
 * The main input is a {@link PeerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Peer} or a {@link Page} of {@link Peer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeerQueryService extends QueryService<Peer> {

    private final Logger log = LoggerFactory.getLogger(PeerQueryService.class);

    private final PeerRepository peerRepository;

    public PeerQueryService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    /**
     * Return a {@link List} of {@link Peer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Peer> findByCriteria(PeerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Peer> specification = createSpecification(criteria);
        return peerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Peer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Peer> findByCriteria(PeerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Peer> specification = createSpecification(criteria);
        return peerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Peer> specification = createSpecification(criteria);
        return peerRepository.count(specification);
    }

    /**
     * Function to convert {@link PeerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Peer> createSpecification(PeerCriteria criteria) {
        Specification<Peer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Peer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Peer_.name));
            }
            if (criteria.getUtility() != null) {
                specification = specification.and(buildSpecification(criteria.getUtility(), Peer_.utility));
            }
            if (criteria.getLoadType() != null) {
                specification = specification.and(buildSpecification(criteria.getLoadType(), Peer_.loadType));
            }
            if (criteria.getMeterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMeterId(), root -> root.join(Peer_.meters, JoinType.LEFT).get(Meter_.id))
                    );
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Peer_.owner, JoinType.LEFT).get(Owner_.id))
                    );
            }
        }
        return specification;
    }
}
