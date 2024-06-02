package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
public class TrainingOperator {

    private final TrainingServiceImpl trainingService;

    @Autowired
    public TrainingOperator(TrainingServiceImpl trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingService.getAllTrainings();
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Training>> getAllTrainingsForUser(@PathVariable Long userId) {
        List<Training> userTrainings = trainingService.getAllTrainingsForUser(userId);
        return ResponseEntity.ok(userTrainings);
    }

    @GetMapping("/finished/{afterTime}")
    public ResponseEntity<List<Training>> getAllFinishedTrainingsAfter(@PathVariable String afterTime) {
        LocalDateTime afterDateTime = LocalDate.parse(afterTime).atStartOfDay();
        List<Training> finishedTrainings = trainingService.getAllFinishedTrainingsAfter(afterDateTime);
        return ResponseEntity.ok(finishedTrainings);
    }

    @GetMapping("/activityType")
    public ResponseEntity<List<Training>> getAllTrainingByActivityType(@RequestParam ActivityType activityType) {
        List<Training> trainingsByType = trainingService.getAllTrainingsByActivityType(activityType);
        return ResponseEntity.ok(trainingsByType);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Training createTraining(@RequestBody TrainingApplication trainingApplication) {
        return trainingService.createTraining(trainingApplication);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<Training> updateTraining(@PathVariable Long trainingId, @RequestBody Training training) {
        Training updatedTraining = trainingService.updateTraining(trainingId, training);
        return ResponseEntity.ok(updatedTraining);
    }
}
