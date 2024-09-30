package org.example.studinfo.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDTO implements Serializable {
    private String name;
    private String number;
}
