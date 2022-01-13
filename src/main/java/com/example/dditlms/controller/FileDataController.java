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
import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.FileDataRepository;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.FileService;
import com.example.dditlms.util.AmazonS3Util;
import com.example.dditlms.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.tools.shell.IO;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequiredArgsConstructor
public class FileDataController {
    // 메인 root URL - /cloud
    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);

    private final AmazonS3Util amazonS3Util;
    private final FileUtil fileUtil;

    private final FileService fileService;
    private final FileDataRepository fileDataRepository;
    private final HistoryRepository historyRepository;


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


        List<History> list = historyRepository.getAll();
        logger.info(String.valueOf("history" + list));

        Map<String, Object> map = new HashMap<>();
        fileService.parentChk(map);     // 부모 체크 후 없으면 루트 폴더 생성
        fileService.jstree(map);        // jstree 구조

        FileData rootFolder = (FileData) map.get("rootFolder");
        JSONObject jsonObj = (JSONObject) map.get("jsonObj");
        Member member = (Member) map.get("member");
        map.put("rootFolder", rootFolder);

        fileService.tokenService(map);
        List<FileDataDTO> dtoList = (List<FileDataDTO>) map.get("dtoList");

        String rootPath = System.getProperty("user.dir");
        logger.info("루트패스: "+rootPath);

        Path directoryPath = Paths.get(rootPath+File.separator+"tmp");
