package com.poicom.function.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.ValidateKit;
import cn.dreampie.routebind.ControllerKey;
import cn.dreampie.shiro.core.SubjectKit;
import cn.dreampie.shiro.hasher.Hasher;
import cn.dreampie.shiro.hasher.HasherInfo;
import cn.dreampie.shiro.hasher.HasherKit;

import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.jfinal.upload.UploadFile;
import com.poicom.basic.kit.ValiKit;
import com.poicom.basic.kit.WebKit;
import com.poicom.function.model.Apartment;
import com.poicom.function.model.ApartmentType;
import com.poicom.function.model.Branch;
import com.poicom.function.model.Comment;
import com.poicom.function.model.Etype;
import com.poicom.function.model.Order;
import com.poicom.function.model.Permission;
import com.poicom.function.model.Position;
import com.poicom.function.model.Role;
import com.poicom.function.model.RolePermission;
import com.poicom.function.model.User;
import com.poicom.function.model.UserInfo;
import com.poicom.function.model.UserOrder;
import com.poicom.function.model.UserRole;
import com.poicom.function.service.UserService;
import com.poicom.function.validator.AdminValidator;

/**
 * 
 * @author 唐东宇
 *
 */
@ControllerKey(value="/admin",path="/app/admin")
public class AdminController extends BaseController {
	
	protected Logger logger=LoggerFactory.getLogger(AdminController.class);
	
	private final static String INDEX_PAGE="index/index.html";
	
	
	private final static String USER_ADD_PAGE="user/add.html";
	private final static String USER_EDIT_PAGE="user/edit.html";
	
	
	
	
	private final static String CENTER_INFO_PAGE="center/center.html";
	private final static String CENTER_PWD_PAGE="center/pwd.html";
	
	private final static String ORDER_QUERY_PAGE="order/query.html";
	
	public void index(){
		Page <Record> ordersPage;
		String orderby=" ORDER BY o.offer_at DESC";
		String where=" 1=1 and o.deleted_at is null ";
		ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
		Order.dao.format(ordersPage,"title");
		setAttr("overOrderPage",ordersPage);
		
		render(INDEX_PAGE);
	}
	
	
	public void ajaxgetper(){
		System.out.println(getPara("roleid"));
		String where = "deleted_at is null and pid=?";
		List<Permission> permissions=Permission.dao.findPermissionsByPid(where,0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(where,permission.get("id"));
			permission.setChildren(pchild);
		}
		renderJson("permissions",permissions);
	}
	
	
	public void loadPermission(){
		int id=getParaToInt("rid");
		List<Permission> permissionList=Permission.dao.findByRole("",id);
		
		renderJson("permissionList",permissionList);
		//render("/page/app/admin/role/loadper.html");
	}
	
	
	
	/**
	 * 用户管理
	 */
	public void user(){
		String w = "";
		if(SubjectKit.getSubject().hasRole("R_ADMIN")){
			
		}else{
			User cUser=SubjectKit.getUser();
			long branchid=UserInfo.dao.findFirstBy("user_id=?",cUser.get("id")).getLong("branch_id");
			w=" and branch.id="+branchid;
		}
		
		Page<User> userPage;
		String orderby="  ORDER BY user.id ";
		if(ValidateKit.isNullOrEmpty(getPara("username"))){
			String where=" 1=1 "+w;
			userPage=User.dao.getAllUserPage(getParaToInt(0,1), 10,where+orderby);
		}else{
			String condition ="%"+getPara("username").trim()+"%";
			String where=" user.full_name like ?  "+w;
			userPage=User.dao.getAllUserPage(getParaToInt(0,1), 10,where+orderby,condition);
			setAttr("username",getPara("username").trim());
		}
		
		for(int i=0;i<userPage.getList().size();i++){
			List<Record> list=UserRole.dao.findUserRolesById(userPage.getList().get(i).get("uuserid"));
			StringBuffer roles=new StringBuffer();
			for(int j=0;j<list.size();j++){
				roles.append(list.get(j).get("rname")+"；");
			}
			userPage.getList().get(i).set("remark", roles);
			
		}
		setAttr("userPage",userPage);
		//UserService.service.list(splitPage);
	}
	
