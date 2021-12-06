package com.example.dditlms.domain.idclass;

import java.io.Serializable;
import java.util.Objects;

public class BookmarkId implements Serializable {
    private String member;
    private String menu;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkId that = (BookmarkId) o;
        return Objects.equals(member, that.member) && Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, menu);
    }
}
