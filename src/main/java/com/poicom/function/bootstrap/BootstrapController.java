package com.poicom.function.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.routebind.ControllerKey;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.poicom.common.kit.excel.PoiKit;
import com.poicom.function.system.model.Permission;

/**
 * 用于指向BootStrap例子的控制器。
 * @author Administrator
 *
 */
@ControllerKey(value="/bootstrap",path="/page/bootstrap")
public class BootstrapController extends Controller {
	protected Logger logger=LoggerFactory.getLogger(getClass());
	
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
	
	public void dash(){
		
	}
	
	/**
	 * test ajax get
	 */
	public void ajaxget(){
		int param=getParaToInt("param");
		List<Permission> permissionList=Permission.dao.findByRole("", param);
		logger.error(permissionList.toString());
		renderJson("permissionList", permissionList);
	}
	
	/**
	 * test ajax post
	 */
	public void ajaxpost(){
		
	}
	
	public void testJson(){
		List<Permission> permissionList=Permission.dao.findByRole("", 3);
		logger.error(permissionList.toString());
		renderJson("msg",permissionList);
	}

	
	public void upfile(){
		UploadFile uploadFile=getFile("upfile");
		//String name=getPara("name");
		//uploadFile.getFile().renameTo(new File("dongyu.log"));
		
		FileInputStream headeris= loadFile(uploadFile.getFile());
		FileInputStream contentis= loadFile(uploadFile.getFile());
		String[] header=PoiKit.readHeader(headeris);
		Map<Integer,String> content=PoiKit.readContent(contentis);
		
		System.out.println(header);
		System.out.println(content);
		
		
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
		//HttpServletResponse resp=getResponse();
		setAttr("pname", name);
		setAttr("uname", uname);
		render("/ajax/index.html");
	}
	
	/**
	 * @描述 将上次文件转换为FileInputStream
	 * @param upload
	 * @return FileInputStream
	 */
	public FileInputStream loadFile(File upload){
		FileInputStream is=null;
		try {
			is=new FileInputStream(upload);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	
	/**
	 * @描述 打印
	 * @param obj
	 */
	public void print(Object obj){
		if(obj instanceof String[]){
			
		}else if(obj instanceof Map){
			
		}
	}
	

}
