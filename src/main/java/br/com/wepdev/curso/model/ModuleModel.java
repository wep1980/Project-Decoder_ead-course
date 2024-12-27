package br.com.wepdev.curso.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)// Oculta valores nulo na serializacao
@Data
@Entity
@Table(name = "TB_MODULE")
public class ModuleModel implements Serializable {
    private static final long serialVersionUID = 835246680973786881L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID moduleId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 250)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;


    // Se em algum momento for necessario mudar o tipo de carregamento de LAZY para EAGER em tempo de execução, pode ser implementada uma consulta no repository que utilize o  @EntityGraph
    // FetchType.LAZY -> forma com que os dados vão ser carregados, esse carregamento so e feito qnd e necessario(carregamento lento)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // em consultas esse campo nao sera mostrado, somente em atualizações, e novas criações
    @ManyToOne(optional = false, fetch = FetchType.LAZY) // um modulo tem sempre que ser associado a um curso
    private CourseModel course; // Muitos modulos para 1 curso

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // em consultas esse campo nao sera mostrado, somente em atualizações, e novas criações
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)// dessa forma e feita 2 consultas, 1 para cursos e 1 para buscar todos os modulos
    private Set<LessonModel> lessons;

}
