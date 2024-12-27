package br.com.wepdev.curso.controller;

import br.com.wepdev.curso.dto.ModuleDTO;
import br.com.wepdev.curso.model.CourseModel;
import br.com.wepdev.curso.model.ModuleModel;
import br.com.wepdev.curso.service.CourseService;
import br.com.wepdev.curso.service.ModuleService;
import br.com.wepdev.curso.specification.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;


    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@RequestBody @Valid ModuleDTO moduleDTO, @PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> moduleModelOptional = courseService.findById(courseId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        var moduleModel = new ModuleModel();

        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(moduleModelOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }


    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "courseId")UUID courseId){

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found for this course");
        }
        moduleService.delete(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully");
    }


    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId")UUID courseId,
                                               @PathVariable(value = "moduleId")UUID moduleId,
                                               @RequestBody @Valid ModuleDTO moduleDTO){

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found for this course");
        }
        var moduleModel = moduleModelOptional.get();
        moduleModel.setTitle(moduleDTO.getTitle());
        moduleModel.setDescription(moduleDTO.getDescription());

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }


    /**
     * Forma avançada de implementar o specification
     * @param courseId
     * @param spec
     * @param pageable
     * @return
     */
    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId")UUID courseId,
                                                           SpecificationTemplate.ModuleSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllModulesByCourseSpec(
                SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }


    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId){

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found for this course");
        }
        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional);
    }
}
