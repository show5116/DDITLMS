package com.example.dditlms.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.example.dditlms.domain.dto.FileDataDTO;
import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.FileDataRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.FileService;
import com.example.dditlms.util.AmazonS3Util;
import com.example.dditlms.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class FileDataController {
    // 메인 root URL - /cloud
    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);

    private final AmazonS3Util amazonS3Util;
    private final FileUtil fileUtil;

    private final FileService fileService;
    private final FileDataRepository fileDataRepository;

    final String endPoint = "https://kr.object.ncloudstorage.com";
    final String regionName = "kr-standard";
    final String accessKey = "qodG8mADcmgoeNxQMQJQ";
    final String secretKey = "KYRW6G83WxwePjZr7xmQEWkXc3n3kcoKvTBnwctz";

    // S3 client
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();





    @GetMapping("/cloud")
    public ModelAndView storageList(ModelAndView mav) {
        // 멤버 세션 객체
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }
        //조회
        Optional<FileData> rootFolderWrapper = fileDataRepository.findByMemberAndParentIsNull(member);
        FileData rootFolder = rootFolderWrapper.orElse(null);
        String targetId = "user"+member.getUserNumber()+"/";

        if(rootFolder==null){
            // 비어있다면 폴더 생성
            rootFolder = fileService.addRootFolder(member);
            String bucketName = "lms-project";
            ObjectListing objectListing = amazonS3Util.getAmazon(targetId);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0L);
            objectMetadata.setContentType("application/x-directory");
            // 생성 유형 - 폴더
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, targetId, new ByteArrayInputStream(new byte[0]), objectMetadata);

            try {
                s3.putObject(putObjectRequest);
                System.out.format("Folder %s has been created.\n", targetId);
            } catch (AmazonS3Exception e) {}
            catch(SdkClientException e) {}
        }
        mav.addObject("idx",rootFolder.getFileIdx());
        mav.addObject("selectFiles",
                fileDataRepository.findAllByMemberAndParentAndExtensionIsNot(member, rootFolder, "folder"));
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, rootFolder, "folder"));
        mav.setViewName("pages/filemanager");

        return mav;
    }

    @PostMapping("/cloud/storageList")
    public ModelAndView storageListPost(ModelAndView mav, @RequestParam Map<String, Object> paramMap) {
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        String idx1 = (String) paramMap.get("targetId");
        Integer idx = Integer.parseInt(idx1);
        Optional<FileData> currentWrapper = fileDataRepository.findByFileIdx(idx);
        FileData current = currentWrapper.orElse(null);
//        List<FileData> selectFolder = fileDataRepository.findAllByMemberAndParent(member,current);
        if(current.getParent()!=null){
            mav.addObject("parentFolder", current.getParent().getFileIdx());
        }
//        mav.addObject("selectFolder", selectFolder);
        mav.addObject("selectFiles",
                fileDataRepository.findAllByMemberAndParentAndExtensionIsNot(member, current, "folder"));
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, current, "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }

    @PostMapping("/cloud/addFolder")
    public ModelAndView addFolder(ModelAndView mav, @RequestBody FileDataDTO fileDataDTO){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        FileData current = fileService.addFolder(fileDataDTO, member);

        StringBuilder name = new StringBuilder();
        FileData parent = current.getParent();
        while(true){
            name.insert(0,"/");
            name.insert(0,parent.getFileName());
            if(parent.getParent()==null){
                System.out.println("끝");
                break;
            }else{
                parent = parent.getParent();
            }
        }
        name.append(current.getFileName());
        name.append("/");
        logger.info(name.toString());

        String bucketName = "lms-project";
        String folderName = name.toString();
        // 집어 넣을때 parent(name) + "/" + idx(name)

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        // 생성 유형 - 폴더
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            System.out.format("Folder %s has been created.\n", folderName);
        } catch (AmazonS3Exception e) {}
        catch(SdkClientException e) {}

        mav.addObject("selectFiles",
                fileDataRepository.findAllByMemberAndParentAndExtensionIsNot(member, current.getParent(), "folder"));
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, current.getParent(), "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }

    @PostMapping("/cloud/upload")
    public ModelAndView uploadTest(@RequestParam(value = "file", required = false) MultipartFile file,
                           MultipartHttpServletRequest request, ModelAndView mav){
        Map<String, MultipartFile> map = request.getFileMap();
        long id = fileUtil.uploadFiles(map);
        System.out.println(map);
        System.out.println(id);
        return mav;
    }


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


//    @GetMapping("/cloud")
//    public String storageList(Model model) {
//        Member member = null;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        try {
//            member = ((AccountContext) authentication.getPrincipal()).getMember();
//        } catch (ClassCastException e) {
//        }
//        String targetId = "user" + member.getUserNumber() +"/";
//        //String targetId = "user" + member.getUserNumber() + "/";
//
//        ObjectListing objectListing = amazonS3Util.getAmazon(targetId);
//
//        model.addAttribute("currentLocation", targetId);
//        model.addAttribute("objectListing", objectListing);
//
//        return "pages/filemanager";
//    }


// //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



}


