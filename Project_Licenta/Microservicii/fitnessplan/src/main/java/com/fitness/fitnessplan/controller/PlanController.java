package com.fitness.fitnessplan.controller;

import com.fitness.fitnessplan.dto.PlanDTO;
import com.fitness.fitnessplan.hateoas.PlanHateoas;
import com.fitness.fitnessplan.service.IPlanService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fitness/plan")
public class PlanController {

    private final IPlanService planService;

    @Autowired
    public PlanController(IPlanService planService) {
        this.planService = planService;
    }

    private Map<String, List<Link>> generateNotFoundResponse() {
        Map<String, List<Link>> links = new HashMap<>();
        List<Link> arrayList = new ArrayList<>();
        Link parentLink = linkTo(methodOn(PlanController.class).getAll()).withRel("parent");
        arrayList.add(parentLink);
        links.put("_links", new ArrayList<>(arrayList));
        return links;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<PlanDTO>>> getAll() {
        try {
            List<PlanDTO> list = planService.getAll();
            List<EntityModel<PlanDTO>> planEntities = new ArrayList<>();
            for (PlanDTO plan : list) {
                planEntities.add(new PlanHateoas().toModel(plan));
            }

            return new ResponseEntity<>(planEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "_id")
    public ResponseEntity<?> getPlanById(@RequestParam ObjectId _id) {
        try {
            Optional<PlanDTO> plans = planService.getById(_id);

            if (plans.isPresent()) {
                EntityModel<PlanDTO> planEntity = new PlanHateoas().toModel(plans.get());
                return new ResponseEntity<>(planEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "id_user")
    public ResponseEntity<?> getPlanByIdUser(@RequestParam Long id_user) {
        try {
            System.out.println("Get plan for user id = " + id_user);

            List<PlanDTO> plans = planService.getByIdUser(id_user);
            if (!plans.isEmpty()) {
                List<EntityModel<PlanDTO>> planEntities = new ArrayList<>();

                for (PlanDTO plan : plans) {
                    planEntities.add(new PlanHateoas().toModel(plan));
                }

                return new ResponseEntity<>(planEntities, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "difficulty")
    public ResponseEntity<?> getPlansByDifficulty(@RequestParam PlanDTO.Difficulty difficulty) {
        try {
            List<PlanDTO> plans = planService.getByDifficulty(difficulty);

            if (!plans.isEmpty()) {
                List<EntityModel<PlanDTO>> planEntities = new ArrayList<>();

                for (PlanDTO plan : plans) {
                    planEntities.add(new PlanHateoas().toModel(plan));
                }

                return new ResponseEntity<>(planEntities, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }



    @PostMapping("/{id_user}")
    public ResponseEntity<?> createPlan(@RequestBody Long planDTO, @PathVariable Long id_user) {
        try {
            System.out.println("Set a plan for user id = " + id_user);

            PlanDTO planTest = createTestPlan(id_user);
            PlanDTO createdPlan = planService.createPlan(planTest);
            return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    private PlanDTO createTestPlan(Long id_user) {
        Map<Integer, List<Integer>> exercises = new HashMap<>();
        exercises.put(1, Arrays.asList(1, 4, 5));
        exercises.put(2, Arrays.asList(3, 8, 9, 2, 7));
        exercises.put(3, Arrays.asList(6, 10, 11));
        exercises.put(4, Arrays.asList(12, 13, 14, 15));
        exercises.put(5, Arrays.asList(1, 3, 5, 7));
        exercises.put(6, Arrays.asList(2, 4, 6, 8));
        exercises.put(7, Arrays.asList(9, 10, 11, 12));
        exercises.put(8, Arrays.asList(13, 14, 15, 1));
        exercises.put(9, Arrays.asList(2, 3, 4, 5));
        exercises.put(10, Arrays.asList(6, 7, 8, 9));
        exercises.put(11, Arrays.asList(10, 11, 12, 13));
        exercises.put(12, Arrays.asList(14, 15, 1, 2));
        exercises.put(13, Arrays.asList(3, 4, 5, 6));
        exercises.put(14, Arrays.asList(7, 8, 9, 10));
        exercises.put(15, Arrays.asList(11, 12, 13, 14));
        exercises.put(16, Arrays.asList(15, 1, 2, 3));
        exercises.put(17, Arrays.asList(4, 5, 6, 7));
        exercises.put(18, Arrays.asList(8, 9, 10, 11));
        exercises.put(19, Arrays.asList(12, 13, 14, 15));
        exercises.put(20, Arrays.asList(1, 2, 3, 4));
        exercises.put(21, Arrays.asList(5, 6, 7, 8));
        exercises.put(22, Arrays.asList(9, 10, 11, 12));
        exercises.put(23, Arrays.asList(13, 14, 15, 1));
        exercises.put(24, Arrays.asList(2, 3, 4, 5));
        exercises.put(25, Arrays.asList(6, 7, 8, 9));
        exercises.put(26, Arrays.asList(10, 11, 12, 13));
        exercises.put(27, Arrays.asList(14, 15, 1, 2));
        exercises.put(28, Arrays.asList(3, 4, 5, 6));
        exercises.put(29, Arrays.asList(7, 8, 9, 10));
        exercises.put(30, Arrays.asList(11, 12, 13, 14));

        Map<Integer, Map<String, Integer>> dishes = new HashMap<>();
        dishes.put(1, Map.of("breakfast", 12, "lunch", 54, "dinner", 34));
        dishes.put(2, Map.of("breakfast", 23, "lunch", 12, "dinner", 45));
        dishes.put(3, Map.of("breakfast", 34, "lunch", 23, "dinner", 56));
        dishes.put(4, Map.of("breakfast", 45, "lunch", 34, "dinner", 67));
        dishes.put(5, Map.of("breakfast", 56, "lunch", 45, "dinner", 78));
        dishes.put(6, Map.of("breakfast", 67, "lunch", 56, "dinner", 89));
        dishes.put(7, Map.of("breakfast", 78, "lunch", 67, "dinner", 12));
        dishes.put(8, Map.of("breakfast", 89, "lunch", 78, "dinner", 23));
        dishes.put(9, Map.of("breakfast", 90, "lunch", 89, "dinner", 34));
        dishes.put(10, Map.of("breakfast", 21, "lunch", 90, "dinner", 45));
        dishes.put(11, Map.of("breakfast", 32, "lunch", 21, "dinner", 56));
        dishes.put(12, Map.of("breakfast", 43, "lunch", 32, "dinner", 67));
        dishes.put(13, Map.of("breakfast", 54, "lunch", 43, "dinner", 78));
        dishes.put(14, Map.of("breakfast", 65, "lunch", 54, "dinner", 89));
        dishes.put(15, Map.of("breakfast", 76, "lunch", 65, "dinner", 90));
        dishes.put(16, Map.of("breakfast", 87, "lunch", 76, "dinner", 21));
        dishes.put(17, Map.of("breakfast", 98, "lunch", 87, "dinner", 32));
        dishes.put(18, Map.of("breakfast", 12, "lunch", 98, "dinner", 43));
        dishes.put(19, Map.of("breakfast", 23, "lunch", 12, "dinner", 54));
        dishes.put(20, Map.of("breakfast", 34, "lunch", 23, "dinner", 65));
        dishes.put(21, Map.of("breakfast", 45, "lunch", 34, "dinner", 76));
        dishes.put(22, Map.of("breakfast", 56, "lunch", 45, "dinner", 87));
        dishes.put(23, Map.of("breakfast", 67, "lunch", 56, "dinner", 98));
        dishes.put(24, Map.of("breakfast", 78, "lunch", 67, "dinner", 12));
        dishes.put(25, Map.of("breakfast", 89, "lunch", 78, "dinner", 23));
        dishes.put(26, Map.of("breakfast", 90, "lunch", 89, "dinner", 34));
        dishes.put(27, Map.of("breakfast", 21, "lunch", 90, "dinner", 45));
        dishes.put(28, Map.of("breakfast", 32, "lunch", 21, "dinner", 56));
        dishes.put(29, Map.of("breakfast", 43, "lunch", 32, "dinner", 67));
        dishes.put(30, Map.of("breakfast", 54, "lunch", 43, "dinner", 78));

        return new PlanDTO(null, id_user, exercises, dishes, 60,PlanDTO.Difficulty.medium);
    }



    @PutMapping("/{_id}")
    public ResponseEntity<?> updatePlan(@PathVariable ObjectId _id, @RequestBody PlanDTO updatedPlanDTO) {
        try {
            planService.updatePlan(_id, updatedPlanDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }


    @DeleteMapping("/{_id}")
    public ResponseEntity<Void> deletePlan(@PathVariable("_id") ObjectId _id) {
        try {
            planService.deleteById(_id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}

