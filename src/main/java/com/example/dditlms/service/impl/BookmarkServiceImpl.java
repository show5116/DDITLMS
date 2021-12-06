package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.Bookmark;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.BookmarkRepository;
import com.example.dditlms.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Override
    @Transactional
    public void saveBookmark(Member member, Menu menu) {
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .menu(menu).build();
        bookmarkRepository.save(bookmark);
    }
}
