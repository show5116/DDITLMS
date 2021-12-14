package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.idclass.AttachmentId;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ATCHMNFL_DETAIL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@IdClass(AttachmentId.class)
public class Attachment {
    @Id
    @Column(name="ATCHMNFL_ID")
    private Long id;

    @Id
    @Column(name="FILE_SN")
    private Integer order;

    @Column(name="FILE_STRE_COURS")
    private String source;

    @Column(name="STRE_FILE_NM")
    private String savedName;

    @Column(name="ORIGNL_FILE_NM")
    private String originName;

    @Column(name="FILE_EXTSN")
    private String extension;

    @Column(name="FILE_SIZE")
    private Long size;

    @Column(name="DWLD_CO")
    private Long downloadCount;
}
