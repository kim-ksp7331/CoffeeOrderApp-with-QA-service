package orderapp.coffeeorder.member;

import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> postMember(@Valid @RequestBody MemberDTO.Post memberPostDTO) {
        Member member = memberService.createMember(mapper.memberPostDTOToMember(memberPostDTO));
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<?> patchMember(@PathVariable("member-id") @Positive long memberId,
                                         @Valid @RequestBody MemberDTO.Patch memberPatchDTO) {
        memberPatchDTO.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchDTOToMember(memberPatchDTO));
        MemberDTO.Response response = mapper.memberToMemberResponseDTO(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<?> getMember(@PathVariable("member-id") @Positive long memberId) {
        Member member = memberService.findMember(memberId);
        MemberDTO.Response response = mapper.memberToMemberResponseDTO(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getMembers() {
        // TODO Not Implemented
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<?> deleteMember(@PathVariable("member-id") @Positive long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
