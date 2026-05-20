package pl.com.chrzanowski.sma.scaffolding.counter.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scaffolding_number_counter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScaffoldingNumberCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scaffolding_log_id", nullable = false)
    private Long scaffoldingLogId;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "last_number", nullable = false)
    private Integer lastNumber;
}