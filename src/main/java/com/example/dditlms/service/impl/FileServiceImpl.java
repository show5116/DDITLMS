package com.example.dditlms.service.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileDataRepository fileDataRepository;
    private final AmazonS3Util amazonS3Util;
    private final FileUtil fileUtil;



    final String endPoint = "https://kr.object.ncloudstorage.com";
    final String regionName = "kr-standard";
    final String accessKey = "qodG8mADcmgoeNxQMQJQ";
    final String secretKey = "KYRW6G83WxwePjZr7xmQEWkXc3n3kcoKvTBnwctz";

    // S3 client
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    @Transactional
    @Override
    public FileData addRootFolder(Member member){
//        Entity에서 할거임 백에서만이니까
        FileData fileData = FileData.builder()
                .member(member)
                .fileName("user"+member.getUserNumber())
                .extension("folder")
                .fileSize(0L)
                .createTime(new Date())
                .openTime(new Date())
                .trash(0)
                .contentType("application/x-directory")
                .build();

        fileDataRepository.save(fileData);
        return fileData;
    }

    @Transactional
    @Override
    public void addFolder(FileDataDTO fileDataDTO, Map<String, Object> map) {
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        FileData fileData = fileDataDTO.toEntity();

        Optional<FileData> parentFolderWrapper = fileDataRepository.findByFileIdx(fileDataDTO.getParentId());
        FileData parentFolder = parentFolderWrapper.orElse(null);

        fileData.setMember(member);
        fileData.setParent(parentFolder);
        fileData.setCreateTime(new Date());
        fileDataRepository.save(fileData);

//        FileData current = fileData;
        StringBuilder name = new StringBuilder();
        FileData parent = fileData.getParent();
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
        name.append(fileData.getFileName());
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

        map.put("member", member);
        map.put("current", fileData);


    }

    @Override
    public void addFiles(List<FileData> fileDataList) {
        fileDataRepository.saveAll(fileDataList);
    }

    @Override
    public void parentChk(Map<String, Object> map){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }
        Optional<FileData> rootFolderWrapper = fileDataRepository.findByMemberAndParentIsNull(member);
        FileData rootFolder = rootFolderWrapper.orElse(null);
        String targetId = "user" + member.getUserNumber() + "/";

        if (rootFolder == null) {
            // 비어있다면 폴더 생성
            rootFolder = addRootFolder(member);
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
        map.put("member", member);
        map.put("rootFolder", rootFolder);

    }


    @Override
    public void jstree(Map<String, Object> map){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

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
            if(fileData.getExtension().equals("folder")) {
//                data.put("icon", "icofont icofont-folder font-theme");
                data.put("icon", "icofont icofont-ui-folder font-theme");
            } else if(fileData.getFileIdx()==1){
                data.put("icon", "icofont icofont-ui-folder font-theme");
            } else {
                data.put("icon", "icofont icofont-file-alt font-dark");
            }


            jsonArr.add(data);
        }

        jsonObj.put("data", jsonArr);
        System.out.println(jsonObj);
        logger.info(jsonObj.toJSONString());

        map.put("jsonObj", jsonObj);
    }

    @Override
    public void changeView(Map<String, Object> map){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }
        String idx1 = (String) map.get("idx1");
        Integer idx = Integer.parseInt(idx1);

        Optional<FileData> currentWrapper = fileDataRepository.findByFileIdx(idx);
        FileData current = currentWrapper.orElse(null);

        map.put("member", member);
        map.put("current", current);
    }

    @Override
    public void upload(Map<String, Object> map){

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        MultipartFile[] files = (MultipartFile[]) map.get("files");
        String parIdx = (String) map.get("parIdx");


        String bucketName = "lms-project";

        Optional<FileData> parentWrapper = fileDataRepository.findByFileIdx(Integer.parseInt(parIdx));
        FileData parent = parentWrapper.orElse(null);
        FileData parent2 = parentWrapper.orElse(null);

        List<FileData> fileDataList = new ArrayList<>();
        for (MultipartFile file : files) {
            int idx = file.getOriginalFilename().lastIndexOf(".");

            String saveFileName = "/Users/inhwan/Documents/uploadThrough/" + file.getOriginalFilename();
///Users/inhwan/Documents/uploadThrough
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
        addFiles(fileDataList);

        map.put("member", member);
        map.put("parent2", parent2);

    }

    @Override
    public void tokenService(Map<String, Object> map){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }


        FileData rootFolder = (FileData) map.get("rootFolder"); // root

        List<FileData> list = fileDataRepository.findAllByMemberAndParentAndExtensionIsNot(member, rootFolder, "folder");

        List<FileDataDTO> dtoList = new ArrayList<>();
        for(FileData fileData: list){
            FileDataDTO fileDataDTO = fileData.toDTO();
            String token = fileUtil.makeFileDownToken(fileData.getFileIdx());
            fileDataDTO.setToken(token);
            dtoList.add(fileDataDTO);
        }

        map.put("dtoList", dtoList);

    }




}
