package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private AccountService accountService;

    public StudentDTO mapAccount(Account account) {
        StudentDTO studentDTO = mapper.map(account, StudentDTO.class);
        return studentDTO;
    }

    public ClassroomDTO mapClassroom(Classroom classroom) {
        ClassroomDTO classroomDTO = mapper.map(classroom, ClassroomDTO.class);
        if (classroom.getTeacherId() == null) {
            classroomDTO.setTeacherName("None");
        } else {
            Teacher teacher = teacherService.findById(classroom.getTeacherId());
            TeacherDTO teacherDTO = mapTeacher(teacher);
            classroomDTO.setTeacherName(teacherDTO.getFirstName() + ' ' + teacherDTO.getLastName());
        }
        if (classroom.getSubjectId() == null) {
            classroomDTO.setSubjectName("None");
        } else {
            Subject subject = subjectService.findById(classroom.getSubjectId());
            classroomDTO.setSubjectName(subject.getSubjectName());
        }
        return classroomDTO;
    }

    public TeacherDTO mapTeacher(Teacher teacher) {
        Account account = accountService.findById(teacher.getAccountId());
        TeacherDTO teacherDTO = mapper.map(account, TeacherDTO.class);
        return teacherDTO;
    }


}
