package br.com.wepdev.curso.service;

import br.com.wepdev.curso.model.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void delete (ModuleModel moduleModel);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

//    List<ModuleModel> findAll();

//    List<ModuleModel> findAllByCourse(UUID courseId);

    Page<ModuleModel> findAllModulesByCourseSpec(Specification<ModuleModel> spec, Pageable pageable);

    Optional<ModuleModel> findById(UUID moduleId);
}
