package br.com.wepdev.curso.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)// Oculta valores nulo na serializacao
@Data
@Entity
@Table(name = "TB_LESSON")
public class LessonModel implements Serializable {
    private static final long serialVersionUID = -343473173279671998L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID lessonId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false)
    private String videoUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    // FetchType.LAZY -> forma com que os dados vão ser carregados, esse carregamento so e feito qnd e necessario(carregamento lento)
    @ManyToOne(optional = false, fetch = FetchType.LAZY) // uma lesson tem sempre que ser associado a um modulo
    @JsonIgnore // Ao Salvar ou buscar lesson os modulos não aparecem na representacao
    private ModuleModel module;

}
