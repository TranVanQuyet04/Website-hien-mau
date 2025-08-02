package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodSeparationSuggestion;
import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.entity.BloodBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodSeparationSuggestionRepository extends JpaRepository<BloodSeparationSuggestion, Long> {

    List<BloodSeparationSuggestion> findBySeparationOrder(SeparationOrder order);

    List<BloodSeparationSuggestion> findByBloodBag(BloodBag bag);

    boolean existsByBloodBag_BloodBagId(Long bloodBagId);
    List<BloodSeparationSuggestion> findByBloodBag_BloodBagId(Long bloodBagId);
    List<BloodSeparationSuggestion> findBySeparationOrder_SeparationOrderId(Long separationOrderId);

}
