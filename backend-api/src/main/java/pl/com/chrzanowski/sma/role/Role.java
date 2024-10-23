package pl.com.chrzanowski.sma.role;

import jakarta.persistence.*;
import lombok.*;
import pl.com.chrzanowski.sma.enumeration.ERole;
import pl.com.chrzanowski.sma.user.User;

import java.util.Set;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ERole name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}