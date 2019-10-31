package com.fh.shop.api.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.cart.vo.CartVo;
import com.fh.shop.api.common.Enum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.mapper.IOrderDetailMapper;
import com.fh.shop.api.order.mapper.IOrderMapper;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.order.po.OrderDetail;
import com.fh.shop.api.paylog.mapper.IPayLogMapper;
import com.fh.shop.api.paylog.po.PayLog;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("orderService")
public class IOrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderMapper orderMapper;
    @Autowired
    private IOrderDetailMapper orderDetailMapper;
    @Autowired
    private IProductMapper productMapper;
    @Autowired
    private IPayLogMapper payLogMapper;

    /**
     * 提交订单
     * @param orderParam
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse saveOrder(OrderParam orderParam, long memberId) {
        String cartJson = RedisUtil.hget(SystemConst.CART_HMAP, KeyUtil.buildMemberKey(String.valueOf(memberId)));
        //存放商品库存不足的商品
        List<CartItem> cartItemListStock = new ArrayList<>();
        //购物车非空判断
        if (StringUtils.isEmpty(cartJson)){
            return ServerResponse.error(Enum.CART_IS_NULL);
        }
        CartVo cartVo = JSONObject.parseObject(cartJson, CartVo.class);
        //购物车内非空判断
        if (cartVo==null){
            return ServerResponse.error(Enum.CART_IS_NULL);
        }
        //雪花算法
        //SnowflakeIdWorkerUtil idWorkerUtil = new SnowflakeIdWorkerUtil(1,2);
        //long orderId = idWorkerUtil.nextId();
        String orderId = IdWorker.getTimeId();

        //填写明细表
        List<CartItem> cartItem = cartVo.getCartItem();
        for (int i = cartItem.size()-1; i >= 0; i--) {
            CartItem item = cartItem.get(i);

            //查询当前商品
            Product product = productMapper.selectById(item.getId());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(item.getId());
            orderDetail.setProductName(item.getProductName());
            orderDetail.setProductPrice(item.getPrice());
            orderDetail.setProductCount(item.getCount());
            orderDetail.setSubTotalPrice(item.getSubTotalPrice());
            orderDetail.setProductImg(item.getImage());

            //判断当前库存是否充足，
            if (product.getStock()<item.getCount()){
                //库存不足，移出到库存不足的商品list
                cartItemListStock.add(item);
                //删除购物车中对应的商品
                cartItem.remove(item);
            }else {
                //库存充足，修改商品表库存量
                long updateStock = productMapper.updateStock(item.getId(), item.getCount());
                if (updateStock <= 0){
                    //库存不足，移出到库存不足的商品list
                    cartItemListStock.add(item);
                    //删除购物车中对应的商品
                    cartItem.remove(item);
                }else{
                    //添加到明细表
                    orderDetailMapper.insert(orderDetail);
                }
            }
        }
        //判断所选商品全部无货
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("orderId",orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectList(orderDetailQueryWrapper);
        if (orderDetails.size()<1){
            return ServerResponse.error(Enum.PRODUCT_IS_STOCK);
        }

        //更新购物车
        //List<CartItem> cartItemList = cartVo.getCartItem();
        if (cartItem.size()<1){
            RedisUtil.hdel(SystemConst.CART_HMAP,KeyUtil.buildMemberKey(String.valueOf(memberId)));
        }else{
            Long totalCount = 0L;
            BigDecimal totalPrice = new BigDecimal(0);
            for (CartItem item : cartItem) {
                totalCount += item.getCount();
                totalPrice = BigDecimalUtil.add(String.valueOf(totalPrice),item.getSubTotalPrice());
            }
            cartVo.setTotalCount(totalCount);
            cartVo.setTotalPrice(totalPrice.toString());
            //String jsonString = JSONObject.toJSONString(cartVo);
            //RedisUtil.hset(SystemConst.CART_HMAP,KeyUtil.buildMemberKey(String.valueOf(memberId)),jsonString);
        }

        //填写订单信息
        Order order = new Order();
        order.setOrderId(String.valueOf(orderId));
        order.setMemberId(memberId);
        order.setPayType(orderParam.getPayType());
        order.setTotalPrice(cartVo.getTotalPrice());
        order.setTotalCount(cartVo.getTotalCount());
        order.setCreateDate(new Date());
        order.setStatus(1);
        order.setConsigneeName(orderParam.getConsigneeName());
        order.setShippingSite(orderParam.getShippingSite());
        order.setConsigneePhone(orderParam.getConsigneePhone());

        orderMapper.insert(order);

        //填写支付日志表
        PayLog payLog = new PayLog();
        payLog.setOutTradeNo(IDUtil.getId());
        payLog.setMemberId(memberId);
        payLog.setOrderId(orderId);
        //payLog.setTransaction(IdWorker.getTimeId());
        payLog.setCreateTime(new Date());
        payLog.setPayMoney(new BigDecimal(order.getTotalPrice()));
        payLog.setPayType(order.getPayType());
        String jsonString = JSONObject.toJSONString(payLog);
        RedisUtil.set("pay"+memberId,jsonString);
        payLogMapper.insert(payLog);
        //删除购物车
        RedisUtil.hdel(SystemConst.CART_HMAP,KeyUtil.buildMemberKey(String.valueOf(memberId)));
        return ServerResponse.success(cartItemListStock);
    }
}
