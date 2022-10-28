package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.repository.SubjectRepository;
import com.example.studentmanagementfa22.service.impl.SubjectServiceImpl;
import com.example.studentmanagementfa22.utility.SubjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.NoSuchElementException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    public void updateSubjectSuccessfully() {
        Subject currentSubject = Subject.builder()
                .id(1)
                .subjectName("DBI202")
                .numberOfCredit(3)
                .build();
        String newSubjectName = "Database with MongoSQL";
        SubjectDTO editedObject = SubjectDTO.builder()
                .id(1)
                .subjectName(newSubjectName)
                .numberOfCredit(5)
                .build();

        doReturn(Optional.of(currentSubject)).when(subjectRepository).findById(1);

        doAnswer((Answer<Void>) invocation -> {
            currentSubject.setSubjectName(editedObject.getSubjectName());
            currentSubject.setNumberOfCredit(editedObject.getNumberOfCredit());
            return null;
        }).when(subjectRepository).save(currentSubject);

        doAnswer((Answer<SubjectDTO>) invocation -> {
            SubjectDTO subjectDTO = SubjectDTO.builder()
                    .id(currentSubject.getId())
                    .subjectName(currentSubject.getSubjectName())
                    .numberOfCredit(currentSubject.getNumberOfCredit())
                    .build();
            return subjectDTO;
        }).when(subjectMapper).mapToDTO(currentSubject);

        SubjectDTO actualSubject = subjectService.updateSubject(editedObject);
        assertEquals(actualSubject.getSubjectName(), editedObject.getSubjectName());
        assertEquals(actualSubject.getNumberOfCredit(), editedObject.getNumberOfCredit());

        verify(subjectRepository).save(currentSubject);
    }

    @Test
    public void updateSubjectWithNotExistId(){
        Integer subjectId = 100;
        SubjectDTO editedObject = SubjectDTO.builder()
                .id(subjectId)
                .subjectName("newSubjectName")
                .numberOfCredit(5)
                .build();

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            subjectService.updateSubject(editedObject);
        });
        verify(subjectRepository).findById(subjectId);
    }
}
