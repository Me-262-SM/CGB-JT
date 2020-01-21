package com.jt.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO implements Serializable {
    private Integer error;//0正常，1失败
    private String url;
    private Integer width;
    private Integer height;

    public static ImageVO fail() {
        return new ImageVO(1, null, null, null);
    }


    public static ImageVO success(String url, Integer width, Integer height) {
        return new ImageVO(0, url, width, height);
    }

}
