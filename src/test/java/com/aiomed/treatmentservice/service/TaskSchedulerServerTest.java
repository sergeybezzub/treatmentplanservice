package com.aiomed.treatmentservice.service;

import com.aiomed.treatmentservice.TreatmentServiceApplication;
import com.aiomed.treatmentservice.entity.TreatmentPlan;
import com.aiomed.treatmentservice.enumeration.TreatmentAction;
import com.aiomed.treatmentservice.enumeration.TreatmentPlanStatus;
import com.aiomed.treatmentservice.repository.TreatmentPlanRepository;
import com.aiomed.treatmentservice.repository.TreatmentTaskRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ContextConfiguration(classes = TreatmentServiceApplication.class)
public class TaskSchedulerServerTest {

    @Autowired
    TreatmentTaskSchedulerService service;

    @Autowired
    TreatmentTaskRepository treatmentTaskRepository;

    @Autowired
    TreatmentPlanRepository treatmentPlanRepository;

    @Test
    public void createTreatmentTasksForSelectedPlan(){
        TreatmentPlan treatmentPlan = TreatmentPlan.builder()
                .treatmentAction(TreatmentAction.ActionA)
                .status(TreatmentPlanStatus.CREATED)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .subjectPatient("PatientA")
                .recurrencePattern("every day at 08:00 and 16:00")
                .build();
        // Persist plan
        treatmentPlanRepository.save(treatmentPlan);

        //Find created plans
        List<TreatmentPlan> createdPlans = service.findTreatmentPlans(TreatmentPlanStatus.CREATED);
        Assert.assertTrue(createdPlans.size() == 1);

        // Create tasks
        service.createTreatmentTasks(createdPlans.get(0));
        Assert.assertTrue(treatmentTaskRepository.findByPlanId(treatmentPlan.getId()).size() == 6);

        // The status of plan should be changed to ACTIVE because all required tasks were created.
        List<TreatmentPlan> activePlans = service.findTreatmentPlans(TreatmentPlanStatus.ACTIVE);
        Assert.assertTrue(activePlans.size() == 1);

    }

    @Test
    public void createTreatmentTasksForAllPlans(){
        TreatmentPlan treatmentPlan1 = TreatmentPlan.builder()
                .treatmentAction(TreatmentAction.ActionA)
                .status(TreatmentPlanStatus.CREATED)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(3))
                .subjectPatient("PatientA")
                .recurrencePattern("every day at 08:00 and 16:00")
                .build();
        TreatmentPlan treatmentPlan2 = TreatmentPlan.builder()
                .treatmentAction(TreatmentAction.ActionB)
                .status(TreatmentPlanStatus.CREATED)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(4))
                .subjectPatient("PatientB")
                .recurrencePattern("every day at 08:00 and 16:00")
                .build();

        // Persist plan
        TreatmentPlan persistedPlan1 = treatmentPlanRepository.save(treatmentPlan1);
        TreatmentPlan persistedPlan2 = treatmentPlanRepository.save(treatmentPlan2);

        //Find created plans
        List<TreatmentPlan> createdPlans = service.findTreatmentPlans(TreatmentPlanStatus.CREATED);
        Assert.assertTrue(createdPlans.size() == 2);

        // Create tasks
        int allTasksAmount = service.activateNewPlans();
        Assert.assertTrue(treatmentTaskRepository.findByPlanId(persistedPlan1.getId()).size() == 4);
        Assert.assertTrue(treatmentTaskRepository.findByPlanId(persistedPlan2.getId()).size() == 5);

        // The status of plan should be changed to ACTIVE because all required tasks were created.
        List<TreatmentPlan> activePlans = service.findTreatmentPlans(TreatmentPlanStatus.ACTIVE);
        Assert.assertEquals(treatmentPlanRepository.findById(treatmentPlan1.getId()).get().getStatus(), TreatmentPlanStatus.ACTIVE);
        Assert.assertEquals(treatmentPlanRepository.findById(treatmentPlan2.getId()).get().getStatus(), TreatmentPlanStatus.ACTIVE);

    }

}
