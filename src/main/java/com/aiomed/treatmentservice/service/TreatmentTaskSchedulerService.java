package com.aiomed.treatmentservice.service;

import com.aiomed.treatmentservice.entity.TreatmentPlan;
import com.aiomed.treatmentservice.entity.TreatmentTask;
import com.aiomed.treatmentservice.enumeration.TreatmentAction;
import com.aiomed.treatmentservice.enumeration.TreatmentPlanStatus;
import com.aiomed.treatmentservice.enumeration.TreatmentTaskStatus;
import com.aiomed.treatmentservice.repository.TreatmentPlanRepository;
import com.aiomed.treatmentservice.repository.TreatmentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class TreatmentTaskSchedulerService {

    @Autowired
    TreatmentTaskRepository treatmentTaskRepository;

    @Autowired
    TreatmentPlanRepository treatmentPlanRepository;

    @Transactional
    public List<TreatmentTask> createTreatmentTasks(TreatmentPlan plan) {
        TreatmentAction action = plan.getTreatmentAction();
        String patient = plan.getSubjectPatient();
        List<TreatmentTask> tasks = new ArrayList<>();
        for(LocalDateTime date = plan.getStartDate(); date.isBefore(plan.getEndDate().plusDays(1));date=date.plusDays(1)) {
            TreatmentTask task = TreatmentTask.builder()
                    .treatmentPlan(plan)
                    .status(TreatmentTaskStatus.ACTIVE)
                    .startDate(date)
                    .subjectPatient(patient)
                    .treatmentAction(action)
                    .build();
            TreatmentTask taskPersisted = treatmentTaskRepository.save(task);
            tasks.add(taskPersisted);
        }
        // Update plan to
        plan.setStatus(TreatmentPlanStatus.ACTIVE);
        treatmentPlanRepository.save(plan);

        return tasks;
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlan> findTreatmentPlans(TreatmentPlanStatus status) {
        switch (status) {
            case ACTIVE:
                return treatmentPlanRepository.findActivatedPlans();
            case CREATED:
                return treatmentPlanRepository.findCreatedPlans();
            default:
                return Collections.emptyList();
        }
    }

}
