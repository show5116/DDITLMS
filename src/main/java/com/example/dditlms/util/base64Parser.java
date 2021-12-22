//package com.example.dditlms.util;
//
//import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.UnsupportedEncodingException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Slf4j
//public class base64Parser {
//
//    private static String pattern = "=\\?(.*?)\\?(.*?)\\?(.*?)\\?=";
//
//    public static String getFileName(String source) throws Base64DecodingException, UnsupportedEncodingException {
//        StringBuilder buffer = new StringBuilder();
//        //2. default charset 지정
//        String charsetMain = "UTF-8";
//        String charsetSub = "B";
//        Pattern r = Pattern.compile(pattern);
//        Matcher matcher = r.matcher(source);
//        //3. 내용 찾아서 decoding
//        while (matcher.find()) {
//            charsetMain = matcher.group(1);
//            charsetSub = matcher.group(2);
//            buffer.append(new String(Base64.decode(matcher.group(3)), charsetMain));
//            String group3 = matcher.group(3);
//            String[] nonBase64 = source.split(group3);
//            String nonBase64content = nonBase64[1];
//            String replace = nonBase64content.replace("?=", "");
//            buffer.append(replace);
//        }
//        //4. decoding할 게 없다면 그대로 반환
//        if (buffer.toString().isEmpty()) {
//            buffer.append(source);
//        }
//        return buffer.toString();
//    }
//}