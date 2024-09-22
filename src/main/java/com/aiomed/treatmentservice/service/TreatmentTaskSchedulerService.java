package com.aiomed.treatmentservice.service;

import com.aiomed.treatmentservice.entity.TreatmentPlan;
import com.aiomed.treatmentservice.entity.TreatmentTask;
import com.aiomed.treatmentservice.enumeration.TreatmentAction;
import com.aiomed.treatmentservice.enumeration.TreatmentPlanStatus;
import com.aiomed.treatmentservice.enumeration.TreatmentTaskStatus;
import com.aiomed.treatmentservice.exception.TreatmentPlanServiceException;
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
    public int activateNewPlans() {
        List<TreatmentPlan> newPlans = new ArrayList<>();
        try {
            newPlans.addAll(treatmentPlanRepository.findCreatedPlans());
        } catch (Exception e) {
            String message = "An attempt to find new treatment plans has failed! " + e.getMessage();
            log.error(message, e);
            throw new TreatmentPlanServiceException(message, e);
        }
        int newTasksAmount = 0;
        for (TreatmentPlan plan : newPlans) {
            newTasksAmount += createTreatmentTasks(plan).size();
        }
        log.info("{} treatment tasks were successfully created for {} new treatment plans", newTasksAmount, newPlans.size());
        return newTasksAmount;

    }

    @Transactional
    public List<TreatmentTask> createTreatmentTasks(TreatmentPlan plan) {
        TreatmentAction action = plan.getTreatmentAction();
        String patient = plan.getSubjectPatient();
        List<TreatmentTask> tasks = new ArrayList<>();
        try {
            for (LocalDateTime date = plan.getStartDate(); date.isBefore(plan.getEndDate().plusDays(1)); date = date.plusDays(1)) {
                TreatmentTask task = TreatmentTask.builder()
                        .treatmentPlan(plan)
                        .status(TreatmentTaskStatus.ACTIVE)
                        .startDate(date)
                        .subjectPatient(patient)
                        .treatmentAction(action)
                        .build();
                TreatmentTask taskPersisted = treatmentTaskRepository.save(task);
                tasks.add(taskPersisted);
                log.info("A new treatment-task:{} has been successfully created for new treatment-plan:{}",
                        taskPersisted.getId().toString(), plan.getId().toString());

            }
        } catch (Exception e) {
            String message = String.format("An attempt to create treatment tasks for treatment-plan:%s has failed! %s", plan.getId().toString(), e.getMessage());
            log.error(message, e);
            throw new TreatmentPlanServiceException(message, e);
        }
        // Update treatment plan status to ACTIVE
        try {
            plan.setStatus(TreatmentPlanStatus.ACTIVE);
            treatmentPlanRepository.save(plan);
        } catch (Exception e) {
            String message = String.format("An attempt to finalize the activation of treatment-plan:%s has failed! %s", plan.getId().toString(), e.getMessage());
            log.error(message, e);
            throw new TreatmentPlanServiceException(message, e);
        }
        log.info("The new treatment-plan:{} has been successfully activated", plan.getId().toString());
        return tasks;
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlan> findTreatmentPlans(TreatmentPlanStatus status) {
        try {
            switch (status) {
                case ACTIVE:
                    return treatmentPlanRepository.findActivatedPlans();
                case CREATED:
                    return treatmentPlanRepository.findCreatedPlans();
                default:
                    return Collections.emptyList();
            }
        } catch (Exception e) {
            String message = String.format("An attempt to find all treatment plans by status:%s has failed! %s", status.name(), e.getMessage());
            log.error(message, e);
            throw new TreatmentPlanServiceException(message, e);
        }
    }

}
