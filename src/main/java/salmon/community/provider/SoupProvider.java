package salmon.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SalmonC
 * @date 2021-09-10 02:15
 */
@Component
public class SoupProvider {

    @Value("${soup.get.url}"+"?key="+"${soup.get.value}")
    private String url;

    public String getSoup() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            String soup = res.split("\"")[11];
            return soup;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
//{"code":200,"msg":"success","newslist":[{"content":"失败乃成功之母，但往往失败都是，不孕不育。"}]}