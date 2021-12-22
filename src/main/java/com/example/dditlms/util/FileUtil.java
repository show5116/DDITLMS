package com.example.dditlms.util;

import com.example.dditlms.domain.entity.Attachment;
import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.AttachmentRepository;
import com.example.dditlms.domain.repository.FileDataRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.security.JwtSecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUtil {

    private final AttachmentRepository attachmentRepository;
    private final FileDataRepository fileDataRepository;

    private final JwtSecurityService jwtSecurityService;

    public String makeFileToken(long id, int order){
        Member member =null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        StringBuilder originToken = new StringBuilder();
        originToken.append(member.getUserNumber());
        originToken.append("&");
        originToken.append(id);
        originToken.append("&");
        originToken.append(order);
        return jwtSecurityService.createToken(originToken.toString(),60000L);
    }

    public String makeFileDownToken(int fileIdx){
        Member member =null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        StringBuilder originToken = new StringBuilder();
        originToken.append(member.getUserNumber());
        originToken.append("&");
        originToken.append(fileIdx);

        return jwtSecurityService.createToken(originToken.toString(),60000L);
    }

    public Map<String,String> getToken(String token){
        Map<String,String> map = new HashMap<String, String>();
        String parseToken = null;
        try{
            parseToken = jwtSecurityService.getToken(token);
        }catch (ExpiredJwtException e){
            map.put("success","N");
            return map;
        }
        String[] parses = parseToken.split("&");
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        if(member.getUserNumber() != Long.parseLong(parses[0])){
            map.put("success","N");
            return map;
        }
        Optional<Attachment> attachmentWrapper = attachmentRepository.findByIdAndOrder(Long.parseLong(parses[1]),Integer.parseInt(parses[2]));
        Attachment attachment = attachmentWrapper.orElse(null);

        map.put("success","Y");
        map.put("path",attachment.getSource());
        return map;
    }

    public Map<String,String> getDownToken(String token){
        Map<String,String> map = new HashMap<String, String>();
        String parseToken = null;
        try{
            parseToken = jwtSecurityService.getToken(token);
        }catch (ExpiredJwtException e){
            map.put("success","N");
            return map;
        }
        String[] parses = parseToken.split("&");
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        if(member.getUserNumber() != Long.parseLong(parses[0])){
            map.put("success","N");
            return map;
        }
        Optional<FileData> fileDataWrapper = fileDataRepository.findByFileIdx(Integer.parseInt(parses[1]));
        FileData filedata = fileDataWrapper.orElse(null);

        map.put("success","Y");
        map.put("cloudToken", String.valueOf(Long.parseLong(parses[1])));
//        map.put("cloudToken", String.valueOf(filedata.getFileIdx()));
        return map;
    }

    public long uploadFiles(Map<String, MultipartFile> map){
        Optional<Attachment> attachmentWrapper = attachmentRepository.findFirstByOrderByIdDesc();
        Attachment topAttachment = attachmentWrapper.orElse(null);
        long id = 0;
        if(topAttachment != null){
            id = topAttachment.getId() + 1;
        }
        int order = 0;
        for(String key : map.keySet()){
            order++;
            copyAndSave(id,order,map.get(key));
        }
        return id;
    }

    private void copyAndSave(long id,int order, MultipartFile file){
        String savedName = FileUtil.uuidMake();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        StringBuilder newName = new StringBuilder();
        newName.append(savedName);
        newName.append(".");
        newName.append(extension);
        File saveFile = new File(PrivateValue.FILEPATH.getValue(), newName.toString());
        if ( ! new File(PrivateValue.FILEPATH.getValue()).exists()) {
            new File(PrivateValue.FILEPATH.getValue()).mkdirs();
        }
        try{
            FileCopyUtils.copy(file.getBytes(),saveFile);
        }catch (IOException e){
        }
        Attachment attachment = Attachment.builder()
                .id(id)
                .order(order)
                .source(saveFile.getPath())
                .savedName(savedName)
                .originName(FilenameUtils.getBaseName(file.getOriginalFilename()))
                .extension(extension)
                .size(file.getSize())
                .downloadCount(0L).build();
        attachmentRepository.save(attachment);
    }

    public static String uuidMake(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }
}
