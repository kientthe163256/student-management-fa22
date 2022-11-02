package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.ClassroomServiceImpl;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherService teacherService;

    @Mock
    private IGenericMapper<Classroom, ClassroomDTO> classroomMapper;

    @InjectMocks
    private ClassroomServiceImpl classroomService;

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
        Page<Classroom> mockPageClassroom = new PageImpl<>(classroomList);
        PageRequest pageRequest = PageRequest.of(1, 5);
        // 2. define behavior of Repository, Mapper
        when(classroomRepository.findAllAvailClassroom(pageRequest, 1)).thenReturn(mockPageClassroom);
        // 3.call service method
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllAvailClassroom(1, 1);
        //4 assert result
        assertNotNull(classroomDTOPage);
    }

//    @Test
//    public void getTeachingClassrooms() {
//        Account mockAccount = Account.builder()
//                .username("TC000000")
//                .id(8)
//                .firstName("Mock")
//                .lastName("TeacherAcc")
//                .build();
//        Teacher mockTeacher = Teacher.builder()
//                .id(3)
////                .accountId(8)
//                .build();
//        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);
//        Classroom classroom1 = Classroom.builder().classroomName("SE1617").currentNoStudent(16).build();
//        List<Classroom> mockClassroomList = new ArrayList<>();
//        mockClassroomList.add(classroom1);
//
//        ClassroomDTO classroomDTO1 = ClassroomDTO.builder().classroomName("SE1617").currentNoStudent(16).build();
//        List<ClassroomDTO> mockClassroomDTOList = new ArrayList<>();
//        mockClassroomDTOList.add(classroomDTO1);
//
//        // define behavior of Repository
//        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(mockOptionalTeacher);
//        when(classroomRepository.findClassroomsByTeacherId(mockTeacher.getId())).thenReturn(mockClassroomList);
//        when(classroomMapper.mapToDTO(classroom1)).thenReturn(classroomDTO1);
//        // call service method
//        List<ClassroomDTO> classroomDTOList = classroomService.getAllTeachingClassrooms(8, pageNumber, pageSize, sort);
//
//        //assert the result
//
//        assertEquals(classroomDTOList.size(), mockClassroomDTOList.size());
//        assertEquals(classroomDTOList.get(0).getClassroomName(), mockClassroomDTOList.get(0).getClassroomName());
//        assertEquals(classroomDTOList.get(0).getCurrentNoStudent(), mockClassroomDTOList.get(0).getCurrentNoStudent());
//    }

    @Test
    public void assignClassroomWithNotExistTeacher() {
        when(teacherService.getById(100)).thenThrow(new NoSuchElementException("Teacher not found"));
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            classroomService.assignClassroom(100, 1);
        });

        String expectedMessage = "Teacher not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void assignClassroomWithNotExistClassId() {
        Integer classId = 100;
        Integer teacherId = 1;
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            classroomService.assignClassroom(teacherId, classId);
        });

        String expectedMessage = "Class not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void assignClassroomSuccessfully() {
        Integer classId = 5;
        Integer teacherId = 3;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .subjectId(1)
                .build();
        Optional<Classroom> optionalClassroom = Optional.of(mockClassroom);
        Teacher mockTeacher = Teacher.builder()
                .id(teacherId)
                .build();

        when(teacherService.getById(teacherId)).thenReturn(mockTeacher);
        doReturn(optionalClassroom).when(classroomRepository).findById(classId);
        doAnswer((Answer<Integer>) invocation -> {
            mockClassroom.setTeacherId(teacherId);
            return 1;
        }).when(classroomRepository).save(mockClassroom);

        ClassroomDTO savedClass = classroomService.assignClassroom(teacherId, classId);

        assertEquals(savedClass.getTeacher().getId(), teacherId);

        verify(classroomRepository, times(1)).save(mockClassroom);
        verify(classroomRepository, times(1)).findById(classId);
    }

    @Test
    public void updateClassroom() {
        Integer classId = 5;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .subjectId(1)
                .build();
        String newClassName = "Edited";
        Classroom editClass = Classroom.builder()
                .id(classId)
                .classroomName(newClassName)
                .subjectId(1)
                .build();
        ClassroomDTO editedClassroomDTO = ClassroomDTO.builder()
                .id(classId)
                .classroomName(newClassName)
                .build();

        doReturn(Optional.of(mockClassroom)).when(classroomRepository).findById(classId);

        doAnswer((Answer<Void>) invocation -> {
            mockClassroom.setClassroomName(newClassName);
            return null;
        }).when(classroomRepository).save(mockClassroom);

        doAnswer((Answer<ClassroomDTO>) invocation -> {
            ClassroomDTO classroomDTO = ClassroomDTO.builder()
                    .id(mockClassroom.getId())
                    .classroomName(mockClassroom.getClassroomName())
                    .build();
            return classroomDTO;
        }).when(classroomMapper).mapToDTO(mockClassroom);

        ClassroomDTO actualClass = classroomService.updateClassroom(editClass, classId);
        assertEquals(actualClass.getClassroomName(), editedClassroomDTO.getClassroomName());

        verify(classroomRepository).save(mockClassroom);
    }

    @Test
    public void deleteClassroomHavingStudent(){
        Integer classId = 5;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .currentNoStudent(2)
                .build();

        when(classroomRepository.findById(classId)).thenReturn(Optional.of(mockClassroom));
        Exception exception = assertThrows(ActionNotAllowedException.class, () -> {
            classroomService.deleteClassroom(classId);
        });

        verify(classroomRepository).findById(classId);
    }
}