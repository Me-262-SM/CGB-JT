package com.jt.service.imp;

import java.util.ArrayList;
import java.util.List;

import com.jt.annotation.CacheFind;
import com.jt.service.ItemCatService;
import com.jt.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

@SuppressWarnings("ALL")
@Service
public class ItemCatServiceImp implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired(required = false)
    private Jedis jedis;


    @Override
    @CacheFind
    public List<EasyUITree> findItemCatByParentId(Long parentId) {
        ArrayList<EasyUITree> treelist = new ArrayList<>();
        List<ItemCat> itemlist = findItemCatListByParentId(parentId);
        for (ItemCat itemCat : itemlist) {
            Long id = itemCat.getId();
            String text = itemCat.getName();
            String state = itemCat.getIsParent() ? "closed" : "open";
            EasyUITree uiTree = new EasyUITree(id, text, state);
            treelist.add(uiTree);
        }
        return treelist;
    }


    public List<ItemCat> findItemCatListByParentId(Long parentId) {
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<ItemCat> itemlist = itemCatMapper.selectList(queryWrapper);
        return itemlist;
    }


    @Override
    public ItemCat findItemCatById(Long itemCatId) {
        return itemCatMapper.selectById(itemCatId);
    }


    @Override
    public List<EasyUITree> findItemCatCache(Long parentId) {
        String key = "com.jt.service.imp.ItemCatServiceImp.findItemCatCache."+parentId;
        String value = jedis.get(key);
        List<EasyUITree> treelist = new ArrayList<>();
        if (StringUtils.isEmpty(value)) {
            //说明用户第一次查询
            treelist = findItemCatByParentId(parentId);
            String json = JacksonUtils.toJson(treelist);
            jedis.set(key, json);
        } else {
            treelist = JacksonUtils.toPojo(value, treelist.getClass());
        }
        return treelist;
    }

}
