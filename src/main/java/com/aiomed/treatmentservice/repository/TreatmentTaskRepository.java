package com.aiomed.treatmentservice.repository;

import com.aiomed.treatmentservice.entity.TreatmentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentTaskRepository  extends JpaRepository<TreatmentTask, UUID> {
    @Query(nativeQuery = true, value = "select * from treatment_task where plan_id = :planId")
    List<TreatmentTask> findByPlanId(UUID planId);

}
