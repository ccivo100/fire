package com.poicom.common.shiro;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.poicom.function.user.model.Permission;
import com.poicom.function.user.model.Role;
import com.poicom.function.user.model.User;

import cn.dreampie.ValidateKit;
import cn.dreampie.shiro.core.SubjectKit;


/**
 * 
 * @author Administrator
 *
 */
public class MyJdbcRealm extends AuthorizingRealm {

	/**
	 * 登录认证
	 * @param token
	 * @return
	 * @throws org.apache.shiro.authc.AuthenticationException
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken=(UsernamePasswordToken) token;
		User user=null;
		
		String username=userToken.getUsername();
		if(ValidateKit.isEmail(username)){
			user=User.dao.findFirstBy("`user`.email = ? AND `user`.deleted_at is null", username);
		}else if (ValidateKit.isMobile(username)) {
			user=User.dao.findFirstBy("`user`.mobile = ? AND `user`.deleted_at is null", username);
		}else {
			user=User.dao.findFirstBy("`user`.username = ? AND `user`.deleted_at is null", username);
		}
		if(user!=null){
			SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getStr("password"),getName());
			return info;
		}else {
			return null;
		}
		
	}
	
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * @param principals 用户信息
	 * @return
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName=((User)principals.fromRealm(getName()).iterator().next()).get("username");
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		//角色集合
		Set<String> roleSet=new LinkedHashSet<String>();
		//权限集合
		Set<String> permissionSet=new LinkedHashSet<String>();
		List<Role> roles=null;
		
		User user = User.dao.findFirstBy(" `user`.username =? AND `user`.deleted_at is null", loginName);
	    if (user != null) {
	      //遍历角色
	      roles = Role.dao.findUserBy("", user.getLong("id"));
	    } else {
	      SubjectKit.getSubject().logout();
	    }
	    
	    loadRole(roleSet, permissionSet, roles);
	    info.setRoles(roleSet); // 设置角色
	    info.setStringPermissions(permissionSet); // 设置权限
	    return info;
	}
	
	/**
	 * @description 1、先判断每一个role是不是可用，即deleted_at为空;
	 * 				2、roleSet保存了数据库表sec_role中的全部信息value字段，如：R_ADMIN、R_MEMBER……等;
	 * 				3、permissionSet保存了数据库表sec_permission中的全部信息value字段，如：P_D_ADMIN、P_ROLE……等;
	 * @param roleSet
	 * @param permissionSet
	 * @param roles
	 */
	private void loadRole(Set<String> roleSet,Set<String> permissionSet,List<Role> roles){
		List<Permission> permissions;
		for (Role role : roles) {
			//角色可用
			if(role.getDate("deleted_at")==null){
				roleSet.add(role.getStr("value"));
				permissions=Permission.dao.findByRole("", role.getLong("id"));
				loadAuth(permissionSet,permissions);
			}
		}
	}
	
	/**
	 * 
	 * @param permissionSet
	 * @param permissions
	 */
	private void loadAuth(Set<String> permissionSet,List<Permission> permissions) {
		// 遍历权限
		for (Permission permission : permissions) {
			// 权限可用
			if (permission.getDate("deleted_at") == null) {
				permissionSet.add(permission.getStr("value"));
			}
		}
	}
	
	/**
	 * 2、找找哪里用到？
	 * 更新用户授权信息缓存.
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(Object principal){
		SimplePrincipalCollection principals=new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}
	
	/**
	 * 3、找找哪里用到？
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
	
	public static void main(String [] args){
		MyJdbcRealm myRealm=new MyJdbcRealm();
		myRealm.test();
		
	}
	
	public void test(){
		String name=getClass().getName();
		String simplename=getClass().getSimpleName();
		System.out.println(name);
		System.out.println(simplename);
	}

	

}
