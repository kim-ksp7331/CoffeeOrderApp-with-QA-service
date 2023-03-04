package orderapp.coffeeorder.question.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orderapp.coffeeorder.audit.Auditable;
import orderapp.coffeeorder.member.entity.Member;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 2000)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTRATION;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuestionAccess questionAccess = QuestionAccess.PUBLIC;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void addAnswer(Answer answer) {
        this.answer = answer;
    }

    public void addMember(Member member) {
        this.member = member;
    }


    public enum QuestionStatus {
        QUESTION_REGISTRATION("질문 등록"),
        QUESTION_ANSWERED("답변 완료"),
        QUESTION_DELETE("질문 삭제");
        @Getter
        private String status;

        QuestionStatus(String status) {
            this.status = status;
        }
    }

    public enum QuestionAccess {
        PUBLIC("공개"), SECRET("비공개");
        @Getter
        private String accessLevel;

        QuestionAccess(String accessLevel) {
            this.accessLevel = accessLevel;
        }
    }
}
