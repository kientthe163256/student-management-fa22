package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.service.SubjectService;
import com.example.studentmanagementfa22.repository.service.TeacherService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ClassroomMapper implements IGenericMapper<Classroom, ClassroomDTO> {


    @Override
    public abstract ClassroomDTO mapToDTO(Classroom source);

}
