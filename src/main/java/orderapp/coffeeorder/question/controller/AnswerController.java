package orderapp.coffeeorder.question.controller;

import orderapp.coffeeorder.question.dto.AnswerDTO;
import orderapp.coffeeorder.question.entity.Answer;
import orderapp.coffeeorder.question.mapper.AnswerMapper;
import orderapp.coffeeorder.question.service.AnswerService;
import orderapp.coffeeorder.response.SingleResponseDTO;
import org.springframework.http.HttpStatus;
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
                                        @Valid @RequestBody AnswerDTO.Post answerPostDTO) {
        answerPostDTO.setQuestionId(questionId);
        if (!answerPostDTO.getEmail().equals("admin@gmail.com")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        answerService.createAnswer(mapper.answerPostDTOToAnswer(answerPostDTO));
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> patchAnswer(@PathVariable("question-id") @Positive long questionId,
                                         @Valid @RequestBody AnswerDTO.Patch answerPatchDTO) {
        answerPatchDTO.setQuestionId(questionId);
        if (!answerPatchDTO.getEmail().equals("admin@gmail.com")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Answer answer = answerService.updateAnswer(mapper.answerPatchDTOToAnswer(answerPatchDTO));
        AnswerDTO.Response response = mapper.answerToAnswerResponseDTO(answer);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAnswer(@PathVariable("question-id") @Positive long questionId,
                                          @RequestParam String email) {
        if (!email.equals("admin@gmail.com")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        answerService.deleteAnswer(questionId);
        return ResponseEntity.noContent().build();
    }


}
