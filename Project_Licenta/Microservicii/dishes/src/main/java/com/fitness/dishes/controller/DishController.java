package com.fitness.dishes.controller;

import com.fitness.dishes.dto.DishDTO;
import com.fitness.dishes.hateoas.DishHateoas;
import com.fitness.dishes.service.IDishService;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fitness/dishes")
public class DishController {

    private final IDishService dishService;

    @Autowired
    public DishController(IDishService dishService) {
        this.dishService = dishService;
    }

    private Map<String, List<Link>> generateNotFoundResponse() {
        Map<String, List<Link>> links = new HashMap<>();
        List<Link> arrayList = new ArrayList<>();
        Link parentLink = linkTo(methodOn(DishController.class).getAll()).withRel("parent");
        arrayList.add(parentLink);
        links.put("_links", new ArrayList<>(arrayList));
        return links;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<DishDTO>>> getAll() {
        try {
            List<DishDTO> list = dishService.getAll();
            List<EntityModel<DishDTO>> dishEntities = new ArrayList<>();
            for (DishDTO dsh : list) {
                dishEntities.add(new DishHateoas().toModel(dsh));
            }

            return new ResponseEntity<>(dishEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "_id")
    public ResponseEntity<?> getDishById(@RequestParam int _id) {
        try {
            Optional<DishDTO> dishes = dishService.getById(_id);

            if (dishes.isPresent()) {
                EntityModel<DishDTO> dishEntity = new DishHateoas().toModel(dishes.get());
                return new ResponseEntity<>(dishEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "name")
    public ResponseEntity<?> getDishByName(@RequestParam String name) {
        try {
            List<DishDTO> dishes = dishService.getByName(name);

            if (!dishes.isEmpty()) {
                List<EntityModel<DishDTO>> dishEntities = new ArrayList<>();

                for (DishDTO dish : dishes) {
                    dishEntities.add(new DishHateoas().toModel(dish));
                }

                return new ResponseEntity<>(dishEntities, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "difficulty")
    public ResponseEntity<?> getDishesByDifficulty(@RequestParam DishDTO.Difficulty difficulty) {
        try {
            List<DishDTO> dishes = dishService.getByDifficulty(difficulty);

            if (!dishes.isEmpty()) {
                List<EntityModel<DishDTO>> dishEntities = new ArrayList<>();

                for (DishDTO dish : dishes) {
                    dishEntities.add(new DishHateoas().toModel(dish));
                }

                return new ResponseEntity<>(dishEntities, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "type")
    public ResponseEntity<?> getDishesByType(@RequestParam DishDTO.Type type) {
        try {
            List<DishDTO> dishes = dishService.getByType(type);

            if (!dishes.isEmpty()) {
                List<EntityModel<DishDTO>> dishEntities = new ArrayList<>();

                for (DishDTO dish : dishes) {
                    dishEntities.add(new DishHateoas().toModel(dish));
                }

                return new ResponseEntity<>(dishEntities, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createDish(@RequestBody DishDTO dishDTO) {
        try {
            DishDTO createdDish = dishService.createDish(dishDTO);
            return new ResponseEntity<>(createdDish, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }


    @PutMapping("/{_id}")
    public ResponseEntity<?> updateDish(@PathVariable int _id, @RequestBody DishDTO updatedDishDTO) {
        try {
            dishService.updateDish(_id, updatedDishDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }


    @DeleteMapping("/{_id}")
    public ResponseEntity<Void> deleteDIsh(@PathVariable("_id") int _id) {
        try {
            dishService.deleteById(_id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //@RequestParam Map<String, String> queryParams
    @GetMapping("/generate_recipe")
    public ResponseEntity<byte[]> generateReport(@RequestParam Map<String, String> queryParams) {
        try {

            System.out.println("Download Recipe!");

            String ingredients = URLDecoder.decode(queryParams.getOrDefault("ingredients", "Default ingredients"), StandardCharsets.UTF_8.name());
            String additionalPreparations = URLDecoder.decode(queryParams.getOrDefault("additional_preparations", "Default method"), StandardCharsets.UTF_8.name());
            String method = URLDecoder.decode(queryParams.getOrDefault("method", "Default method"), StandardCharsets.UTF_8.name());


            InputStream reportStream = getClass().getResourceAsStream("/recipe_report.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("Could not find recipe_report.jrxml");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Parameters for report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ingredients", ingredients);
            parameters.put("additional_preparations", additionalPreparations);
            parameters.put("method", method);
            //parameters.put("ingredients", "200g pearl couscous\n100g radishes, sliced\n1 cucumber, seeds scraped out and sliced into half-moons\n1 green apple, thinly sliced into matchsticks\n150g hot smoked trout or salmon, flaked into chunks");
            //parameters.put("method", "Step 1: Cook the pearl couscous following pack instructions, then drain well and rinse with cold water. Dry the pan, then mix all the dressing ingredients in it and season well. Tip in the prepared veg and apple along with the couscous. Mix well and check for seasoning.\nStep 2: Spread out on a platter and flake the smoked trout over the top. Will keep chilled for up to two days.");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Convert JasperPrint to byte array for response
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Prepare response with PDF content
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recipe_report.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
