package com.example.dditlms.service;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.Bookmark;
import com.example.dditlms.domain.entity.Member;

import java.util.Set;

public interface BookmarkService {
    public void saveBookmark(Member member, Menu menu);
    public Set<Bookmark> getBookmarks(Member member);
    public void removeBookmark(Member member, Menu menu);
}
