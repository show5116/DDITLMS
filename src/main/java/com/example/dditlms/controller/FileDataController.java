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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

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
    public ModelAndView storageList(ModelAndView mav, HttpServletResponse response) {
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
        String targetId = "user" + member.getUserNumber() + "/";

        if (rootFolder == null) {
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
            } catch (AmazonS3Exception e) {
            } catch (SdkClientException e) {
            }
        }

        // json parser
        List<FileData> selectAll = fileDataRepository.findAllByMember(member);
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        for(FileData fileData : selectAll){
            JSONObject data = new JSONObject();

            data.put("id", fileData.getFileIdx());

            if(fileData.getParent()==null){
                data.put("parent","#");
            } else {
                data.put("parent", fileData.getParent().getFileIdx());
            }
            data.put("text", fileData.getFileName());
            data.put("extension", fileData.getExtension());

            jsonArr.add(data);
        }

        jsonObj.put("data", jsonArr);
        System.out.println(jsonObj);
        logger.info(jsonObj.toJSONString());

        mav.addObject("jsonObj", jsonObj);
        mav.addObject("idx", rootFolder.getFileIdx());
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
        if (current.getParent() != null) {
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
    public ModelAndView addFolder(ModelAndView mav, @RequestBody FileDataDTO fileDataDTO) {
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        FileData current = fileService.addFolder(fileDataDTO, member);

        StringBuilder name = new StringBuilder();
        FileData parent = current.getParent();
        while (true) {
            name.insert(0, "/");
            name.insert(0, parent.getFileName());
            if (parent.getParent() == null) {
                System.out.println("끝");
                break;
            } else {
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
        } catch (AmazonS3Exception e) {
        } catch (SdkClientException e) {
        }

        if (current.getParent() != null) {
            mav.addObject("parentFolder", current.getParent().getFileIdx());
        }
        mav.addObject("selectFiles",
                fileDataRepository.findAllByMemberAndParentAndExtensionIsNot(member, current.getParent(), "folder"));
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, current.getParent(), "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }

    @PostMapping(value = "/cloud/upload")
    public ModelAndView uploadTest(
            @RequestParam(value = "parIdx", required = false) String parIdx,
            @RequestParam(value = "files") MultipartFile[] files,
            ModelAndView mav, HttpServletResponse response) {
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        String bucketName = "lms-project";

        Optional<FileData> parentWrapper = fileDataRepository.findByFileIdx(Integer.parseInt(parIdx));
        FileData parent = parentWrapper.orElse(null);
        FileData parent2 = parentWrapper.orElse(null);
        logger.info("parent" + parent);
        List<FileData> fileDataList = new ArrayList<>();
        for (MultipartFile file : files) {
            int idx = file.getOriginalFilename().lastIndexOf(".");

            logger.info("files : " + file);
            logger.info("origin : " + file.getOriginalFilename());
            logger.info("filename : " + file.getOriginalFilename().substring(0, idx));
            logger.info("extension : " + file.getOriginalFilename().substring(
                    idx, file.getOriginalFilename().length()));
            logger.info("size : " + file.getSize());
            logger.info("contentType : " + file.getContentType());
            logger.info("targetId : " + parIdx);


            String saveFileName = "/Users/inhwan/Documents/uploadThrough/" + file.getOriginalFilename();

            try (
                    FileOutputStream fos = new FileOutputStream(saveFileName);
                    InputStream is = file.getInputStream();
            ) {
                int readCount = 0;
                byte[] buffer = new byte[1024];
                while ((readCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, readCount);
                }
            } catch (Exception ex) {
                throw new RuntimeException("file Save Error");
            }
            StringBuilder fileName = new StringBuilder();

            while (true) {
                fileName.insert(0, "/");
                fileName.insert(0, parent.getFileName());
                if (parent.getParent() == null) {
                    System.out.println("끝");
                    break;
                } else {
                    parent = parent.getParent();
                }
            }
            System.out.println(fileName.toString());

            String objectName = fileName.toString() + file.getOriginalFilename();
            // 풀네임(폴더 계층형이요)
            String filePath = "/Users/inhwan/Documents/uploadThrough/" + file.getOriginalFilename();
            // 다운 받을 곳 path

            try {
                s3.putObject(bucketName, objectName, new File(filePath));
                System.out.format("Object %s has been created.\n", objectName);
            } catch (AmazonS3Exception e) {
                e.printStackTrace();
            } catch (SdkClientException e) {
                e.printStackTrace();
            }
            //파일 받아오는데 나중에 스케줄러 써서 삭제 시키는거도 해보기

//            String saveFileName = "/Users/inhwan/Documents/uploadThrough/"+file.getOriginalFilename();
//            File saveFile = new File(saveFileName);
            // 여기는 디비에서 받아와서 쓰는거라구 클릭시 idx가져와서 쏘는거야
//            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getOriginalFilename() + "\";");
//            response.setHeader("Content-Transfer-Encoding", "binary");
//            response.setHeader("Content-Type", file.getContentType());
//            response.setHeader("Content-Length", "" + saveFile.length());
//            response.setHeader("Pragma", "no-cache;");
//            response.setHeader("Expires", "-1;");

//            try(
//                FileInputStream fis = new FileInputStream(saveFileName);
//                OutputStream out = response.getOutputStream();
//                ) {
//                int readCount = 0;
//                byte[] buffer = new byte[1024];
//
//                while((readCount = fis.read(buffer)) != -1){
//                    out.write(buffer,0,readCount);
//                }
//            } catch (Exception ex) {
//                throw new RuntimeException("file Save Error");
//            }


            FileData fileData = FileData.builder()
                    .createTime(new Date())
                    .openTime(new Date())
                    .extension(file.getOriginalFilename().substring(
                            idx, file.getOriginalFilename().length()))
                    .fileName(file.getOriginalFilename().substring(0, idx))
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .trash(0)
                    .member(member)
                    .parent(parent2)
                    .build();

            fileDataList.add(fileData);
        }
        fileService.addFiles(fileDataList);

        if (parent2.getParent() != null) {
            mav.addObject("parentFolder", parent2.getParent().getFileIdx());
        }
        mav.addObject("selectFiles",
                fileDataRepository.findAllByMemberAndParentAndExtensionIsNot(member, parent2, "folder"));
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, parent2, "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }


    //아작스 안쓰고 a태그 쓸건데 idx값만 보내줄거야
    @GetMapping("/cloud/download/{id}")
    public ModelAndView download(
//            HttpServletResponse response,
            @PathVariable(value = "id") int fileTargetId,
            ModelAndView mav) {

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

//        String fileTargetId = paramMap.get("id");
        String bucketName = "lms-project";

//        System.out.println(fileTargetId);
//
        Optional<FileData> fileDataWrapper = fileDataRepository.findByFileIdx(fileTargetId);
//        FileData current = fileDataWrapper.orElse(null);
        FileData current2 = fileDataWrapper.orElse(null);
//        StringBuilder sb = new StringBuilder();
//        while (true){
//            sb.insert(0, "/");
//            sb.insert(0, current.getFileName());
//            if(current.getParent()==null){
//                sb.setLength(sb.length()-1);
//                sb.append(current2.getExtension());
//                System.out.println("끝");
//                break;
//            } else {
//                current = current.getParent();
//            }
//        }
//        logger.info("sb : " + sb.toString());
        String filename = current2.getFileName() + current2.getExtension();
        StringBuilder URLaddress = new StringBuilder();
        URLaddress.append("https://kr.object.ncloudstorage.com/");
        URLaddress.append(bucketName);
        URLaddress.append("/user");
        URLaddress.append(member.getUserNumber());
        URLaddress.append("/");
        String encodeURL = null;
        try {
            encodeURL = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info(encodeURL);

        URLaddress.append(encodeURL);
        // 이거 인코딩 해야함.
        logger.info(URLaddress.toString());
        mav.setViewName("redirect:" + URLaddress.toString());

        return mav;
    }


}
//        int Idx = fileTargetId.lastIndexOf("/")+1;
//        String objectName = fileTargetId.substring(Idx,fileTargetId.length());
        // 파일 이름만 적으니까 이거 뜸 The specified key does not exist.(지정한 키가 존재하지 않습니다.)(결국 경로상에 이미지 까지 적어줘야한다는 거)
//        String objectName = sb.toString();
        // 왠지 다운로드 할 파일의 이름을 풀로 적어주는 것 같다.(파일의 이름 or 경로)
//        String objectName = "user1/135%20x%20155%20inhwan2.jpg";
//        String targetId = fileTargetId.substring(0, Idx);
//        System.out.println("targetId입니다 다운 : " + targetId);




        // 다운때 이름만 받아서
//        File saveFile = new File(objectName);
//        response.setContentType("text/html; charset=utf-8");
//        response.setCharacterEncoding("utf-8");

//        String encordedFilename = null;
//        try {
//            encordedFilename = URLEncoder.encode(filename,"UTF-8").replace("+", "%20");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        response.setHeader("Content-Disposition", "attachment; filename=" + encordedFilename + ";filename*=UTF-8''"+encordedFilename);
//            response.setHeader("Content-Transfer-Encoding", "binary");
//            response.setHeader("Content-Type", current2.getContentType());
//            response.setHeader("Content-Length", "" + saveFile.length());
//            response.setHeader("Pragma", "no-cache;");
//            response.setHeader("Expires", "-1;");

//        System.out.println("objectName : "+objectName);
//        String downloadFilePath = fileTargetId;
//        String downloadFilePath = "/Users/inhwan/Documents/uploadThrough/"+ current2.getFileName()+current2.getExtension();
//        다운로드 할 경로 같은데 아무리 봐도 근데 또 없으면 없다고 뭐라함.
        // 이거 파일 적으면 directory없다고 뭐라함
//        String downloadFilePath = targetId;
        //"/폴더 위치/아까 다운 받는애 이름

        // 다운의 경로를 받아서
// download object
//        try {
//            System.out.println("안나오니?");
//            S3Object s3Object = s3.getObject(bucketName, objectName);
//            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
//
//            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath));
//            byte[] bytesArray = new byte[4096];
//            int bytesRead = -1;
//            while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
//                outputStream.write(bytesArray, 0, bytesRead);
//            }
//
//            outputStream.close();
//            s3ObjectInputStream.close();
//            System.out.format("Object %s has been downloaded.\n", objectName);
//        } catch (AmazonS3Exception e) {}
//        catch(SdkClientException e) {}
//        catch (FileNotFoundException e) {}
//        catch (IOException e) {}
//    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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

////        for(MultipartFile file : paramFile){
////            File file1 = new File(file);
//

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