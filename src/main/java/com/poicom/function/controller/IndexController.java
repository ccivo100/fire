package com.poicom.function.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.captcha.CaptchaRender;
import cn.dreampie.encription.EncriptionKit;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.freemarker.HasRoleTag;
import cn.dreampie.shiro.hasher.Hasher;
import cn.dreampie.shiro.hasher.HasherInfo;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.kit.AlertKit;
import com.poicom.basic.kit.DateKit;
import com.poicom.basic.thread.ThreadAlert;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Order;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.service.IndexService;
import com.poicom.function.service.OrderService;
import com.poicom.function.validator.IndexValidator;

/**
 * 
 * @author 陈宇佳
 *
 */
@ControllerKey(value = "/", path = "/app/index")
public class IndexController extends BaseController {

    private final static String indexView = "index.html";
    private final static String allView = "all.html";
    private final static String undealView = "undeal.html";
    private final static String dealingView = "dealing.html";
    private final static String waitreplyView = "waitreply.html";
    private final static String waitrevisitView = "waitrevisit.html";
    private final static String revisitView = "revisit.html";
    private final static String INDEX_QUERY_PAGE="query.html";
    private final static String RETRIEVE_PAGE="retrieve.html";
    private final static String REPASSWORD_PAGE="repassword.html";
    protected Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 主页根目录
     * */
    @Before(IndexValidator.class)
    public void index() {
        
        User user=SubjectKit.getUser();
        if(ValidateKit.isNullOrEmpty(user)){
            redirect("/signin");
        }
        else if(SubjectKit.getSubject().hasRole("R_ADMIN")){
            redirect("/admin");
        }
        else{
            Page <Record> ordersPage;
            String orderby=" ORDER BY o.offer_at DESC";
            
            String where=" 1=1 and o.deleted_at is null ";
            ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
            OrderService.service.format(ordersPage, "title");
            setAttr("overOrderPage",ordersPage);
            render(indexView);
        } 
    }
    /**
     * 主页根目录
     * */
    @Before(IndexValidator.class)
    public void all() {
        User user=User.dao.getCurrentUser();//查询本账号下的工单
        Page<Record> allPage;//返回查询结果，并覆盖原来的申报列表
        
        //拼接where语句
        StringBuffer whereadd=new StringBuffer();
        //保存condition条件
        List<Object> conditions=new ArrayList<Object>();
        String title=getPara("title");
        String client_num=getPara("client_num");
        String client_phone=getPara("client_phone");
        //String otherss=getPara("otherss");
        //String product_type=getPara("product_type");
        //String typess=getPara("typess");
        //String offertime=getPara("offertime");
        //String offertime1=getPara("offertime1");
        //String branch=getPara("branch");
        //String offeruser=getPara("offeruser");
        
        //若无查询条件 则按正常查询。
        if(ValidateKit.isNullOrEmpty(title)
                &&ValidateKit.isNullOrEmpty(client_num)
                &&ValidateKit.isNullOrEmpty(client_phone)){
                //&&ValidateKit.isNullOrEmpty(otherss)
                //&&ValidateKit.isNullOrEmpty(product_type)
                //&&ValidateKit.isNullOrEmpty(typess)
                //&&ValidateKit.isNullOrEmpty(offertime)
                //&&ValidateKit.isNullOrEmpty(offertime1)){
            
            String where="  1=1 and o.deleted_at is null ";
            String orderby=" ORDER BY o.offer_at DESC ";
            allPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10,where,orderby);
        }else{
            //查询标题
            if(!ValidateKit.isNullOrEmpty(title)){
                whereadd.append(" and o.title like ? ");
                conditions.add("%"+getPara("title").trim()+"%");
                setAttr("title",title);
            }
            //查询客户编号
            if(!ValidateKit.isNullOrEmpty(client_num)){
                whereadd.append(" and o.client_num like ? ");
                conditions.add("%"+getPara("client_num").trim()+"%");
                setAttr("client_num",client_num);
            }
            //查询终端手机号
            if(!ValidateKit.isNullOrEmpty(client_phone)){
                whereadd.append(" and o.client_phone like ? ");
                conditions.add("%"+getPara("client_phone").trim()+"%");
                setAttr("client_phoner",client_phone);
            }
            /*
            //查询故障大类
            if(!ValidateKit.isNullOrEmpty(typess)){
                whereadd.append(" and o.typess like ? ");
                conditions.add("%"+getPara("typess").trim()+"%");
                setAttr("typess",typess);
            }
            //查询IMEI码
            if(!ValidateKit.isNullOrEmpty(otherss)){
                whereadd.append(" and o.otherss like ? ");
                conditions.add("%"+getPara("otherss").trim()+"%");
                setAttr("otherss",otherss);
            }
            //查询产品名称
            if(!ValidateKit.isNullOrEmpty(product_type)){
                whereadd.append(" and o.product_type like ? ");
                conditions.add("%"+getPara("product_type").trim()+"%");
                setAttr("product_type",product_type);
            }
            //查询申报时间
            if(ValidateKit.isNullOrEmpty(offertime)&&!ValidateKit.isNullOrEmpty(offertime1)){
                System.out.println("请输入开始时间，结束时间必须大于开始时间！");
            }
            //查询申报时间
            if(!ValidateKit.isNullOrEmpty(offertime)&&ValidateKit.isNullOrEmpty(offertime1)){
                System.out.println("请输入结束时间，结束时间必须大于开始时间！");
            }
            //查询申报时间
            if(!ValidateKit.isNullOrEmpty(offertime)){
                whereadd.append(" and o.offer_at like ? ");
                conditions.add(getPara("offertime").trim()+"%");
            /*
            //查询申报人
            if(!ValidateKit.isNullOrEmpty(offeruser)){
                whereadd.append(" and u1.full_name like ? ");
                conditions.add("%"+getPara("offeruser").trim()+"%");
                setAttr("offeruser",offeruser);
            }
            //查询申报人公司
            if(!ValidateKit.isNullOrEmpty(branch)){
                whereadd.append(" and u1.bname like ? ");
                conditions.add("%"+getPara("branch").trim()+"%");
                setAttr("branch",branch);
            }
            */
            String where=" 1=1 and o.deleted_at is null "+whereadd.toString();
            String orderby=" ORDER BY o.offer_at DESC ";
            Object[] condition= new Object[conditions.size()];
            conditions.toArray(condition);
            allPage=Order.dao.findReportsByUserId(getParaToInt(0,1), 10,where,orderby,condition);
        }
        OrderService.service.format(allPage, "title");
        setAttr("allPage",allPage);
        render(allView);
    }
    /**
     * 未处理
     * */
    @Before(IndexValidator.class)
    public void undeal() {
        
        User user=SubjectKit.getUser();
        if(ValidateKit.isNullOrEmpty(user)){
            redirect("/signin");
        }
        else if(SubjectKit.getSubject().hasRole("R_ADMIN")){
            redirect("/admin");
        }
        else{
            Page <Record> ordersPage;
            String orderby=" ORDER BY o.offer_at DESC";
            
            String where=" 1=1 and o.status=2 and o.deleted_at is null ";
            ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
            OrderService.service.format(ordersPage, "title");
            setAttr("overOrderPage",ordersPage);
            render(undealView);
        } 
    }
    /**
     * 处理中
     * */
    @Before(IndexValidator.class)
    public void dealing() {
        
        User user=SubjectKit.getUser();
        if(ValidateKit.isNullOrEmpty(user)){
            redirect("/signin");
        }
        else if(SubjectKit.getSubject().hasRole("R_ADMIN")){
            redirect("/admin");
        }
        else{
            Page <Record> ordersPage;
            String orderby=" ORDER BY o.offer_at DESC";
            
            String where=" 1=1 and o.status=1 and o.deleted_at is null ";
            ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
            OrderService.service.format(ordersPage, "title");
            setAttr("overOrderPage",ordersPage);
            render(dealingView);
        } 
    }
    /**
     * 待回复
     * */
    @Before(IndexValidator.class)
    public void waitreply() {
        
        User user=SubjectKit.getUser();
        if(ValidateKit.isNullOrEmpty(user)){
            redirect("/signin");
        }
        else if(SubjectKit.getSubject().hasRole("R_ADMIN")){
            redirect("/admin");
        }
        else{
            Page <Record> ordersPage;
            String orderby=" ORDER BY o.offer_at DESC";
            
            String where=" 1=1 and o.status=3 and o.deleted_at is null ";
            ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
            OrderService.service.format(ordersPage, "title");
            setAttr("overOrderPage",ordersPage);
            render(waitreplyView);
        } 
    }
    /**
     * 待回访
     * */
    @Before(IndexValidator.class)
    public void waitrevisit() {
        
        User user=SubjectKit.getUser();
        if(ValidateKit.isNullOrEmpty(user)){
            redirect("/signin");
        }
        else if(SubjectKit.getSubject().hasRole("R_ADMIN")){
            redirect("/admin");
        }
        else{
            Page <Record> ordersPage;
            String orderby=" ORDER BY o.offer_at DESC";
            
            String where=" 1=1 and o.status=4 and o.deleted_at is null ";
            ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
            OrderService.service.format(ordersPage, "title");
            setAttr("overOrderPage",ordersPage);
            render(waitrevisitView);
        } 
    }
    /**
     * 已回访
     * */
    @Before(IndexValidator.class)
    public void revisit() {
        
        User user=SubjectKit.getUser();
        if(ValidateKit.isNullOrEmpty(user)){
            redirect("/signin");
        }
        else if(SubjectKit.getSubject().hasRole("R_ADMIN")){
            redirect("/admin");
        }
        else{
            Page <Record> ordersPage;
            String orderby=" ORDER BY o.offer_at DESC";
            
            String where=" 1=1 and o.status=0 and o.deleted_at is null ";
            ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
            OrderService.service.format(ordersPage, "title");
            setAttr("overOrderPage",ordersPage);
            render(revisitView);
        } 
    }
    /**
     * 查询故障工单详细内容
     */
    public void query(){
        String where="o.id=?";
        Record order = Order.dao.findReportById(where,getParaToInt("id"));

        //获取工单申报者的分公司信息
        Record offer=UserInfo.dao.getUserBranch(order.getLong("oofferid"));
        
        if(!ValidateKit.isNullOrEmpty(offer)){
            setAttr("offer",offer);
        }
        //获取工单处理意见
        List<Record> commentList=Comment.dao.findCommentsByOrderId(" comments.order_id=? order by comments.add_at asc ",order.getLong("oorderid"));
        setAttr("commentList",commentList);
            
        //工单详细信息
        setAttr(order);

        render(INDEX_QUERY_PAGE);
    }

    /**
     * 验证是否
     */
    public void authed() {
        if (getAttr("isAuthed") == null) {
            setAttr("isAuthed", SubjectKit.isAuthed());
        } else {
            removeAttr("isAuthed");
            setAttr("isAuthed", SubjectKit.isAuthed());
        }

        render(indexView);
    }

    public void tosignin() {
        redirect("/signin");
    }

    public void signin() {
        if(ValidateKit.isNullOrEmpty(getCUser())){
            render("_signin.ftl");
        }else{
            redirect("/");
        }
        
    }

    /**
     * 验证码
     */
    public void captcha() {
        int width = 0, height = 0, minnum = 0, maxnum = 0, fontsize = 0;
        //int width = 0, height = 0, minnum = 0, maxnum = 0, fontsize = 0, fontmin = 0, fontmax = 0;
        if(isParaExists("timestamp")){
            System.out.println(getPara("timestamp"));
        }
        CaptchaRender captcha = new CaptchaRender();
        if (isParaExists("width")) {
            width = getParaToInt("width");
        }
        if (isParaExists("height")) {
            height = getParaToInt("height");
        }
        if (width > 0 && height > 0)
            captcha.setImgSize(width, height);
        if (isParaExists("minnum")) {
            minnum = getParaToInt("minnum");
        }
        if (isParaExists("maxnum")) {
            maxnum = getParaToInt("maxnum");
        }
        if (minnum > 0 && maxnum > 0)
            captcha.setFontNum(minnum, maxnum);
        if (isParaExists("fontsize")) {
            fontsize = getParaToInt("fontsize");
        }
        if (fontsize > 0)
            captcha.setFontSize(fontsize, fontsize);
        // 干扰线数量 默认0
        captcha.setLineNum(1);
        // 噪点数量 默认50
        captcha.setArtifactNum(20);
        // 使用字符 去掉0和o 避免难以确认
        captcha.setCode("123456789");
        // 验证码在session里的名字 默认 captcha,创建时间为：名字_time
        // captcha.setCaptchaName("captcha");
        // 验证码颜色 默认黑色
        // captcha.setDrawColor(new Color(255,0,0));
        // 背景干扰物颜色 默认灰
        // captcha.setDrawBgColor(new Color(0,0,0));
        // 背景色+透明度 前三位数字是rgb色，第四个数字是透明度 默认透明
        // captcha.setBgColor(new Color(225, 225, 0, 100));
        // 滤镜特效 默认随机特效 //曲面Curves //大理石纹Marble //弯折Double //颤动Wobble //扩散Diffuse
        captcha.setFilter(CaptchaRender.FilterFactory.Double);
        // 随机色 默认黑验证码 灰背景元素
        captcha.setRandomColor(true);
        render(captcha);
    }
    
    /**
     * 找回密码页面
     */
    public void retrieve(){
        
        render(RETRIEVE_PAGE);
    }
    
    /**
     * 找回密码操作
     */
    @Before(IndexValidator.class)
    public void doretrieve(){
        String username=getPara("username");
        String email=getPara("email");
        String context = getRequest().getHeader("Host")+getRequest().getContextPath();
        IndexService.serivce.retrieve(username, email, context);
        
        redirect("/signin");
    }
    
    @Before(IndexValidator.class)
    public void repassword(){
        String username=getPara("username");
        String newValidCode=getPara("newValidCode");
        String email=getPara("email");
        setAttr("username",username);
        setAttr("email",email);
        setAttr("newValidCode",newValidCode);
        System.out.println(newValidCode);
        System.out.println(email);
        render(REPASSWORD_PAGE);
    }
    
    @Before(IndexValidator.class)
    public void dorepassword(){
        
        String newValidCode=getPara("newValidCode");
        String username =  getPara("username");
        String email=getPara("email");
        String password=getPara("password");
        
        User user=User.dao.findFirstBy("`user`.username = ? AND `user`.email = ? AND `user`.deleted_at is null", username,email);
        if(!ValidateKit.isNullOrEmpty(user)){
            HasherInfo passwordInfo=HasherKit.hash(password,Hasher.DEFAULT);
            user.set("password", passwordInfo.getHashResult());
            user.set("hasher", passwordInfo.getHasher().value());
            user.set("salt", passwordInfo.getSalt());
            if(user.update()){
                redirect("/signin");
            }
        }
        redirect("/signin");
    }
    
    public void help(){
        try{
            File downfile=new File(PathKit.getWebRootPath() +"/res/doc/网络故障申报系统操作指引.doc");
            if(downfile.exists()){
                renderFile(downfile);
                return ;
            }else{
                renderError(404);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

}
