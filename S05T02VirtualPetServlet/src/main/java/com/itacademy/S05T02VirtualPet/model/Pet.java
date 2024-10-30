package com.itacademy.S05T02VirtualPet.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pets")
public class Pet {
    @Id
    private String id;
    private String name;
    private String type;
    private String color;
    private String mood = "happy";
    private Integer energyLevel;
    private Map<String, String> characteristics;
}

