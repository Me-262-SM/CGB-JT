package com.jt.service.imp;

import com.jt.annotation.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.util.HttpService;
import com.jt.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private HttpService httpService;

    @Override
    @CacheFind
    public Item findItemById(Long itemId) {
        String url = "http://manage.jt.com/web/item/findItemById/"+itemId;
        String json = httpService.doGet(url);
        return JacksonUtils.toPojo(json, Item.class);
    }


    @Override
    @CacheFind
    public ItemDesc findItemDescById(Long itemId) {
        String url = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
        String json = httpService.doGet(url);
        return JacksonUtils.toPojo(json, ItemDesc.class);
    }
}
