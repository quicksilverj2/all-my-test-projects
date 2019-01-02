package com.smu.stakeme.model;

import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by jitheshrajan on 11/19/18.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Document(collection="GameHost")
public class GameHost {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String desc;

    @Getter
//    @Id
    private String id;

    @Getter @Setter
    private String url;

    @Getter @Setter
    private String imgUrl;

}
