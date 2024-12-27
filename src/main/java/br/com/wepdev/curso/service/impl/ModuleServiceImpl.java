package br.com.wepdev.curso.service.impl;

import br.com.wepdev.curso.model.LessonModel;
import br.com.wepdev.curso.model.ModuleModel;
import br.com.wepdev.curso.repository.LessonRepository;
import br.com.wepdev.curso.repository.ModuleRepository;
import br.com.wepdev.curso.service.ModuleService;
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
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;


    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessonModelsList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
        if(!lessonModelsList.isEmpty()){
            lessonRepository.deleteAll(lessonModelsList);
        }
        moduleRepository.delete(moduleModel);
    }

    @Override
    public ModuleModel save(ModuleModel moduleModel) {
        return moduleRepository.save(moduleModel);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findModuleIntoCourse(courseId, moduleId);
    }

    @Override
    public Page<ModuleModel> findAllModulesByCourseSpec(Specification<ModuleModel> spec, Pageable pageable) {
        return moduleRepository.findAll(spec, pageable);
    }

//    @Override
//    public List<ModuleModel> findAllByCourse(UUID courseId) {
//        return moduleRepository.findAllModulesIntoCourse(courseId);
//    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {
        return moduleRepository.findById(moduleId);
    }

//    @Override
//    public List<ModuleModel> findAll() {
//        return null;
//    }
}
