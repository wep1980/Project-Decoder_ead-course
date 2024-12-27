package br.com.wepdev.curso.repository;

import br.com.wepdev.curso.model.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

//    /**
//     * Dessa forma em consigo alterar em tempo de execução a forma de carregamento entre LAZY E EAGER,
//     * nessa consulta especifica ele traz tb os cursos que estao como LAZY
//     * @param title
//     * @return
//     */
//    @EntityGraph(attributePaths = {"course"})
//    ModuleModel findByTitle(String title);

    // Consulta para buscar os modulos presentes dentro de um curso
    @Query(value = "select * from tb_module where course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "select * from tb_module where course_course_id = :courseId and module_id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId") UUID moduleId);
}
