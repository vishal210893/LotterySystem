package com.poppula.poppula_test.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "line_numbers")
    @Column(name = "number")
    private List<Integer> numbers;

    private int result;

}
