package com.mi.hundsun.oxchains.base.core.aws.s3.bucketPolicy;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketPolicy;

public class GetBucketPolicy {
    public static void main(String[] args)
    {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    GetBucketPolicy <bucket>\n\n" +
                "Where:\n" +
                "    bucket - the bucket to get the policy from.\n\n" +
                "Example:\n" +
                "    GetBucketPolicy testbucket\n\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bucket_name = args[0];
        String policy_text = null;

        System.out.format("Getting policy for bucket: \"%s\"\n\n", bucket_name);

        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            BucketPolicy bucket_policy = s3.getBucketPolicy(bucket_name);
            policy_text = bucket_policy.getPolicyText();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        if (policy_text == null) {
            System.out.println("The specified bucket has no bucket policy.");
        } else {
            System.out.println("Returned policy:");
            System.out.println("----");
            System.out.println(policy_text);
            System.out.println("----\n");
        }

        System.out.println("Done!");
    }
}
