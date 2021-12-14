package com.example.dditlms.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
//import com.example.dditlms.service.FileService;
import com.example.dditlms.util.AmazonS3Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Date;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FileDataController {
    // 메인 root URL - /cloud
    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);

    private final AmazonS3Util amazonS3Util;

//    private final FileService fileService;

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
    public String storageList(Model model) {
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }
        String targetId = "user" + member.getUserNumber() +"/";
        //String targetId = "user" + member.getUserNumber() + "/";

        ObjectListing objectListing = amazonS3Util.getAmazon(targetId);

        model.addAttribute("currentLocation", targetId);
        model.addAttribute("objectListing", objectListing);

        return "pages/filemanager";
    }

    @PostMapping("/cloud/storageList")
    public String storageListPost(Model model, @RequestParam Map<String, String> paramMap) {

        String targetId = paramMap.get("targetId");
        int Idx = targetId.substring(0, targetId.length() - 1).lastIndexOf("/");

        String result = null;
        if (Idx == -1) {
            result = targetId;
        } else {
            result = targetId.substring(0, Idx) + "/";
        }

        ObjectListing objectListing = amazonS3Util.getAmazon(targetId);

        model.addAttribute("currentLocation", result);
        model.addAttribute("objectListing", objectListing);

        return "pages/filemanager :: #folderReplace";
    }

    @PostMapping("/cloud/folderUpload")
    public String upload(Model model,
                         @RequestParam Map<String, Object> paramMap){

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {}

        String bucketName = "lms-project";


        String targetId = (String) paramMap.get("targetId");

        if(paramMap.get("targetId") == null || "".equals(paramMap.get("targetId"))){
            targetId = "user" + member.getUserNumber() + "/";
        }
        System.out.println(targetId);


    // create folder
            String folderName = targetId + paramMap.get("createFolder") + "/";
            // 폴더 이름 + 경로도 같이해서 만드는게 아닐까?

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


        int Idx = targetId.substring(0, targetId.length() - 1).lastIndexOf("/");

        String result = null;
        Integer parent = null;
        if (Idx == -1) {
            result = targetId;
            System.out.println("이거 뭐나오게 " + targetId.substring(0,targetId.length()-1));
        } else {
            result = targetId.substring(0, Idx) + "/";
            System.out.println("이거 뭐나오게 " + targetId.substring(Idx+1,targetId.length()-1));
//            parent = 리스트를 돌려봐서 return idx를 하는데 where조건이
//            filename.equals(targetId.substring(Idx+1,targetId.length()-1))인 것을 찾아서
        }

        ObjectListing objectListing = amazonS3Util.getAmazon(targetId);

//        FileData fileData = null;
//        fileData = FileData.builder()
//                .fileName((String) paramMap.get("createFolder"))
//                .extension("folder")
//                .createTime(new Date())
//                .openTime(new Date())
//                .member(member)
//                .fileSize(0L)
//                .parentId(parent)
//                .trash(0)
//                .build();

        model.addAttribute("currentLocation", result);
        model.addAttribute("objectListing", objectListing);


        return "pages/filemanager :: #folderReplace";
    }


    @PostMapping("/cloud/fileUpload")
    public String fileUpload(Model model, @RequestParam Map<String, Object> paramMap){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {}

        String bucketName = "lms-project";


        String targetId = (String) paramMap.get("targetId");

        if(paramMap.get("targetId") == null || "".equals(paramMap.get("targetId"))){
            targetId = "user" + member.getUserNumber() + "/";
        }
        System.out.println(targetId);

        // upload local file
        System.out.println("value : " + (String) paramMap.get("value"));
//        String objectName = targetId;
        String objectName = "user1/everybody.txt";
        //        // 오브젝트의 이름 (여기 왠지 폴더가 대상이고 그 안에 넣는다는 의미 같음)그럼 targetId가 필요하죠
        String filePath = (String) paramMap.get("value");

//            String filePath = "/tmp/sample.txt";
        //        // 컴퓨터상의 파일의 경로(구하는 방법을 찾아야 겠죠?) 드롭존에 올리면 그 filePath요
        //
        try {
            s3.putObject(bucketName, objectName, new File(filePath));
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {}
        catch(SdkClientException e) {}


        int Idx = targetId.substring(0, targetId.length() - 1).lastIndexOf("/");

        String result = null;
        if (Idx == -1) {
            result = targetId;
        } else {
            result = targetId.substring(0, Idx) + "/";
        }
        ObjectListing objectListing = amazonS3Util.getAmazon(targetId);
        model.addAttribute("currentLocation", result);
        model.addAttribute("objectListing", objectListing);


        return "pages/filemanager :: #folderReplace";
    }




    @GetMapping("/cloud/download")
    public String download(@RequestParam Map<String, String> paramMap, Model model){
        String fileTargetId = paramMap.get("fileTargetId");
        String bucketName = "lms-project";

        System.out.println(fileTargetId);
        int Idx = fileTargetId.lastIndexOf("/")+1;
//        String objectName = fileTargetId.substring(Idx,fileTargetId.length());
        // 파일 이름만 적으니까 이거 뜸 The specified key does not exist.(지정한 키가 존재하지 않습니다.)(결국 경로상에 이미지 까지 적어줘야한다는 거)
        String objectName = fileTargetId;
        // 왠지 다운로드 할 파일의 이름을 풀로 적어주는 것 같다.(파일의 이름 or 경로)
//        String objectName = "user1/135%20x%20155%20inhwan2.jpg";
        String targetId = fileTargetId.substring(0, Idx);
        System.out.println("targetId입니다 다운 : " + targetId);
        // 다운때 이름만 받아서
        System.out.println("objectName : "+objectName);
//        String downloadFilePath = fileTargetId;
        String downloadFilePath = "/Users/inhwan/Documents/"+fileTargetId.substring(Idx,fileTargetId.length());
//        다운로드 할 경로 같은데 아무리 봐도 근데 또 없으면 없다고 뭐라함.
        // 이거 파일 적으면 directory없다고 뭐라함
//        String downloadFilePath = targetId;
        //"/폴더 위치/아까 다운 받는애 이름

        // 다운의 경로를 받아서
// download object
        try {
            System.out.println("안나오니?");
            S3Object s3Object = s3.getObject(bucketName, objectName);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath));
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
                outputStream.write(bytesArray, 0, bytesRead);
            }

            outputStream.close();
            s3ObjectInputStream.close();
            System.out.format("Object %s has been downloaded.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ObjectListing objectListing = amazonS3Util.getAmazon(targetId);
        model.addAttribute("objectListing", objectListing);

        return "pages/filemanager";
    }




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