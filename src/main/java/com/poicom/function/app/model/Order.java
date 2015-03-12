package com.poicom.function.app.model;

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
		/*return Db.paginate(pageNumber, pageSize,
				"SELECT o.id, o.description, o.offer_at, f.full_name off_user,"
				+ " o.status, d.full_name deal_user , t.name type_name"
				+ blank,
				SqlKit.sql("order.findReportFullOffersByFrom"), paras);*/
	}

}
