package com.myapp.repository;

import com.myapp.domain.Ownership;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OwnershipRepositoryWithBagRelationships {
    Optional<Ownership> fetchBagRelationships(Optional<Ownership> ownership);

    List<Ownership> fetchBagRelationships(List<Ownership> ownerships);

    Page<Ownership> fetchBagRelationships(Page<Ownership> ownerships);
}
