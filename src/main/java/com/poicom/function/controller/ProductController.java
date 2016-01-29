package com.poicom.function.controller;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.poicom.function.model.Product;
import com.poicom.function.service.ProductService;
import com.poicom.function.validator.ProductValidator;

@ControllerKey(value = "/admin/product", path="/app/admin")
public class ProductController extends BaseController {
    
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ProductController.class);
    
    private final static String PRODUCT_ADD_PAGE="product/add.html";
    private final static String PRODUCT_EDIT_PAGE="product/edit.html";
    
    public void index(){
        Page<Product> productPage;
        String orderby=" ORDER BY product.id ";
        if(ValidateKit.isNullOrEmpty(getPara("product"))){
            String where = " 1=1 ";
            productPage=Product.dao.findProductPage(getParaToInt(0,1), 10, where, orderby);
        }else{
            String where=" product.name like ?  ";
            String condition ="%"+getPara("product").trim()+"%";
            productPage=Product.dao.findProductPage(getParaToInt(0,1), 10, where, orderby, condition);
            setAttr("product",getPara("product").trim());
        }
        setAttr("productPage",productPage);
        render("product.html");
    }
    
    /**
     * 准备新增单位
     */
    public void add(){
        render(PRODUCT_ADD_PAGE);
    }
    
    /**
     * 新增单位
     */
    @Before(ProductValidator.class)
    public void save(){
        Product product=getModel(Product.class);
        ProductService.service.save(product);
        redirect("/admin/product");
    }
    
    /**
     * 准备更新单位
     */
    public void edit(){
        Product product=Product.dao.findById(getPara("id"));
        setAttr("product",product);
        render(PRODUCT_EDIT_PAGE);
    }
    
    /**
     * 更新单位
     */
    @Before(ProductValidator.class)
    public void update(){
        Product product=getModel(Product.class);
        ProductService.service.update(product);
        redirect("/admin/product");
    }
    
    /**
     * 设置不可用
     */
    public void off(){
        ProductService.service.off(Product.dao.findById(getPara("id")));
        redirect("/admin/product");
    }
    
    /**
     * 设置可用
     */
    public void on(){
        ProductService.service.on(Product.dao.findById(getPara("id")));
        redirect("/admin/product");
    }
    
    public void treeData(){
        
        String json=ProductService.service.treeNodeData();
        renderJson(json);
    }

}
