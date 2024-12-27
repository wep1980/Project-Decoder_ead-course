package br.com.wepdev.curso.specification;

import br.com.wepdev.curso.model.CourseModel;
import br.com.wepdev.curso.model.LessonModel;
import br.com.wepdev.curso.model.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "courseStatus", spec = Equal.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}


    @And({
            @Spec(path = "title", spec = LikeIgnoreCase.class)
    })
    public interface ModuleSpec extends Specification<ModuleModel> {}


    @And({
            @Spec(path = "title", spec = LikeIgnoreCase.class)
    })
    public interface LessonSpec extends Specification<LessonModel> {}


    /**
     * Pesquisando uma lista de modulos de acordo com o ID do curso informado
     * @param courseId
     * @return
     */
    public static Specification<ModuleModel> moduleCourseId(final UUID courseId){

        // Estrutura lambda, utilizando o criteria builder e query
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // elimina os dados duplicados da query
            Root<ModuleModel> module = root; // Entidade que faz parte desta consulta
            Root<CourseModel> cource = query.from(CourseModel.class); // Entidade que faz parte desta consulta
            Expression<Collection<ModuleModel>> courseModules = cource.get("modules"); // Extraindo a colecao de modulos que esta dentro de cursos
            // executa a query
            return criteriaBuilder.and(criteriaBuilder.equal(cource.get("courseId"), courseId), criteriaBuilder.isMember(module, courseModules));
        };
    }


    /**
     * Pesquisando uma lista de lessons de acordo com o ID do modulo informado
     */
    public static Specification<LessonModel> lessonModuleId(final UUID moduleId){

        // Estrutura lambda, utilizando o criteria builder e query
        return (root, query, criteriaBuilder) -> {
            query.distinct(true); // elimina os dados duplicados da query
            Root<LessonModel> lesson = root; // Entidade que faz parte desta consulta
            Root<ModuleModel> module = query.from(ModuleModel.class); // Entidade que faz parte desta consulta
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons"); // Extraindo a colecao de modulos que esta dentro de cursos
            // executa a query
            return criteriaBuilder.and(criteriaBuilder.equal(module.get("moduleId"), moduleId), criteriaBuilder.isMember(lesson, moduleLessons));
        };
    }
}
