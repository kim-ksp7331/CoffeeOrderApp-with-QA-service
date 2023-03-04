package orderapp.coffeeorder.question.repository;

import orderapp.coffeeorder.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
