package orderapp.coffeeorder.member;

import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.member.mapper.MemberMapper;
import orderapp.coffeeorder.response.MultiResponseDTO;
import orderapp.coffeeorder.response.SingleResponseDTO;
import orderapp.coffeeorder.utils.AuthenticationUtils;
import orderapp.coffeeorder.utils.UriCreator;
import org.springframework.data.domain.Page;
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
@RequestMapping("/members")
@Validated
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final AuthenticationUtils authenticationUtils;

    public MemberController(MemberService memberService, MemberMapper mapper, AuthenticationUtils authenticationUtils) {
        this.memberService = memberService;
        this.mapper = mapper;
        this.authenticationUtils = authenticationUtils;
    }

    @PostMapping
    public ResponseEntity<?> postMember(@Valid @RequestBody MemberDTO.Post memberPostDTO) {
        Member member = memberService.createMember(mapper.memberPostDTOToMember(memberPostDTO));
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<?> patchMember(@PathVariable("member-id") @Positive long memberId,
                                         @Valid @RequestBody MemberDTO.Patch memberPatchDTO,
                                         Authentication authentication) {
        authenticationUtils.verifyMemberId(authentication, memberId);
        memberPatchDTO.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchDTOToMember(memberPatchDTO));
        MemberDTO.Response response = mapper.memberToMemberResponseDTO(member);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<?> getMember(@PathVariable("member-id") @Positive long memberId,
                                       Authentication authentication) {
        authenticationUtils.verifyMemberIdForUser(authentication, memberId);
        Member member = memberService.findMember(memberId);
        MemberDTO.Response response = mapper.memberToMemberResponseDTO(member);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getMembers(@RequestParam @Positive int page,
                                        @RequestParam @Positive int size) {
        Page<Member> memberPage = memberService.findMembers(page - 1, size);
        List<Member> members = memberPage.getContent();
        return new ResponseEntity<>(
                new MultiResponseDTO<>(mapper.membersToMemberResponseDTOs(members), memberPage), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<?> deleteMember(@PathVariable("member-id") @Positive long memberId,
                                          Authentication authentication) {
        authenticationUtils.verifyMemberId(authentication, memberId);
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
