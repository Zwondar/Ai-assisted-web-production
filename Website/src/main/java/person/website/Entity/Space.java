package person.website.Entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Space {
    private int id;//主键id
    private String filename;//文件名称
    private int user_id;//该文件的拥有者/0：公共文件/1：管理员文件/2-n：普通用户文件
    private String url;//地址
    private String username;//分享者是谁
    private int star;//点赞数
    private String upDataTime;
    private MultipartFile file;//专门给保存文件写的
    private String name;
    private String spaceImg;
}
