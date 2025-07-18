package br.com.wepdev.curso.model;

import br.com.wepdev.curso.enums.CourseLevel;
import br.com.wepdev.curso.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)// Oculta valores nulo na serializacao
@Data
@Entity
@Table(name = "TB_COURSE")
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1797403939943885600L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 250)
    private String description;

    @Column
    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Ao comentar essa linha pode se utilizar a classe de config global de data e hora utc
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Ao comentar essa linha pode se utilizar a classe de config global de data e hora utc
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus courseStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseLevel courseLevel;

    @Column(nullable = false)
    private UUID userInstructor;

    //@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)// cascade = CascadeType.ALL, orphanRemoval = true -> // Ao deletar um curso os modulos tb serao, incluse os modulos que n possuem cursos
    //@OnDelete(action = OnDeleteAction.CASCADE)// sempre um curso for deletado o banco de dados vai buscar os modulos associados e deleta los
    // LAZY -> forma com que os dados vão ser carregados, esse carregamento so e feito qnd e necessario(carregamento lento)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // em consultas esse campo nao sera mostrado, somente em atualizações
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT) // dessa forma e feita 2 consultas, 1 para cursos e 1 para buscar todos os modulos
    private Set<ModuleModel> modules; // Um curso tem varios modulos. set<> -> o set nao e ordenado nao permite duplicados

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course",  fetch = FetchType.LAZY)
    private Set<CourseUserModel> courseUsers;

}
