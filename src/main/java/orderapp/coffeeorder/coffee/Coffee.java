package orderapp.coffeeorder.coffee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Coffee {
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

}
