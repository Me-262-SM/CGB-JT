package com.jt.service.imp;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.vo.EasyUITable;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("ALL")
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired    //spring容器创建代理对象
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public EasyUITable findItemByPage(Integer page, Integer rows) {
//        Integer count = itemMapper.selectCount(null);
//        Integer start = (page-1)*rows;
//        List<Item> list = itemMapper.findItemByPage(start, rows);
        IPage<Item> iPage = new Page<>(page, rows);
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("updated");
        IPage<Item> itemPage = itemMapper.selectPage(iPage, queryWrapper);
        int total = (int) itemPage.getTotal();
        List<Item> itemList = itemPage.getRecords();

        return new EasyUITable(total, itemList);
    }


    /**
     * Spring中事务回滚策略
     * 默认策略:
     * 如果程序执行,遇到运行时异常,则事务自动回滚.
     * 如果程序执行,遇到检查异常/编译异常,
     * spring不负责事务的回滚.
     *
     * @throws SQLException
     */
    @Transactional
    @Override
    public void saveItem(Item item) {
        item.setStatus(1)  //表示正常
                .setCreated(new Date())
                .setUpdated(item.getCreated());
        itemMapper.insert(item);
    }


    /**
     * Spring中事务回滚策略
     * 默认策略:
     * 如果程序执行,遇到运行时异常,则事务自动回滚.
     * 如果程序执行,遇到检查异常/编译异常,
     * spring不负责事务的回滚.
     *
     * @throws SQLException
     */
    @Transactional
    @Override
    public void saveItem(Item item, ItemDesc itemDesc) {
        item.setStatus(1)  //表示正常
                .setCreated(new Date())
                .setUpdated(item.getCreated());
        //MP:当数据入库之后,会自动的回显主键信息.
        itemMapper.insert(item);

        //补齐数据
        //问题: item是主键自增.只有入库之后才有主键.但是对象中没有主键信息.
        itemDesc.setItemId(item.getId())
                .setCreated(item.getCreated())
                .setUpdated(item.getCreated());
        itemDescMapper.insert(itemDesc);
    }


    @Override
    public ItemDesc findItemDescById(Long itemId) {
        return itemDescMapper.selectById(itemId);
    }


    /**
     * 一般使用主键充当更新条件
     */
    @Transactional
    @Override
    public void updateItem(Item item, ItemDesc itemDesc) {
        item.setUpdated(new Date());
        //主键充当where条件,其他属性充当set属性值.
        itemMapper.updateById(item);

        itemDesc.setItemId(item.getId())
                .setUpdated(item.getUpdated());
        //时间保持一致
        itemDescMapper.updateById(itemDesc);
    }

    @Override
    @Transactional
    public void deleteItems(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        itemMapper.deleteBatchIds(idList);
        itemDescMapper.deleteBatchIds(idList);
    }


    /**
     * 修改内容为status/updated,条件id
     * sql: update tb_item set status=#{status},updated=#{updated}
     * where id in (id1,id2,id3);
     */
    @Override
    @Transactional
    public void updateStatus(Long[] ids, int status) {
        Item item = new Item();
        item.setStatus(status).setUpdated(new Date());
        UpdateWrapper<Item> updateWrapper = new UpdateWrapper<Item>();
        List<Long> idList = Arrays.asList(ids);
        updateWrapper.in("id", idList);
        itemMapper.update(item, updateWrapper);
    }


    @Override
    public Item findItemById(Long itemId) {
        return itemMapper.selectById(itemId);
    }


}
