package orderapp.coffeeorder.question.mapper;

import orderapp.coffeeorder.question.dto.QuestionDTO;
import orderapp.coffeeorder.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper{
    @Mapping(target = "member.memberId", source = "memberId")
    @Mapping(target = "questionAccess", defaultValue = "PUBLIC")
    Question questionPostDTOToQuestion(QuestionDTO.Post questionPostDTO);

    @Mapping(target = "member.memberId", source = "memberId")
    Question questionPatchDTOToQuestion(QuestionDTO.Patch questionPatchDTO);

    @Mapping(source = "answer.content", target = "answer")
    @Mapping(source = "member.memberId", target = "memberId")
    QuestionDTO.Response questionToQuestionResponseDTO(Question question);
}
