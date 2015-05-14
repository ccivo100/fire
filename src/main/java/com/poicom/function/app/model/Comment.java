package com.poicom.function.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;
/**
 * 
 * @author 唐东宇
 *
 */
@TableBind(tableName = "com_comment")
public class Comment extends Model<Comment> {

	private static final long serialVersionUID = 6736075553842251384L;
	
	public final static Comment dao=new Comment();
	
	public List<Record> findCommentsByOrderId(String where,Object... paras){
		return Db.find(SqlKit.sql("comment.findCommentsBySelect")+SqlKit.sql("comment.findCommentsByFrom")+getWhere(where), paras); 
	}

}
