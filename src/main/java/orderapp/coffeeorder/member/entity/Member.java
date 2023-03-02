package orderapp.coffeeorder.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orderapp.coffeeorder.audit.Auditable;
import orderapp.coffeeorder.order.entity.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, unique = true, length = 13)
    private String phone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;
    @OneToMany(mappedBy = "member")
    public List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "STAMP_ID")
    public Stamp stamp;

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");
        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
}
