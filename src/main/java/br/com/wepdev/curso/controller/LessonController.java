package br.com.wepdev.curso.controller;

import br.com.wepdev.curso.dto.LessonDTO;
import br.com.wepdev.curso.model.LessonModel;
import br.com.wepdev.curso.model.ModuleModel;
import br.com.wepdev.curso.service.CourseService;
import br.com.wepdev.curso.service.LessonService;
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
public class LessonController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;



    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@RequestBody @Valid LessonDTO lessonDTO, @PathVariable(value = "moduleId") UUID moduleId){

        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var lessonModel = new LessonModel();

        BeanUtils.copyProperties(lessonDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }


    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId){

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this module");
        }
        lessonService.delete(lessonModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully");
    }


    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId")UUID moduleId,
                                               @PathVariable(value = "lessonId")UUID lessonId,
                                               @RequestBody @Valid LessonDTO lessonDTO){

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this module");
        }
        var lessonModel = lessonModelOptional.get();
        lessonModel.setTitle(lessonDTO.getTitle());
        lessonModel.setDescription(lessonDTO.getDescription());
        lessonModel.setVideoUrl(lessonDTO.getVideoUrl());

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
    }


    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId")UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllLessonsByModuleSpec( SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }


    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson Not Found for this module");
        }
        return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional);
    }
}
