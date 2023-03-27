package orderapp.coffeeorder.auth.user_details;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.auth.util.CustomAuthorityUtils;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.member.MemberRepository;
import orderapp.coffeeorder.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new MemberUserDetails(findMember);
    }

    private final class MemberUserDetails extends Member implements UserDetails {
        MemberUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setEmail(member.getEmail());
            setName(member.getName());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.getAuthorities(getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