	public void adduser(){
		
		if(SubjectKit.getSubject().hasRole("R_ADMIN")){
			
		}else{
			User subUser=SubjectKit.getUser();
			long branchid=UserInfo.dao.findFirstBy("user_id=?",subUser.get("id")).getLong("branch_id");
			setAttr("branchid",branchid);
		}
		
		setAttr("branchList",Branch.dao.getAllBranch());
		setAttr("apartmentList",Apartment.dao.getAllApartment());
		setAttr("positionList",Position.dao.getAllPosition());
		render(USER_ADD_PAGE);
	}
	
	/**
	 * @描述 部门选择 一级、二级。
	 */
	public void handler(){
		String type=getPara("type");
		
		if(type.equals("afacher")){
			List<Apartment> afacherList=Apartment.dao.rootNode(" deleted_at is null and pid=? ",0);
			renderJson("afacherList", afacherList);
		}else if(type.equals("achilren")){
			List<Apartment> achilrenList=Apartment.dao.rootNode(" deleted_at is null and pid=?",getParaToInt("typeid"));
			renderJson("achilrenList", achilrenList);
		}
	}
	
	/**
	 * 新增用户
	 */
	@Before(AdminValidator.class)
	public void doadduser(){
		
		if(ValidateKit.isNullOrEmpty(User.dao.findFirstBy("username=?", getPara("user.username")))){
			User subUser=SubjectKit.getUser();
			long branchid;
			
			if(SubjectKit.getSubject().hasRole("R_ADMIN")){
				branchid=getParaToInt("selectBranch");
			}else{
				branchid=UserInfo.dao.findFirstBy("user_id=?",subUser.get("id")).getLong("branch_id");
			}
			
			
			User user=getModel(User.class);
			user.set("username", WebKit.delHTMLTag(user.getStr("username")));
			
			boolean result=false;
			HasherInfo hasher = HasherKit.hash("123456");
			user.set("password", hasher.getHashResult())
			.set("salt", hasher.getSalt())
			.set("hasher", hasher.getHasher().value())
			.set("providername", subUser.getStr("full_name"))
			.set("full_name", WebKit.delHTMLTag(user.getStr("first_name"))+WebKit.delHTMLTag(user.getStr("last_name")));
			if(getParaToInt("selectGender")==1){
				user.set("avatar_url", "/admin/assets/avatars/avatar3.png");
			}else{
				user.set("avatar_url", "/admin/assets/avatars/avatar4.png");
			}
			result=user.save();
			if(result){
				System.out.println(user.get("id")+"："+user.get("username"));
				UserInfo userinfo=new UserInfo();
				userinfo.set("user_id", user.get("id"))
				.set("creator_id", subUser.get("id"))
				.set("gender", getParaToInt("selectGender"))
				.set("branch_id", branchid)
				.set("apartment_id", getParaToInt("selectApartment"))
				.set("position_id", getParaToInt("selectPosition"));
				userinfo.save();
			}
		}
		redirect("/admin/user");
	}
	
