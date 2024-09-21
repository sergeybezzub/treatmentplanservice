package com.aiomed.treatmentservice.entity;

import com.aiomed.treatmentservice.enumeration.TreatmentAction;
import com.aiomed.treatmentservice.enumeration.TreatmentTaskStatus;
import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "treatment_task")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class TreatmentTask {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID id;

    @Column(name = "treatment_action", nullable = false)
    @Enumerated(value = STRING)
    private TreatmentAction treatmentAction;

    @Column(name = "subject_patient", nullable = false)
    private String subjectPatient;

    /**
     * Date and time when treatment task action should be performed
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * Status of treatment task
     * - CREATED means not performed yet
     * - COMPLETED - means has already performed
     */
    @Column(name = "status", nullable = false)
    @Enumerated(value = STRING)
    private TreatmentTaskStatus status;

    /**
     * Relation to the parent plan
     */
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private TreatmentPlan treatmentPlan;

}
