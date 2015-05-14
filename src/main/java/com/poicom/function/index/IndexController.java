package com.poicom.function.index;

import java.io.File;

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
import com.poicom.common.kit.AlertKit;
import com.poicom.common.kit.DateKit;
import com.poicom.common.thread.ThreadAlert;
import com.poicom.function.app.model.Order;
import com.poicom.function.system.model.User;

/**
 * 
 * @author 唐东宇
 *
 */
@ControllerKey(value = "/", path = "/app/index")
public class IndexController extends Controller {

	private final static String indexView = "index.html";
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
			if(ValidateKit.isNullOrEmpty(getPara("condition"))){
				String where=" 1=1 and o.deleted_at is null ";
				ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
				
			}else{
				String condition ="%"+getPara("condition").trim()+"%";
				String where=" 1=1 and o.deleted_at is null and o.description like ?";
				ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby, condition);
			}
			
			Order.dao.format(ordersPage,"title");
			setAttr("overOrderPage",ordersPage);
			render(indexView);
		}
		
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
		render("_signin.ftl");
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
		User user=User.dao.findFirstBy("`user`.username = ? AND `user`.deleted_at is null", username);
		AlertKit alertKit=new AlertKit();
		String body=AlertKit.getMailBody(user,getRequest().getHeader("Host")+getRequest().getContextPath()).toString();
		if(ValidateKit.isEmail(email)){
			alertKit.setEmailTitle("找回密码！【一点通】").setEmailBody(body).setEmailAdd(email);
		}
		if(!ValidateKit.isNullOrEmpty(user.getStr("phone"))){
			if(ValidateKit.isPhone(user.getStr("phone"))){
				//短信内容
				String smsBody="找回密码，重置密码的链接（24小时内有效）已经发到您邮箱，让及时登录处理";
				alertKit.setSmsContext(smsBody).setSmsPhone(user.getStr("phone"));
			}
		}
		//加入进程
		logger.info("日志添加到入库队列 ---> 找回密码");
		ThreadAlert.add(alertKit);
		redirect("/signin");
	}
	
	@Before(IndexValidator.class)
	public void repassword(){
		String newValidCode=getPara("newValidCode");
		String email=getPara("email");
		setAttr("email",email);
		setAttr("newValidCode",newValidCode);
		System.out.println(newValidCode);
		System.out.println(email);
		render(REPASSWORD_PAGE);
	}
	
	@Before(IndexValidator.class)
	public void dorepassword(){
		
		String newValidCode=getPara("newValidCode");
		String email=getPara("email");
		String password=getPara("password");
		
		User user=User.dao.findFirstBy("`user`.email = ? AND `user`.deleted_at is null", email);
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
				renderError(404);;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
