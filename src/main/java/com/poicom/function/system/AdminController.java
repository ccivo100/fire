package com.poicom.function.system;

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
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.poicom.common.kit.WebKit;
import com.poicom.function.app.model.Apartment;
import com.poicom.function.app.model.ApartmentType;
import com.poicom.function.app.model.Branch;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Order;
import com.poicom.function.app.model.Position;
import com.poicom.function.system.model.Permission;
import com.poicom.function.system.model.Role;
import com.poicom.function.system.model.RolePermission;
import com.poicom.function.system.model.User;
import com.poicom.function.system.model.UserInfo;
import com.poicom.function.system.model.UserRole;

@ControllerKey(value="/admin",path="/app/admin")
public class AdminController extends Controller {
	
	protected Logger logger=LoggerFactory.getLogger(AdminController.class);
	
	private final static String INDEX_PAGE="index/index.html";
	
	private final static String ROLE_ADD_PAGE="role/add.html";
	private final static String ROLE_EDIT_PAGE="role/edit.html";
	private final static String ROLE_ASSIGN_PAGE="role/assign.ftl";
	private final static String PERMISSION_ADD_PAGE="permission/add.html";
	private final static String PERMISSION_EDIT_PAGE="permission/edit.html";
	private final static String USER_ADD_PAGE="user/add.html";
	private final static String USER_EDIT_PAGE="user/edit.html";
	private final static String BRANCH_ADD_PAGE="branch/add.html";
	private final static String BRANCH_EDIT_PAGE="branch/edit.html";
	private final static String APARTMENT_ADD_PAGE="apartment/add.html";
	private final static String APARTMENT_EDIT_PAGE="apartment/edit.html";
	
	private final static String POSITION_ADD_PAGE="position/add.html";
	private final static String POSITION_EDIT_PAGE="position/edit.html";
	private final static String TYPE_ADD_PAGE="type/add.html";
	private final static String TYPE_EDIT_PAGE="type/edit.html";
	private final static String TYPE_TASK_PAGE="type/task.html";
	
	private final static String CENTER_INFO_PAGE="center/center.html";
	private final static String CENTER_PWD_PAGE="center/pwd.html";
	
	public void index(){
		Page <Record> ordersPage;
		String orderby=" ORDER BY o.offer_at DESC";
		String where=" 1=1 and o.deleted_at is null ";
		ordersPage=Order.dao.findIndexOrders(getParaToInt(0,1), 10, where,orderby);
		Order.dao.format(ordersPage,"title");
		setAttr("overOrderPage",ordersPage);
		
		render(INDEX_PAGE);
	}
	
	/**
	 * 角色管理
	 */
	@Before(CacheInterceptor.class)
	@CacheName("/admin/role")
	public void role(){
		List<Role> roles=Role.dao.findRolesByPid(0);
		for(Role role:roles){
			List<Role> rchild=Role.dao.findRolesByPid(role.get("id"));
			role.setChildren(rchild);
		}
		
		setAttr("roleTree",roles);
		render("role.html");
	}
	
	/**
	 * 新增角色
	 */
	public void addrole(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render(ROLE_ADD_PAGE);
	}
	/**
	 * 新增角色操作
	 */
	@Before({AdminValidator.class,EvictInterceptor.class})
	@CacheName("/admin/role")
	public void doadd(){
		Role role=getModel(Role.class);
		role.set("name", WebKit.delHTMLTag(role.getStr("name"))).set("value", WebKit.delHTMLTag(role.getStr("value")));
		role.save();
		redirect("/admin/role");
	}
	
	/**
	 * 更新角色
	 */
	public void editrole(){
		Role role=Role.dao.findById(getPara("id"));
		setAttr("role",role);
		render(ROLE_EDIT_PAGE);
	}
	/**
	 * 更新角色操作
	 */
	@Before({AdminValidator.class,EvictInterceptor.class})
	@CacheName("/admin/role")
	public void doedit(){
		Role role=getModel(Role.class);
		role.set("name", WebKit.delHTMLTag(role.getStr("name"))).set("value", WebKit.delHTMLTag(role.getStr("value")));
		role.update();
		redirect("/admin/role");
	}
	
