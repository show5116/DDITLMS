package com.example.dditlms.util;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MemberUtil {

    public static Member getLoginMember(){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        return member;
    }
}
