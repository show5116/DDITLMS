package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.entity.FileData;
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

    private Integer fileIdx;

    private Integer parentId;

    private String fileName;

    private String extension;

    private Long fileSize;

    private Date createTime;

    private Date openTime;

    private Integer trash;

    private String contentType;

    private Long userNumber;

    private String token;

    public FileData toEntity(){
        FileData fileData = FileData.builder()
                .fileName(this.fileName)
                .extension(this.extension)
                .fileSize(this.fileSize)
                .createTime(this.createTime)
                .openTime(this.openTime)
                .contentType(this.contentType)
                .trash(this.trash)
                .build();
        return fileData;
    }

}
