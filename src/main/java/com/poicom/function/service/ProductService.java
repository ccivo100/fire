package com.poicom.function.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.jfinal.log.Logger;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.Phone;
import com.poicom.function.model.Product;

public class ProductService extends BaseService {
    
    private static Logger log = Logger.getLogger(ProductService.class);
    
    public static final ProductService service = new ProductService();
    
    /**
     * 新增
     * @param product
     * @return
     */
    public Long save(Product product){
        product.set("name", WebKit.delHTMLTag(product.getStr("name")));
        product.save();
        return product.getPKValue();
    }
    
    /**
     * 更新、设置不可用
     * @param product
     */
    public void update(Product product){
        product.set("name", WebKit.delHTMLTag(product.getStr("name")));
        product.update();
        Product.dao.cacheAdd(product.getPKValue());
    }
    
    /**
     * 删除
     * @param productid
     */
    public void drop(Long productid){
        Product.dao.cacheRemove(productid);
        Product.dao.deleteById(productid);
    }
    
    public boolean off(Product product){
        return product.set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
    }
    
    public boolean on(Product product){
        return product.set("deleted_at", null).update();
    }
    
    /**
     * 用于zTree，查询分公司分配部门
     * @return
     */
    public String treeNodeData(){
        
        List<Phone> rootNode = Phone.dao.rootNode();
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        
        int phoneSize= rootNode.size();
        int phoneIndexSize =phoneSize-1;
        for(Phone phone:rootNode){
            sb.append(" { ");
            sb.append(" id : '").append(phone.getPKValue()).append("', ");
            sb.append(" pId : '").append(phone.get("pid")).append("', ");
            sb.append(" name : '").append(phone.getStr("name")).append("', ");
            sb.append(" checked : false, ");
            sb.append(" font : {'font-weight':'bold'} ");
            sb.append(" }");
            
            List<Phone> childNode = Phone.dao.rootNode(" pid=? ",phone.get("id"));
            int childPhoneSize= childNode.size();
            if ((rootNode.indexOf(phone) < phoneIndexSize)||childPhoneSize>0) {
                sb.append(", ");
            }
            for(Phone child:childNode){
                sb.append(" { ");
                sb.append(" id : '").append(child.getPKValue()).append("', ");
                sb.append(" pId : '").append(child.get("pid")).append("', ");
                sb.append(" name : '").append(child.getStr("name")).append("', ");
                sb.append(" checked : false, ");
                sb.append(" font : {'font-weight':'bold'},");
                sb.append(" }");
                sb.append(", ");
            }
        }
        sb.append("]");
        
        return sb.toString();
    }

}
