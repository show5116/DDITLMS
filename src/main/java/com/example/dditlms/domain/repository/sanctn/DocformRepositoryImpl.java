package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.entity.sanction.QDocform;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.example.dditlms.domain.entity.sanction.QDocform.docform;

public class DocformRepositoryImpl implements DocformRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DocformRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public String findLastCode(String formCode) {
        String findDocFormCode = queryFactory.selectFrom(docform)
                .where(docform.docFormId.startsWith(formCode))
                .orderBy(docform.docFormId.desc())
                .fetchFirst()
                .getDocFormId();
        return findDocFormCode;
    }

}
