package com.poicom.basic.plugin.routebind;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.routebind.ControllerKey;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.poicom.basic.kit.ClassSearchKit;
import com.poicom.basic.kit.CollectionKit;
/**
 * 
 * @author 陈宇佳
 *
 */
public class RouteBind extends Routes {
	
	private boolean autoScan=true;
	
	private List<Class<? extends Controller>> excludeClasses=Lists.newArrayList();
	private List<Class<? extends Controller>> includeClasses=Lists.newArrayList();
	
	private List<String> includeClassPaths = Lists.newArrayList();
	private List<String> excludeClassPaths = Lists.newArrayList();
	
	protected final Logger logger=LoggerFactory.getLogger(RouteBind.class);
	
	private String suffix="Controller";
	
	public RouteBind autoScan(boolean autoScan){
		this.autoScan=autoScan;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public RouteBind addExcludeClasses(Class<? extends Controller>... clazzes){
		for(Class<? extends Controller> clazz:clazzes){
			excludeClasses.add(clazz);
		}
		return this;
	}
	
	public RouteBind addExcludeClasses(List<Class<? extends Controller>> clazzes){
		if(clazzes!=null){
			excludeClasses.addAll(clazzes);
		}
		return this;
	}
	
	public RouteBind addExcludePaths(String... paths){
		for(String path:paths){
			excludeClassPaths.add(path);
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public RouteBind addIncludeClasses(Class<? extends Controller>... clazzes){
		for (Class<? extends Controller> clazz : clazzes) {
			includeClasses.add(clazz);
		}
		return this;
	}
	
	public RouteBind addIncludeClasses(List<Class<? extends Controller>> clazzes) {
		if (clazzes != null) {
			includeClasses.addAll(clazzes);
		}
		return this;
	}
	
	public RouteBind addIncludePaths(String... paths) {
		for (String path : paths) {
			includeClassPaths.add(path);
		}
		return this;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void config() {
		List<Class<? extends Controller>> controllerClasses = ClassSearchKit.of(Controller.class).includepaths(includeClassPaths).search();
		controllerClasses=CollectionKit.removeDuplicate(controllerClasses);
		
		ControllerKey controllerKey = null;
		for (Class controller : controllerClasses) {
			if (excludeClasses.contains(controller)) {
				continue;
			} 
			controllerKey = (ControllerKey) controller	.getAnnotation(ControllerKey.class);
			if (controllerKey == null) {//如果没有该注解
				if (!autoScan) {
					continue;
				}
				this.add(controllerKey(controller), controller);
				logger.debug("routes.add(" + controllerKey(controller) + ", "+ controller.getName() + ")");
			} else if (StrKit.isBlank(controllerKey.path())) {
				this.add(controllerKey.value(), controller);//不存在viewPath则使用actionKey作为viewPath
				logger.debug("routes.add(" + controllerKey.value() + ", "	+ controller.getName() + ")");
			} else {
				this.add(controllerKey.value(), controller,controllerKey.path());//存在viewPath
				logger.debug("routes.add(" + controllerKey.value() + ", "	+ controller + "," + controllerKey.path() + ")");
			}
		}
		
	}
	
	private String controllerKey(Class<Controller> clazz) {
		Preconditions.checkArgument(clazz.getSimpleName().endsWith(suffix),
				" does not has a @ControllerKey annotation and it's name is not end with "	+ suffix);//如果没有该注解则报错。
		String simpleName = clazz.getSimpleName();//类名
		String controllerKey = "/";
		if (!simpleName.equalsIgnoreCase(suffix)) {
			controllerKey += StrKit.firstCharToLowerCase(simpleName.replace(suffix, ""));//XXXController，截取前面一部分当做actionKey
		}
		return controllerKey;
	}
	
	public RouteBind suffix(String suffix){
		this.suffix=suffix;
		return this;
	}

}
