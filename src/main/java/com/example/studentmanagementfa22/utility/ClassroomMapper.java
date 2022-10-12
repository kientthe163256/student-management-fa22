package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.service.impl.TeacherServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ClassroomMapper implements IGenericMapper<Classroom, ClassroomDTO> {
    @Autowired
    protected TeacherService teacherService;

    @Autowired
    protected SubjectService subjectService;

    @Override
    @Mapping(source = "teacherId", target = "teacher", qualifiedByName = "mapTeacherById")
    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "mapSubjectById")
    public abstract ClassroomDTO toDTO(Classroom source);

    @Named("mapTeacherById")
    Teacher mapTeacherById(Integer teacherId) {
        //Class without teacher
        if (teacherId == null){
            return null;
        }
        return teacherService.findById(teacherId);
    }

    @Named("mapSubjectById")
    Subject mapSubjectById(Integer subjectId) {
        //Class without subject
        if (subjectId == null){
            return null;
        }
        return subjectService.findById(subjectId);
    }
}
