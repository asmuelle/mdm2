package com.myapp.service.impl;

import com.myapp.domain.Peer;
import com.myapp.repository.PeerRepository;
import com.myapp.service.PeerService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Peer}.
 */
@Service
@Transactional
public class PeerServiceImpl implements PeerService {

    private final Logger log = LoggerFactory.getLogger(PeerServiceImpl.class);

    private final PeerRepository peerRepository;

    public PeerServiceImpl(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    @Override
    public Peer save(Peer peer) {
        log.debug("Request to save Peer : {}", peer);
        return peerRepository.save(peer);
    }

    @Override
    public Peer update(Peer peer) {
        log.debug("Request to update Peer : {}", peer);
        return peerRepository.save(peer);
    }

    @Override
    public Optional<Peer> partialUpdate(Peer peer) {
        log.debug("Request to partially update Peer : {}", peer);

        return peerRepository
            .findById(peer.getId())
            .map(existingPeer -> {
                if (peer.getName() != null) {
                    existingPeer.setName(peer.getName());
                }
                if (peer.getUtility() != null) {
                    existingPeer.setUtility(peer.getUtility());
                }
                if (peer.getLoadType() != null) {
                    existingPeer.setLoadType(peer.getLoadType());
                }

                return existingPeer;
            })
            .map(peerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Peer> findAll() {
        log.debug("Request to get all Peers");
        return peerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Peer> findOne(Long id) {
        log.debug("Request to get Peer : {}", id);
        return peerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Peer : {}", id);
        peerRepository.deleteById(id);
    }
}
