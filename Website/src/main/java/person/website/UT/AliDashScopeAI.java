package person.website.UT;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AliDashScopeAI implements Runnable{

    public Object Flock =new Object();
    public  int code=0;
    public Thread thread;
    private String currentMessage="";
    private String currentRespond="";
    public void callWithMessage()
        throws NoApiKeyException, ApiException, InputRequiredException, InterruptedException {
        Constants.apiKey="更改为你自己的阿里云通义万象APIKey";
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("你是智能助手机器人,你的名字叫做小I").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(this.currentMessage).build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_MAX).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .enableSearch(true)
                        .build();
        GenerationResult result = gen.call(param);
        JSONObject jsonObject=(JSONObject) JSON.parse(JsonUtils.toJson(result));
        String content=this.JsonParse(jsonObject);
        this.currentRespond=content;
        //-----------------------------------------------
        while(code==1) {
            this.currentMessage="";

            this.code=0;
            synchronized (this.Flock) {
                this.Flock.wait();
            }

            this.currentRespond="";
            if(code==1) {
                msgManager.add(result);//添加上文对话，保持对话连续性
                param.setPrompt(this.getCurrentMessage());
                param.setMessages(msgManager.get());
                result = gen.call(param);
                jsonObject = (JSONObject) JSON.parse(JsonUtils.toJson(result));
                content = this.JsonParse(jsonObject);
                this.currentRespond = content;
            }
        }

    }

    public void setCurrentMessage(String message){
        this.currentMessage=message;
    }

    public String getCurrentMessage(){
        String CurrentMessage=this.currentMessage;
        return CurrentMessage;
    }

    public void setCurrentRespond(String str){
        this.currentRespond=str;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String JsonParse(JSONObject jsonObject){
        JSONObject output=(JSONObject) jsonObject.get("output");
        JSONObject choiceJson = output.getJSONArray("choices").getJSONObject(0);
        JSONObject messageJson = choiceJson.getJSONObject("message");
        String content = messageJson.getString("content");
        return content;
    }

    public String getCurrentRespond() {
        return this.currentRespond;
    }

    @Override
    public void run() {
        try {

                this.callWithMessage();

        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        thread=new Thread(this);
        thread.start();
    }


}
