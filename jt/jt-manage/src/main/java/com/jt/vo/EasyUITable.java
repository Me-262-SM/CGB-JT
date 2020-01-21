package com.jt.vo;

import java.io.Serializable;
import java.util.List;

import com.jt.pojo.Item;

import lombok.Data;

@Data
public class EasyUITable implements Serializable{
    private static final long serialVersionUID = -1935396315380805931L;
    private Integer total;
    private List<Item> rows;
    
    
    public EasyUITable(Integer total, List<Item> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
     
    
    
}
