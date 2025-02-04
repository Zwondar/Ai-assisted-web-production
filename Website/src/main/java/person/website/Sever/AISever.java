package person.website.Sever;

import org.springframework.stereotype.Service;
import person.website.UT.AliDashScopeAI;

import java.util.ArrayList;
import java.util.List;

@Service
public class AISever {
    List<AliDashScopeAI> aliDashScopeAIS =new ArrayList<>();
   public AISever(){
       for(int i=0;i<99;i++){
           aliDashScopeAIS.add(new AliDashScopeAI());
       }
   }

    public String[] openConversation(String message, int code) throws InterruptedException {
       int currentObject=0;
        for(int i=0;i<100;i++){
            if(aliDashScopeAIS.get(i).thread==null) {
                currentObject=i;
                break;
            }
            if(i==99) {
                String str[]={"99","目前服务繁忙，请待会使用"};
                return str;
            }
        }
        aliDashScopeAIS.get(currentObject).setCurrentMessage(message);
        aliDashScopeAIS.get(currentObject).setCode(code);
        aliDashScopeAIS.get(currentObject).start();
        while(true){
            if(aliDashScopeAIS.get(currentObject).getCurrentRespond()!="") {
                String respond []=new String[2];
                respond[0]=String.valueOf(currentObject);
                respond[1]=aliDashScopeAIS.get(currentObject).getCurrentRespond();
                return
                       respond;
            }else Thread.sleep(10);
        }
    }

    public String linkConversation(String message, int code,int currentCode) throws InterruptedException {
        aliDashScopeAIS.get(currentCode).setCode(code);
       aliDashScopeAIS.get(currentCode).setCurrentMessage(message);
        synchronized (aliDashScopeAIS.get(currentCode).Flock){
            aliDashScopeAIS.get(currentCode).Flock.notifyAll();
            Thread.sleep(10);
        }
        aliDashScopeAIS.get(currentCode).setCurrentRespond("");
        while(true){
            if(aliDashScopeAIS.get(currentCode).getCurrentRespond()!="") return
                    aliDashScopeAIS.get(currentCode).getCurrentRespond();
            else Thread.sleep(10);
        }
    }

    public String closeConversation(int code,int currentCode) throws InterruptedException {
        aliDashScopeAIS.get(currentCode).setCode(code);
        synchronized (aliDashScopeAIS.get(currentCode).Flock) {
            aliDashScopeAIS.get(currentCode).Flock.notifyAll();
        }
        aliDashScopeAIS.get(currentCode).thread=null;
        return "该会话以成功结束";
    }
}
