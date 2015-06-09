package com.poicom.function.validator;

import java.util.List;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.function.model.Etype;

public class EtypeValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		
		if(getActionKey().equals("/admin/type/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("etype.name").trim())){
				addError("nameMsg", "故障名称不可为空");
			}else if(!ValidateKit.isLength(c.getPara("etype.name"), 3, 15)){
				addError("nameMsg", "输入字数不少于3...");
			}
			
			List<Etype> etypes=Etype.dao.rootNode(" pid=? ",0);
			if(ValidateKit.isNullOrEmpty(c.getPara("etype.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				if(c.getParaToLong("etype.pid")!=0){
					addError("pidMsg","父节点应该为0");
				}
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				boolean flag=false;
				StringBuffer str=new StringBuffer();
				for(Etype etype:etypes){
					str.append(etype.get("id")+" ");
					if(etype.getLong("id")==c.getParaToLong("pid")){
						
						flag=true;
					}
				}
				if(!flag){
					addError("pidMsg","父节点应为："+str.toString());
				}
			}
			
		}
		//更新故障类型验证
		else if(getActionKey().equals("/admin/type/update")){
			if(ValidateKit.isNullOrEmpty(c.getPara("etype.name").trim())){
				addError("nameMsg", "故障名称不可为空");
			}else if(!ValidateKit.isLength(c.getPara("etype.name"), 3, 15)){
				addError("nameMsg", "输入字数不少于3...");
			}
			if(ValidateKit.isNullOrEmpty(c.getPara("etype.pid").trim())){
				addError("pidMsg","父节点不可为空");
				
			}else{
				Etype etype=Etype.dao.findById(c.getParaToInt("etype.id"));
				if(etype.getLong("pid")==0&c.getParaToLong("etype.pid")!=etype.getLong("pid")){
					addError("pidMsg","根节点不可修改");
				}else if(etype.getLong("pid")!=0&c.getParaToLong("etype.pid")==0){
					addError("pidMsg","子节点不可修改为根节点");
				}
				else if(c.getParaToLong("etype.pid")!=0){
					List<Etype> etypes=Etype.dao.rootNode(" pid=? ",0);
					boolean flag=false;
					StringBuffer str=new StringBuffer();
					for(Etype e:etypes){
						str.append(e.get("id")+" ");
						if(c.getParaToLong("etype.pid")==e.getLong("id")){
							flag=true;
						}
					}
					if(!flag){
						addError("pidMsg","父节点应为："+str.toString());
					}
				}
			}
		}

	}

	@Override
	protected void handleError(Controller c) {
		if(getActionKey().equals("/admin/type/save")){
			if(ValidateKit.isNullOrEmpty(c.getPara("etype.id"))&ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("etype.pid");
			}else if(!ValidateKit.isNullOrEmpty(c.getPara("pid"))){
				c.keepPara("pid");
			}
			c.render("/app/admin/type/add.html");
		}else if(getActionKey().equals("/admin/type/update")){
			c.keepModel(Etype.class);
			c.render("/app/admin/type/edit.html");
		}

	}

}
