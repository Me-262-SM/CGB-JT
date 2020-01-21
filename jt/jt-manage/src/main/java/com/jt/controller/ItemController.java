package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;

@RestController
@RequestMapping("/item/")
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping("query")
    public EasyUITable findItemByPage(Integer page, Integer rows) {
        return itemService.findItemByPage(page, rows);
    }


    /**
     * 实现商品信息新增
     * url:/item/save
     * 参数: key=value&key2=value2
     * 返回值: SysResult
     */
    @RequestMapping("save")
    public SysResult saveItem(Item item, ItemDesc itemDesc) {
        try {
            itemService.saveItem(item, itemDesc);
            return SysResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.fail();
        }
    }


    /**
     * 需求: 根据itemId查询商品详情信息
     * url:/item/query/item/desc/1474391972
     * 返回值结果: SysResult 携带数据返回(html).
     */
    @RequestMapping("/query/item/desc/{itemId}")
    public SysResult findItemDescById(@PathVariable Long itemId) {

        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        return SysResult.success(itemDesc);
    }


    /**
     * 需求:实现商品更新   item itemDesc
     * url: /item/update
     *   参数: 表单数据
     *  返回值: SysResult数据
     */
    @RequestMapping("/update")
    public SysResult updateItem(Item item,ItemDesc itemDesc) {

        itemService.updateItem(item,itemDesc);
        return SysResult.success();
    }


    /**
     * url: /item/delete
     *   参数:  {"ids":ids}  100,101,102
     *   返回值: SysResult
     * @return
     */
    @RequestMapping("/delete")
    public SysResult deleteItems(Long[] ids) {

        itemService.deleteItems(ids);
        return SysResult.success();
    }


    /**
     * 完成下架操作
     * url:/item/instock
     *   参数:   {"ids":ids}
     *   返回值: SysResult对象
     */
    @RequestMapping("/instock")
    public SysResult instockItems(Long[] ids) {
        int status = 2;	//下架状态
        itemService.updateStatus(ids,status);
        return SysResult.success();
    }

    @RequestMapping("/reshelf")
    public SysResult reshelfItems(Long[] ids) {
        int status = 1;	//上架状态
        itemService.updateStatus(ids,status);
        return SysResult.success();
    }

}
