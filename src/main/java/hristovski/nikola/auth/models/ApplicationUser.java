package hristovski.nikola.auth.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser {

    public ApplicationUser(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

    public String getRolesAsString() {
        return this.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .peek(System.out::println)
                .collect(Collectors.joining(","));
    }

}
