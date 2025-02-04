package person.website.Control;

import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import person.website.Sever.AISever;
import person.website.Sever.AiDrawSever;
import person.website.UT.AIDrawTwo;
import person.website.UT.AliOSSUtilsUpload;
import person.website.UT.Result;

import java.io.IOException;

@RestController
public class AIControl {
    @Autowired
    AISever aiSever;
    @Autowired
    AiDrawSever aiDrawSever;
    @Autowired
    AliOSSUtilsUpload aliOSSUtilsUpload;
@PostMapping("/Log/ai/talk/open")
    public Result open(String message,int code) throws InterruptedException {
        String respond[]=aiSever.openConversation(message,code);
        return Result.success(respond);
    }

@PostMapping("/Log/ai/talk/link")
    public Result link(String message,int code,int currentCode) throws InterruptedException {
        String respond=aiSever.linkConversation(message,code,currentCode);
        return Result.success(respond);
    }
    @PostMapping("/Log/ai/talk/close")
    public Result close(int code,int currentCode) throws InterruptedException {
        String respond=aiSever.closeConversation(code,currentCode);
        return Result.success(respond);
    }
    @PostMapping("/Log/ai/TDraw")
    public Result TDraw(String message) throws NoApiKeyException {
    String url =aiDrawSever.TDraw(message);
    return Result.success(url);
    }
    @PostMapping("/Log/ai/RDraw")
    /*
    *
    *
0 复古漫画

1 3D童话

2 二次元

3 小清新

4 未来科技

5 国画古风

6 将军百战

7 炫彩卡通

8 清雅国风

9 喜迎新年
    *
    *
    * */

    public Result RDraw(MultipartFile file,String styleIndex) throws IOException, InterruptedException {
        String url = aliOSSUtilsUpload.upload(file);
        int styleIndexs=Integer.parseInt(styleIndex);
        aiDrawSever.start(url,styleIndexs);
        while(true){
            if(aiDrawSever.getRUrl()!="") {
                aiDrawSever.stop();
                String result=aiDrawSever.getRUrl();
                aiDrawSever.setRUrl("");
                return Result.success(result);
            }
            else Thread.sleep(10);
        }

    }
}
