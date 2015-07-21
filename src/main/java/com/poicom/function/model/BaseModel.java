package com.poicom.function.model;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.poicom.basic.plugin.sqlxml.SqlXmlKit;

/**
 * Model基础类
 * @author FireTercel 2015年6月3日 
 *
 * @param <M>
 */
public abstract class BaseModel<M extends Model<M>> extends Model<M> {

	private static final long serialVersionUID = -2520632200187508195L;
	
	private static Logger log = Logger.getLogger(BaseModel.class);
	
	private Table table;
	private String tableName;
	private String primaryKey;
	private String modelName;

	private String selectSql;
	private String fromSql;
	private String updateSql;
	private String deleteSql;
	private String dropSql;
	private String countSql;
	
	protected static String blank = " ";
	
	/**
	 * 获得属性列表
	 */
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
	
	/**
	 * 将Model转为Json
	 * @return
	 */
	public String getJson() {
		return JsonKit.toJson(this);
	}
	
	public List<M> findAll() {
		return find(getSelectSql() + getFromSql());
	}
	
	public List<M> findBy(String where, Object... paras) {
		return find(getSelectSql() + getFromSql() + getWhere(where), paras);
	}

	/**
	 * 查询前N个数据
	 * @param topNumber
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<M> findTopBy(int topNumber, String where, Object... paras) {
		return paginate(1, topNumber, getSelectSql(),
				getFromSql() + getWhere(where), paras).getList();
	}

	/**
	 * 查询第一个数据
	 * @param where
	 * @param paras
	 * @return
	 */
	public M findFirstBy(String where, Object... paras) {
		return findFirst(getSelectSql() + getFromSql() + getWhere(where), paras);
	}

	/**
	 * 全部分页
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<M> paginateAll(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, getSelectSql(), getFromSql());
	}

	/**
	 * 条件分页
	 * @param pageNumber
	 * @param pageSize
	 * @param where
	 * @param paras
	 * @return
	 */
	public Page<M> paginateBy(int pageNumber, int pageSize, String where,
			Object... paras) {
		return paginate(pageNumber, pageSize, getSelectSql(), getFromSql()
				+ getWhere(where), paras);
	}
	
	/**
	 * 更新所有
	 * @param set
	 * @param paras
	 * @return
	 */
	public boolean updateAll(String set, Object... paras) {
		return Db.update(getUpdateSql() + getSet(set), paras) > 0;
	}

	/**
	 * 条件更新
	 * @param set
	 * @param where
	 * @param paras
	 * @return
	 */
	public boolean updateBy(String set, String where, Object... paras) {
		return Db.update(getUpdateSql() + getSet(set) + getWhere(where), paras) > 0;
	}

	/**
	 * 设置所有不可用
	 * @return
	 */
	public boolean deleteAll() {
		return Db.update(getDeleteSql()) > 0;
	}

	/**
	 * 条件设置不可用
	 * @param where
	 * @param paras
	 * @return
	 */
	public boolean deleteBy(String where, Object... paras) {
		return Db.update(getDeleteSql() + getWhere(where), paras) > 0;
	}

	/**
	 * 删除所有
	 * @return
	 */
	public boolean dropAll() {
		return Db.update(getDropSql()) > 0;
	}

	/**
	 * 条件删除
	 * @param where
	 * @param paras
	 * @return
	 */
	public boolean dropBy(String where, Object... paras) {
		return Db.update(getDropSql() + getWhere(where), paras) > 0;
	}

	/**
	 * 统计所有
	 * @return
	 */
	public Long countAll() {
		return Db.queryFirst(getCountSql());
	}

	/**
	 * 条件统计
	 * @param where
	 * @param paras
	 * @return
	 */
	public Long countBy(String where, Object... paras) {
		return Db.queryFirst(getCountSql() + getWhere(where), paras);
	}

