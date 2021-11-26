package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.SMSDTO;
import com.example.dditlms.service.SMSService;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class SMSServiceImpl implements SMSService {
    public static final String API_KEY = "NCSG1FDMPTVIAJKP";
    public static final String API_SECRET = "YCLK2LBW3DMAH96NRSYY92YZFPPAOO6P";

    @Override
    public void SendMessage(SMSDTO smsdto) {
        Message coolsms = new Message(API_KEY,API_SECRET);
        try{
            coolsms.send(smsdto.ToHashMap());
        }catch(CoolsmsException e){
        }
    }
}
