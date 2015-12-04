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
	private final static String INDEX_QUERY_PAGE="query.html";
	private final static String RETRIEVE_PAGE="retrieve.html";
	private final static String REPASSWORD_PAGE="repassword.html";
	protected Logger logger = LoggerFactory.getLogger(IndexController.class);

	/**
	 * 主页根目录
	 */
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
	
    /*public void indexs(){
    User user=User.dao.getCurrentUser();;
     Page <Record> ordersPage;;//返回查询结果，并覆盖原来的申报列表
    
    //拼接where语句
    StringBuffer whereadd=new StringBuffer();
    //保存condition条件
    List<Object> conditions=new ArrayList<Object>();
    Integer status=getParaToInt("status");
    //若无查询条件 则按正常查询。
    if(getParaToInt("status")==-1){
        
        String where=" WHERE o.offer_user=? ";
        String orderby=" ORDER BY o.deleted_at ASC,o.status DESC, o.offer_at DESC ";
        //reportPage=Order.dao.findReportsByUserId(getParaToInt(0,1), 10,where,orderby, user.get("id"));
        ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10,where,orderby, user.get("id"));
    }
    else{
        
        conditions.add(user.get("id"));//这句待定

        if(getParaToInt("status")!=-1){
            whereadd.append(" and u1.full_name like ? ");
            conditions.add("%"+getPara("offeruser").trim()+"%");
            setAttr("status",status);
        }

        String where=" WHERE o.offer_user=? "+whereadd.toString();
        String orderby=" ORDER BY o.deleted_at ASC,o.status DESC, o.offer_at DESC ";
        Object[] condition= new Object[conditions.size()];
        conditions.toArray(condition);
        ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10,where,orderby,condition);
    }
    OrderService.service.format(ordersPage, "title");
    setAttr("overOrderPage",ordersPage);
    
    render("index.html");
}*/
	
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