	/**
	 * SET语句
	 * @param set
	 * @return
	 */
	protected String getSet(String set) {
		if (set != null && !set.isEmpty()
				&& !set.trim().toUpperCase().startsWith("SET")) {
			set = " SET " + set;
		}
		return set;
	}

	/**
	 * WHERE语句
	 * @param where
	 * @return
	 */
	protected String getWhere(String where) {
		if (where != null && !where.isEmpty()
				&& !where.trim().toUpperCase().startsWith("WHERE")) {
			where = " WHERE " + where;
		}
		return where;
	}
	
	/**
	 * 获取SQL，固定SQL
	 * @param sqlId
	 * @return
	 */
	protected String getSql(String sqlId){
		return SqlXmlKit.sql(sqlId);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,Object> param){
		return SqlXmlKit.sql(sqlId, param);
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param 查询参数
     * @param list 用于接收预处理的值
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,String> param, LinkedList<Object> list){
		return SqlXmlKit.sql(sqlId, param, list);
	}
	
	/**
	 * 获取表映射对象
	 * @return 
	 */
	protected Table getTable(){
		if (table == null) {
			table = TableMapping.me().getTable(getClass());
		}
		return table;
	}
	
	/**
	 * 获取主键值
	 * @return
	 */
	public Long getPKValue(){
		if (primaryKey == null) {
			primaryKey = getTable().getPrimaryKey();
		}
		return this.getLong(primaryKey);
	}
	
	/**
	 * 获取表名
	 * @return
	 */
	public String getTableName() {
		if (tableName == null) {
			tableName = getTable().getName();
		}
		return tableName;
	}

	/**
	 * 获取model名称
	 * @return
	 */
	public String getModelName() {
		if (modelName == null) {
			byte[] items = getClass().getSimpleName().getBytes();
			items[0] = (byte) ((char) items[0] + ('a' - 'A'));
			modelName = new String(items);
		}
		return modelName;
	}
	
	/**
	 * 是否存在该属性
	 * @param column
	 * @return
	 */
	public boolean hasColumn(String column){
		return getTable().hasColumnLabel(column);
	}

	/**
	 * 查询语句SELECT
	 * @return
	 */
	public String getSelectSql() {
		if (selectSql == null) {
			selectSql = " SELECT `" + getModelName() + "`.* ";
		}
		return selectSql;
	}

	/**
	 * 查询语句FROM
	 * @return
	 */
	public String getFromSql() {
		if (fromSql == null) {
			fromSql = " FROM " + getTableName() + " `" + getModelName() + "` ";
		}
		return fromSql;
	}

	/**
	 * 更新语句UPDATE
	 * @return
	 */
	public String getUpdateSql() {
		if (updateSql == null) {
			updateSql = " UPDATE " + getTableName() + " `" + getModelName()
					+ "` ";
		}
		return updateSql;
	}

	/**
	 * 删除语句UPDATE 设置不可用
	 * @return
	 */
	public String getDeleteSql() {
		if (deleteSql == null) {
			deleteSql = " UPDATE " + getTableName() + " `" + getModelName()
					+ "` SET `" + getModelName() + "`.deleted_at='"
					+ new Date() + "' ";
		}
		return deleteSql;
	}

	/**
	 * 删除语句DELETE
	 * @return
	 */
	public String getDropSql() {
		if (dropSql == null) {
			dropSql = " DELETE FROM " + getTableName() + " ";
		}
		return dropSql;
	}

	/**
	 * 统计语句 SELECT COUNT(*)
	 * @return
	 */
	public String getCountSql() {
		if (countSql == null) {
			countSql = " SELECT COUNT(*) count FROM " + getTableName() + " `"
					+ getModelName() + "` ";
		}
		return countSql;
	}

	
	public String getNextSql(String where) {
		String nextSql = " WHERE `" + getModelName() + "`." + getPKValue()
				+ "=(SELECT MIN(`_" + getModelName() + "`." + getPKValue()
				+ ") FROM " + getTableName() + " `_" + getModelName() + "`"
				+ getWhere(where) + ")";

		return nextSql;
	}

