package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.utility.ClassroomMapper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.internal.util.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private ClassroomMapper classroomMapper;

    @InjectMocks
    private ClassroomService classroomService;


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
        when(classroomMapper.mapToDTO(mockClassroom)).thenReturn(mockClassroomDTO);
        // 3.call service method
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllAvailClassroom(1,1);
        //4 assert result
        assertNotNull(classroomDTOPage);
    }

}
