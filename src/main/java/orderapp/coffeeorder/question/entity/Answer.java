package orderapp.coffeeorder.question.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orderapp.coffeeorder.audit.Auditable;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Answer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;
    @Column(nullable = false, length = 2000)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AnswerStatus answerStatus = AnswerStatus.ANSWER_REGISTRATION;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    public enum AnswerStatus {
        ANSWER_REGISTRATION("답변 등록"),
        ANSWER_DELETED("답변 삭제");
        @Getter
        private String status;

        AnswerStatus(String status) {
            this.status = status;
        }
    }
}