	/**
	 * 角色分配权限
	 */
	public void assignrole(){
		
		Role role=Role.dao.findById(getPara("id"));
		List<String> userpermission=RolePermission.dao.findPermissionIds("role_id=?",role.get("id"));
		List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(permission.get("id"));
			permission.setChildren(pchild);
		}
		
		setAttr("role",role);
		setAttr("userpermission",userpermission);
		setAttr("permissions",permissions);
		render(ROLE_ASSIGN_PAGE);
	}
	
	@Before(Tx.class)
	public void doassignrole(){
		//页面传值
		String[] permissions=getParaValues("pers");
		Long roleid=getParaToLong("roleid");
		//当前角色已分配权限List
		List<RolePermission> rps=RolePermission.dao.findRolePermissionByRoleId(roleid);
		
		if(ValidateKit.isNullOrEmpty(permissions)){
			for(int i=0;i<rps.size();i++){
				logger.error(rps.get(i).getLong("permission_id")+" 已取消，执行删除操作！");
				rps.get(i).delete();
			}
		}else {
			Map<Long,Permission> perMap=new HashMap<Long,Permission>();
			for(int i=0;i<permissions.length;i++){
				//根据页面选择的id，查询该Permission的父id，根据父id查询到父Permission。
				Permission pf=Permission.dao.findById(Permission.dao.findById(permissions[i]).get("pid"));
				if(perMap.get(pf.get("id"))==null){
					perMap.put(pf.getLong("id"), pf);
				}
				new RolePermission().set("role_id", roleid).set("permission_id", permissions[i]).save();
			}
			for(RolePermission rp:rps){
				rp.delete();
			}
			for(Permission p:perMap.values()){
				new RolePermission().set("role_id", roleid).set("permission_id", p.get("id")).save();
			}
		}
		redirect("/admin/role");
	}
	
	@Before({EvictInterceptor.class})
	@CacheName("/admin/role")
	public void offrole(){
		Role.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/role");
	}
	@Before({EvictInterceptor.class})
	@CacheName("/admin/role")
	public void onrole(){
		Role.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/role");
	}
	
	public void ajaxgetper(){
		System.out.println(getPara("roleid"));
		List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(permission.get("id"));
			permission.setChildren(pchild);
		}
		renderJson("permissions",permissions);
	}
	
	
	/**
	 * 权限管理
	 */
	@Before(CacheInterceptor.class)
	@CacheName("/admin/permission")
	public void permission(){
		List<Permission> permissions=Permission.dao.findPermissionsByPid(0);
		for(Permission permission:permissions){
			List<Permission> pchild=Permission.dao.findPermissionsByPid(permission.get("id"));
			permission.setChildren(pchild);
		}
		setAttr("permissionTree",permissions);
		render("permission.html");
	}
	/**
	 * 新增权限
	 */
	public void addpermission(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render(PERMISSION_ADD_PAGE);
	}
	/**
	 * 新增权限操作
	 */
	@Before({AdminValidator.class,EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void doaddpermission(){
		Permission permission=getModel(Permission.class);
		permission.set("name", WebKit.delHTMLTag(permission.getStr("name"))).set("value", WebKit.delHTMLTag(permission.getStr("value"))).set("url", WebKit.delHTMLTag(permission.getStr("url")));
		permission.save();
		redirect("/admin/permission");
	}
	
	/**
	 * 更新权限
	 */
	public void editpermission(){
		Permission permission=Permission.dao.findById(getPara("id"));
		setAttr("permission",permission);
		render(PERMISSION_EDIT_PAGE);
	}
	/**
	 * 更新权限操作
	 */
	@Before({AdminValidator.class,EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void doeditpermission(){
		Permission permission=getModel(Permission.class);
		permission.set("name", WebKit.delHTMLTag(permission.getStr("name"))).set("value", WebKit.delHTMLTag(permission.getStr("value"))).set("url", WebKit.delHTMLTag(permission.getStr("url")));
		permission.update();
		redirect("/admin/permission");
	}
	
	@Before({EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void offpermission(){
		Permission.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/permission");
	}
	@Before({EvictInterceptor.class})
	@CacheName("/admin/permission")
	public void onpermission(){
		Permission.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/permission");
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
		
		Page<User> userPage;
		String orderby="  ORDER BY user.id ";
		if(ValidateKit.isNullOrEmpty(getPara("username"))){
			String where=" 1=1 ";
			userPage=User.dao.getAllUserPage(getParaToInt(0,1), 10,where+orderby);
		}else{
			String condition ="%"+getPara("username").trim()+"%";
			String where=" user.full_name like ?  ";
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
	}
	
	public void adduser(){
		
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
			List<Apartment> afacherList=Apartment.dao.findApartmentsByPid(" deleted_at is null and pid=? ",0);
			renderJson("afacherList", afacherList);
		}else if(type.equals("achilren")){
			List<Apartment> achilrenList=Apartment.dao.findApartmentsByPid(" deleted_at is null and pid=?",getParaToInt("typeid"));
			renderJson("achilrenList", achilrenList);
		}
	}
	
	/**
	 * 新增用户
	 */
	@Before(AdminValidator.class)
	public void doadduser(){
		User user=getModel(User.class);
		user.set("username", WebKit.delHTMLTag(user.getStr("username")));
		User subUser=SubjectKit.getUser();
		boolean result=false;
		HasherInfo hasher = HasherKit.hash("123456");
		user.set("password", hasher.getHashResult())
		.set("salt", hasher.getSalt())
		.set("hasher", hasher.getHasher().value())
		.set("providername", subUser.getStr("full_name"))
		.set("full_name", WebKit.delHTMLTag(user.getStr("first_name"))+WebKit.delHTMLTag(user.getStr("last_name")));
		result=user.save();
		if(result){
			System.out.println(user.get("id")+"："+user.get("username"));
			UserInfo userinfo=new UserInfo();
			userinfo.set("user_id", user.get("id"))
			.set("creator_id", subUser.get("id"))
			.set("gender", getParaToInt("selectGender"))
			.set("branch_id", getParaToInt("selectApartment"))
			.set("apartment_id", getParaToInt("selectApartment"))
			.set("position_id", getParaToInt("selectPosition"));
			userinfo.save();
		}
		
		redirect("/admin/user");
	}
	
	public void edituser(){
		String userid=getPara("id");
		Record userinfo=UserInfo.dao.getAllUserInfo(userid);
		Apartment papartment=Apartment.dao.findById(Apartment.dao.findById(userinfo.get("info_apartmentid")).get("pid"));
		setAttr("userinfo",userinfo);
		setAttr("userrole",UserRole.dao.findUserRolesById(userid));
		setAttr("roleList",Role.dao.findAll());
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
		
		if(!ValidateKit.isPhone(uphone)){
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
	 * 单位管理
	 */
	public void branch(){
		Page<Branch> branchPage;
		String orderby=" ORDER BY branch.id ";
		if(ValidateKit.isNullOrEmpty(getPara("branch"))){
			String where = " 1=1 ";
			branchPage=Branch.dao.findBranchPage(getParaToInt(0,1), 10, where, orderby);
		}else{
			String where=" branch.name like ?  ";
			String condition ="%"+getPara("branch").trim()+"%";
			branchPage=Branch.dao.findBranchPage(getParaToInt(0,1), 10, where, orderby, condition);
			setAttr("branch",getPara("branch").trim());
		}
		setAttr("branchPage",branchPage);
	}
	
	public void addbranch(){
		render(BRANCH_ADD_PAGE);
	}
	@Before(AdminValidator.class)
	public void doaddbranch(){
		Branch branch=getModel(Branch.class);
		branch.set("name", WebKit.delHTMLTag(branch.getStr("name")));
		branch.save();
		redirect("/admin/branch");
	}
	
	public void editbranch(){
		Branch branch=Branch.dao.findById(getPara("id"));
		setAttr("branch",branch);
		render(BRANCH_EDIT_PAGE);
	}
	@Before(AdminValidator.class)
	public void doeditbranch(){
		Branch branch=getModel(Branch.class);
		branch.set("name", WebKit.delHTMLTag(branch.getStr("name")));
		branch.update();
		redirect("/admin/branch");
	}
	
	public void onbranch(){
		Branch.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/branch");
	}
	public void offbranch(){
		Branch.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/branch");
	}
	
	/**
	 * 部门管理
	 */
	public void apartment(){
		
		List<Apartment> apartments=Apartment.dao.findApartmentsByPid(" pid=? ",0);
		
		for(Apartment apartment:apartments){
			List<Apartment> achild=Apartment.dao.findApartmentsByPid(" pid=? ",apartment.get("id"));
			apartment.setChildren(achild);
		}
		setAttr("apartmentTree",apartments);
		
		/*Page<Apartment> apartmentPage;
		String orderby=" ORDER BY apartment.id ";
		if(ValidateKit.isNullOrEmpty(getPara("apartment"))){
			String where = " 1=1 ";
			apartmentPage=Apartment.dao.findApartmentPage(getParaToInt(0,1), 10, where, orderby);
		}else{
			String where=" apartment.name like ?  ";
			String condition ="%"+getPara("apartment").trim()+"%";
			apartmentPage=Apartment.dao.findApartmentPage(getParaToInt(0,1), 10, where, orderby, condition);
			setAttr("apartment",getPara("apartment").trim());
		}
		setAttr("apartmentPage",apartmentPage);*/
		
	}
	public void addapartment(){
		if(ValidateKit.isNullOrEmpty(getPara("id"))){

		}else{
			System.out.println(getParaToLong("id"));
			setAttr("pid",getParaToLong("id"));
		}
		render(APARTMENT_ADD_PAGE);
	}
	@Before(AdminValidator.class)
	public void doaddapartment(){
		Apartment apartment=getModel(Apartment.class);
		apartment.set("name", WebKit.delHTMLTag(apartment.getStr("name")));
		apartment.save();
		redirect("/admin/apartment");
	}
	public void editapartment(){
		Apartment apartment=Apartment.dao.findById(getPara("id"));
		setAttr("apartment",apartment);
		render(APARTMENT_EDIT_PAGE);
	}
	@Before(AdminValidator.class)
	public void doeditapartment(){
		Apartment apartment=getModel(Apartment.class);
		apartment.set("name", WebKit.delHTMLTag(apartment.getStr("name")));
		apartment.update();
		redirect("/admin/apartment");
	}
	public void onapartment(){
		Apartment.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/apartment");
	}
	public void offapartment(){
		Apartment.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/apartment");
	}
	
	/**
	 * @描述 对运维部 二级部门分配运维任务。
	 */
	public void apartmentType(){
		String where=" apartment.pid=?  ";
		String orderby=" ORDER BY apartment.id ";
		//父id为2 即为运维中心的子部门
		int pid =2;
		Page<Apartment> apartmentPage=Apartment.dao.findApartmentPage(getParaToInt(0,1), 10, where, orderby, pid);
		
		for(int i=0;i<apartmentPage.getList().size();i++){
			List <Record> list=ErrorType.dao.findApartmentType(apartmentPage.getList().get(i).get("id"));
			apartmentPage.getList().get(i).set("remark", list);
		}
		
		setAttr("apartmentPage",apartmentPage);
		setAttr("typeList",ErrorType.dao.getAllType());
		
		render(TYPE_TASK_PAGE);
	}
	
	/**
	 * @描述 执行分配操作
	 */
	@Before(Tx.class)
	public void doassign(){
		
		//前台选中的 类型s
		String[] types =getParaValues("types");
		
		List<ApartmentType> apartmenttype=ApartmentType.dao.findBy("apartmentType.apartment_id=?",getParaToLong("id"));
		
		if(ValidateKit.isNullOrEmpty(types)){
			for(int i=0;i<apartmenttype.size();i++){
				System.out.println(apartmenttype.get(i).getLong("type_id")+" 已取消，执行删除操作！");
				apartmenttype.get(i).delete();
			}
		}else if(ValidateKit.isNullOrEmpty(apartmenttype)){
			for(int i=0;i<types.length;i++){
				System.out.println(types[i]+" 不存在，执行新增操作！");
				ApartmentType at=new ApartmentType().set("apartment_id",  getPara("id")).set("type_id", types[i]);
				at.save();
			}
			
		}else{
			//需要处理该故障类型?，不存在则新增。
			for(int i=0;i<types.length;i++){
				boolean flag=false;
				for(int j=0;j<apartmenttype.size();j++){
					if(Integer.parseInt(types[i])==apartmenttype.get(j).getLong("type_id")){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(types[i]+" 存在，保留不删除！");
				}else if(!flag){
					System.out.println(types[i]+" 不存在，执行新增操作！");
					ApartmentType at=new ApartmentType().set("apartment_id",  getPara("id")).set("type_id", types[i]);
					at.save();
				}
			}
			
			//需要保留该故障类型?，不保留则删除
			for(int i=0;i<apartmenttype.size();i++){
				boolean flag=false;
				for(int j=0;j<types.length;j++){
					if(apartmenttype.get(i).getLong("type_id")==Integer.parseInt(types[j])){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(apartmenttype.get(i).getLong("type_id")+" 未取消，保留不删除！");
				}else if(!flag){
					System.out.println(apartmenttype.get(i).getLong("type_id")+" 已取消，执行删除操作！");
					apartmenttype.get(i).delete();
				}
			}
			
		}
		redirect("/admin/apartmentType");
		
	}
	
	/**
	 * 职位管理
	 */
	public void position(){
		
		Page<Position> positionPage;
		String orderby=" ORDER BY position.id ";
		if(ValidateKit.isNullOrEmpty(getPara("position"))){
			String where = " 1=1 ";
			positionPage=Position.dao.findPositionPage(getParaToInt(0,1), 10, where, orderby);
		}else{
			String where=" position.name like ?  ";
			String condition ="%"+getPara("position").trim()+"%";
			positionPage=Position.dao.findPositionPage(getParaToInt(0,1), 10, where, orderby, condition);
			setAttr("position",getPara("position").trim());
		}
		setAttr("positionPage",positionPage);
	}
	public void addposition(){
		render(POSITION_ADD_PAGE);
	}
	@Before(AdminValidator.class)
	public void doaddposition(){
		Position position=getModel(Position.class);
		position.set("name", WebKit.delHTMLTag(position.getStr("name")));
		position.save();
		redirect("/admin/position");
	}
	public void editposition(){
		Position position=Position.dao.findById(getPara("id"));
		setAttr("position",position);
		render(POSITION_EDIT_PAGE);
	}
	@Before(AdminValidator.class)
	public void doeditposition(){
		Position position=getModel(Position.class);
		position.set("name", WebKit.delHTMLTag(position.getStr("name")));
		position.update();
		redirect("/admin/position");
	}
	public void onposition(){
		Position.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/position");
	}
	public void offposition(){
		Position.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/position");
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
	 * 故障类型管理
	 */
	public void type(){
		
		Page<ErrorType> errorTypePage;
		String orderby=" ORDER BY errorType.id ";
		if(ValidateKit.isNullOrEmpty(getPara("errorType"))){
			String where = " 1=1 ";
			errorTypePage=ErrorType.dao.findErrorTypePage(getParaToInt(0,1), 10, where, orderby);
		}else{
			String where=" errorType.name like ?  ";
			String condition ="%"+getPara("errorType").trim()+"%";
			errorTypePage=ErrorType.dao.findErrorTypePage(getParaToInt(0,1), 10, where, orderby, condition);
			setAttr("errorType",getPara("errorType").trim());
		}
		setAttr("errorTypePage",errorTypePage);
	}
	
	public void addtype(){
		render(TYPE_ADD_PAGE);
	}
	@Before({AdminValidator.class,EvictInterceptor.class})
	@CacheName("/admin/type")
	public void doaddtype(){
		ErrorType errorType=getModel(ErrorType.class);
		errorType.set("name", WebKit.delHTMLTag(errorType.getStr("name")));
		errorType.save();
		redirect("/admin/type");
	}
	public void edittype(){
		ErrorType errorType=ErrorType.dao.findById(getPara("id"));
		setAttr("errorType",errorType);
		render(TYPE_EDIT_PAGE);
	}
	@Before({AdminValidator.class,EvictInterceptor.class})
	@CacheName("/admin/type")
	public void doedittype(){
		ErrorType errorType=getModel(ErrorType.class);
		errorType.set("name", WebKit.delHTMLTag(errorType.getStr("name")));
		errorType.update();
		redirect("/admin/type");
	}
	@Before({EvictInterceptor.class})
	@CacheName("/admin/type")
	public void ontype(){
		ErrorType.dao.findById(getPara("id")).set("deleted_at", null).update();
		redirect("/admin/type");
	}
	@Before({EvictInterceptor.class})
	@CacheName("/admin/type")
	public void offtype(){
		ErrorType.dao.findById(getPara("id")).set("deleted_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).update();
		redirect("/admin/type");
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
