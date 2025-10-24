// model/Resource.java
package pl.com.chrzanowski.sma.common.security.resource.model;

import jakarta.persistence.*;
import lombok.*;
import pl.com.chrzanowski.sma.common.security.enums.ResourceKey;
import pl.com.chrzanowski.sma.role.model.Role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_key", unique = true, nullable = false, length = 100)
    private ResourceKey resourceKey;

    @Column(name = "endpoint_pattern", nullable = false)
    private String endpointPattern;

    @Column(length = 500)
    private String description;

    @Column(name = "http_method", length = 10)
    private String httpMethod;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "resource_role",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> allowedRoles = new HashSet<>();

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
