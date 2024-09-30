package org.example.studinfo.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GROUPS", indexes = @Index(columnList = "number"))

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    @Column(name = "id")
    Long id;

    @Column(name = "number", columnDefinition = "varchar(10)", nullable = false)
    private String number;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;
}
