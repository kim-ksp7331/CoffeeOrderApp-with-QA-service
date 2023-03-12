package orderapp.coffeeorder.question.repository;

import orderapp.coffeeorder.member.MemberRepository;
import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.member.entity.Stamp;
import orderapp.coffeeorder.question.entity.Answer;
import orderapp.coffeeorder.question.entity.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        Member member = new Member("abc@gmail.com", "kim", "010-0000-0000");
        member.setStamp(new Stamp());
        memberRepository.save(member);

        Question question1 = new Question();
        question1.setMember(member);
        question1.setTitle("제목1");
        question1.setContent("내용1");
        questionRepository.save(question1);

        Answer answer1 = new Answer();
        answer1.setContent("답변1");
        question1.setAnswer(answer1);
        answer1.setAnswerStatus(Answer.AnswerStatus.ANSWER_DELETED);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setContent("답변2");
        question1.setAnswer(answer2);
        answerRepository.save(answer2);

        Question question2 = new Question();
        question2.setMember(member);
        question2.setTitle("제목2");
        question2.setContent("내용2");
        question2.setQuestionAccess(Question.QuestionAccess.SECRET);
        questionRepository.save(question2);

        Question question3 = new Question();
        question3.setMember(member);
        question3.setTitle("제목3");
        question3.setContent("내용3");
        questionRepository.save(question3);

        Question question4 = new Question();
        question4.setMember(member);
        question4.setTitle("제목4");
        question4.setContent("내용4");
        questionRepository.save(question4);


    }

    @Test
    void findByQuestionIdWithAnswerNotDeleted() {
    }

    @Test
    void findAllByPublicWithAnswerNotDeleted() {
        // given
        // init()
        int page = 0;
        int size = 5;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // when
        Page<Question> questionPage = questionRepository.findAllByPublicWithAnswerNotDeleted(pageRequest);
        Question question = questionRepository.findByQuestionIdWithAnswerNotDeleted(1L).get();

        // then
        List<Question> questions = questionPage.getContent();
        Assertions.assertEquals(3, questions.size());

        Assertions.assertEquals(questionRepository.findByQuestionIdWithAnswerNotDeleted(4L).get(), questions.get(0));
        Assertions.assertEquals(questionRepository.findByQuestionIdWithAnswerNotDeleted(3L).get(), questions.get(1));

        Assertions.assertEquals(question, questions.get(2));
        Assertions.assertEquals(answerRepository.findById(2L).get(), questions.get(2).getAnswer());

    }
}