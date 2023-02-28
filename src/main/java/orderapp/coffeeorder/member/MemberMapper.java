package orderapp.coffeeorder.member;

import orderapp.coffeeorder.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDTOToMember(MemberDTO.Post memberPostDTO);
    Member memberPatchDTOToMember(MemberDTO.Patch memberPatchDTO);
    MemberDTO.Response memberToMemberResponseDTO(Member member);
}
