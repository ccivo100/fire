package com.poicom.function.controller;

import cn.dreampie.routebind.ControllerKey;

import com.jfinal.core.Controller;
import com.poicom.function.model.Role;

@ControllerKey(value="/autho",path="/page/app/autho")
public class AuthoController extends Controller{
	
	public void index(){
		setAttr("roletree", Role.dao.findAll());
		render("roles.html");
	}

}
