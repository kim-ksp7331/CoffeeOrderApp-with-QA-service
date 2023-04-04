package orderapp.coffeeorder.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthenticationUtils {
    public Long getMemberIdFromAuthentication(Authentication authentication) {
        return ((Integer) ((Map) authentication.getPrincipal()).get("memberId")).longValue();
    }

    public void verifyMemberId(Authentication authentication, long memberId) {
        long authMemberId = ((Integer) ((Map) authentication.getPrincipal()).get("memberId")).longValue();
        if(authMemberId != memberId) throw new AccessDeniedException("Access Denied");
    }

    public void verifyMemberIdForUser(Authentication authentication, long memberId) {
        boolean isUser = authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        if(isUser) verifyMemberId(authentication, memberId);
    }
}