	@Before(Tx.class)
	public void doadduser1(){
		String username=getPara("username");
		String first_name=getPara("first_name");
		String last_name=getPara("last_name");
		long selectGender=getParaToLong("selectGender");
		String phone=getPara("phone");
		String email=getPara("email");
		long selectBranch=getParaToLong("selectBranch");
		long selectApartment=getParaToLong("selectApartment");
		long selectPosition=getParaToLong("selectPosition");
		
		if(!ValidateKit.isGeneral(username.trim(), 2, 16)){
			renderJson("state","用户名为英文字母 、数字和下划线，2~16个字符！");
		}else if(!ValidateKit.isNullOrEmpty(User.dao.findFirstBy("username=?", username.trim()))){
			renderJson("state","该用户名已存在！");
		}else if(ValidateKit.isNullOrEmpty(first_name.trim())||ValidateKit.isNullOrEmpty(last_name.trim())){
			renderJson("state","姓名不能为空！");
		}else if(ValidateKit.isNullOrEmpty(selectGender)){
			renderJson("state","性别不能为空！");
		}else if(!ValiKit.isPhone(phone)){
			renderJson("state","联系方式格式不正确！");
		}else if(!ValidateKit.isEmail(email)){
			renderJson("state","邮箱格式不正确！");
		}else if(selectBranch==-1){
			if(SubjectKit.getSubject().hasRole("R_ADMIN")){
				renderJson("state","请选择单位名称！");
			}else{
			}
		}else if(selectApartment==-1){
			renderJson("state","请选择部门名称！");
		}else if(selectPosition==-1){
			renderJson("state","请选择职位名称！");
		}else {
			User subUser=SubjectKit.getUser();
			long branchid;
			
			if(SubjectKit.getSubject().hasRole("R_ADMIN")){
				branchid=getParaToInt("selectBranch");
			}else{
				branchid=UserInfo.dao.findFirstBy("user_id=?",subUser.get("id")).getLong("branch_id");
			}
			
			User user=new User();
			user
				.set("username", WebKit.delHTMLTag(username))
				.set("first_name", first_name.trim())
				.set("last_name", last_name.trim())
				.set("phone", phone.trim())
				.set("email", email.trim());
			
			boolean result=false;
			HasherInfo hasher = HasherKit.hash("123456");
			user.set("password", hasher.getHashResult())
			.set("salt", hasher.getSalt())
			.set("hasher", hasher.getHasher().value())
			.set("providername", subUser.getStr("full_name"))
			.set("full_name", WebKit.delHTMLTag(first_name.trim())+WebKit.delHTMLTag(last_name.trim()));
			if(getParaToInt("selectGender")==1){
				user.set("avatar_url", "/admin/assets/avatars/avatar3.png");
			}else{
				user.set("avatar_url", "/admin/assets/avatars/avatar4.png");
			}
			result=user.save();
			if(result){
				System.out.println(user.get("id")+"："+user.get("username"));
				UserInfo userinfo=new UserInfo();
				userinfo.set("user_id", user.get("id"))
				.set("creator_id", subUser.get("id"))
				.set("gender", getParaToInt("selectGender"))
				.set("branch_id", branchid)
				.set("apartment_id", getParaToInt("selectApartment"))
				.set("position_id", getParaToInt("selectPosition"));
				userinfo.save();
				renderJson("state","提交成功！");
			}else {
				renderJson("state","提交失败！");
			}
			
		}
		
	}
	
