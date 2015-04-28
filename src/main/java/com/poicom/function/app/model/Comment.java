package com.poicom.function.app.model;

import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "com_comment")
public class Comment extends Model<Comment> {

	private static final long serialVersionUID = 6736075553842251384L;
	
	public final static Comment dao=new Comment();
	

}
