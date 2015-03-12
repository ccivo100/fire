package com.poicom.function.bootstrap;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import cn.dreampie.resource.HttpResource;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

/**
 * 用于指向BootStrap例子的控制器。
 * @author Administrator
 *
 */
@ControllerKey(value="/bootstrap",path="/page/bootstrap")
public class BootstrapController extends Controller {
	
	public void index(){
		
		render("bootstrap.html");
	}
	
	public void theme(){
		render("theme.html");
	}
	
	public void tree(){
		//URL:http://www.js-css.cn/jscode/nav/nav23/
		//URL:http://bookshadow.com/weblog/2014/05/17/jquery-bootstrap-tree-list/
		render("tree.html");
	}
	
	public void school(){
		
		render("school.html");
	}
	
	public void upfile(){
		UploadFile uploadFile=getFile("upfile");
		//String name=getPara("name");
		uploadFile.getFile().renameTo(new File("dongyu.log"));
		
		//System.out.println(name);
		boolean success=false;
		//if (name!=null&&uploadFile!=null) {
		if (uploadFile!=null) {
			success=true;
			setAttr("success", success);
		}
		render("school.html");
	}
	
	public void ajax(){
		String name=getPara("pname");
		String uname=getPara("uname");
		HttpServletResponse resp=getResponse();
		setAttr("pname", name);
		setAttr("uname", uname);
		render("/ajax/index.html");
	}
	

}
