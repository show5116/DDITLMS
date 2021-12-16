package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILEDATA")
@NoArgsConstructor
@Getter
@Setter
public class FileData {

    @Id
    @Column(name="FILE_IDX")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileIdx;

    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
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

    private Integer trash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USERNUMBER")
    private Member member;

    @Builder
    public FileData(Integer fileIdx, FileData parent, List<FileData> children, String fileName, String extension, Long fileSize, Date createTime, Date openTime, Integer trash, Member member) {
        this.fileIdx = fileIdx;
        this.parent = parent;
        this.children = children;
        this.fileName = fileName;
        this.extension = extension;
        this.fileSize = fileSize;
        this.createTime = createTime;
        this.openTime = openTime;
        this.trash = trash;
        this.member = member;
    }
}
