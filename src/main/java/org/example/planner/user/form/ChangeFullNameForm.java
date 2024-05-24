package org.example.planner.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFullNameForm {

    private String firstName;
    private String lastName;
}
