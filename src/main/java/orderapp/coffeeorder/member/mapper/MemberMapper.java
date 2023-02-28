package orderapp.coffeeorder.member.mapper;

import orderapp.coffeeorder.member.MemberDTO;
import orderapp.coffeeorder.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDTOToMember(MemberDTO.Post memberPostDTO);
    Member memberPatchDTOToMember(MemberDTO.Patch memberPatchDTO);
    MemberDTO.Response memberToMemberResponseDTO(Member member);
    List<MemberDTO.Response> membersToMemberResponseDTOs(List<Member> members);
}
