package com.poicom.function.app;

import java.util.List;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;

import cn.dreampie.ValidateKit;
import cn.dreampie.mail.Mailer;
import cn.dreampie.routebind.ControllerKey;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.poicom.function.app.model.ErrorType;
import com.poicom.function.app.model.Order;
import com.poicom.function.user.model.User;
import com.poicom.function.user.model.UserInfo;

/**
 * @描述 运维管理
 * @author poicom7
 *
 */
@ControllerKey(value="/operate",path="/page/app/operate")
public class OperateController extends Controller{
	
	public void index(){
		
	}
	
	/**
	 * @描述 运维人员查询本账号处理范围内的故障工单
	 */
	public void deal(){
		User user=User.dao.getCurrentUser();
		
		Page <Record> operatePage=Order.dao.getOperateOrderPage(getParaToInt(0,1), 10, user.get("id"));
		Order.dao.format(operatePage,"description");
		setAttr("operatePage",operatePage);
		render("operate.html");
	}
	
	/**
	 * @描述 进入故障工单处理页面
	 */
	public void edit(){
		//故障类型
		setAttr("typeList",ErrorType.dao.getAllType());
		
		//工单详细信息
		Record order = Order.dao.getCommonOrder(getParaToInt("id"));
		setAttr("order", order);
		
		//当前用户详细信息
		Record userinfo=UserInfo.dao.getAllUserInfo(User.dao.getCurrentUser().get("id"));
		setAttr("userinfo",userinfo);
		
		//获取工单申报者的分公司信息
		Record offer=UserInfo.dao.getUserBranch(order.getLong("offerid"));

		if(!ValidateKit.isNullOrEmpty(offer))
			setAttr("offer_branch",offer.getStr("branch"));

		
	}
	
	/**
	 * @描述 提交故障处理建议
	 */
	@Before(Tx.class)
	public void update(){
		
		//order_id
		Integer orderid=getParaToInt("orderid");
		//comment
		String comment=getPara("commen");
		
		Order.dao.findById(orderid).set("deal_user", getParaToInt("dealid"))
				.set("comment", comment)
				.set("deal_at", 
						DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
				.set("status", 1).update();
		
		redirect("/operate/deal");
	}
	
	/**
	 * @描述 任务分配
	 */
	public void task(){
		//Branch_id 为10的用户。
		Page<Record> userPage=UserInfo.dao.getUserByBranch(getParaToInt(0,1), 10);
		
		for(int i=0;i<userPage.getList().size();i++){
			List<Record> list= ErrorType.dao.getOperatorType(userPage.getList().get(i).get("userid"));
			StringBuffer types=new StringBuffer();
			for(int j=0;j<list.size();j++){
				types.append(list.get(j).getStr("typename")+"；");
			}
			//userPage.getList().get(i).set("remark", types.toString());
			userPage.getList().get(i).set("remark", list);
		}
		
		setAttr("userPage",userPage);
		setAttr("typeList",ErrorType.dao.getAllType());
		
	}
	
	/**
	 * @描述 分配操作
	 */
	public void assign(){
		String userid=getPara("id");
		setAttr("userinfo",UserInfo.dao.getAllUserInfo(userid));
		setAttr("usertype",ErrorType.dao.getOperatorType(userid));
		setAttr("typeList",ErrorType.dao.getAllType());
	}
	
	/**
	 * @描述 执行分配操作
	 */
	@Before(Tx.class)
	public void doassign(){
		//前台选中的 类型s
		String[] types=getParaValues("types");
		
		//用户-类型 中间表
		List<Record> cut=ErrorType.dao.getTypeByUser(getParaToLong("userid"));
		
		
		if(ValidateKit.isNullOrEmpty(types)){
			for(int i=0;i<cut.size();i++){
				System.out.println(cut.get(i).getLong("typeid")+" 已取消，执行删除操作！");
				Db.deleteById("com_user_type", cut.get(i).getLong("id"));
			}
		}else if(ValidateKit.isNullOrEmpty(cut)){
			for(int i=0;i<types.length;i++){
				System.out.println(types[i]+" 不存在，执行新增操作！");
				Record record=new Record().set("user_id", getPara("userid")).set("type_id", types[i]);
				Db.save("com_user_type", record);
			}
		}else{
			//需要处理该故障类型?，不存在则新增。
			for(int i=0;i<types.length;i++){
				boolean flag=false;
				for(int j=0;j<cut.size();j++){
					if(Integer.parseInt(types[i])==cut.get(j).getLong("typeid")){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(types[i]+" 存在，保留不删除！");
				}else if(!flag){
					System.out.println(types[i]+" 不存在，执行新增操作！");
					Record record=new Record().set("user_id", getPara("userid")).set("type_id", types[i]);
					Db.save("com_user_type", record);
				}
			}
			
			//需要保留该故障类型?，不保留则删除
			for(int i=0;i<cut.size();i++){
				boolean flag=false;
				for(int j=0;j<types.length;j++){
					if(cut.get(i).getLong("typeid")==Integer.parseInt(types[j])){
						flag=true;
						break;
					}
				}
				if(flag){
					System.out.println(cut.get(i).getLong("typeid")+" 未取消，保留不删除！");
				}else if(!flag){
					System.out.println(cut.get(i).getLong("typeid")+" 已取消，执行删除操作！");
					Db.deleteById("com_user_type", cut.get(i).getLong("id"));
				}
			}
		}
		
		redirect("/operate/task");
	}


}
