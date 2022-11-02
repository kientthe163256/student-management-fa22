package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class Pagination<T> {
    private List<T> data;
    private Integer first;
    private Integer previous;
    private Integer next;
    private Integer last;
    private Integer total;
}