//        Path directoryPath = Paths.get(rootPath+"\\tmp");

        try {
            // 없으면 디렉토리 생성
            Files.createDirectory(directoryPath);

            logger.info(directoryPath + "디렉토리가 생성되었습니다.");
        } catch (FileAlreadyExistsException e){
            logger.info("이미 디렉토리 있어");
        } catch (NoSuchFileException e){
            logger.info("디렉토리 경로가 없어");
        } catch (IOException e){
            e.printStackTrace();
        }


        mav.addObject("jsonObj", jsonObj);
        mav.addObject("idx", rootFolder.getFileIdx());
        mav.addObject("selectFiles",
                dtoList);
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, rootFolder, "folder"));
        mav.setViewName("pages/filemanager");

        return mav;
    }

    @PostMapping("/cloud/storageList")
    public ModelAndView storageListPost(ModelAndView mav, @RequestParam Map<String, Object> paramMap) {

        Map<String, Object> map = new HashMap<>();
        map.put("idx1", (String) paramMap.get("targetId"));

        fileService.changeView(map);    // view 전환되는 시점에서 부분만 ajax로 replace함.
        FileData current = (FileData) map.get("current");
        Member member = (Member) map.get("member");
        map.put("rootFolder", current);

        fileService.tokenService(map);
        List<FileDataDTO> dtoList = (List<FileDataDTO>) map.get("dtoList");

        if (current.getParent() != null) {
            mav.addObject("parentFolder", current.getParent().getFileIdx());
        }
        mav.addObject("selectFiles",
                dtoList);
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, current, "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }

    @PostMapping("/cloud/addFolder")
    public ModelAndView addFolder(ModelAndView mav, @RequestBody FileDataDTO fileDataDTO) {

        Map<String, Object> map = new HashMap<>();

        fileService.addFolder(fileDataDTO, map);    // 새로운 폴더 생성

        FileData current = (FileData) map.get("current");
        Member member = (Member) map.get("member");
        map.put("rootFolder", current.getParent());

        fileService.tokenService(map);
        List<FileDataDTO> dtoList = (List<FileDataDTO>) map.get("dtoList");
        logger.info("current : " + current.getFileName());
        logger.info("parent : " + current.getParent().getFileName());
        logger.info("member : " + member);

        if (current.getParent().getParent() != null) {
            mav.addObject("parentFolder", current.getParent().getParent().getFileIdx());
        }
        mav.addObject("selectFiles",
                dtoList);
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, current.getParent(), "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }

    @PostMapping(value = "/cloud/upload")
    public ModelAndView uploadTest(
            @RequestParam(value = "parIdx", required = false) String parIdx,
            @RequestParam(value = "files") MultipartFile[] files,
            ModelAndView mav) {

        Map<String, Object> map = new HashMap<>();
        map.put("parIdx", parIdx);
        map.put("files", files);

        fileService.upload(map);


        Member member = (Member) map.get("member");
        FileData parent2 = (FileData) map.get("parent2");

        map.put("rootFolder", parent2);

        fileService.tokenService(map);
        List<FileDataDTO> dtoList = (List<FileDataDTO>) map.get("dtoList");


        if (parent2.getParent() != null) {
            mav.addObject("parentFolder", parent2.getParent().getFileIdx());
        }
        mav.addObject("selectFiles",
                dtoList);
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, parent2, "folder"));
        mav.setViewName("pages/filemanager :: #folderReplace");

        return mav;
    }

    @GetMapping("/cloud/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(
            @PathVariable(value = "id") String token
    ){

            Map<String, String> result = fileUtil.getDownToken(token);
            if(result.get("success").equals("N")){
                return null;
            }
            String cloudToken = result.get("cloudToken");
            logger.info("result : " + cloudToken);
            int fileTargetId = Integer.parseInt(cloudToken);

        logger.info("fileIdx : " + fileTargetId);

        Optional<FileData> fileDataWrapper = fileDataRepository.findByFileIdx(fileTargetId);
        FileData fileData = fileDataWrapper.orElse(null);

        String bucketName = "lms-project";
        FileData current = fileDataWrapper.orElse(null);
        FileData current2 = fileDataWrapper.orElse(null);
        StringBuilder sb = new StringBuilder();
        while (true){
            sb.insert(0, "/");
            sb.insert(0, current.getFileName());
            if(current.getParent()==null){
                sb.setLength(sb.length()-1);
                sb.append(current2.getExtension());
                System.out.println("끝");
                break;
            } else {
                current = current.getParent();
            }
        }
        logger.info("sb : " + sb.toString());
        String filename = current2.getFileName() + current2.getExtension();

        String objectName = sb.toString();
//        String downloadFilePath = "/Users/inhwan/Documents/uploadThrough/"+filename;        // 이 경로 대신 /tmp로 해버리면 지우지 않아도 될까?
        String downloadFilePath = "tmp"+File.separator+filename;        // 이 경로 대신 /tmp로 해버리면 지우지 않아도 될까?

// download object
        try {
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
        } catch(SdkClientException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Resource resource = new FileSystemResource("/Users/inhwan/Documents/uploadThrough/"+filename);      // 그럼 여기도 tmp에서 꺼내와야해
        Resource resource = new FileSystemResource("tmp"+File.separator+filename);      // 그럼 여기도 tmp에서 꺼내와야해


        String resourceName = resource.getFilename();

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Disposition", "attachment; filename=" + new String(resourceName.getBytes("UTF-8"),
                    "ISO-8859-1"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/cloud/deleteObject")
    public ModelAndView deleteObject(ModelAndView mav, @RequestParam Map<String, Object> paramMap) {
        String objectIdx = (String) paramMap.get("objectIdx");
        logger.info("objectIdx : " + Integer.parseInt(objectIdx));
        Optional<FileData> currentWrapper = fileDataRepository.findByFileIdx(Integer.parseInt(objectIdx));
        FileData current = currentWrapper.orElse(null);
        Map<String, Object> map = new HashMap<>();
        fileDataRepository.delete(current);



        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        map.put("rootFolder", current.getParent());

        fileService.tokenService(map);

        List<FileDataDTO> dtoList = (List<FileDataDTO>) map.get("dtoList");


        if (current.getParent().getParent() != null) {
            mav.addObject("parentFolder", current.getParent().getParent().getFileIdx());
        }
        mav.addObject("selectFiles",
                dtoList);
        mav.addObject("selectFolder",
                fileDataRepository.findAllByMemberAndParentAndExtension(member, current.getParent(), "folder"));

        mav.setViewName("pages/filemanager :: #folderReplace");
        return mav;
    }


    @PostMapping("/cloud/chkList")
    public ModelAndView chkList(@RequestBody Map<String, Object> paramMap, ModelAndView mav) {
        List<String> chkArr = (List<String>) paramMap.get("chkArr");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chkArr",chkArr);
        logger.info("chkArr : " + chkArr);

        String token = fileUtil.makeZipDownToken(chkArr);
        logger.info("token : " + token);

        mav.addObject("chkArr", token);
        // 여기서 토큰으로 쏴주고 근데 배열은 토큰 음 보자

        mav.setViewName("pages/filemanager :: #zipReplace");

        return mav;
    }

    @GetMapping("/cloud/zipDownload/{chkArr}")
    public void CompressZip(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @PathVariable(value = "chkArr") String token){

        //폴더가 있으면 폴더 이름만 두고 폴더를 생성? 그 안에 file넣고 또 폴더 있으면 폴더 만들고 file넣고
        String bucketName = "lms-project";

        Map<String, List<FileData>> chkArr = (Map<String, List<FileData>>)fileUtil.getZipDownToken(token);
        List<FileData> zipToken = chkArr.get("cloudToken");
        logger.info("token : " + token);
        logger.info("리스트 사이즈 궁금해서 : " + zipToken.size());
        String[] files = new String[zipToken.size()];
        Integer[] filesIdx = new Integer[zipToken.size()];
        int i = 0;
        for(FileData fileData : zipToken){

            String fileName = fileData.getFileName()+fileData.getExtension();
            files[i] = fileName;
            filesIdx[i] = fileData.getFileIdx();
            logger.info("fileName : " + fileName);
            i++;
        }

        Optional<FileData> fileDataWrapper = fileDataRepository.findByFileIdx(filesIdx[0]);
        FileData current = fileDataWrapper.orElse(null);
        FileData parent = current.getParent();
        StringBuilder sb = new StringBuilder();
        while (true){
            sb.insert(0,"/");
            sb.insert(0, parent.getFileName());
            if(parent.getParent()==null) {
                System.out.println("끝!");
                break;
            } else {
                parent = parent.getParent();
            }
        }
        logger.info("여기 sb는 : " + sb.toString());


//        String[] files = {"3-3M2블록(국임)추가입주자모집공고문최종(21.11.26).pdf",
//                "01.수행계획서.hwp"};


        ZipOutputStream zout = null;
        String zipName = "아카이브.zip";
        String tempPath = "";

        String rootPath = System.getProperty("user.dir");
        logger.info("루트패스: "+rootPath);

        if(files.length > 0){
            try{
//                tempPath = "/Users/inhwan/Documents/uploadThrough";
                tempPath = rootPath+File.separator+"tmp"+File.separator;

//                zout = new ZipOutputStream(new FileOutputStream(tempPath + "/" + zipName));
                zout = new ZipOutputStream(new FileOutputStream(tempPath + File.separator + zipName));
                byte[] buffer = new byte[1024];
                FileInputStream in = null;

                for(int k=0; k<files.length; k++){
                    //여기서 다운로드 tmp 폴더에 s3집어넣고

                    S3Object s3Object = s3.getObject(bucketName, sb.toString()+files[k]);
                    S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("tmp"+ File.separator+files[k]));
                    byte[] bytesArray = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1){
                        outputStream.write(bytesArray, 0, bytesRead);
                    }
                    outputStream.close();
                    s3ObjectInputStream.close();

                    //
                    in = new FileInputStream(tempPath + files[k]);
//                    in = new FileInputStream("/Users/inhwan/Documents/uploadThrough/" + files[k]);
                    zout.putNextEntry(new ZipEntry(files[k]));

                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zout.write(buffer, 0, len);
                    }

                    zout.closeEntry();
                    in.close();
                }

                zout.close();

                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(
                        zipName.getBytes("UTF-8"), "ISO-8859-1"));

                FileInputStream fis = new FileInputStream(tempPath + File.separator + zipName);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ServletOutputStream so = response.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(so);

                int n = 0;
                while ((n = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, n);
                    bos.flush();
                }

                if(bos != null) bos.close();
                if(bis != null) bis.close();
                if(so != null) so.close();
                if(fis != null) fis.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(zout != null) {
                    zout = null;
                }
            }
        }
    }

}