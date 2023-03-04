package orderapp.coffeeorder.question.service;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.question.entity.Answer;
import orderapp.coffeeorder.question.entity.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {
    private final QuestionService questionService;

    public Answer createAnswer(Answer answer) {
        Question verifiedQuestion = questionService.findVerifiedQuestion(answer.getQuestion().getQuestionId());
        verifiedQuestion.setQuestionStatus(Question.QuestionStatus.QUESTION_ANSWERED);
        verifiedQuestion.setAnswer(answer);
        return answer;
    }
}
