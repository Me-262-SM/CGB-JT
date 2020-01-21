package com.jt.web;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/item/")
public class WebItemController {
    @Autowired
    private ItemService itemService;


    @RequestMapping("findItemById/{itemId}")
    public Item findItemById(@PathVariable Long itemId){
        return itemService.findItemById(itemId);
    }


    @RequestMapping("findItemDescById/{itemId}")
    public ItemDesc findItemDescById(@PathVariable Long itemId){
        return itemService.findItemDescById(itemId);
    }
}
