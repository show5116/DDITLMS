package com.example.dditlms.service;

import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.repository.sanctn.DocformRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.dditlms.domain.entity.sanction.QDocform.docform;

@SpringBootTest
@Transactional
class DocformServiceTest {

    @Autowired
    DocformService docformService;
    @Autowired
    DocformRepository docformRepository;
    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;


    @Test
    public void saveTest() {
        Docform docform = new Docform("ST0001", "폼제목", "폼내용");


        docformService.saveDocform(docform);

        Optional<Docform> st0001 = docformRepository.findById("ST0001");
        System.out.println(st0001.get().getDocformCn());
        //Assertions.assertThat(docform).isEqualTo(st0001);

    }
    @Test
    public void queryDslTest() {

        queryFactory = new JPAQueryFactory(em);

        Docform docform3 = new Docform("ST0001","폼제목1", "폼내용1");
        Docform docform4 = new Docform("ST0002","폼제목2", "폼내용2");


        docformService.saveDocform(docform3);
        docformService.saveDocform(docform4);

        String find = "ST";
        List<Docform> findDocForm = queryFactory
                .selectFrom(docform)
                .where(docform.docFormId.startsWith(find)).fetch();
        for (Docform docform1 : findDocForm) {
            System.out.println(docform1.getDocFormId());
        }
    }

    @Test
    public void queryDslTest2() {

        Docform docform3 = new Docform("ST0001","폼제목1", "폼내용1");
        Docform docform4 = new Docform("ST0002","폼제목2", "폼내용2");


       docformService.saveDocform(docform3);
       docformService.saveDocform(docform4);

        String st = docformRepository.findLastCode("ST");

        System.out.println(st);
    }

    @Test
    public void quertDslTest3() {
        Docform docform3 = new Docform("ST0001","폼제목1", "폼내용1");
        Docform docform4 = new Docform("ST0002","폼제목2", "폼내용2");

        docformService.saveDocform(docform3);
        docformService.saveDocform(docform4);

        String result = docformService.findLastCode("ST"); //서비스에서 호출
        String lastCode = docformRepository.findLastCode("ST"); //레포지토리에서 호출

        //둘 다 사용 가능 확인, 실제로 쓸 때는 단순 조회의 경우만 레포지토리에서 바로 호출!!
        System.out.println(lastCode);
        System.out.println(result);

    }
}