package pl.com.chrzanowski.sma.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import pl.com.chrzanowski.sma.role.model.Role;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "login"),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_sequence", allocationSize = 1)
    @Column(nullable = false)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false, name = "email")
    private String email;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false, name = "login")
    private String login;

    @NotBlank
    @Size(min = 8, max = 120)
    private String password;

    @NotBlank
    @Size(max = 30)
    @Column(length = 30, name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 30)
    @Column(length = 30, name = "last_name")
    private String lastName;

    @Size(max = 50)
    @Column(length = 50, name = "position")
    private String position;

    private Boolean locked;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "create_date")
    private Instant createdDatetime;

    @Column(name = "modify_date")
    private Instant lastModifiedDatetime;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
