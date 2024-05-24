package org.example.planner.auth.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
