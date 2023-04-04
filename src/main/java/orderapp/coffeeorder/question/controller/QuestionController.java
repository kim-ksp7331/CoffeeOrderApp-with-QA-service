package orderapp.coffeeorder.question.controller;

import orderapp.coffeeorder.question.dto.QuestionDTO;
import orderapp.coffeeorder.question.entity.Question;
import orderapp.coffeeorder.question.mapper.QuestionMapper;
import orderapp.coffeeorder.question.service.QuestionService;
import orderapp.coffeeorder.response.MultiResponseDTO;
import orderapp.coffeeorder.response.SingleResponseDTO;
import orderapp.coffeeorder.utils.AuthenticationUtils;
import orderapp.coffeeorder.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/questions")
@Validated
public class QuestionController {
    private final static String Question_DEFAULT_URL = "/questions";
    private final QuestionService questionService;
    private final QuestionMapper mapper;
    private final AuthenticationUtils authenticationUtils;

    public QuestionController(QuestionService questionService, QuestionMapper mapper, AuthenticationUtils authenticationUtils) {
        this.questionService = questionService;
        this.mapper = mapper;
        this.authenticationUtils = authenticationUtils;
    }

    @PostMapping
    public ResponseEntity<?> postQuestion(@Valid @RequestBody QuestionDTO.Post questionPostDTO,
                                          Authentication authentication) {
        Long memberId = authenticationUtils.getMemberIdFromAuthentication(authentication);
        questionPostDTO.setMemberId(memberId);
        Question question = questionService.createQuestion(mapper.questionPostDTOToQuestion(questionPostDTO));
        URI location = UriCreator.createUri(Question_DEFAULT_URL, question.getQuestionId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity<?> patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                           @Valid @RequestBody QuestionDTO.Patch questionPatchDTO,
                                           Authentication authentication) {
        questionPatchDTO.setQuestionId(questionId);
        Question question = questionService.updateQuestion(mapper.questionPatchDTOToQuestion(questionPatchDTO), authentication);
        QuestionDTO.Response response = mapper.questionToQuestionResponseDTO(question);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping("/{question-id}")
    public ResponseEntity<?> getQuestion(@PathVariable("question-id") @Positive long questionId,
                                         Authentication authentication) {
        Question question = questionService.findQuestion(questionId, authentication);
        QuestionDTO.Response response = mapper.questionToQuestionResponseDTO(question);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getQuestions(@RequestParam @Positive int page,
                                          @RequestParam @Positive int size,
                                          @RequestParam(defaultValue = "CREATED_AT") Question.QuestionOrder order,
                                          @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Page<Question> questionPage = questionService.findQuestions(page - 1, size, order, direction);
        List<Question> questions = questionPage.getContent();
        List<QuestionDTO.Response> responses = mapper.questionsToQuestionResponseDTOs(questions);
        return new ResponseEntity<>(new MultiResponseDTO<>(responses, questionPage), HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("question-id") @Positive long questionId,
                                            Authentication authentication) {
        Long memberId = authenticationUtils.getMemberIdFromAuthentication(authentication);
        questionService.deleteQuestion(questionId, authentication);
        return ResponseEntity.noContent().build();
    }
}
