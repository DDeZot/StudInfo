package org.example.studinfo.DTO;

import lombok.*;
import org.example.studinfo.entities.Group;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO implements Serializable {
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthDate;
    private Group group;
}
