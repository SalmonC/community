package salmon.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.exception.CustomizeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
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

    @Value("${aliyun.oss.filePathHeader}")
    private String filePathHeader;

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
            //上传文件
            ossClient.putObject(bucketName, generateFileName, fileStream);

            //设置失效时间并获取url
            return filePathHeader + generateFileName;
        } catch (Exception e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } finally {
            ossClient.shutdown();
        }
    }

    private String getSTSUrl(OSS ossClient, String generateFileName) throws Exception {
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL urlObject = ossClient.generatePresignedUrl(bucketName, generateFileName, expiration);
        if (urlObject != null) {
            String url = urlObject.toString();
            return url;
        } else {
            throw new Exception();
        }
    }

}
