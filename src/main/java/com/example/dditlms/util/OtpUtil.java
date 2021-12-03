package com.example.dditlms.util;

import lombok.NoArgsConstructor;

import java.util.Random;

public class OtpUtil {

    private Random random;

    public OtpUtil(){
        random = new Random();
    }

    public String[] getOTP(){
        char otpCollection[] = new char[]{
                '0','1','2','3','4','5','6','7','8','9'
        };
        String[] otp = new String[2];
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for(int i=0;i<6;i++){
            sb.append(otpCollection[random.nextInt(otpCollection.length)]);
        }
        otp[0] = sb.toString();
        sb2.append("인증번호는 [");
        sb2.append(otp[0]);
        sb2.append("]입니다.");
        otp[1] = sb2.toString();
        return otp;
    }
}
