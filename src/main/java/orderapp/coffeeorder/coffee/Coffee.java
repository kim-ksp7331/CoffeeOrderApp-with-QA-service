package orderapp.coffeeorder.coffee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orderapp.coffeeorder.audit.Auditable;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Coffee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coffeeId;
    @Column(nullable = false, length = 100)
    private String korName;
    @Column(nullable = false, length = 100)
    private String engName;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false, unique = true, length = 3)
    private String coffeeCode;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CoffeeStatus coffeeStatus = CoffeeStatus.COFFEE_FOR_SALE;

    public enum CoffeeStatus {
        COFFEE_FOR_SALE("판매중"),
        COFFEE_SOLD_OUT("판매중지");
        @Getter
        private String status;

        CoffeeStatus(String status) {
            this.status = status;
        }
    }
}
