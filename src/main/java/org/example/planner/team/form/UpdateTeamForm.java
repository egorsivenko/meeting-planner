package org.example.planner.team.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamForm {

    private Integer id;
    private String name;
    private String description;
}
