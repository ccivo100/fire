package com.poicom.function.user;

import java.sql.Timestamp;
import java.util.Date;

import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.HasherInfo;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.user.model.User;

@ControllerKey(value="/admin",path="/page/app/admin")
public class AdminController extends Controller {
	
	public void index(){
		setAttr("userPage", User.dao.getUserPage(getParaToInt(0, 1), 10));
		render("user.html");
	}
	
	public void role(){
		
	}
	
	public void user(){
		
	}
	
	public void order(){
		
	}
	
	
	public void add(){}
	
	/**
	 * @description 添加新用户
	 * 				添加sec_user基础数据
	 * 				添加sec_user_role用户角色
	 * 				添加sec_user_info用户拓展数据
	 */
	@Before(Tx.class)
	public void control(){
		
		keepModel(User.class);
		User user=getModel(User.class);
		User subUser=SubjectKit.getUser();
		boolean result=false;
		String todo=getPara("do");
		System.out.println(todo);
		if(todo!=null){
			if(todo.equals("delete")&&user.getLong("id")!=null){
				result = user.set("deleted_at", new Timestamp(new Date().getTime())).update();
			}else if(todo.equals("save")&&user.getLong("id")!=null){
				HasherInfo hasher = HasherKit.hash(user.getStr("password"));
				if(user.getStr("first_name")==null)
					user.set("first_name", "三角").set("last_name", "中心");
				
				user.set("password", hasher.getHashResult())
				.set("salt", hasher.getSalt())
				.set("hasher", hasher.getHasher().value())
				.set("providername", subUser.getStr("full_name"))
				.set("full_name", user.getStr("last_name") + "." + user.getStr("first_name"));
				result=user.save();
			}else if(todo.equals("update")&&user.getLong("id")!=null){
				
			}
		}
		
		
		/*if(todo!=null){
			if(todo.equals("delete")&&(user.getLong("id")!=null)){
				user.set("deleted_at", new Timestamp(new Date().getTime())).update();
			}else if(todo.equals("update")&&user.getLong("id")!=null){
				user.removeNullValueAttrs();
				Boolean reuse = getParaToBoolean("reuse");
				if(reuse!=null&&reuse)
					user.set("deleted_at", null);
				if(user.getBoolean("last_name")!=null)
					user.set("full_name", user.get("last_name")+"."+user.get("first_name"));
				result=user.update();
			}
		}*/
		
		if (result) {
			setAttr("state", "success");
			redirect("/admin");
		} else{
			setAttr("state", "failure");
			redirect("/admin");
		}
	}
	
	public void edit(){
		User user=User.dao.findById(getParaToInt());
		HasherInfo passwordInfo =HasherKit.hash(user.getStr("password"));
		setAttr("user", User.dao.findById(getParaToInt()));
	}
	
	public void update(){
		
	}
	
	public void query(){
		
	}
	

}
