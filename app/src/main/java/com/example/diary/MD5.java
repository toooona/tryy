package com.example.diary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private String plainText;

    public MD5(String plainText) {
        this.plainText = plainText;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String transform() {
        byte[] secretBytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            //获取MessageDigest对象
            secretBytes = md5.digest(plainText.getBytes());
            //加密
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }

        //避免所得byte数组中可能存在的不可打印的字符---转化成16进制保存

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < secretBytes.length; i++) {
            String hex = Integer.toHexString(secretBytes[i] & 0xFF);

            /*
             * & 0XFF---高24位置0
             * java.lang.Integer.toHexString() 方法的参数是int(32位)类型
             * 如果输入一个byte(8位)类型的数字，这个方法会把这个数字的高24为也看作有效位，这就必然导致错误
             */

            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();

    }
}
