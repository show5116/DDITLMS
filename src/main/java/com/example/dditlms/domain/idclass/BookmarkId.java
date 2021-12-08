package com.example.dditlms.domain.idclass;

import com.example.dditlms.domain.common.Menu;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookmarkId implements Serializable {

    private Long member;

    private Menu menu;

}
