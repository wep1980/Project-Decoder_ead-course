package br.com.wepdev.curso.service.impl;

import br.com.wepdev.curso.repository.CourseUserRepository;
import br.com.wepdev.curso.service.CourseUserService;
import org.springframework.stereotype.Service;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    final
    CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }
}
