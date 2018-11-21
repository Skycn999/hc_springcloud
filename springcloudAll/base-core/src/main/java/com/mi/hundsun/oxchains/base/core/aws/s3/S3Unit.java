package com.mi.hundsun.oxchains.base.core.aws.s3;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Unit {
    private static final Logger logger    = LoggerFactory.getLogger(S3Unit.class);
    private final static String accessKey = "AKIAJGAP4UCGWLNRYY3A";
    private final static String secretKey = "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";
    private AWSCredentials      credentials;
    private AmazonS3            s3;

    private S3Unit() {

        credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.DEFAULT_REGION).build();
    }

    private static class HolderClass {
        private final static S3Unit instance = new S3Unit();

        private HolderClass() {
        }
    }

    public static S3Unit getInstance() {
        return HolderClass.instance;
    }

    private boolean checkExist(String bucketName) {
        List<Bucket> list = s3.listBuckets();
        for (Bucket b : list) {
            if (b.getName().equals(bucketName))
                return true;
        }
        return false;
    }

    /**
     * 创建Bucket,无法重复创建
     * @param bucketName 最小6个字符
     * @return
     */
    private Bucket createBucket(String bucketName) {
        return s3.createBucket(bucketName);
    }

    public void deleteBucket(String bucketName) {
        s3.deleteBucket(bucketName);
    }

    public String saveFile(String bucketName, File file) throws Exception {
        try {
            String saveKey = UUID.randomUUID().toString();
            if (!checkExist(bucketName)) {
                createBucket(bucketName);
            }
            s3.putObject(new PutObjectRequest(bucketName, saveKey, file));
            logger.info("存放文件成功：bucketName={};key:={}", bucketName, saveKey);
            return saveKey;
        } catch (Exception e) {
            logger.error("保存文件出错" + e.toString());
            throw new Exception(e);

        }
    }

    public InputStream getFile(String bucketName, String key) throws Exception {
        try {
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            logger.info("提取文件成功：bucketName={};key:={}", bucketName, key);
            return object.getObjectContent();
        } catch (Exception e) {
            logger.error("获得文件出错" + e.toString());
            throw new Exception(e);
        }
    }
}
