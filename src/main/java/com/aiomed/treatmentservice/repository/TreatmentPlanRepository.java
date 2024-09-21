package com.aiomed.treatmentservice.repository;

import com.aiomed.treatmentservice.entity.TreatmentPlan;
import com.aiomed.treatmentservice.enumeration.TreatmentPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, UUID> {

    @Query(nativeQuery = true, value = "select * from treatment_plan where status = 'CREATED'  order by start_date")
    List<TreatmentPlan> findCreatedPlans();

    @Query(nativeQuery = true, value = "select * from treatment_plan where status = 'ACTIVE' order by start_date")
    List<TreatmentPlan> findActivatedPlans();


}
