package org.example.studinfo.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "STUDENTS", indexes = @Index(columnList = "surname, group_id"))

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    @Column(name = "id")
    @Getter
    @Setter
    Long id;

    @Column(name = "name", columnDefinition = "varchar(60)", nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(name = "surname", columnDefinition = "varchar(60)", nullable = false)
    @Getter
    @Setter
    private String surname;

    @Column(name = "patronymic", columnDefinition = "varchar(60)")
    @Getter
    @Setter
    private String patronymic;

    @Column(name = "birth_date", nullable = false)
    @Basic
    @Getter
    @Setter
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = MERGE)
    @JoinColumn(name = "group_id")
    @Nullable
    @Getter
    private Group group;

    @Transient
    @Nullable
    @Setter
    private String groupNumber;

    public String getGroupNumber() {
        if(group == null)
            return "";
        else
            return group.getNumber();
    }

    public void setGroup(Group group){
        this.group = group;
        setGroupNumber(group.getNumber());
    }

    public void setBirth_date(Date birth_date){
        this.birthDate = birth_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
