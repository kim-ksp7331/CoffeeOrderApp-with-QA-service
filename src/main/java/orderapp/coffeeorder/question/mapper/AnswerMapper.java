package orderapp.coffeeorder.question.mapper;

import orderapp.coffeeorder.question.dto.AnswerDTO;
import orderapp.coffeeorder.question.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    @Mapping(target = "question.questionId", source = "questionId")
    Answer answerPostDTOToAnswer(AnswerDTO.Post answerPostDTO);
}
