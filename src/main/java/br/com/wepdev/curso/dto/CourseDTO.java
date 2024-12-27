package br.com.wepdev.curso.dto;

import br.com.wepdev.curso.enums.CourseLevel;
import br.com.wepdev.curso.enums.CourseStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CourseDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String imageUrl;

    @NotNull
    private CourseStatus courseStatus;

    @NotNull
    private UUID userInstructor;

    @NotNull
    private CourseLevel courseLevel;
}
