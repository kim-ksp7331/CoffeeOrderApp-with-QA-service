package orderapp.coffeeorder.question.service;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.question.entity.Answer;
import orderapp.coffeeorder.question.entity.Question;
import orderapp.coffeeorder.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {
    private final QuestionService questionService;
    private final CustomBeanUtils<Answer> beanUtils;

    public Answer createAnswer(Answer answer) {
        Question findQuestion = questionService.findVerifiedQuestion(answer.getQuestion().getQuestionId());
        verifyExistAnswer(findQuestion);
        findQuestion.setQuestionStatus(Question.QuestionStatus.QUESTION_ANSWERED);
        findQuestion.setAnswer(answer);
        return answer;
    }

    public Answer updateAnswer(Answer answer) {
        Answer findAnswer = findVerifiedAnswer(answer.getQuestion().getQuestionId());
        Answer updatedAnswer = beanUtils.copyNonNullProperties(answer, findAnswer);
        return updatedAnswer;
    }

    public void deleteAnswer(long questionId) {
        Answer findAnswer = findVerifiedAnswer(questionId);
        if (findAnswer.getAnswerStatus() == Answer.AnswerStatus.ANSWER_DELETED) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_DELETE_ANSWER);
        }
        findAnswer.setAnswerStatus(Answer.AnswerStatus.ANSWER_DELETED);
        findAnswer.getQuestion().setQuestionStatus(Question.QuestionStatus.QUESTION_REGISTRATION);
    }


    private Answer findVerifiedAnswer(long questionId) {
        Question findQuestion = questionService.findVerifiedQuestion(questionId);
        Optional<Answer> optionalAnswer = Optional.ofNullable(findQuestion.getAnswer());
        return optionalAnswer.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
    }

    private void verifyExistAnswer(Question findQuestion) {
        Optional<Answer> optionalAnswer = Optional.ofNullable(findQuestion.getAnswer());
        if (optionalAnswer.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ANSWER_EXISTS);
        }
    }
}
