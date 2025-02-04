package person.website.Sever;

import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import person.website.UT.AIDrawOne;
import person.website.UT.AIDrawTwo;
@Service
public class AiDrawSever implements Runnable{

    Thread thread;
    String taskId;
    @Autowired
    AIDrawTwo aiDrawTwo;
    private String url;
    private int styleIndex;

    private String RUrl="";
    public String TDraw(String message) throws NoApiKeyException {
        String url=AIDrawOne.basicCall(message);
        return url;
    }

    public String getRUrl(){
        return RUrl;

    }
    public void setRUrl(String RUrl){
        this.RUrl=RUrl;
    }

    public void start(String url, int styleIndex) {
        thread =new Thread(this);
        this.styleIndex=styleIndex;
        this.url=url;
        thread.start();

    }

    public void stop(){
        thread.interrupt();
    }

    @Override
    public void run() {
        try {

            taskId=aiDrawTwo.sendPostRequest(url,styleIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        while(true){
            try {
              String result=aiDrawTwo.queryTaskStatus(taskId);

              JSONObject jsonObject =  JSON.parseObject(result);

              JSONObject output= jsonObject.getJSONObject("output");


              String task=(String)output.get("task_status");

              if(task.equals("SUCCEEDED")){

              JSONObject GetImage = output.getJSONArray("results").getJSONObject(0);

              String url=GetImage.getString("url");
              this.RUrl=url;
              break;
              }else {
                  thread.sleep(100);

              }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
