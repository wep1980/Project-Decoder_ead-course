package br.com.wepdev.curso.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)// Oculta valores nulo na serializacao
@Data
@Entity
@Table(name = "TB_COURSES_USERS")
public class CourseUserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Um usuario pode estar cadastrado em varios cursos
    private CourseModel course;

    @Column(nullable = false)
    private UUID userId; // Esse valor vai vim do microservice de User
}
