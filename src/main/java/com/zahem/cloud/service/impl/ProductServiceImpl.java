package com.zahem.cloud.service.impl;

import com.zahem.cloud.config.AxiosResponse;
import com.zahem.cloud.dao.ProductMapper;
import com.zahem.cloud.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询所有商品
     * @return
     */
    public AxiosResponse selectAll(){
        Product product = productMapper.selectProduct();
        return AxiosResponse.success(product);
    }

}
