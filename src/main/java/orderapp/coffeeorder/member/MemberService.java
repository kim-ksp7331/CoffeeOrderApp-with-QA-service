package orderapp.coffeeorder.member;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.member.entity.Stamp;
import orderapp.coffeeorder.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomBeanUtils<Member> beanUtils;

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        setStamp(member);
        return memberRepository.save(member);
    }

    private void setStamp(Member member) {
        member.setStamp(new Stamp());
    }

    public Member updateMember(Member member) {
        Member findMember = findVerifiedMember(member.getMemberId());
        Member updatedMember = beanUtils.copyNonNullProperties(member, findMember);
        return memberRepository.save(updatedMember);
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Member> findMembers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("memberId").descending());
        return memberRepository.findAll(pageRequest);
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);
        memberRepository.delete(findMember);
    }

    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
}
