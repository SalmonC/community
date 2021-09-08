package salmon.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author SalmonC
 * @date 2021-09-08 16:32
 */
@Service
public class AliYunProvider {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    public String upload(InputStream fileStream,
                       String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String generateFileName;
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            generateFileName = UUID.randomUUID().toString() + "." + split[split.length - 1];
        } else {
            return null;
        }

        // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        try {
            ossClient.putObject(bucketName, generateFileName, fileStream);
        } finally {
            ossClient.shutdown();
        }
        return generateFileName;
    }
}
