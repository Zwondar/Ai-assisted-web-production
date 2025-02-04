package person.website.DAO;

import org.apache.ibatis.annotations.*;
import person.website.Entity.Sel;
import person.website.Entity.Space;
import person.website.Entity.User;

import java.util.List;

@Mapper
public interface UseMap {
@Select("select * from users where password=#{password} and username=#{username}")
    User dataLogin(User user);
@Select("select * from users where username=#{username}")
    User dataSignSelect(User user);
@Insert("insert into users (username, password) VALUE (#{username},#{password})")
    void dataSignInto(User user);
@Insert("insert into space (filename, user_id, url, upDataTime ,username) VALUE (#{filename},#{user_id},#{urlR},now(),#{username})")
//该处username是为了防止用户给公共区资源点赞后，私人区的点赞数也能即时更新
    void dataSave(String filename,String user_id,String username, String urlR);
@Select("select * from space where user_id=#{user_id} and filename LIKE CONCAT('%', #{input}, '%')")
List<Space> getUserData( Sel sel);
@Select("select * from space where user_id=#{user_id} and filename=#{filename}")
Space dataSelect(String filename ,String user_id);
@Select("select space.url from space where user_id=#{user_id} and filename=#{filename}")
String dataView(Space space);
@Select("select space.url from space where user_id=#{user_id} and filename=#{filename}")
    String dataDownload(Space space);
@Delete("delete from space where user_id=#{user_id} and filename=#{filename}")
    void dataDelete(Space space);
@Update("update  space set isOpen=0 where id=#{id}")
    void dataShare(Space space);
@Insert("insert into stars (user_id, space_id,filenameId) VALUE (#{userId},#{spaceId},#{filenameId})")
    void dataStar(String userId, String spaceId,String filenameId);
@Update("update space set star=#{newStar} where id=#{filenameId}")
    void dataToStar(String filenameId,int newStar);
@Select("select id from stars where user_id=#{userId} and space_id=#{spaceId} and filenameId=#{filenameId}")
    Integer dataSelectStar(String userId, String spaceId ,String filenameId);
@Select("select space.star from space where username=#{spaceId} and filename=#{filename}")
    int dataSelectToStar(String spaceId,String filename);
@Select("select users.userImg from users where id=#{userId}")
    String dataPutImage(String userId);
@Select("select * from space where isOpen=0 and filename LIKE CONCAT('%', #{input}, '%')")
    List<Space> dataSelectOpen(Sel sel);
@Insert("insert into space (filename, user_id, url, upDataTime, username,name,spaceImg) VALUE (#{filename},#{user_id},#{url},now(),#{user_id},#{name},#{imageUrl})")
    void dataUserUpLoad(String filename, String user_id, String url, String name, String imageUrl);
@Update("update users set name=#{name} ,userImg=#{url} where id=#{user_id} ")
    void dataStanding(String name, String url,int user_id);

@Select("select users.name from users where id=#{user_id}")
    String dataSelectName(int user_id);
@Update("update space set name =#{name} where user_id=#{user_id}")
    void dataStandingSpace(String name,int user_id);
}
