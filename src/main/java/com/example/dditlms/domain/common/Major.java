package com.example.dditlms.domain.common;

import lombok.Getter;

@Getter
public enum Major {
    COMPUTERSCIENCE("COMPUTERSCIENCE","컴퓨터공학과","011"),
    MECHANICALENGINEERING("MECHANICALENGINEERING","기계과","012"),
    CIVILENGINEERING("CIVILENGINEERING","토목과","013"),
    ELECTRICENGINEERING("ELECTRICENGINEERING","전자과","014"),
    CHEMISTRY("CHEMISTRY","화학과","015"),
    ARCHITECTURE("ARCHITECTURE","건축학과","016"),
    ECONOMIC("ECONOMIC","경제학과","111"),
    PUBLICADMINISTRATION("PUBLICADMINISTRATION","행정학과","112"),
    PRACTICALMUSIC("PRACTICALMUSIC","실용음악과","211"),
    NURSING("NURSING","간호학과","311"),
    POLICEADMINISTRATION("POLICEADMINISTRATION","경찰행정학과","411"),
    ENGLISH("ENGLISH","영어학과","511"),
    EARLYCHILDHOODEDUCATION("EARLYCHILDHOODEDUCATION","유아교육과","611"),
    SPECIALWEAPONS("SPECIALWEAPONS","특수무기학과","999");

    private String value;
    private String name;
    private String code;

    Major(String value, String name, String code) {
        this.value = value;
        this.name = name;
        this.code = code;
    }
}
