package com.example.dditlms.service;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.Member;

public interface BookmarkService {
    public void saveBookmark(Member member, Menu menu);
}
