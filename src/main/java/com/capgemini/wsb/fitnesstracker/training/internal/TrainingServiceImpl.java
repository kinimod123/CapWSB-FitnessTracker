package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingApplication;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TrainingServiceImpl implements TrainingProvider {

    @Autowired
    private final TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getTraining(final Long trainingId) {
        throw new UnsupportedOperationException("Not finished yet");
    }

    @Override
    public void deleteTrainingByUserId(Long userId) {
        Optional<Training> first = trainingRepository.findAll().stream()
                .filter(training -> Objects.equals(training.getUser().getId(), userId))
                .findFirst();
        first.ifPresent(trainingRepository::delete);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getAllTrainingsForUser(Long userId) {
        return trainingRepository.findAllByUserId(userId);
    }

    @Override
    public List<Training> getAllFinishedTrainingsAfter(LocalDateTime afterTime) {
        return trainingRepository.findAllByEndTimeAfter(afterTime);
    }

    @Override
    public List<Training> getAllTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findAllByActivityType(activityType);
    }

    @Override
    public Training createTraining(TrainingApplication trainingApplication) {
        User user = userRepository.findById(trainingApplication.userId())
                .orElseThrow(() -> new UserNotFoundException(trainingApplication.userId()));

        Training training = new Training(
                user,
                trainingApplication.startTime(),
                trainingApplication.endTime(),
                trainingApplication.activityType(),
                trainingApplication.distance(),
                trainingApplication.averageSpeed()
        );

        return trainingRepository.save(training);
    }

    @Override
    public Training updateTraining(Long trainingId, Training updatedTraining) {
        Optional<Training> existingTrainingOpt = trainingRepository.findById(trainingId);
        if (existingTrainingOpt.isPresent()) {
            Training existingTraining = existingTrainingOpt.get();
            existingTraining.setStartTime(updatedTraining.getStartTime());
            existingTraining.setEndTime(updatedTraining.getEndTime());
            existingTraining.setActivityType(updatedTraining.getActivityType());
            existingTraining.setDistance(updatedTraining.getDistance());
            existingTraining.setAverageSpeed(updatedTraining.getAverageSpeed());
            return trainingRepository.save(existingTraining);
        } else {
            throw new TrainingNotFoundException(trainingId);
        }
    }

}
