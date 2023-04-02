package orderapp.coffeeorder.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthenticationUtils {
    // TODO : ADMIN 권한일때 memberId를 확인하지 않도록 수정
    public Long getMemberIdFromAuthentication(Authentication authentication) {
        return ((Integer) ((Map) authentication.getPrincipal()).get("memberId")).longValue();
    }

    public void verifyMemberId(Authentication authentication, long memberId) {
        long authMemberId = ((Integer) ((Map) authentication.getPrincipal()).get("memberId")).longValue();
        if(authMemberId != memberId) throw new AccessDeniedException("Access Denied");
    }
}
