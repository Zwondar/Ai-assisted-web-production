package person.website.UT;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class AIDrawTwo {

    private static final String BASE_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image-generation/generation";

    public String sendPostRequest(String imageUrl, int styleIndex) throws Exception {
        // 创建 HttpClient 实例
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 HttpPost 实例
            HttpPost httpPost = new HttpPost(BASE_URL);

            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", ""); // 使用您自己的 API-Key
            httpPost.setHeader("X-DashScope-Async", "enable");

            // 构建请求体
            String json = "{\"model\":\"wanx-style-repaint-v1\",\"input\":{\"image_url\":\"" + imageUrl + "\",\"style_index\":" + styleIndex + "}}";
            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);

            httpPost.setEntity(entity);

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            // 读取响应内容
            String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            // 关闭响应实体

            JSONObject jsonObject = JSON.parseObject(result);

            JSONObject output=jsonObject.getJSONObject("output");

            String getTaskId=output.getString("task_id");

             //返回响应内容
            return getTaskId;
        }
    }

    //------------请求任务id

    public String queryTaskStatus(String taskId) throws Exception {

        // 创建 HttpClient 实例
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 HttpGet 实例
            HttpGet httpGet = new HttpGet("https://dashscope.aliyuncs.com/api/v1/tasks/" + taskId);

            // 设置请求头
            httpGet.setHeader("Authorization", ""); // 使用您自己的 API-Key

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();

            // 读取响应内容
            String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);


            // 返回任务信息
            return result;
        }
    }

}


