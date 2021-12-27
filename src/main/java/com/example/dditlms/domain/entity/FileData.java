package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.dto.FileDataDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILEDATA")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@SequenceGenerator(
        name="FILEDATA_SEQ_GEN",
        sequenceName = "FILEDATA_SEQ",
        initialValue =1
)
public class FileData {

    @Id
    @Column(name="FILE_IDX")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "FILEDATA_SEQ_GEN")
    private Integer fileIdx;

    @ManyToOne(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
    @JoinColumn(name="PAR_FILEDATA_CODE")
    private FileData parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAR_FILEDATA_CODE")
    private List<FileData> children;

    @Column(name = "FILE_NAME")
    private String fileName;

    private String extension;

    @Column(name = "FILE_SIZE")
    private Long fileSize;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "OPEN_TIME")
    private Date openTime;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    private Integer trash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USERNUMBER")
    private Member member;


    public FileDataDTO toDTO(){
        FileDataDTO fileDataDTO = FileDataDTO.builder()
                .fileName(this.fileName)
                .extension(this.extension)
                .fileSize(this.fileSize)
                .openTime(this.openTime)
                .fileIdx(this.fileIdx)
                .build();
        return fileDataDTO;
    }


}
