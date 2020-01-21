package com.jt.service;

import java.util.List;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {
    
    EasyUITable findItemByPage(Integer page, Integer rows);

    void saveItem(Item item);

    void saveItem(Item item, ItemDesc itemDesc);

    ItemDesc findItemDescById(Long itemId);

    void updateItem(Item item, ItemDesc itemDesc);

    void deleteItems(Long[] ids);

    void updateStatus(Long[] ids, int status);

    Item findItemById(Long itemId);
}