	public String getPreviousSql(String where) {
		String previousSql = " WHERE `" + getModelName() + "`."
				+ getPKValue() + "=(SELECT MAX(`_" + getModelName() + "`."
				+ getPKValue() + ") FROM " + getTableName() + " `_"
				+ getModelName() + "`" + getWhere(where) + ")";
		return previousSql;
	}
	
	/**
	 * 重写save方法
	 */
	public boolean save(){
		// 是否需要乐观锁控制
		if(getTable().hasColumnLabel("version")){
			// 初始化乐观锁版本号
			this.set("version", Long.valueOf(0));
		}
		return super.save();
	}
	
	/**
	 * 重写update方法
	 */
	@SuppressWarnings("unchecked")
	public boolean update(){
		
		boolean hasVersion = hasColumn("version");
		
		// 是否需要乐观锁控制，表是否有version字段
		if(hasVersion){
			String name=getTableName();
			Long pk=getPKValue();
			
			// 1.数据是否还存在
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("table", name);
			param.put("pk", pk);
			String sql= SqlXmlKit.sql("basic.BaseModel.version", param);
			Model<M> modelOld = findFirst(sql , getPKValue());
			if(null == modelOld){ // 数据已经被删除
				throw new RuntimeException("数据库中此数据不存在，可能数据已经被删除，请刷新数据后在操作");
			}
			
			// 2.乐观锁控制
			Set<String> modifyFlag = null;
			try{
				Field field =  this.getClass().getSuperclass().getSuperclass().getDeclaredField("modifyFlag");
				//  由于属性modifyFlag 是private，所以要获得当前对象中当前Field的value就需要先进行该设置。
				field.setAccessible(true);
				Object object = field.get(this);
				if(null != object){
					modifyFlag = (Set<String>) object;
				}
				field.setAccessible(false);
			}catch (NoSuchFieldException | SecurityException e) {
				log.error("业务Model类必须继承BaseModel");
				e.printStackTrace();
				throw new RuntimeException("业务Model类必须继承BaseModel");
			}catch (IllegalArgumentException | IllegalAccessException e) {
				log.error("BaseModel访问modifyFlag异常");
				e.printStackTrace();
				throw new RuntimeException("BaseModel访问modifyFlag异常");
			}
			boolean versionModify = modifyFlag.contains("version"); // 表单是否包含version字段
			if(versionModify){
				Long versionDB = modelOld.getNumber("version").longValue(); // 数据库中的版本号
				Long versionForm = getNumber("version").longValue() + 1; // 表单中的版本号
				if(!(versionForm > versionDB)){
					throw new RuntimeException("表单数据版本号和数据库数据版本号不一致，可能数据已经被其他人修改，请重新编辑");
				}
			}
		}
		
		return super.update();
	}
	
	public void format(List<M> list,int length,String... paras){
		for(M model:list){
			for(String attr:paras){
				model.set(attr, StringUtils.abbreviate(model.getStr(attr), length));
			}
			
		}
	}
	
	/**
	 * 针对Oracle做特殊处理
	 */
	public Date getDate(String attr){
		Object obj = this.get(attr);
		if(null == obj){
			return null;
		}
		return (Date) obj;
	}
	
	/**
	 * 根据参数数量拼接sql语句
	 * @param paras
	 * @return
	 */
	public static String getParaNumber(Object... paras){
		int length=paras.length;
		StringBuffer sb=new StringBuffer();
		sb.append("(");
		for(int i=1;i<=length;i++){
			if(i!=length){
				sb.append("?,");
			}else{
				sb.append("?");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	public static String getParaNumber_1(Object... paras){
		int length=paras.length-1;
		StringBuffer sb=new StringBuffer();
		sb.append("(");
		for(int i=1;i<=length;i++){
			if(i!=length){
				sb.append("?,");
			}else{
				sb.append("?");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	
}
