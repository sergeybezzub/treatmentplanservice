package com.aiomed.treatmentservice.entity;

import com.aiomed.treatmentservice.enumeration.TreatmentAction;
import com.aiomed.treatmentservice.enumeration.TreatmentPlanStatus;
import com.aiomed.treatmentservice.enumeration.TreatmentTaskStatus;
import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "treatment_plan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID id;

    @Column(name = "treatment_action", nullable = false)
    @Enumerated(value = STRING)
    private TreatmentAction treatmentAction;

    @Column(name = "subject_patient", nullable = false)
    private String subjectPatient;

    /**
     * The date and time when treatment plan is started
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * The date and time when treatment plan is finished
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "recurrence_pattern", nullable = false)
    private String recurrencePattern;

    /**
     * The status of treatment plan
     * - CREATED - means the corresponding treatment tasks are not created yet
     * - ACTIVE - means the corresponding treatment task have been alredy scheduled (created in data-base)
     */
    @Column(name = "status", nullable = false)
    @Enumerated(value = STRING)
    private TreatmentPlanStatus status;

    /**
     * Relation to the list of child tasks.
     * The list contains treatment tasks that were created for every date between start_date and end_date:
     * ( date >= start-date && date <= end_date )
     */
    @OneToMany(cascade = REMOVE, fetch = LAZY)
    @OrderBy("startDate ASC")
    private List<TreatmentTask> tasks;

}
