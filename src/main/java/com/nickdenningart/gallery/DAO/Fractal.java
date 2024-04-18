package com.nickdenningart.gallery.DAO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class Fractal implements Comparable<Fractal>{
    private String id;
    private String date;
    private String title;
    private List<String> tags; 
    private String printsUrl;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Fractal fractal) {
        return -1 * date.compareTo(fractal.date);
    }

}
