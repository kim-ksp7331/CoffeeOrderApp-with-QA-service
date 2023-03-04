package orderapp.coffeeorder.question.controller;

import orderapp.coffeeorder.question.dto.AnswerDTO;
import orderapp.coffeeorder.question.mapper.AnswerMapper;
import orderapp.coffeeorder.question.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/questions/{question-id}/answers")
@Validated
public class AnswerController {
    private final AnswerService answerService;
    private final AnswerMapper mapper;

    public AnswerController(AnswerService answerService, AnswerMapper mapper) {
        this.answerService = answerService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> postAnswer(@PathVariable("question-id") @Positive long questionId,
                                        @Valid RequestEntity<AnswerDTO.Post> requestEntity) {
        AnswerDTO.Post answerPostDTO = requestEntity.getBody();
        answerPostDTO.setQuestionId(questionId);
        if (!answerPostDTO.getEmail().equals("admin@gmail.com")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        answerService.createAnswer(mapper.answerPostDTOToAnswer(answerPostDTO));
        URI location = requestEntity.getUrl();
        return ResponseEntity.created(location).build();
    }
}
