package com.nickdenningart.fractal.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nickdenningart.fractal.dto.printify.Image;
import com.nickdenningart.fractal.dto.printify.Placeholder;
import com.nickdenningart.fractal.dto.printify.PrintArea;
import com.nickdenningart.fractal.dto.printify.Product;
import com.nickdenningart.fractal.dto.printify.PublishingProperties;
import com.nickdenningart.fractal.dto.printify.Upload;
import com.nickdenningart.fractal.dto.printify.Variant;
import com.nickdenningart.fractal.exception.DynamoDbItemNotFoundException;
import com.nickdenningart.fractal.model.Fractal;

import software.amazon.awssdk.regions.providers.AwsRegionProvider;

@Service
public class PrintifyService {
    private final FractalService fractalService;
    private final String printifyUrl;
    private final String token;
    private final String baseImageUrl;
    private final String shop;
    private boolean prod = false;

    public PrintifyService(FractalService fractalService,  
                            @Value("${printify-api-url}") String printifyUrl,
                            @Value("${printify-token}") String token,
                            @Value("${printify-shop}") String shop,
                            @Value("${image-bucket}") String bucket,
                            Environment environment,
                            AwsRegionProvider regionProvider){
        this.fractalService = fractalService;
        this.printifyUrl = printifyUrl;
        this.shop = shop;
        this.token = token;
        // set prod if on prod profile
        for(String profile : environment.getActiveProfiles()){
            if (profile.equals("prod")) this.prod = true;
        }
        // build s3 image url to send to printify
        String region = regionProvider.getRegion().toString();
        this.baseImageUrl = "https://"+bucket+".s3."+region+".amazonaws.com/";
    }
    public void createProducts(String id) throws DynamoDbItemNotFoundException{
        Fractal fractal = fractalService.getFractal(id);

        // http client
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<PublishingProperties> publishingProperties = new HttpEntity<>(
            new PublishingProperties(true,true,true,true,true), headers);

        // upload landscape image
        // Upload upload14400x9600 = Upload.builder()
        //     .url(baseImageUrl+id+"/14400x9600.jpg")
        //     .file_name(id+"-144000x9600")
        //     .build();
        // HttpEntity<Upload> request14400x9600 = new HttpEntity<>(upload14400x9600,headers);
        // upload14400x9600 = restTemplate.postForObject(printifyUrl+"/uploads/images.json", request14400x9600, Upload.class);
        // Image image14400x9600 = Image.builder()
        //     .id(upload14400x9600.id())
        //     .x(0.5f)
        //     .y(0.5f)
        //     .angle(0)
        //     .scale(1.0f)
        //     .build();

        // upload portrait image
        // Upload upload9600x14400 = Upload.builder()
        //     .url(baseImageUrl+id+"/9600x14400.jpg")
        //     .file_name(id+"-9600x144000")
        //     .build();
        // HttpEntity<Upload> request9600x14400 = new HttpEntity<>(upload9600x14400,headers);
        // upload9600x14400 = restTemplate.postForObject(printifyUrl+"/uploads/images.json", request9600x14400, Upload.class);
        // Image image9600x14400 = 
        //     Image.builder()
        //     .id(upload9600x14400.id())
        //     .x(0.5f)
        //     .y(0.5f)
        //     .angle(0)
        //     .scale(1.0f)
        //     .build();

        // upload square image
        Upload upload4096x4096 = Upload.builder()
            .url(baseImageUrl+id+"/4096x4096.jpg")
            .file_name(id+"-4096x4096")
            .build();
        HttpEntity<Upload> request4096x4096 = new HttpEntity<>(upload4096x4096,headers);
        upload4096x4096 = restTemplate.postForObject(printifyUrl+"/uploads/images.json", request4096x4096, Upload.class);
        Image image4096x4096 = Image.builder()
            .id(upload4096x4096.id())
            .x(0.5f)
            .y(0.5f)
            .angle(0)
            .scale(1.0f)
            .build();

        // System.out.println("Horizontal Posters");
        // // Create Matte Horizontal Posters
        // Product mhp = Product.builder()
        //     .title("\""+fractal.getTitle()+"\" Fine Art Paper Horizontal")
        //     .tags(Arrays.asList(fractal.getTitle()))
        //     .description("High resolution print using archival inks on 175gm fine art paper")
        //     .blueprint_id(284)
        //     .print_provider_id(2)
        //     .variants(Arrays.asList(
        //         // 18x12 $25
        //         new Variant(43166,25_00, true),
        //         // 24x16 $35
        //         new Variant(101263,35_00, true),
        //         // 36x24 $45
        //         new Variant(43178,45_00, true)))
        //     .print_areas(Arrays.asList(
        //         PrintArea.builder()
        //             .variant_ids(Arrays.asList(
        //                 43166,101263,43178))
        //             .placeholders(Arrays.asList(
        //                 new Placeholder("front",Arrays.asList(image14400x9600))))
        //             .build()))
        //     .build();
        // HttpEntity<Product> requestMhp = new HttpEntity<>(mhp,headers);
        // mhp = restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products.json", requestMhp, Product.class);
        // if(prod) restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products/"+mhp.id()+"/publish.json", publishingProperties, PublishingProperties.class);

        // System.out.println("Vertical Posters");
        // // Create Matte Vertical Posters
        // Product mvp = Product.builder()
        //     .title("\""+fractal.getTitle()+"\" Fine Art Paper Vertical")
        //     .tags(Arrays.asList(fractal.getTitle()))
        //     .description("High resolution print using archival inks on 175gm fine art paper")
        //     .blueprint_id(282)
        //     .print_provider_id(2)
        //     .variants(Arrays.asList(
        //         // 12x18 $25
        //         new Variant(43138,25_00, true),
        //         // 16x24 $35
        //         new Variant(101113,35_00, true),
        //         // 24x36 $45
        //         new Variant(43150,45_00, true)))
        //     .print_areas(Arrays.asList(
        //         PrintArea.builder()
        //             .variant_ids(Arrays.asList(
        //                 43138,101113,43150))
        //             .placeholders(Arrays.asList(
        //                 new Placeholder("front",Arrays.asList(image9600x14400))))
        //             .build()))
        //     .build();
        // HttpEntity<Product> requestMvp = new HttpEntity<>(mvp,headers);
        // mvp = restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products.json", requestMvp, Product.class);
        // if(prod) restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products/"+mvp.id()+"/publish.json", publishingProperties, PublishingProperties.class);

        System.out.println("Square Posters");
        // Create Matte Square Poster
        Product msp = Product.builder()
            .title("\""+fractal.getTitle()+"\" Fine Art Paper")
            .tags(Arrays.asList(fractal.getTitle()))
            .description("High resolution print using archival inks on 175gm fine art paper")
            .blueprint_id(282)
            .print_provider_id(2)
            .variants(Arrays.asList(
                // 10x10 $20
                new Variant(101119,20_00, true),
                // 18x18 $25
                new Variant(101133,25_00, true),
                // 24x24 $30
                new Variant(101140,30_00, true)))
            .print_areas(Arrays.asList(
                PrintArea.builder()
                    .variant_ids(Arrays.asList(
                        101119,101133,101140))
                    .placeholders(Arrays.asList(
                        new Placeholder("front",Arrays.asList(image4096x4096))))
                    .build()))
            .build();
        HttpEntity<Product> requestMsp = new HttpEntity<>(msp,headers);
        msp = restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products.json", requestMsp, Product.class);
        if(prod) restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products/"+msp.id()+"/publish.json", publishingProperties, PublishingProperties.class);

        // System.out.println("Horizontal Acrylic");
        // // Create Horizontal Acrylic
        // Product ha = Product.builder()
        //     .title("\""+fractal.getTitle()+"\" Acrylic Horizontal")
        //     .tags(Arrays.asList(fractal.getTitle()))
        //     .description("High resolution print on acrylic panel with included mounting hardware")
        //     .blueprint_id(921)
        //     .print_provider_id(2)
        //     .variants(Arrays.asList(
        //         // 18x12 $200.00
        //         new Variant(78311,200_00, true),
        //         // 30x20 $400.00
        //         new Variant(78313,400_00, true),
        //         // 36x24 $500.00
        //         new Variant(78315,500_00, true)))
        //     .print_areas(Arrays.asList(
        //         PrintArea.builder()
        //             .variant_ids(Arrays.asList(
        //                 78311,78313,78315))
        //             .placeholders(Arrays.asList(
        //                 new Placeholder("front",Arrays.asList(image14400x9600))))
        //             .build()))
        //     .build();
        // HttpEntity<Product> requestHa = new HttpEntity<>(ha,headers);
        // ha = restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products.json", requestHa, Product.class);
        // if(prod) restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products/"+ha.id()+"/publish.json", publishingProperties, PublishingProperties.class);

        // System.out.println("Vertical Acrylic");
        // // Create Vertical Acrylic
        // Product va = Product.builder()
        //     .title("\""+fractal.getTitle()+"\" Acrylic Vertical")
        //     .tags(Arrays.asList(fractal.getTitle()))
        //     .description("High resolution print on acrylic panel with included mounting hardware")
        //     .blueprint_id(921)
        //     .print_provider_id(2)
        //     .variants(Arrays.asList(
        //         // 12x18 $200
        //         new Variant(78305,200_00, true),
        //         // 20x30 $400
        //         new Variant(78308,400_00, true),
        //         // 24x36 $500
        //         new Variant(78309,500_00, true)))
        //     .print_areas(Arrays.asList(
        //         PrintArea.builder()
        //             .variant_ids(Arrays.asList(
        //                 78305,78308,78309))
        //             .placeholders(Arrays.asList(
        //                 new Placeholder("front",Arrays.asList(image9600x14400))))
        //             .build()))
        //     .build();
        // HttpEntity<Product> requestVa = new HttpEntity<>(va,headers);
        // va = restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products.json", requestVa, Product.class);
        // if(prod) restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products/"+va.id()+"/publish.json", publishingProperties, PublishingProperties.class);

        System.out.println("Square Acrylic");
        // Create Square Acrylic
        Product sa = Product.builder()
            .title("\""+fractal.getTitle()+"\" Acrylic")
            .tags(Arrays.asList(fractal.getTitle()))
            .description("High resolution print on acrylic panel with included mounting hardware")
            .blueprint_id(921)
            .print_provider_id(2)
            .variants(Arrays.asList(
                // 12x12 $100.00
                new Variant(78316,100_00, true)))
            .print_areas(Arrays.asList(
                PrintArea.builder()
                    .variant_ids(Arrays.asList(78316))
                    .placeholders(Arrays.asList(
                        new Placeholder("front",Arrays.asList(image4096x4096))))
                    .build()))
            .build();
        HttpEntity<Product> requestSa = new HttpEntity<>(sa,headers);
        sa = restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products.json", requestSa, Product.class);
        if(prod) restTemplate.postForObject(printifyUrl+"/shops/"+shop+"/products/"+sa.id()+"/publish.json", publishingProperties, PublishingProperties.class);
    };
}
