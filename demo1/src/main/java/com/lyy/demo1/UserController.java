package com.lyy.demo1;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author LYY
 * @version 1.0
 */
public class UserController {

    @RequestMapping("/user/{resource}")
    public String accessResource(@RequestHeader("Authorzation") String authorization, @PathVariable("resource") String resource){
        //解码授权头
        String decodedHeader = new String(Base64.getDecoder().decode(authorization.substring(6)));
        Map<String, Object> userInfo = parseUserInfo(decodedHeader);

        //检查用户是否具有访问权限
        if (!hashAccess(userInfo.get("userId").toString(),resource)){
            return "无访问权限";
        }else{
            return "访问成功";
        }



    }
    private boolean hashAccess(String userId, String resource){
        //读取文件中的访问信息并检查用户是否具有访问权限
        File file = new File("access.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!= null){
                String[] parts = line.split(" ");
                if(parts[0].equals(userId) && parts[1].equals(resource)){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String,Object> parseUserInfo(String decodeHeader) {
        //解析解码后的用户信息
        Map<String,Object> userInfo = new HashMap<>();
        String[] parts = decodeHeader.split(",");
//        userInfo.put("role", header[0]);
        for (String part : parts) {
            String[] keyValue = part.split(":");
            userInfo.put(keyValue[0].trim(),keyValue[1].trim());
        }
        return userInfo;
    }
}
/**
 *
 * UserController 中的 accessResource 方法用于用户访问资源。它解码授权头并检查用户是否具有访问权限。如果没有权限，则返回 "无访问权限"，否则返回 "访问成功"。
 *
 * 在代码中，我们使用了一个文件 access.txt 来保存用户的访问信息。你可以根据实际情况选择其他的数据存储方式，如数据库。
 */
