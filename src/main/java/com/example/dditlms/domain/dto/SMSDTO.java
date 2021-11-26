package com.example.dditlms.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class SMSDTO {
    private String to;
    private String from;
    private String type;
    private String text;
    private String app_version;

    @Builder
    public SMSDTO(String to, String from, String type, String text, String app_version) {
        this.to = to;
        this.from = from;
        this.type = type;
        this.text = text;
        this.app_version = app_version;
    }

    public HashMap<String,String> ToHashMap(){
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("to", this.to);
        params.put("from", this.from);
        params.put("type", this.type);
        params.put("text", this.text);
        params.put("app_version", this.app_version);
        return params;
    }
}
