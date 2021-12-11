package com.example.dditlms.security;


import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

        private final MemberRepository memberRepository;

        @Override
        public void onAuthenticationFailure(HttpServletRequest request,
                HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String msg = "";
        System.out.print(exception);
        if(exception instanceof BadCredentialsException){
            msg = loginFail(username);
        }else if(exception instanceof InternalAuthenticationServiceException){
            msg = "Account";
        }else if(exception instanceof InsufficientAuthenticationException){
            msg = "Secret";
        }else if(exception instanceof LockedException){
            msg = "Disable";
        }else if(exception instanceof SessionAuthenticationException){
            msg = "Duplicate";
        }else if(exception instanceof DisabledException){
            System.out.print(exception);
        }else{
            System.out.print(exception);
        }

        setDefaultFailureUrl("/login?error=true&exception="+msg);

        super.onAuthenticationFailure(request,response,exception);
    }

    public String loginFail(String username){
        long userNumber;
        Optional<Member> memberEntityWrapper;
        try{
            userNumber = Long.parseLong(username);
            memberEntityWrapper = memberRepository.findByUserNumber(userNumber);
        }catch (NumberFormatException e){
            memberEntityWrapper = memberRepository.findByMemberId(username);
        }
        Member memberEntity = memberEntityWrapper.orElse(null);
        memberEntity.setFailCount(memberEntity.getFailCount()+1);
        memberRepository.save(memberEntity);
        if(memberEntity.getFailCount() >= 5){
            return "Disable";
        }
        return "Password&cnt=" + memberEntity.getFailCount();
    }
}
