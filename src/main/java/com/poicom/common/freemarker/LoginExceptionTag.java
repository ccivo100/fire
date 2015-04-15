package com.poicom.common.freemarker;

import java.io.IOException;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.shiro.freemarker.SecureTag;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

/**
 * 自定义登陆验证
 * @author poicom7
 *
 */
public class LoginExceptionTag extends SecureTag{
	
	protected final static Logger logger=LoggerFactory.getLogger(LoginExceptionTag.class);
	
	String getAttr(Map params){
		return getParam(params,"name");
	}

	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body)
			throws IOException, TemplateException {
		String result=null;
		Subject subject=getSubject();
		Session session=getSubject().getSession();
		String attr=getAttr(params);
		String value=null;
		if(subject!=null&& session!=null&&attr!=null){
			if(logger.isDebugEnabled()){
				logger.debug("Attr is exist");
			}
			if(session.getAttribute(attr)!=null){
				value=String.valueOf(session.getAttribute(attr));
				if(value.equalsIgnoreCase("UnknownUserException")){
					result="账户验证失败或已被禁用!";
				}else if(value.equalsIgnoreCase("IncorrectCredentialsException")){
					result = "密码错误，请重新输入!";
				}else if (value.equalsIgnoreCase("IncorrectCaptchaException")) {
			          result = "验证码验证失败!";
		        } else {
		          result = "用户不存在，请重新输入正确用户名!";
		        }
			}
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("Attr is not exist.");
			}
		}
		
		if(result!=null){
			try {
				env.getOut().write(result);
			} catch (IOException ex) {
				throw new TemplateException("Error writing [" + result
						+ "] to Freemarker.", ex, env);
			}
		}else{
			renderBody(env, body);
		}
		
	}

}
