package br.com.wepdev.curso.controller;

import br.com.wepdev.curso.dto.CourseDTO;
import br.com.wepdev.curso.model.CourseModel;
import br.com.wepdev.curso.service.CourseService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;


    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO){
        var courseModel = new CourseModel();

        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }


    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId")UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
    }


    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId")UUID courseId,
                                               @RequestBody @Valid CourseDTO courseDTO){

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        var courseModel = courseModelOptional.get();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageUrl(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseDTO.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }


//    @GetMapping
//    public ResponseEntity<List<CourseModel>> getAllCourses(){
//        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll());
//    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable){
//        Page<CourseModel> courseModelPage = courseService.findAll(spec, pageable);
//
//        if(!courseModelPage.isEmpty()) {
//            for (CourseModel course : courseModelPage.toList()) { // Acessando cada elemento dessa lista
//
//                // hateoas => Construindo o link de navegação para cada usuario que existir nessa lista
//                course.add(linkTo(methodOn(UserController.class).getOneUser(course.getUserId())).withSelfRel());
//            }
//        }
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional);
    }
}
