package com.poicom.function.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.tablebind.TableBind;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_comment")
public class Comment extends BaseModel<Comment> {

	private static final long serialVersionUID = 6736075553842251384L;
	
	public final static Comment dao=new Comment();
	
	public List<Record> findCommentsByOrderId(String where,Object... paras){
		return Db.find(getSql("comment.findCommentsBySelect")+getSql("comment.findCommentsByFrom")+getWhere(where), paras); 
	}

}
