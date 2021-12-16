package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDataDTO {

    private Integer parentId;

    private String fileName;

    private String extension;

    private Long fileSize;

    private Date createTime;

    private Date openTime;

    private Integer trash;

    private Long userNumber;

    public FileData toEntity(){
        FileData fileData = FileData.builder()
                .fileName(this.fileName)
                .extension(this.extension)
                .fileSize(this.fileSize)
                .createTime(this.createTime)
                .openTime(this.openTime)
                .trash(this.trash)
                .build();
        return fileData;
    }

}
