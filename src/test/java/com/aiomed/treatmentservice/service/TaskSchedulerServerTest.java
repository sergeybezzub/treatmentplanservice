package com.aiomed.treatmentservice.service;

import com.aiomed.treatmentservice.TreatmentServiceApplication;
import com.aiomed.treatmentservice.entity.TreatmentPlan;
import com.aiomed.treatmentservice.entity.TreatmentTask;
import com.aiomed.treatmentservice.enumeration.TreatmentAction;
import com.aiomed.treatmentservice.enumeration.TreatmentPlanStatus;
import com.aiomed.treatmentservice.enumeration.TreatmentTaskStatus;
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
    public void createTreatmentTask(){
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

        // Add tasks
        service.createTreatmentTasks(createdPlans.get(0));
        Assert.assertTrue(treatmentTaskRepository.findByPlanId(treatmentPlan.getId()).size() == 6);

        // The status of plan should be changed to ACTIVE because all required tasks were created.
        List<TreatmentPlan> activePlans = service.findTreatmentPlans(TreatmentPlanStatus.ACTIVE);
        Assert.assertTrue(activePlans.size() == 1);

    }

}