//    @GetMapping("/cloud/fileManager")
//    public String folderManager(Model model, HttpServletRequest request) {
//        String extension = "folder";
//        int parentId = 1;
//
//        String path = request.getSession().getServletContext().getRealPath("/source1");
//        System.out.println(path);
//        System.out.println(path);
//        System.out.println(path);
//        System.out.println(path);
//
//        List<FileData> List = fileDataRepository.findTop4ByParentIdOrderByOpenTimeDesc(parentId);
//        List<FileData> folderList = fileDataRepository.findAllByExtensionAndParentId(extension, parentId);
//        List<FileData> fileList = fileDataRepository.findAllByExtensionNotAndParentId(extension, parentId);
//
//        model.addAttribute("List", List);
//        model.addAttribute("folderList", folderList);
//        model.addAttribute("fileList", fileList);
//
//        return "pages/filemanager";
//    }
//
//    @GetMapping("/cloud/fileManager/{id}")
//    public String folderManagerGet(@PathVariable(value = "id") int fileIdx, Model model) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        Member member = ((AccountContext) authentication.getPrincipal()).getMember();
//        String extension = "folder";
//
//        List<FileData> List = fileDataRepository.findTop4ByParentIdOrderByOpenTimeDesc(fileIdx);
//        List<FileData> folderList = fileDataRepository.findAllByExtensionAndParentId(extension, fileIdx);
//        List<FileData> fileList = fileDataRepository.findAllByExtensionNotAndParentId(extension, fileIdx);
//
//
////        for (FileData fol : folderList) {
////            File file = new File("/Users/inhwan/Desktop/경로파일들 모음/uploadFiles/1/" + fol.getFileName());
////            System.out.println("파일의 리스트 : " + file.list());
////        }
//        logger.info("folderList : " + folderList);
//        model.addAttribute("List", List);
//        model.addAttribute("folderList", folderList);
//        model.addAttribute("fileList", fileList);
//
//
////        return "redirect:/file/fileManager";
//        return "pages/filemanager";
//    }

//    @PostMapping("/cloud/fileManager")
//    public String fileUpload(@RequestParam(value = "paramFile", required = false) MultipartFile paramFile) {
//        //required = false 를 사용하는 이유는 NULL에러를 막기위해서고 그를 위해 값을 설정해주면 일단 돌아는감.
//        logger.info("아무거나");
//        logger.info("아무거나");
//        logger.info("아무거나");
//        logger.info("아무거나");
////        for(MultipartFile file : paramFile){
////            File file1 = new File(file);
//
//
//        logger.info("2341432t3421");
//        logger.info("getName : " + paramFile.getName());
//        logger.info("getSize : " + paramFile.getSize());
////        }
////        FileData fileData = FileData.builder()
////                .fileName("qw")
////                .build();
//        return "pages/filemanager";
//    }
//
//    @PostMapping("/cloud/folder")
//    public ModelAndView rename(ModelAndView mav, MultipartFile file, HttpServletRequest request
//            , @RequestParam("folderName") String folderName
//    ){
//        String path = request.getSession().getServletContext().getRealPath("/");
//        System.out.println(path);
//        System.out.println(path);
//        System.out.println(path);
//        System.out.println(path);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Member member = ((AccountContext)authentication.getPrincipal()).getMember();
//        String rootFolder = "/Users/inhwan/Desktop/경로 파일들 모음/uploadFiles/" + member.getUserNumber();
//        String addFolder = "/"+folderName;
//        // 유저 경로(/pages/fileManager에 적합) but 없어도 상관없으니 뺌
//        // 여기는 그냥 유저경로에 추가적인애들 뒤에 ""에 경로 따로 +
//
//        String createFolder = rootFolder + addFolder;
//        //경로 생성
//
//        int i = 2;
//        while (true){
//            if ( ! new File(createFolder).exists()) {
//                new File(createFolder).mkdirs();
//                System.out.println("1");
//                break;
//            } else if( ! new File(createFolder+"("+i+")").exists()){
//                System.out.println("2");
//                new File(createFolder+"("+i+")").mkdirs();
//                break;
//            } else {
//                System.out.println("3");
//                i++;
//            }
//        }
//
//        File dir = new File(createFolder);    // rootFolder을 파일로 읽기 위한 방법
//
//        System.out.println(member);
//        System.out.println(member.getMemberId());
//
//        mav.setViewName("/cloud/fileManager");
//        return mav;
//    }