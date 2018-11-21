package com.mi.hundsun.oxchains.base.core.aws.s3.bucketPolicy;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class DeleteBucketPolicy {
    public static void main(String[] args)
    {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    DeleteBucketPolicy <bucket>\n\n" +
                "Where:\n" +
                "    bucket - the bucket to delete the policy from.\n\n" +
                "Example:\n" +
                "    DeleteBucketPolicy testbucket\n\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bucket_name = args[0];
        String policy_text = null;

        System.out.format("Deleting policy from bucket: \"%s\"\n\n", bucket_name);

        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            s3.deleteBucketPolicy(bucket_name);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        System.out.println("Done!");
    }
}
