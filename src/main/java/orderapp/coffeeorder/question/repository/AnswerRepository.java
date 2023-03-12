package orderapp.coffeeorder.question.repository;

import orderapp.coffeeorder.question.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
