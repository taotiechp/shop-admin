package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.cart.mapper.ICartMapper;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.cart.vo.CartVo;
import com.fh.shop.api.common.Enum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.*;
import com.itextpdf.text.log.SysoCounter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

@Service("cartService")
@Component
@EnableScheduling
public class ICartServiceImpl implements ICartService {
    @Autowired
    private ICartMapper serviceCart;
    @Autowired
    private IProductMapper productMapper;

    /**
     * 购物车增加商品
     * @param productId
     * @param count
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse savaCart(long productId, long count, long memberId) {
        //判断商品是否存在
        Product product = productMapper.selectById(productId);
        if (product==null){
            return ServerResponse.success(Enum.PRODUCT_IS_NAME);
        }
        //判断商品状态
        int isPutAway = product.getIsPutaway();
        if (isPutAway != 1){
            return ServerResponse.error(Enum.PRODUCT_IS_NUIP);
        }

        //判断是否有购物车
        String cartJson = RedisUtil.hget(SystemConst.CART_HMAP, KeyUtil.buildMemberKey(String.valueOf(memberId)));
        //没有购物车
        if (StringUtils.isEmpty(cartJson)){
            //创建购物车容器
            CartVo cartVo = new CartVo();
            CartVo cartVos = buildCartItem(cartVo, count, product);
            //更新购物车
            updateCart(cartVos,memberId);
            return ServerResponse.success();
        }
        //有购物车，则从购物车取出商品
        CartVo cartVo = JSONObject.parseObject(cartJson, CartVo.class);
        List<CartItem> cartItem = cartVo.getCartItem();
        boolean existflag = false;
        CartItem cartItems = null;
        for (CartItem item : cartItem) {
            if (item.getId()==productId){
                existflag = true;
                cartItems = item;
                break;
            }
        }
        //如果添加的商品不存在，则将商品加入购物车
        if (!existflag){
            //创建购物车容器
            CartVo cartVo2 = buildCartItem(cartVo,count, product);
            //更新购物车
            updateCart(cartVo2,memberId);
            return ServerResponse.success();
        }
        //商品如果存在，则更新商品数量及小计
        long counted =  cartItems.getCount()+count;
        //判断当商品数量等于0时删除当前商品
        if (counted < 1){
            cartVo.getCartItem().remove(cartItems);
            //更新购物车
            updateCart(cartVo,memberId);
            //RedisUtil.hdel(SystemConst.CART_HMAP,String.valueOf(memberId));
        }else{
            cartItems.setCount(cartItems.getCount()+count);
            String totalPrice = BigDecimalUtil.mul(cartItems.getPrice().toString(),String.valueOf(cartItems.getCount())).toString();
            cartItems.setSubTotalPrice(totalPrice);
            //更新购物车
            updateCart(cartVo,memberId);
        }
        return ServerResponse.success();
    }

    /**
     * 购物车列表
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse findCart(long memberId) {
        //判断是否有购物车
        String cartJson = RedisUtil.hget(SystemConst.CART_HMAP, KeyUtil.buildMemberKey(String.valueOf(memberId)));
        if (StringUtils.isEmpty(cartJson)){
            return ServerResponse.error();
        }
        CartVo cartVo = JSONObject.parseObject(cartJson, CartVo.class);
        List<CartItem> cartItem = cartVo.getCartItem();
        return ServerResponse.success(cartVo);
    }

    /**
     * 购物车删除
     * @param productId
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse delCartProduct(long productId, long memberId) {
        String cartJson = RedisUtil.hget(SystemConst.CART_HMAP, KeyUtil.buildMemberKey(String.valueOf(memberId)));
        //判断redis中是否有数据
        if (StringUtils.isEmpty(cartJson)){
            return ServerResponse.success(Enum.PRODUCT_IS_NAME);
        }
        //判断商品列表是否有我们需要的数据
        Boolean flag = false;
        CartVo cartVo = JSONObject.parseObject(cartJson, CartVo.class);
        List<CartItem> cartItem = cartVo.getCartItem();
        for (CartItem item : cartItem) {
            if (item.getId()==productId){
                cartItem.remove(item);
                flag = true;
                break;
            }
        }
        if (!flag){
            return ServerResponse.success(Enum.PRODUCT_IS_NAME);
        }
        //更新购物车
        cartVo.setCartItem(cartItem);
        updateCart(cartVo,memberId);
        return ServerResponse.success();
    }

    /**
     * 结算获取token
     * @return
     */
    @Override
    public ServerResponse kontCart() {
        String token = UUID.randomUUID().toString();
        RedisUtil.setEX(token,token,60*30);
        return ServerResponse.success(token);
    }

    /**
     * 定时查询数据库库存
     * @throws Exception
     */
    //@Scheduled(cron = "*/5 * * * * ?")//每隔5秒执行一次
    /*public void test(){
        List<Product> productList = productMapper.findStock();
        String table = "";
        table+="<table border = '1'>";
        table+="<tr>\n" +
                "        <td>商品</td>\n" +
                "        <td>库存</td>\n" +
                "        <td>价格</td>\n" +
                "    </tr>";
        for (Product product : productList) {
            table+=
                    "<tr>\n" +
                    "        <td>"+product.getProductName()+"</td>\n" +
                    "        <td>"+product.getStock()+"</td>\n" +
                    "        <td>"+product.getProductPrice()+"</td>\n" +
                    "    </tr>";

        }
        table+="</table>";
        try {
            Email.sendEmail("1670415267@qq.com","王磊",table);//532028476
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }*/

    //====================================================================================//

    //更新购物车
    private void updateCart(CartVo cartVo,long memberId) {
        List<CartItem> cartItemList = cartVo.getCartItem();
        if (cartItemList.size()<1){
            RedisUtil.hdel(SystemConst.CART_HMAP,KeyUtil.buildMemberKey(String.valueOf(memberId)));
        }else{
        Long totalCount = 0L;
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItem item : cartItemList) {
            totalCount += item.getCount();
            totalPrice = BigDecimalUtil.add(String.valueOf(totalPrice),item.getSubTotalPrice());
        }
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        String jsonString = JSONObject.toJSONString(cartVo);
        RedisUtil.hset(SystemConst.CART_HMAP,KeyUtil.buildMemberKey(String.valueOf(memberId)),jsonString);
        }
    }

    //创建购物车容器
    private CartVo buildCartItem(CartVo cartVo,long count, Product product) {
        //创建商品容器
        CartItem cartItem = new CartItem();
        cartItem.setId(product.getId());
        cartItem.setCount(count);
        cartItem.setImage(product.getProductPhoto());
        cartItem.setPrice(product.getProductPrice());
        cartItem.setProductName(product.getProductName());
        //小计
        String mul = BigDecimalUtil.mul(String.valueOf(product.getProductPrice()), String.valueOf(count)).toString();
        cartItem.setSubTotalPrice(mul);
        //添加商品
        cartVo.getCartItem().add(cartItem);
        return cartVo;
    }


}
