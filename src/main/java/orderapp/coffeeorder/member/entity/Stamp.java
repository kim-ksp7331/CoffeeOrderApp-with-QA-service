package orderapp.coffeeorder.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orderapp.coffeeorder.audit.Auditable;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Stamp extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stampId;
    @Column(nullable = false)
    private Integer stampCount = 0;
}
