package person.website.Sever;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import person.website.DAO.UseMap;
import person.website.Entity.Sel;
import person.website.Entity.Space;
import person.website.Entity.User;
import person.website.UT.AliOSSUtilsUpload;

import java.io.IOException;
import java.util.List;

@Service
public class UserSever {
    @Autowired
    UseMap useMap;
    public User login(User user) {
        User u=useMap.dataLogin(user);
        return u;
    }

    public User signSelect(User user) {
        User u= useMap.dataSignSelect(user);
        return u;
    }

    public void signInto(User user) {
        useMap.dataSignInto(user);
    }

    public void upLoad(String filename ,String user_id ,String username, String url){
        useMap.dataSave(filename,user_id,username,url);
    }

    public List<Space> severGetUserData( Sel sel){
    List<Space> spaces=useMap.getUserData(sel);
    return spaces;
    }

    public boolean saveSelect(String filename ,String user_id){
        Space s=useMap.dataSelect(filename,user_id);
        if(s==null) return false;
        else return true;
    }

    public String SeverView(Space space){
        String url=useMap.dataView(space);
        return url;
    }

    public String SeverDownload(Space space) {
        String url=useMap.dataDownload(space);
        return url;
    }

    public void SeverDelete(Space space) {
        useMap.dataDelete(space);
    }

    public void SeverShare(Space space) {
        useMap.dataShare(space);
    }

    public int SevertoStar(String userId, String spaceId,String filename) {
        Space space=useMap.dataSelect(filename,spaceId);
        String filenameId=String.valueOf(space.getId());
        Integer id=useMap.dataSelectStar(userId,spaceId,filenameId);
        if(id!=null) return 0;
        useMap.dataStar(userId,spaceId,filenameId);
        int oldStar =useMap.dataSelectToStar(spaceId,filename);
        int newStar=++oldStar;
        useMap.dataToStar(filenameId,newStar);
        return 1;
    }

    public String severPutImage(String userId) {
        String url=useMap.dataPutImage(userId);
        return url;
    }

    public List severGetOpenData(Sel sel) {
       List<Space> result= useMap.dataSelectOpen(sel);
       return result;
    }

    public int SeverUserUpLoad(String filename, String user_id, MultipartFile file) throws IOException {
        AliOSSUtilsUpload aliOSSUtilsUpload =new AliOSSUtilsUpload();
        String url=aliOSSUtilsUpload.upload(file);
        int id=Integer.parseInt(user_id);
        String name=useMap.dataSelectName(id);
        String imageUrl=useMap.dataPutImage(user_id);
        useMap.dataUserUpLoad(filename,user_id,url,name,imageUrl);
        return 1;
    }

    public String SeverStanding(String name, MultipartFile file,int user_id) throws IOException {
        AliOSSUtilsUpload aliOSSUtilsUpload=new AliOSSUtilsUpload();
        String url=aliOSSUtilsUpload.upload(file);
        useMap.dataStanding(name,url,user_id);
        useMap.dataStandingSpace(name,user_id);
        return url;
    }

    public String SeverGetName(int user_id) {
        String name=useMap.dataSelectName(user_id);
        return name;
    }


}
