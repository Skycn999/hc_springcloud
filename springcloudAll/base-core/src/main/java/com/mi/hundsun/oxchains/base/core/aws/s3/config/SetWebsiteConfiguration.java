package com.mi.hundsun.oxchains.base.core.aws.s3.config;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.AmazonServiceException;

public class SetWebsiteConfiguration {
    public static void setWebsiteConfig(
            String bucket_name, String index_doc, String error_doc)
    {
        BucketWebsiteConfiguration website_config = null;

        if (index_doc == null) {
            website_config = new BucketWebsiteConfiguration();
        } else if (error_doc == null) {
            website_config = new BucketWebsiteConfiguration(index_doc);
        } else {
            website_config = new BucketWebsiteConfiguration(index_doc, error_doc);
        }

        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            s3.setBucketWebsiteConfiguration(bucket_name, website_config);
        } catch (AmazonServiceException e) {
            System.out.format(
                    "Failed to set website configuration for bucket '%s'!\n",
                    bucket_name);
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args)
    {
        final String USAGE = "\n" +
                "SetWebsiteConfiguration - set the website configuration for an S3 bucket\n\n" +
                "Usage: SetWebsiteConfiguration <bucket> [indexdoc] [errordoc]\n\n" +
                "Where:\n" +
                "   bucket   - the bucket to set the website configuration on\n" +
                "   indexdoc - (optional) the index document, ex. 'index.html'\n" +
                "              If not specified, 'index.html' will be set.\n" +
                "   errordoc - (optional) the error document, ex. 'notfound.html'\n" +
                "              If not specified, no error doc will be set.\n";

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        final String bucket_name = args[0];
        final String index_doc = (args.length > 1) ? args[1] : "index.html";
        final String error_doc = (args.length > 2) ? args[2] : null;
        setWebsiteConfig(bucket_name, index_doc, error_doc);
    }
}
