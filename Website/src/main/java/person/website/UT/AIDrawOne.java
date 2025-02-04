package person.website.UT;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
    public class AIDrawOne {
        public static String basicCall(String message) throws ApiException, NoApiKeyException {
            Constants.apiKey="更改为你自己的阿里云通义万象APIKey";
            ImageSynthesis is = new ImageSynthesis();
            ImageSynthesisParam param =
                    ImageSynthesisParam.builder()
                            .model(ImageSynthesis.Models.WANX_V1)
                            .n(1)
                            .size("1024*1024")
                            .prompt(message)
                            .build();

            ImageSynthesisResult result = is.call(param);
          JSONObject jsonObject = (JSONObject) JSON.parse(JsonUtils.toJson(result));
          JSONObject output=(JSONObject) jsonObject.get("output");
          JSONObject GetImage = output.getJSONArray("results").getJSONObject(0);
          String url=GetImage.getString("url");
          return url;
        }
    }


