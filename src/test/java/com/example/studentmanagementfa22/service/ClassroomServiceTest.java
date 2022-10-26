package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.ClassroomServiceImpl;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private IGenericMapper<Classroom, ClassroomDTO> classroomMapper;
    @InjectMocks
    private ClassroomServiceImpl classroomService;


    @Test
    public void checkExistedClassroom(){
        boolean classExisted = classroomService.classroomExisted("SE1605");
        Assert.isTrue(classExisted);
    }

    @Test
    public void getAllClassroom(){
        List<ClassroomDTO> classroomDTOList = classroomService.getAllClassrooms();
        Assert.notNull(classroomDTOList);
    }

    @Test
    public void getAllClassroomPaging(){
        Page<ClassroomDTO> classroomDTO = classroomService.getAllClassroomsPaging(1);
        Assert.notNull(classroomDTO);
    }
    @Test
    public void getAllRegisteredClass() {
        Page<ClassroomDTO> classroomDTO = classroomService.getAllRegisteredClass(2, 1);
        Assert.notNull(classroomDTO);
    }

    @Test
    public void getAllAvailClassroom() {
        // 1. Create mock data
        Classroom mockClassroom = Classroom.builder()
                .id(5)
                .classroomName("mock class")
                .subjectId(1)
                .build();
        ClassroomDTO mockClassroomDTO = ClassroomDTO.builder()
                .id(5)
                .classroomName("mock class")
                .build();
        List<Classroom> classroomList = new ArrayList<>();
        classroomList.add(mockClassroom);
        Page<Classroom> mockPageClassroom = new PageImpl<>(classroomList) ;
        PageRequest pageRequest = PageRequest.of(1, 5);
        // 2. define behavior of Repository, Mapper
        when(classroomRepository.findAllAvailClassroom(pageRequest, 1)).thenReturn(mockPageClassroom);
        // 3.call service method
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllAvailClassroom(1,1);
        //4 assert result
        assertNotNull(classroomDTOPage);
    }
    @Test
    public void getTeachingClassrooms() {
        Account mockAccount = Account.builder()
                .username("TC000000")
                .id(8)
                .firstName("Mock")
                .lastName("TeacherAcc")
                .build();
        Teacher mockTeacher = Teacher.builder()
                .id(3)
//                .accountId(8)
                .build();
        Optional<Teacher> mockOptionalTeacher= Optional.of(mockTeacher);
        Classroom classroom1 = Classroom.builder().classroomName("SE1617").currentNoStudent(16).build();
        List<Classroom> mockClassroomList = new ArrayList<>();
        mockClassroomList.add(classroom1);

        ClassroomDTO classroomDTO1 = ClassroomDTO.builder().classroomName("SE1617").currentNoStudent(16).build();
        List<ClassroomDTO> mockClassroomDTOList = new ArrayList<>();
        mockClassroomDTOList.add(classroomDTO1);

        // define behavior of Repository
        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(mockOptionalTeacher);
        when(classroomRepository.findClassroomsByTeacherId(mockTeacher.getId())).thenReturn(mockClassroomList);
        when(classroomMapper.mapToDTO(classroom1)).thenReturn(classroomDTO1);
        // call service method
        List<ClassroomDTO> classroomDTOList = classroomService.getAllTeachingClassrooms(8);

        //assert the result

        assertEquals(classroomDTOList.size(), mockClassroomDTOList.size());
        assertEquals(classroomDTOList.get(0).getClassroomName(), mockClassroomDTOList.get(0).getClassroomName());
        assertEquals(classroomDTOList.get(0).getCurrentNoStudent(), mockClassroomDTOList.get(0).getCurrentNoStudent());
    }



}
