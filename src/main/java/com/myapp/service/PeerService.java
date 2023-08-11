package com.myapp.service;

import com.myapp.domain.Peer;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Peer}.
 */
public interface PeerService {
    /**
     * Save a peer.
     *
     * @param peer the entity to save.
     * @return the persisted entity.
     */
    Peer save(Peer peer);

    /**
     * Updates a peer.
     *
     * @param peer the entity to update.
     * @return the persisted entity.
     */
    Peer update(Peer peer);

    /**
     * Partially updates a peer.
     *
     * @param peer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Peer> partialUpdate(Peer peer);

    /**
     * Get all the peers.
     *
     * @return the list of entities.
     */
    List<Peer> findAll();

    /**
     * Get the "id" peer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Peer> findOne(Long id);

    /**
     * Delete the "id" peer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
