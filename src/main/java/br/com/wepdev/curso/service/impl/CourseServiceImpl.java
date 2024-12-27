package br.com.wepdev.curso.service.impl;

import br.com.wepdev.curso.model.CourseModel;
import br.com.wepdev.curso.model.LessonModel;
import br.com.wepdev.curso.model.ModuleModel;
import br.com.wepdev.curso.repository.CourseRepository;
import br.com.wepdev.curso.repository.LessonRepository;
import br.com.wepdev.curso.repository.ModuleRepository;
import br.com.wepdev.curso.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;


    @Transactional // O metodo esta sendo gerenciado por uma transacao
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelsList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if(!moduleModelsList.isEmpty()){
            for (ModuleModel module : moduleModelsList){
                List<LessonModel> lessonModelsList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if(!lessonModelsList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelsList);
                }
            }
            moduleRepository.deleteAll(moduleModelsList);
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
