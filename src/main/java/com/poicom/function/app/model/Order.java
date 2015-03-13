package com.poicom.function.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import cn.dreampie.sqlinxml.SqlKit;
import cn.dreampie.tablebind.TableBind;
import cn.dreampie.web.model.Model;

@TableBind(tableName = "com_order")
public class Order extends Model<Order> {

	public static Order dao = new Order();

	public Page<Record> getReportOrderPage(int pageNumber, int pageSize,
			Object... paras) {
		return Db.paginate(pageNumber, pageSize,
				SqlKit.sql("order.findReportFullOffersBySelect") + blank,
				SqlKit.sql("order.findReportFullOffersByFrom"), paras);
	}
	
	public Record getReportOrder(Object... paras){
		return Db.findFirst(SqlKit.sql("order.findReportOfferBySelect") + blank
				+ SqlKit.sql("order.findReportOfferByFrom"), paras);
	}
	
	/**
	 * 获得所有故障类型
	 * @return
	 */
	public List<Record> getAllType(){
		return Db.find("select * from com_type");
	}

}
