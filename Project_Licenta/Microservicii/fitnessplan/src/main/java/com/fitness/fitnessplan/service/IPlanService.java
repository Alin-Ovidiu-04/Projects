package com.fitness.fitnessplan.service;

import com.fitness.fitnessplan.dto.PlanDTO;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

public interface IPlanService {

    List<PlanDTO> getAll();
    Optional<PlanDTO> getById(ObjectId _id);

    List<PlanDTO> getByIdUser(Long id_user);

    PlanDTO createPlan(PlanDTO plan);

    PlanDTO updatePlan(ObjectId _id, PlanDTO updatedPlanDTO) throws ChangeSetPersister.NotFoundException;

    void deleteById(ObjectId _id) throws ChangeSetPersister.NotFoundException;

    List<PlanDTO> getByDifficulty(PlanDTO.Difficulty difficulty);

}
