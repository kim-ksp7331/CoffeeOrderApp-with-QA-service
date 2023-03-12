package orderapp.coffeeorder.question.repository;

import orderapp.coffeeorder.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    /**
     * answerStatus가 ANSWER_DELETED인 ANSWER는 가져오지 않는 메서드
     * @param questionId
     * @return
     */
    @Query("FROM Question q LEFT JOIN q.answer a ON a.answerStatus <> 'ANSWER_DELETED' " +
            "LEFT JOIN q.member m LEFT JOIN m.stamp s " +
            "WHERE q.questionId = :questionId ")
    @EntityGraph(attributePaths = {"member", "answer"})
    Optional<Question> findByQuestionIdWithAnswerNotDeleted(long questionId);

    @Query("FROM Question q LEFT JOIN q.answer a ON a.answerStatus <> 'ANSWER_DELETED' " +
            "LEFT JOIN q.member m LEFT JOIN m.stamp s " +
            "WHERE q.questionAccess = 'PUBLIC'")
    @EntityGraph(attributePaths = {"answer", "member", "member.stamp"})
    Page<Question> findAllByPublicWithAnswerNotDeleted(Pageable pageable);
}
