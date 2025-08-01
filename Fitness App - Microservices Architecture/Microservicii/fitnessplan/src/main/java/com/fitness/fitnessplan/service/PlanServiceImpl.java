package com.fitness.fitnessplan.service;

import com.fitness.fitnessplan.dto.PlanDTO;
import com.fitness.fitnessplan.repository.PlanRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements IPlanService {


    private final PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<PlanDTO> getAll() {
        return planRepository.findAll();
    }

    @Override
    public Optional<PlanDTO> getById(ObjectId _id) {
        return planRepository.getById(_id);
    }

    @Override
    public List<PlanDTO> getByIdUser(Long id_user) {
        return planRepository.getByIdUser(id_user);
    }

    @Override
    public PlanDTO createPlan(PlanDTO plan) {
        return planRepository.save(plan);
    }

    @Override
    public PlanDTO updatePlan(ObjectId _id, PlanDTO updatedPlanDTO) throws ChangeSetPersister.NotFoundException {
        Optional<PlanDTO> existingPlan = planRepository.getById(_id);

        if (existingPlan.isPresent()) {
            PlanDTO updatedPlan = existingPlan.get();
            updatedPlan.setId_user(updatedPlanDTO.getId_user());
            updatedPlan.setExercises(updatedPlanDTO.getExercises());
            updatedPlan.setTraining_time(updatedPlanDTO.getTraining_time());
            updatedPlan.setPlan_difficulty((updatedPlanDTO.getPlan_difficulty()));

            planRepository.save(updatedPlan);
            return updatedPlan;
        }
        else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Override
    public void deleteById(ObjectId _id) throws ChangeSetPersister.NotFoundException {
        Optional<PlanDTO> plan = planRepository.getById(_id);
        if (plan.isPresent()) {
            planRepository.deleteById(_id);
        } else {
            throw  new ChangeSetPersister.NotFoundException();
        }
    }


    @Override
    public List<PlanDTO> getByDifficulty(PlanDTO.Difficulty difficulty) {
        return planRepository.getByDifficulty(difficulty);
    }

}
