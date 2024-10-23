package com.itacademy.S05T02VirtualPet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pets")
public class Pet {
    @Id
    private String id;
    private String name;
    private String type;
    private String color;
    private String mood;
    private int energyLevel;
    private Map<String, String> characteristics;
}

