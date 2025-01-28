package pl.com.chrzanowski.sma.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import pl.com.chrzanowski.sma.user.model.User;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditableEntity {

    @Column(name = "create_date", updatable = false)
    private Instant createdDatetime;

    @Column(name = "modify_date")
    private Instant lastModifiedDatetime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "created_by", updatable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @PrePersist
    protected void onCreate() {
        this.createdDatetime = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedDatetime = Instant.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AuditableEntity that = (AuditableEntity) o;
        return getCreatedDatetime() != null && getCreatedDatetime().equals(that.getCreatedDatetime());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

}
