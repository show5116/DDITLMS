package com.example.dditlms.controller;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.entity.Bbs;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.BbsRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class boardController {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);

    private final FileUtil fileUtil;
    private final BbsRepository bbsRepository;


    @GetMapping("/community/freeboard")
    public ModelAndView freeboard(ModelAndView mav){

        Optional<List<Bbs>> bbsWrapper = bbsRepository.findAllByCategory(BoardCategory.FREEBOARD);
        List<Bbs> bbsList = bbsWrapper.orElse(null);
        for (Bbs bbs : bbsList){
            String content = bbs.getContent();
            logger.info("<img>여부 : " + content);
            if(content.indexOf("<img") == -1){
                logger.info("이미지를 안넣은거야");
            } else {
                logger.info("이미지를 넣은거야 : " + content.indexOf("<img"));
                logger.info("\n이미지만 출력 : \n : "
                        + content.substring(content.indexOf("<img"), content.indexOf("/>")+2));
            }
        }

        mav.addObject("bbsList", bbsList);
        mav.setViewName("/pages/freeboard");
        return mav;
    }

    @GetMapping("/community/boardWrite")
    public ModelAndView boardWrite(ModelAndView mav){
        mav.setViewName("pages/boardWrite");
        return mav;
    }

    @ResponseBody
    @PostMapping("/community/boardWritePost.do")
    public String boardWritePost(MultipartHttpServletRequest multiRequest, HttpServletRequest request
        , Model model){
        JSONObject jsonObject = new JSONObject();

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        Map<String,MultipartFile> map = multiRequest.getFileMap();
        long id = fileUtil.uploadFiles(map);
        if(map.isEmpty()){
            logger.info("아아아아???");
            logger.info(String.valueOf(id));
        }
        System.out.println(map);

        String title = request.getParameter("title");
        logger.info("title : " + title);
         String content = request.getParameter("content");


         logger.info(String.valueOf(member.getUserNumber()));
        Bbs bbs = Bbs.builder()
                .member(member)
                .title(title)
                .content(content)
                .category(BoardCategory.FREEBOARD)
                .bbsCnt(0L)
                .bbsDate(new Date())
                .atchmnflId(id)
                .build();
        bbsRepository.save(bbs);
        model.addAttribute("id", bbs.getIdx());
        jsonObject.put("idx",bbs.getIdx());
        return jsonObject.toJSONString();
    }


    @GetMapping("/community/detailBoard/{targetId}")
    public ModelAndView detailBoard(ModelAndView mav,
//                                    @RequestParam String idx,
                                    @PathVariable(value = "targetId") String idx,
                                    HttpServletRequest request, HttpServletResponse response){

        logger.info(idx);
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        logger.info("idx : " + idx);
        Optional<Bbs> bbsWrapper = bbsRepository.findByIdx(Long.parseLong(idx));
        Bbs bbs = bbsWrapper.orElse(null);

        Long cnt = bbs.getBbsCnt();

        String code = member + idx;

    //////////////////////////
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + code.toString() + "]")) {
                cnt++;
                bbs.setBbsCnt(cnt);
                bbsRepository.save(bbs);
                oldCookie.setValue(oldCookie.getValue() + "_[" + code + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            cnt++;
            bbs.setBbsCnt(cnt);
            bbsRepository.save(bbs);
            Cookie newCookie = new Cookie("postView","[" + code + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

    ////////////////////////////////

        logger.info(String.valueOf(bbs.getCategory()));


        mav.addObject("bbs", bbs);
        mav.setViewName("pages/detailBoard");
        return mav;
    }

    @ResponseBody
    @PostMapping("/community/detailBoard/{targetId}")
    public String detailBoardPost(@RequestParam Map<String, Object> paramMap
                            ,@PathVariable(value = "targetId") String idx){
//        logger.info("idx : " + paramMap.get("targetId"));
        logger.info("idx : " + idx);

        //replace할거 아니면 redirect해야지
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idx", idx);
        return jsonObject.toJSONString();
    }
}
