package orderapp.coffeeorder.question.controller;

import orderapp.coffeeorder.question.dto.QuestionDTO;
import orderapp.coffeeorder.question.entity.Question;
import orderapp.coffeeorder.question.mapper.QuestionMapper;
import orderapp.coffeeorder.question.service.QuestionService;
import orderapp.coffeeorder.response.SingleResponseDTO;
import orderapp.coffeeorder.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/questions")
@Validated
public class QuestionController {
    private final static String Question_DEFAULT_URL = "/questions";
    private final QuestionService questionService;
    private final QuestionMapper mapper;

    public QuestionController(QuestionService questionService, QuestionMapper mapper) {
        this.questionService = questionService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> postQuestion(@Valid @RequestBody QuestionDTO.Post questionPostDTO) {
        Question question = questionService.createQuestion(mapper.questionPostDTOToQuestion(questionPostDTO));
        URI location = UriCreator.createUri(Question_DEFAULT_URL, question.getQuestionId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{question-id}")
    public ResponseEntity<?> getQuestion(@PathVariable("question-id") @Positive long questionId,
                                         @RequestParam(required = false) @Positive Long memberId) {
        Question question = questionService.findQuestion(questionId, memberId);
        QuestionDTO.Response response = mapper.questionToQuestionResponseDTO(question);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }
}
