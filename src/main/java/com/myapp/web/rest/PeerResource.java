package com.myapp.web.rest;

import com.myapp.domain.Peer;
import com.myapp.repository.PeerRepository;
import com.myapp.service.PeerQueryService;
import com.myapp.service.PeerService;
import com.myapp.service.criteria.PeerCriteria;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.Peer}.
 */
@RestController
@RequestMapping("/api")
public class PeerResource {

    private final Logger log = LoggerFactory.getLogger(PeerResource.class);

    private static final String ENTITY_NAME = "peer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeerService peerService;

    private final PeerRepository peerRepository;

    private final PeerQueryService peerQueryService;

    public PeerResource(PeerService peerService, PeerRepository peerRepository, PeerQueryService peerQueryService) {
        this.peerService = peerService;
        this.peerRepository = peerRepository;
        this.peerQueryService = peerQueryService;
    }

    /**
     * {@code POST  /peers} : Create a new peer.
     *
     * @param peer the peer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peer, or with status {@code 400 (Bad Request)} if the peer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/peers")
    public ResponseEntity<Peer> createPeer(@RequestBody Peer peer) throws URISyntaxException {
        log.debug("REST request to save Peer : {}", peer);
        if (peer.getId() != null) {
            throw new BadRequestAlertException("A new peer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Peer result = peerService.save(peer);
        return ResponseEntity
            .created(new URI("/api/peers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /peers/:id} : Updates an existing peer.
     *
     * @param id the id of the peer to save.
     * @param peer the peer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peer,
     * or with status {@code 400 (Bad Request)} if the peer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/peers/{id}")
    public ResponseEntity<Peer> updatePeer(@PathVariable(value = "id", required = false) final Long id, @RequestBody Peer peer)
        throws URISyntaxException {
        log.debug("REST request to update Peer : {}, {}", id, peer);
        if (peer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Peer result = peerService.update(peer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /peers/:id} : Partial updates given fields of an existing peer, field will ignore if it is null
     *
     * @param id the id of the peer to save.
     * @param peer the peer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peer,
     * or with status {@code 400 (Bad Request)} if the peer is not valid,
     * or with status {@code 404 (Not Found)} if the peer is not found,
     * or with status {@code 500 (Internal Server Error)} if the peer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/peers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Peer> partialUpdatePeer(@PathVariable(value = "id", required = false) final Long id, @RequestBody Peer peer)
        throws URISyntaxException {
        log.debug("REST request to partial update Peer partially : {}, {}", id, peer);
        if (peer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Peer> result = peerService.partialUpdate(peer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peer.getId().toString())
        );
    }

    /**
     * {@code GET  /peers} : get all the peers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peers in body.
     */
    @GetMapping("/peers")
    public ResponseEntity<List<Peer>> getAllPeers(PeerCriteria criteria) {
        log.debug("REST request to get Peers by criteria: {}", criteria);

        List<Peer> entityList = peerQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /peers/count} : count all the peers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/peers/count")
    public ResponseEntity<Long> countPeers(PeerCriteria criteria) {
        log.debug("REST request to count Peers by criteria: {}", criteria);
        return ResponseEntity.ok().body(peerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /peers/:id} : get the "id" peer.
     *
     * @param id the id of the peer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/peers/{id}")
    public ResponseEntity<Peer> getPeer(@PathVariable Long id) {
        log.debug("REST request to get Peer : {}", id);
        Optional<Peer> peer = peerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peer);
    }

    /**
     * {@code DELETE  /peers/:id} : delete the "id" peer.
     *
     * @param id the id of the peer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/peers/{id}")
    public ResponseEntity<Void> deletePeer(@PathVariable Long id) {
        log.debug("REST request to delete Peer : {}", id);
        peerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
