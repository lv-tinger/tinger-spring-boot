package org.tinger.template;

import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Tags;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.tinger.common.utils.IoUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Minio client 封装.
 *
 * @author Administrator
 */
@Component
public class MinioClientTemplate {

    @Resource
    private MinioClient minioClient;

    /**
     * 返回原始 MinioClient
     *
     * @return MinioClient
     */
    public MinioClient getMinioClient() {
        return minioClient;
    }

    /**
     * 检测bucket是否存在.
     *
     * @param bucketName 存储桶的名字
     * @return true表示存在
     */
    public boolean bucketExists(String bucketName) {
        boolean flag = false;
        BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
        try {
            flag = minioClient.bucketExists(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 创建bucket
     *
     * @return true创建成功
     */
    public boolean makeBucket(String bucketName) {
        // 如果bucket已存在，直接返回成功
        if (this.bucketExists(bucketName)) {
            return true;
        }
        // 不存在bucket，创建一个
        MakeBucketArgs args = MakeBucketArgs.builder().bucket(bucketName).build();
        try {
            minioClient.makeBucket(args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建bucket，并设置tags.
     *
     * @param bucketName 存储桶
     * @param tags       标签
     * @return true 成功
     */
    public boolean makeBucket(String bucketName, Map<String, String> tags) {
        // 如果bucket不存在，先创建一个
        if (!this.bucketExists(bucketName)) {
            this.makeBucket(bucketName);
        }
        // 设置存储桶的标签
        return setBucketTags(bucketName, tags);
    }

    /**
     * 删除存储bucket
     *
     * @return true 成功
     */
    public boolean removeBucket(String bucketName) {
        RemoveBucketArgs args = RemoveBucketArgs.builder().bucket(bucketName).build();
        try {
            minioClient.removeBucket(args);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     *
     * @return buckets list
     */
    public List<Bucket> buckets() {
        List<Bucket> buckets = new ArrayList<>();
        try {
            buckets = minioClient.listBuckets();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buckets;
    }

    /**
     * 设置存储桶的标签.
     *
     * @param bucketName 存储桶名字
     * @param tags       标签map
     * @return true 成功
     */
    public boolean setBucketTags(String bucketName, Map<String, String> tags) {
        // 设置存储桶的标签
        SetBucketTagsArgs args = SetBucketTagsArgs.builder()
                .bucket(bucketName)
                .tags(tags)
                .build();
        boolean bool = false;
        try {
            minioClient.setBucketTags(args);
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 获取bucket的标签.
     *
     * @param bucketName 存储桶
     * @return 存储桶标签
     */
    public Tags getBucketTags(String bucketName) {
        GetBucketTagsArgs args = GetBucketTagsArgs.builder().bucket(bucketName).build();
        Tags tags = null;
        try {
            tags = minioClient.getBucketTags(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tags;
    }

    /**
     * 获取对象流.
     *
     * @param bucketName 存储桶
     * @param filePath   文件路径
     * @return the object
     */
    public GetObjectResponse getObject(String bucketName, String filePath) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .build();
        GetObjectResponse response = null;
        try {
            response = minioClient.getObject(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 上传文件流到存储桶
     *
     * @param bucketName  存储桶
     * @param filePath    文件保存路径
     * @param in          文件流
     * @param objectSize  文件大小
     * @param contentType 文件类型
     * @return 文件保存路径
     */
    public String upload(String bucketName, String filePath, InputStream in, Long objectSize, String contentType) {
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .stream(in, objectSize, -1)
                .contentType(contentType)
                .build();
        try {
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return filePath;
    }

    /**
     * 文件保存到本地路径
     *
     * @param bucketName 存储桶
     * @param filePath   文件保存路径
     * @param savePath   保存地址
     */
    public void download(String bucketName, String filePath, String savePath) {
        DownloadObjectArgs args = DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .filename(savePath)
                .build();
        try {
            minioClient.downloadObject(args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文档下载失败");
        }
    }

    /**
     * 文件输出到流，
     * 注意：输出流不关闭，请自行关闭
     *
     * @param bucketName 存储桶
     * @param filePath   文件保存路径
     * @param out        输出流
     */
    public GetObjectResponse download(String bucketName, String filePath, OutputStream out) {
        if (Objects.isNull(out)) {
            throw new RuntimeException("输出流不存在");
        }
        GetObjectResponse response = getObject(bucketName, filePath);
        if (Objects.nonNull(response)) {
            IoUtils.copy(response, out);
            IoUtils.close(response);
            return response;
        }
        throw new RuntimeException("文档获取失败");
    }

    /**
     * 删除对象.
     *
     * @param bucketName 存储桶
     * @param filePath   文件路径
     */
    public void removeObject(String bucketName, String filePath) {
        if (bucketExists(bucketName)) {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build();
            try {
                minioClient.removeObject(args);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("文件删除失败");
            }
        }
    }
}