	public void edituser(){
		String userid=getPara("id");
		Record userinfo=UserInfo.dao.getAllUserInfo(userid);
		Apartment papartment=Apartment.dao.findById(Apartment.dao.findById(userinfo.get("info_apartmentid")).get("pid"));
		setAttr("userinfo",userinfo);
		setAttr("userrole",UserRole.dao.findUserRolesById(userid));
		if(SubjectKit.getSubject().hasRole("R_ADMIN")){
			setAttr("roleList",Role.dao.findRolesExceptId(1));
		}else if(SubjectKit.getSubject().hasRole("R_MANAGER")){
			setAttr("roleList",Role.dao.findRolesExceptId(1,2));
		}
		setAttr("branchList",Branch.dao.getAllBranch());
		setAttr("apartmentList",Apartment.dao.getAllApartment());
		setAttr("papartment",papartment);
		setAttr("positionList",Position.dao.getAllPosition());
		
		render(USER_EDIT_PAGE);
	}
	/**
	 * @描述 执行分配角色操作，更改单位操作
	 */
	@Before(Tx.class)
	public void doedituser(){
		
		long userid=getParaToLong("userid");
		String uphone=getPara("uphone");
		String uemail=getPara("uemail");
		long selectBranch=getParaToLong("selectBranch");
		long selectApartment=getParaToLong("selectApartment");
		long selectPosition=getParaToLong("selectPosition");
		
		if(!ValiKit.isPhone(uphone)){
			renderJson("state","电话格式不正确！");
		}else if(!ValidateKit.isEmail(uemail)){
			renderJson("state","邮箱格式不正确！");
		}else if(selectBranch==-1){
			renderJson("state","请选择单位名称！");
		}else if(selectApartment==-1){
			renderJson("state","请选择部门名称！");
		}else if(selectPosition==-1){
			renderJson("state","请选择职位名称！");
		}else{
			//前台选中的 角色s
			String[] roles=getParaValues("roles[]");
			
			User user=User.dao.findById(userid);
			user.set("phone", uphone).set("email", uemail).update();
			
			//用户-角色 中间表
			List<Record> sur=UserRole.dao.findUserRolesById(getParaToLong("userid"));
			
			//1、用户有角色，取消所有角色
			if(ValidateKit.isNullOrEmpty(roles)){
				for(int i=0;i<sur.size();i++){
					logger.info(sur.get(i).getLong("roleid")+" 已取消，执行删除操作！");
					UserRole.dao.findById(sur.get(i).getLong("id")).delete();
				}
			}
			//2、用户无角色，新增角色
			else if(ValidateKit.isNullOrEmpty(sur)){
				for(int i=0;i<roles.length;i++){
					logger.info(roles[i]+" 不存在，执行新增操作！");
					new UserRole().set("user_id", getPara("userid")).set("role_id", roles[i]).save();
				}
			}
			//3、用户有角色
			else{
				//需要分配该角色?，不存在则新增。
				for(int i=0;i<roles.length;i++){
					boolean flag=false;
					for(int j=0;j<sur.size();j++){
						if(Integer.parseInt(roles[i])==sur.get(j).getLong("roleid")){
							flag=true;
							break;
						}
					}
					if(flag){
						logger.info(roles[i]+" 存在，保留不删除！");
					}else if(!flag){
						logger.info(roles[i]+" 不存在，执行新增操作！");
						new UserRole().set("user_id", getPara("userid")).set("role_id", roles[i]).save();
					}
				}
				
				//需要保留该角色?，不保留则删除
				for(int i=0;i<sur.size();i++){
					boolean flag=false;
					for(int j=0;j<roles.length;j++){
						if(sur.get(i).getLong("roleid")==Integer.parseInt(roles[j])){
							flag=true;
							break;
						}
					}
					if(flag){
						logger.info(sur.get(i).getLong("roleid")+" 未取消，保留不删除！");
					}else if(!flag){
						logger.info(sur.get(i).getLong("roleid")+" 已取消，执行删除操作！");
						UserRole.dao.findById(sur.get(i).getLong("id")).delete();
					}
				}
			}
			
			logger.info(getPara("selectBranch"));
			UserInfo userinfo=UserInfo.dao.get("user_id", getParaToLong("userid"));
			if(userinfo.get("branch_id")!=getPara("selectBranch")){
				userinfo.set("branch_id", getPara("selectBranch"));
			}
			if(userinfo.get("apartment_id")!=getPara("selectApartment")){
				userinfo.set("apartment_id", getPara("selectApartment"));
			}
			if(userinfo.get("position_id")!=getPara("selectPosition")){
				userinfo.set("position_id", getPara("selectPosition"));
			}
			if(userinfo.update()){
				renderJson("state","提交成功！");
			}else{
				renderJson("state","提交失败！");
			}
		}
		
	}
	
	public void migrateuser(){
		//List<Branch> branchList=Branch.dao.getAllBranch();
		
		render("/admin/migrate.html");
	}
	
