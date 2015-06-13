package com.poicom.function.model;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;

import cn.dreampie.tablebind.TableBind;
/**
 * 工单model
 * @author 唐东宇
 *
 */
@SuppressWarnings("unused")
@TableBind(tableName = "com_order")
public class Order extends BaseModel<Order> {

	private static final long serialVersionUID = -3474341426070972713L;
	
	private static Logger log = Logger.getLogger(Order.class);
	
	public final static Order dao = new Order();

	
	/**
	 * @描述 查询Reports 分页
	 * @param pageNumber
	 * @param pageSize
	 * @param where
	 * @param orderby
	 * @param paras
	 * @return
	 */
	public Page<Record> findReportsByUserId(int pageNumber, int pageSize,String where,String orderby,
			Object... paras) {
		return Db.paginate(pageNumber,pageSize,
				getSql("order.findOfferQueryBySelect"),
				getSql("order.findOfferQueryByFrom") + getWhere(where)
						+ orderby, paras);
	}
	
	/**
	 * @描述 查询Report 详细内容
	 * @param where
	 * @param paras
	 * @return
	 */
	public Record findReportById(String where,Object... paras){
		return Db.findFirst(getSql("order.findOrderInfoBySelect") + blank
				+ getSql("order.findOrderInfoByFrom")+getWhere(where), paras);
	}
	
	/**
	 * @描述 查询Operates 分页
	 * @param pageNumber
	 * @param pageSize
	 * @param where
	 * @param orderby
	 * @param paras
	 * @return
	 */
	public Page<Record> findOperatesByUserId(int pageNumber, int pageSize,String where,String orderby,
			Object... paras) {
		return Db.paginate(pageNumber,pageSize,
				getSql("order.findOfferQueryBySelect"),
				getSql("order.findOfferQueryByFrom") + getWhere(where)
						+ orderby, paras);
	}
	
	public List<Record>findOperatesByUserId(String where,String orderby,Object...  paras){
		return Db.find(getSql("order.findOfferQueryBySelect")+getSql("order.findOfferQueryByFrom") + getWhere(where)+ orderby, paras);
	}
	
	/**
	 * @描述 查询Operate 详细内容
	 * @param where
	 * @param paras
	 * @return
	 */
	public Record findOperateById(String where,Object... paras){
		return Db.findFirst(getSql("order.findOrderInfoBySelect") + blank
				+ getSql("order.findOrderInfoByFrom")+getWhere(where), paras);
	}
	
	/**
	 * @描述 查询Orders 主页 分页
	 * @param pageNumber
	 * @param pageSize
	 * @param where
	 * @param orderby
	 * @param paras
	 * @return
	 */
	public Page<Record> findIndexOrders(int pageNumber, int pageSize,String where,String orderby,
			Object... paras) {
		return Db.paginate(pageNumber,pageSize,
				getSql("order.findOfferQueryBySelect"),
				getSql("order.findOfferQueryByFrom") + getWhere(where)
						+ orderby, paras);
	}
	
	/**
	 * @描述 查询Orders 管理员 不分页
	 * @param where
	 * @param orderby
	 * @param paras
	 * @return
	 */
	public List<Record> findAdminOrders(String where,String orderby,Object... paras){
		return Db.find(getSql("order.findOfferQueryBySelect")+
				getSql("order.findOfferQueryByFrom") + getWhere(where)
						+ orderby, paras);
	}
	
	/**
	 * 获得所有故障类型（弃用）
	 * @return
	 */
	@Deprecated
	public List<Record> getAllType(){
		return Db.find("select * from com_type");
	}
	
	/**
	 * @描述 如果attr，如description的长度超过30（随机，由Random决定），则后面用...代替。
	 * @param page
	 * @param paras
	 */
	public void format(Page<Record> page,String... paras){
		for(Record record:page.getList()){
			for(String attr:paras){
				record.set(attr, StringUtils.abbreviate(record.getStr(attr), (new Random().nextInt(2)+35)));
			}
			
		}
	}
	
	public void format(List<Record> list,String... paras){
		for(Record record:list){
			for(String attr:paras){
				record.set(attr, StringUtils.abbreviate(record.getStr(attr), (new Random().nextInt(2)+35)));
			}
			
		}
	}
	
	/**
	 * 根据status获取Order。
	 * @param paras
	 * @return
	 */
	public List<Order> findOrderByStatus(Object... paras){
		return find("select * from com_order where status=?",paras);
	}
	
	/**
	 * 根据条件获取Order。
	 * @param where
	 * @param paras
	 * @return
	 */
	public Page<Record> findExceptionOrders(int pageNumber,int pageSize,String where,Object... paras){
		return Db.paginate(pageNumber, pageSize,
				getSql("order.findOrderInfoBySelect"),
				getSql("order.findOrderInfoByFrom") + getWhere(where), paras);
	}
	

}
