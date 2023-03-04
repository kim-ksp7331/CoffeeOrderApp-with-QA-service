package orderapp.coffeeorder.question.service;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.member.MemberService;
import orderapp.coffeeorder.question.entity.Question;
import orderapp.coffeeorder.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberService memberService;

    public Question createQuestion(Question question) {
        memberService.findVerifiedMember(question.getMember().getMemberId());
        return questionRepository.save(question);
    }

    public Question findQuestion(long questionId, Long memberId) {
        Question verifiedQuestion = findVerifiedQuestion(questionId);
        verifySecretQuestionAccessible(memberId, verifiedQuestion);
        return verifiedQuestion;
    }

    private void verifySecretQuestionAccessible(Long memberId, Question verifiedQuestion) {
        if (verifiedQuestion.getQuestionAccess() == Question.QuestionAccess.SECRET
                && verifiedQuestion.getMember().getMemberId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_ACCESS_QUESTION);
        }
    }

    public Question findVerifiedQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        return optionalQuestion.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
    }
}
