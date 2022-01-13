package com.example.dditlms.security;

import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.entity.Bookmark;
import com.example.dditlms.domain.entity.Employee;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.sanctn.EmployeeRepository;
import com.example.dditlms.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final MemberRepository memberRepository;

    private final EmployeeRepository employeeRepository;

    private final BookmarkService bookmarkService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        Member member = ((AccountContext)authentication.getPrincipal()).getMember();
        if(member.getFailCount()>=5){
            throw new LockedException("Account locked");
        }else{
            member.setFailCount(0);
            memberRepository.save(member);
        }

        // 쿠팡 둘러보기 하다가 로그인 성공 시 다시 거기로 가는 경우
        setDefaultTargetUrl("/");
        // 에러 세션 삭제, Member 세션 추가
        HttpSession session = request.getSession();
        if(session!=null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        session.setAttribute("userName",member.getName());
        if(member.getMemberImg() == null){
            session.setAttribute("memberImg","/static/images/memberImg/user.png");
        }else{
            session.setAttribute("memberImg",member.getMemberImg());
        }
        Set<Bookmark> bookmarkList = bookmarkService.getBookmarks(member);
        if(bookmarkList!=null && !bookmarkList.isEmpty()){
            session.setAttribute("bookmarks",bookmarkList);
        }
        if(member.getRole().equals(Role.ROLE_STUDENT)){
            session.setAttribute("depart","학생");
            session.setAttribute("grade",member.getStudent().getGrade().getKorean());
            session.setAttribute("major",member.getStudent().getMajor().getKorean());
        }else if(member.getRole().equals(Role.ROLE_PROFESSOR)){
            session.setAttribute("depart","교수");
        }else if(member.getRole().equals(Role.ROLE_ACCADEMIC_DEP)){
            Employee employee = employeeRepository.findByMember(member).get();
            session.setAttribute("grade",employee.getEmployeeRole().getKrName());
            session.setAttribute("depart","교무과");
        }else if(member.getRole().equals(Role.ROLE_ADMIN_DEP)){
            Employee employee = employeeRepository.findByMember(member).get();
            session.setAttribute("grade",employee.getEmployeeRole().getKrName());
            session.setAttribute("depart","행정과");
        }else if(member.getRole().equals(Role.ROLE_GENERAL_DEP)){
            Employee employee = employeeRepository.findByMember(member).get();
            session.setAttribute("grade",employee.getEmployeeRole().getKrName());
            session.setAttribute("depart","총무과");
        }else if(member.getRole().equals(Role.ROLE_STUDENT_DEP)){
            Employee employee = employeeRepository.findByMember(member).get();
            session.setAttribute("grade",employee.getEmployeeRole().getKrName());
            session.setAttribute("depart","학생과");
        }
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        redirectStrategy.sendRedirect(request,response,getDefaultTargetUrl());
    }

}
