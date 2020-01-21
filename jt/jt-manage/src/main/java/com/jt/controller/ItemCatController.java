package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat/")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("queryItemName")
    public String findItemCatNameById(Long itemCatId) {
        ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
        return itemCat.getName();
    }


    /**
     * defaultValue: 默认值 如果不传数据则使用默认值
     * name/value:   表示接收的数据.
     * required:     是否为必传数据. 默认值true
     */
    @RequestMapping("list")
    public List<EasyUITree> findItemCatByParentId(@RequestParam(defaultValue = "0", name = "id") Long parentId) {
        //临时方法
//        return itemCatService.findItemCatCache(parentId);
        return itemCatService.findItemCatByParentId(parentId);
    }

}
