package com.example.dditlms.controller;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.entity.Bbs;
import com.example.dditlms.domain.entity.FileData;
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
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class boardController {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);

    private final FileUtil fileUtil;
    private final BbsRepository bbsRepository;


    @GetMapping("/community/boardA")
    public ModelAndView boardA(ModelAndView mav){

        Optional<List<Bbs>> bbsWrapper = bbsRepository.findAllByCategory(BoardCategory.FREEBOARD);
        List<Bbs> bbsList = bbsWrapper.orElse(null);

        mav.addObject("bbsList", bbsList);
        mav.setViewName("pages/boardA");
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
                .bbsDate(new Date())
                .atchmnflId(id)
                .build();
        bbsRepository.save(bbs);
        model.addAttribute("id", bbs.getIdx());
        jsonObject.put("idx",bbs.getIdx());
        return jsonObject.toJSONString();
    }


    @GetMapping("/community/detailBoard")
    public ModelAndView detailBoard(ModelAndView mav,
                                    @RequestParam String idx){
        logger.info("idx : " + idx);
        Optional<Bbs> bbsWrapper = bbsRepository.findByIdx(Long.parseLong(idx));
        Bbs bbs = bbsWrapper.orElse(null);
        logger.info(String.valueOf(bbs.getCategory()));
        mav.addObject("bbs", bbs);
        mav.setViewName("pages/detailBoard");
        return mav;
    }

    @ResponseBody
    @PostMapping("/community/detailBoard")
    public String detailBoardPost(@RequestParam Map<String, Object> paramMap){
        logger.info("idx : " + paramMap.get("targetId"));

        //replace할거 아니면 redirect해야지
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idx", paramMap.get("targetId"));
        return jsonObject.toJSONString();
    }
}
