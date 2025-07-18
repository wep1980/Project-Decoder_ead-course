package br.com.wepdev.curso.repository;

import br.com.wepdev.curso.model.CourseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {
}
