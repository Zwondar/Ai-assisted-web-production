package person.website.Control;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import person.website.Entity.*;
import person.website.Sever.UserSever;
import person.website.UT.AliOSSUtilsUpload;
import person.website.UT.JwtUtils;
import person.website.UT.Result;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserControl {
    @Autowired
    UserSever userSever;
    @Autowired
    AliOSSUtilsUpload aliOSSUtilsUpload;
    @PostMapping("/Detection")
    public Result detection(@RequestBody User user) {
        User u = userSever.login(user);

        if (u != null) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            String str = String.valueOf(u.getId());
            String jwt = JwtUtils.generateJwt(m);
            return Result.success(jwt, str);
        } else {
            return Result.error("账号或密码错误");
        }
    }

    @PostMapping("/Sign")
    public Result sign(@RequestBody User user) {
        User u = userSever.signSelect(user);
        if (u != null) return Result.error("该账号已存在");
        userSever.signInto(user);
        return Result.success("", "创建账号成功");
    }

    @PostMapping("/Log/verify")
    public Result verify(HttpServletRequest request) {
        String jwt = request.getHeader("Token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int id =  (int)claims.get("id");
        return Result.success(id);
    }
     //先不动，等前端看下用什么格式数据传输保存文件给我
    @PostMapping("/Log/save")
    public Result save (String filename ,String user_id ,String username ,MultipartFile file) throws IOException {
         if(userSever.saveSelect(filename,user_id)) return Result.error("该名称已存在");
         else {
             String url = aliOSSUtilsUpload.upload(file);
             userSever.upLoad(filename,user_id,username, url);
             return Result.success("数据创建成功");
         }
    }

    @PostMapping("/Log/getUserData")
    public Result getUserData(@RequestBody Sel sel){
     List<Space> list=userSever.severGetUserData(sel);
     return Result.success(list);
    }

    @PostMapping("/Log/view")
    public Result view(@RequestBody Space space){
    String url=userSever.SeverView(space);
    return Result.success(url);
    }

    @PostMapping("/Log/download")
    public Result download(@RequestBody Space space){
        String url=userSever.SeverDownload(space);
        return Result.success(url);
    }

    @PostMapping("/Log/delete")
    public Result delete(@RequestBody Space space){
        userSever.SeverDelete(space);
        return Result.success("删除成功");
    }

    @PostMapping("/Log/share")
    public Result share(@RequestBody Space space){
        userSever.SeverShare(space);
        return Result.success("分享成功");
    }
    @PostMapping("/Log/toStar")
    public Result toStar(@RequestBody Star star){//userid点赞用户的id，spaceid被点赞用户的id
       int code= userSever.SevertoStar(star.getUser_id(),star.getSpace_id(),star.getFilename());
       if(code==0) return Result.error("点过赞了");
       else return Result.success("点赞成功");
    }
    @PostMapping("/Log/getmusic")
    public Result getMusic(MultipartFile file) throws IOException {
        String url=aliOSSUtilsUpload.upload(file);

        return Result.success(url);
    }

    @PostMapping("/Log/getimage")
    public Result getimage(MultipartFile file) throws IOException {
        String url=aliOSSUtilsUpload.upload(file);
        return Result.success(url);
    }

    @PostMapping("/Log/getvideo")
    public Result getVideo(MultipartFile file) throws IOException {
        String url=aliOSSUtilsUpload.upload(file);
        return Result.success(url);
    }

    @PostMapping("/Log/putImage")
    public Result putImage(@RequestBody Sel sel){
        String url=userSever.severPutImage(String.valueOf(sel.getUser_id()));
        return Result.success(url);
    }

    @PostMapping ("/Log/getOpenData")
    public Result getOpenData(@RequestBody Sel sel){
       List<Space> result= userSever.severGetOpenData(sel);
       return Result.success(result);
    }

    @PostMapping("/Log/UserUpLoad")
    public Result userUpLoad(String filename ,String user_id,MultipartFile file) throws IOException {
        int code =userSever.SeverUserUpLoad(filename,user_id,file);
        if(code==0) return Result.error("上传失败");
        else return Result.success("上传成功");
    }

    @PostMapping("/Log/Standing")
    public Result Standing(String name,MultipartFile file,int user_id) throws IOException {
    String url=userSever.SeverStanding(name,file,user_id);
    return Result.success(url);
    }
    @PostMapping("/Log/getName")
    public Result getName(@RequestBody Sel sel){
        String name=userSever.SeverGetName(sel.getUser_id());
        return Result.success(name);
    }
}
