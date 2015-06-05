package com.poicom.basic.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.SimplePrincipalCollection;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.poicom.function.model.User;
/**
 * 获取session中用户资料
 * @author 唐东宇
 *
 */
public class SessionInterceptor implements Interceptor {
	
	private boolean createSession = false;
	
	public SessionInterceptor() {
	}
	
	public SessionInterceptor(boolean createSession) {
		this.createSession = createSession;
	}

	@Override
	public void intercept(ActionInvocation ai) {
		ai.invoke();
		
		Controller c = ai.getController();
		HttpSession hs=c.getSession(createSession);
		if(hs!=null){
			for (Enumeration<String> names=hs.getAttributeNames(); names.hasMoreElements();) {
				String name = names.nextElement();
				if(name.equals("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY")){
					SimplePrincipalCollection obj=(SimplePrincipalCollection) hs.getAttribute(name);
					User user=(User) obj.getPrimaryPrincipal();
					if(c.getSessionAttr("current_user")==null){
						c.setSessionAttr("current_user", user);
					}
					
				}
			}
		}else{
			c.removeSessionAttr("current_user");
		}
	}
}
