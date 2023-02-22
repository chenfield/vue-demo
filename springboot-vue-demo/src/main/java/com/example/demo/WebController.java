package com.example.demo;
import com.example.demo.user.User;
import com.example.demo.user.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class WebController {

    @Resource
    private UserServiceImpl userService;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Integer delete(Integer id) {
        System.out.println(id);
        int result = userService.delete(id);
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(User user) {
        int result = userService.update(user);
        if (result >= 1) {
            return "修改成功";
        } else {
            return "修改失败";
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public User add(User user) {
        return userService.add(user);
    }

    @RequestMapping("/userList")
    @ResponseBody
    public List<User> userList() {
        return userService.findAll();
    }

    @RequestMapping("/findByName")
    @ResponseBody
    public List<User> findByName(String name) {
        return userService.findByName(name);
    }


    @RequestMapping(value="/page")
    @ResponseBody
    public List<User> page(Integer page, Integer size){
        int pageNow = page == null ? 1 : page;
        int pageSize = size;
        int startRows = pageSize*(pageNow-1);
        List<User> list = userService.queryPage(startRows,pageSize);
        return list;
    }

    @RequestMapping(value="/rows")
    @ResponseBody
    public int rows(){
        return userService.getRowCount();
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String uuuid = UUID.randomUUID().toString();

        String paths = "D:/file-path/";  // 此处替换要上传的文件地址

        //String filePath = paths;
        File f = new File(paths  + uuuid +fileName ); // 此处路径地址需要转义，/改为//，文件名称相同，前一个文件会被替换掉。

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

        file.transferTo(f); // 上传文件
        // 保存上传文件SQL
        //Map<String, Object> map = new HashMap<>();
        //map.put("paths", filePath);
        //map.put("fileName", fileName);
        //返回页面相应数据
        //Map<String, Object> resultMap= new HashMap<>();
        //resultMap.put("uuid", uuuid);
        //resultMap.put("name", fileName);
        return f.getPath();
    }
}
