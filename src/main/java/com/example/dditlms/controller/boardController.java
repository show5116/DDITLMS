package com.example.dditlms.controller;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.dto.BbsDTO;
import com.example.dditlms.domain.dto.PageDTO;
import com.example.dditlms.domain.entity.Attachment;
import com.example.dditlms.domain.entity.Bbs;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.AttachmentRepository;
import com.example.dditlms.domain.repository.BbsRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.util.FileUtil;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final AttachmentRepository attachmentRepository;

    @GetMapping("/community/{mapping}")
    public ModelAndView mappingBoard(ModelAndView mav, @PathVariable(value = "mapping") String mapping,
                                     @PageableDefault(size = 4)Pageable pageable){

        Member loginMember = MemberUtil.getLoginMember();
        logger.info(String.valueOf(loginMember.getRole()));


        BoardCategory boardCategory = null;
        if(mapping.equals("freeboard")){
            boardCategory = BoardCategory.FREEBOARD;
        } else if(mapping.equals("general")){
            boardCategory = BoardCategory.GENERAL;
        } else if(mapping.equals("scholarship")){
            boardCategory = BoardCategory.SCHOLARSHIPNOTICE;
        } else if(mapping.equals("bachelor")){
            boardCategory = BoardCategory.BACHELORNOTICE;
        } else if(mapping.equals("major")){
            boardCategory = BoardCategory.MAJORNOTICE;
        }
        Page<BbsDTO> results = bbsRepository.pageWithFree(pageable, boardCategory);
        Long i = 1L;
        for (BbsDTO result : results) {
            result.setTotal(i);
            i++;
        }



        mav.addObject("boardCategory", boardCategory);
        mav.addObject("mapping", mapping);
        mav.addObject("bbsList", results);
        mav.addObject("page", new PageDTO(results.getTotalElements(), pageable));
        mav.setViewName("pages/freeboard");
        return mav;
    }

    @GetMapping("/community/replaceBoard/{mapping}")
    public ModelAndView replaceBoard(ModelAndView mav, @PageableDefault(size = 4)Pageable pageable,
                                     @PathVariable(value = "mapping") String mapping){


        logger.info("mapping이란다: " + mapping);

        BoardCategory boardCategory = null;
        if(mapping.equals("freeboard")){
            boardCategory = BoardCategory.FREEBOARD;
        } else if(mapping.equals("general")){
            boardCategory = BoardCategory.GENERAL;
        } else if(mapping.equals("scholarship")){
            boardCategory = BoardCategory.SCHOLARSHIPNOTICE;
        } else if(mapping.equals("bachelor")){
            boardCategory = BoardCategory.BACHELORNOTICE;
        } else if(mapping.equals("major")){
            boardCategory = BoardCategory.MAJORNOTICE;
        }

        Page<BbsDTO> results = bbsRepository.pageWithFree(pageable, boardCategory);

        Long i = 1L;
        for (BbsDTO result : results) {
            result.setTotal(i);
            i++;
        }

        mav.addObject("boardCategory", boardCategory);
        mav.addObject("bbsList", results);
        mav.addObject("page", new PageDTO(results.getTotalElements(), pageable));
        mav.setViewName("pages/freeboard::#test");
        return mav;
    }

    @GetMapping("/community/{mapping}/boardWrite")
    public ModelAndView boardWrite(ModelAndView mav, @PathVariable(value = "mapping") String mapping){

        BoardCategory boardCategory = null;
        if(mapping.equals("freeboard")){
            boardCategory = BoardCategory.FREEBOARD;
        } else if(mapping.equals("general")){
            boardCategory = BoardCategory.GENERAL;
        } else if(mapping.equals("scholarship")){
            boardCategory = BoardCategory.SCHOLARSHIPNOTICE;
        } else if(mapping.equals("bachelor")){
            boardCategory = BoardCategory.BACHELORNOTICE;
        } else if(mapping.equals("major")){
            boardCategory = BoardCategory.MAJORNOTICE;
        }

        mav.addObject("mapping", mapping);
        mav.addObject("boardCategory", boardCategory);
        mav.setViewName("pages/boardWrite");
        return mav;
    }

    @ResponseBody
    @PostMapping("/community/{mapping}/boardWritePost")
    public String boardWritePost(MultipartHttpServletRequest multiRequest, HttpServletRequest request
        , ModelAndView mav, @PathVariable(value = "mapping") String mapping){
        JSONObject jsonObject = new JSONObject();

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        Map<String,MultipartFile> map = multiRequest.getFileMap();

        Long id =null;
        if(!map.isEmpty()){
            id = fileUtil.uploadFiles(map);
            logger.info("아아아아???");
            logger.info(String.valueOf(id));
        } else {
            logger.info("비었나?");
        }
        System.out.println(map);

        String title = request.getParameter("title");
        logger.info("title : " + title);
         String content = request.getParameter("content");

        BoardCategory boardCategory = null;
        if(mapping.equals("freeboard")){
            boardCategory = BoardCategory.FREEBOARD;
        } else if(mapping.equals("general")){
            boardCategory = BoardCategory.GENERAL;
        } else if(mapping.equals("scholarship")){
            boardCategory = BoardCategory.SCHOLARSHIPNOTICE;
        } else if(mapping.equals("bachelor")){
            boardCategory = BoardCategory.BACHELORNOTICE;
        } else if(mapping.equals("major")){
            boardCategory = BoardCategory.MAJORNOTICE;
        }

        logger.info(String.valueOf(member.getUserNumber()));
        Bbs bbs = Bbs.builder()
                .member(member)
                .title(title)
                .content(content)
                .category(boardCategory)
                .bbsCnt(0L)
                .bbsDate(new Date())
                .atchmnflId(id)
                .build();
        bbsRepository.save(bbs);

        mav.addObject("mapping", mapping);
        mav.addObject("id", bbs.getIdx());
        jsonObject.put("idx",bbs.getIdx());
        return jsonObject.toJSONString();
    }


    @GetMapping("/community/{mapping}/detailBoard/{targetId}")
    public ModelAndView detailBoard(ModelAndView mav,
                                    @PathVariable(value = "targetId") String idx,
                                    @PathVariable(value = "mapping") String mapping,
                                    HttpServletRequest request, HttpServletResponse response){

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        BoardCategory boardCategory = null;
        if(mapping.equals("freeboard")){
            boardCategory = BoardCategory.FREEBOARD;
        } else if(mapping.equals("general")){
            boardCategory = BoardCategory.GENERAL;
        } else if(mapping.equals("scholarship")){
            boardCategory = BoardCategory.SCHOLARSHIPNOTICE;
        } else if(mapping.equals("bachelor")){
            boardCategory = BoardCategory.BACHELORNOTICE;
        } else if(mapping.equals("major")){
            boardCategory = BoardCategory.MAJORNOTICE;
        }

        logger.info("idx : " + idx);
        Optional<Bbs> bbsWrapper = bbsRepository.findByIdx(Long.parseLong(idx));
        Bbs bbs = bbsWrapper.orElse(null);

        List<Attachment> attachList = attachmentRepository.findAllById(bbs.getAtchmnflId());
        List<Map<String,String>> tokenList = new ArrayList<>();
        for(Attachment attach : attachList) {
            Map<String,String> token = new HashMap<>();
            token.put("name",attach.getOriginName()+attach.getExtension());
            token.put("token",fileUtil.makeFileToken(attach.getId(), attach.getOrder()));
            tokenList.add(token);
        }
        BbsDTO bbsDTO = bbs.toDTO();
        bbsDTO.setTokenList(tokenList);

        Member writer = bbs.getMember();
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

        if((writer.getUserNumber()).equals(member.getUserNumber())){
            mav.addObject("flag", true);
        }

        mav.addObject("mapping", mapping);
        mav.addObject("boardCategory", boardCategory);
        mav.addObject("bbs", bbsDTO);
        mav.setViewName("pages/detailBoard");
        return mav;
    }

    @ResponseBody
    @PostMapping("/community/{mapping}/detailBoard/{targetId}")
    public String detailBoardPost(@RequestParam Map<String, Object> paramMap
                            ,@PathVariable(value = "targetId") String idx
                            ,@PathVariable(value = "mapping") String mapping){

        logger.info("idx : " + idx);

        //replace할거 아니면 redirect해야지
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("idx", idx);
        return jsonObject.toJSONString();
    }

    @PostMapping("/community/delete")
    public ModelAndView deleteBoard(@RequestParam(value = "bbs-idx") Long bbsIdx,
                                    @RequestParam(value = "mapping") String mapping,
                                    ModelAndView mav){
        logger.info("boardController - deleteBoard - bbsIdx : {}", bbsIdx);

        logger.info("boardController - deleteBoard - boardId : {}", mapping);

        Optional<Bbs> bbsWrapper = bbsRepository.findByIdx(bbsIdx);
        Bbs bbs = bbsWrapper.orElse(null);
        logger.info("boardController - deleteBoard - bbs : {} : ", bbs);
        bbsRepository.delete(bbs);

        mav.addObject("mapping", mapping);
        mav.setViewName("redirect:/community/"+mapping);
        return mav;
    }
}
