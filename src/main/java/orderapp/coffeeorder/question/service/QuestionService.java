package orderapp.coffeeorder.question.service;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.member.MemberService;
import orderapp.coffeeorder.question.entity.Question;
import orderapp.coffeeorder.question.repository.QuestionRepository;
import orderapp.coffeeorder.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberService memberService;
    private final CustomBeanUtils<Question> beanUtils;

    public Question createQuestion(Question question) {
        memberService.findVerifiedMember(question.getMember().getMemberId());
        return questionRepository.save(question);
    }

    public Question updateQuestion(Question question) {
        Question findQuestion = findVerifiedQuestion(question.getQuestionId());
        verifyAccessibleMember(question.getMember().getMemberId(), findQuestion.getMember().getMemberId());
        Question updatedQuestion = beanUtils.copyNonNullProperties(question, findQuestion);
        return questionRepository.save(updatedQuestion);
    }

    public Question findQuestion(long questionId, Long memberId) {
        Question findQuestion = findVerifiedQuestion(questionId);
        verifyAccessibleSecretQuestion(memberId, findQuestion);
        findQuestion.setViews(findQuestion.getViews() + 1);
        return findQuestion;
    }

    public Page<Question> findQuestions(int page, int size, Question.QuestionOrder questionOrder, Sort.Direction direction) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, questionOrder.getFieldName()));
        return questionRepository.findAllByPublicWithAnswerNotDeleted(pageRequest);
    }

    public void deleteQuestion(long questionId, long memberId) {
        Question findQuestion = findVerifiedQuestion(questionId);
        verifyAccessibleMember(memberId, findQuestion.getMember().getMemberId());
        if (findQuestion.getQuestionStatus() == Question.QuestionStatus.QUESTION_DELETED) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_DELETE_QUESTION);
        }
        findQuestion.setQuestionStatus(Question.QuestionStatus.QUESTION_DELETED);
    }


    public Question findVerifiedQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findByQuestionIdWithAnswerNotDeleted(questionId);
        Question question = optionalQuestion.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
        verifyDeletedQuestion(question);
        return question;
    }

    private void verifyAccessibleMember(long memberId, long findMemberId) {
        if (memberId != findMemberId) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    private void verifyAccessibleSecretQuestion(Long memberId, Question findQuestion) {
        if (findQuestion.getQuestionAccess() == Question.QuestionAccess.SECRET
                && findQuestion.getMember().getMemberId() != memberId) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    private void verifyDeletedQuestion(Question question) {
        if (question.getQuestionStatus() == Question.QuestionStatus.QUESTION_DELETED) {
            throw new BusinessLogicException(ExceptionCode.QUESTION_DELETED);
        }
    }

}
