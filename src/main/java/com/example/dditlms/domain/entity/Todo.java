package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.TodoStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="TODO")
@Getter @Setter
@SequenceGenerator(
        name="TODO_SEQ_GEN",
        sequenceName = "TODO_SEQ",
        initialValue =1
)
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "TODO_SEQ_GEN")
    @Column(name ="TODO_SN")
    private Long id;

    @Column(name ="TODO_CN")
    private String content;

    @Column(name ="TODO_STTUS")
    @ColumnDefault("'TODO'")
    @Enumerated(EnumType.STRING)
    private TodoStatus todoStatus;

    @ManyToOne
    @JoinColumn(name="MBER_NO")
    private Member member;
}
