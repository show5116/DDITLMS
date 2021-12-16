package com.example.dditlms.websocket;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URLDecoder;
import java.util.*;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static Map<WebSocketSession,String> map = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = new String(Base64.getDecoder().decode(message.getPayload()));
        String decodePayload = URLDecoder.decode(payload);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(decodePayload);
        String command = (String)jsonObject.get("command");

        if(command.equals("")){

        }else if(command.equals("")){

        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        map.put(session,session.getPrincipal().getName());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        map.remove(session);
    }

    private void getId(WebSocketSession session){
    }
}
