package com.lyy.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LYY
 * @version 1.0
 */
@RestController
public class AdminController {

    @PostMapping("admin/addUser")
    public void addUser(@RequestHeader("Authorization") String authorization, @RequestParam("userId") String userId, @RequestParam("endpoint") String[] endpoints) {
    //解码授权头
        String decodeHeader = new String(Base64.getDecoder().decode(authorization.substring(6)));
        Map<String,Object> userInfo = parseUserInfo(decodeHeader);

        //检查rola是否为管理员
        if (!"admin".equals(userInfo.get("role"))) {
            throw new RuntimeException("非管理员账户无权访问此断点");
        }

        //将访问信息保存到文件中
        saveAccessInfo(userId,endpoints);
    }
    private void saveAccessInfo(String userId, String[] endpoints) {
        //将访问信息保存到文件中
        File file = new File("access.txt");
        try {
            FileWriter write = new FileWriter(file,true);
            for (String endpoint : endpoints){
                write.write(userId + " " + endpoint + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String,Object> parseUserInfo(String decodeHeader) {
    //解析解码后的用户信息
        Map<String,Object> userInfo = new HashMap<>();
        String[] parts = decodeHeader.split(" ,");
//        userInfo.put("role", header[0]);
        for (String part : parts) {
            String[] keyValue = part.split(":");
            userInfo.put(keyValue[0].trim(),keyValue[1].trim());
        }
        return userInfo;
    }
    /**
     * 在上述代码中，我们定义了两个控制器：AdminController 和 UserController。
     *
     * AdminController 中的 addUser 方法用于管理员添加用户的访问权限。它检查授权头中的角色是否为管理员，如果不是，则抛出异常。然后，它将访问信息保存到文件中。
     *
     * 此外，我们还添加了一些错误处理代码，以确保在发生错误时返回可读的错误消息。
     */
}