	public void onuser(){
		User.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/user");
	}
	public void offuser(){
		User.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/user");
	}
	
	
	
	/**
	 * 异常工单管理
	 */
	public void exception(){
		String where=" o.status=2 OR o.spend_time IS NOT NULL";
		Page<Record> orderPage=Order.dao.findExceptionOrders(getParaToInt(0,1), 10,where);
		setAttr("orderPage",orderPage);
	}
	
	
	
	/**
	 * 进入修改用户密码页面
	 */
	public void center(){
		User user=SubjectKit.getUser();
		if(!ValidateKit.isNullOrEmpty(user)){
			setAttr("userInfo",UserInfo.dao.getAllUserInfo(user.get("id")));
			setSessionAttr("current_user", user);
		}
		render(CENTER_INFO_PAGE);
		
	}
	
	/**
	 * 
	 */
	public void pwdPage(){
		User user=SubjectKit.getUser();
		if(!ValidateKit.isNullOrEmpty(user)){
			setAttr("user", user);
		}
		render(CENTER_PWD_PAGE);
	}
	
	/**
	 * 修改用户密码
	 */
	@Before({AdminValidator.class,Tx.class})
	public void updatePwd(){
		keepModel(User.class);
		User upUser=getModel(User.class);
		User user=SubjectKit.getUser();
		upUser.set("id", user.get("id"));
		HasherInfo passwordInfo=HasherKit.hash(upUser.getStr("password"),Hasher.DEFAULT);
		upUser.set("password", passwordInfo.getHashResult());
		upUser.set("hasher", passwordInfo.getHasher().value());
		upUser.set("salt", passwordInfo.getSalt());
		
		if (upUser.update()) {
			SubjectKit.getSubject().logout();
			setAttr("state", "success");
			redirect("/tosignin");
			return;
		}else {
			setAttr("state", "falure");
			render("/page/app/admin/center.html");
		}
		
	}
	
	
	public void order(){
		List<Record> ordersList;
		String where=" 1=1 ";
		String orderby=" ORDER BY o.offer_at DESC";
		ordersList=Order.dao.findAdminOrders(where, orderby);
		setAttr("ordersList",ordersList);
	}
	
	public void jqorder(){
		List<Record> ordersList;
		String where=" 1=1 ";
		String orderby=" ORDER BY o.offer_at DESC";
		ordersList=Order.dao.findAdminOrders(where, orderby);
		setAttr("ordersList",ordersList);
	}
	
	public void loadOrder(){
		List<Record> ordersList;
		String where=" 1=1 ";
		String orderby=" ORDER BY o.offer_at DESC";
		ordersList=Order.dao.findAdminOrders(where, orderby);
		
		renderJson(ordersList);
	}
	
	/**
	 * AJAX上传图片
	 */
	public void ajaxUpload(){
		
		UploadFile uploadFile=getFile("img");
		//System.out.println(uploadFile);
		
		renderJson("imgurl","/upload/"+uploadFile.getFileName());
	}
	
	@Before(Tx.class)
	public void deleteOrder(){
		long orderid=getParaToLong("orderid");
		Order order=Order.dao.findById(orderid);
		
		List<UserOrder> userOrderList=UserOrder.dao.findBy(" order_id=?", orderid);
		List<Comment> commentList=Comment.dao.findBy(" order_id=?", orderid);
		
		if(order.delete()){
			for(UserOrder userorder:userOrderList){
				userorder.delete();
			}
			for(Comment comment:commentList){
				comment.delete();
			}
			renderJson("state","删除成功！");
		}else{
			renderJson("state","删除失败！");
		}
		
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
					user.set("first_name", "点通").set("last_name", "科技");
				
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
		//User user=User.dao.findById(getParaToInt());
		//HasherInfo passwordInfo =HasherKit.hash(user.getStr("password"));
		setAttr("user", User.dao.findById(getParaToInt()));
	}
	
	public void update(){
		
	}
	
	public void query(){
		
	}
	

}
