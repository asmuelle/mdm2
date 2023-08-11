package com.myapp.repository;

import com.myapp.domain.Ownership;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OwnershipRepositoryWithBagRelationshipsImpl implements OwnershipRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ownership> fetchBagRelationships(Optional<Ownership> ownership) {
        return ownership.map(this::fetchMeters).map(this::fetchClassifications);
    }

    @Override
    public Page<Ownership> fetchBagRelationships(Page<Ownership> ownerships) {
        return new PageImpl<>(fetchBagRelationships(ownerships.getContent()), ownerships.getPageable(), ownerships.getTotalElements());
    }

    @Override
    public List<Ownership> fetchBagRelationships(List<Ownership> ownerships) {
        return Optional.of(ownerships).map(this::fetchMeters).map(this::fetchClassifications).orElse(Collections.emptyList());
    }

    Ownership fetchMeters(Ownership result) {
        return entityManager
            .createQuery(
                "select ownership from Ownership ownership left join fetch ownership.meters where ownership.id = :id",
                Ownership.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ownership> fetchMeters(List<Ownership> ownerships) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ownerships.size()).forEach(index -> order.put(ownerships.get(index).getId(), index));
        List<Ownership> result = entityManager
            .createQuery(
                "select ownership from Ownership ownership left join fetch ownership.meters where ownership in :ownerships",
                Ownership.class
            )
            .setParameter("ownerships", ownerships)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Ownership fetchClassifications(Ownership result) {
        return entityManager
            .createQuery(
                "select ownership from Ownership ownership left join fetch ownership.classifications where ownership.id = :id",
                Ownership.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ownership> fetchClassifications(List<Ownership> ownerships) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ownerships.size()).forEach(index -> order.put(ownerships.get(index).getId(), index));
        List<Ownership> result = entityManager
            .createQuery(
                "select ownership from Ownership ownership left join fetch ownership.classifications where ownership in :ownerships",
                Ownership.class
            )
            .setParameter("ownerships", ownerships)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
