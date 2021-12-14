package com.example.dditlms.util;

import com.example.dditlms.domain.entity.Attachment;
import com.example.dditlms.domain.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUtil {

    private final AttachmentRepository attachmentRepository;

    public long uploadFile(MultipartFile file){
        Optional<Attachment> attachmentWrapper = attachmentRepository.findFirstByOrderByIdDesc();
        Attachment topAttachment = attachmentWrapper.orElse(null);
        long id = 0;
        if(topAttachment != null){
            id = topAttachment.getId() + 1;
        }
        copyAndSave(id,1,file);
        return id;
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
