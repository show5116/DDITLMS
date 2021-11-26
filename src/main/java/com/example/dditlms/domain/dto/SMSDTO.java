package com.example.dditlms.domain.dto;

import lombok.*;

import java.util.HashMap;

@Getter @Setter
@AllArgsConstructor
@ToString
public class SMSDTO {
    private String to;
    private String from;
    private String type;
    private String text;
    private String app_version;

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
