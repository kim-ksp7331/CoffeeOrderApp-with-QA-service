package orderapp.coffeeorder.member.mapper;

import orderapp.coffeeorder.member.MemberDTO;
import orderapp.coffeeorder.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDTOToMember(MemberDTO.Post memberPostDTO);
    Member memberPatchDTOToMember(MemberDTO.Patch memberPatchDTO);
    @Mapping(source = "stamp.stampCount", target = "stampCount")
    MemberDTO.Response memberToMemberResponseDTO(Member member);
    List<MemberDTO.Response> membersToMemberResponseDTOs(List<Member> members);
}
