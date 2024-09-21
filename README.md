## Treatments sheduler (Test task)


1. Following logic has been implemented in service `com.aiomed.treatmentservice.service.TreatmentTaskSchedulerService`
- Finds TreatmentPlan records with CREATED status in database
- The TreatmentTask will be created and persisted to DB for every date in the range between the effective date of the plan and its termination 
- When the latest TreatmentTask record of particular TreatmentPlan will be created, the TreatmentPlan status will be immediately changed from CREATED value to ACTIVE

2. Following tables have been created `treatment_plan` and `treatment_task`

3. Required JPA entities `TreatmentTask`, `TreatmentPlan` and repositories `TreatmentPlanRepository`, `TreatmentTaskRepository` have been implemented

4. Implemented a test  `TaskSchedulerServerTest` where described above logic is validating.

