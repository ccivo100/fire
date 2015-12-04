package com.poicom.function.validator;

import org.apache.commons.lang3.ArrayUtils;

import cn.dreampie.ValidateKit;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.poicom.basic.kit.DateKit;
import com.poicom.basic.kit.ValiKit;
import com.poicom.function.model.Order;

public class ReportValidator extends Validator {

    @Override
    protected void validate(Controller c) {
        if(getActionKey().equals("/report/save")){
            String[] selectApartment, selectMail, selectSms;
            //  选择一级部门 multiple or not
            if(c.getParaValues("selectApartment")==null){
                selectApartment=c.getParaValues("selectApartment[]");
            }else{
                selectApartment=c.getParaValues("selectApartment");
            }
            selectMail = c.getParaValues("selectMail[]");    //  选择邮件接收者 multiple or not
            selectSms = c.getParaValues("selectSms[]");    //  选择短信接收者 multiple or not
            
            if(ValidateKit.isNullOrEmpty(c.getPara("order.title"))){
                addError("state","故障单标题不能为空！");
            }
            else if(Order.dao.findBy(" offer_user=? and title=? ", c.getPara("order.offer_user"),c.getPara("order.title").trim()).size()>0){
                addError("state","已存在该故障单，请重新输入！");
            }
            else if(c.getParaToLong("selectType")==-1){
                addError("state","请选择故障大类！");
            }
            else if(c.getParaToLong("order.type")==-1){
                addError("state","请选择故障小类！");
            }
            else if(ValidateKit.isNullOrEmpty(selectApartment[0])){
                addError("state","请选择运维部门！");
            }
            else if(selectApartment.length==1&&c.getParaToLong("selectChildApartment")==-1){
                addError("state","请选择运维组别！");
            }
            /*else if(ValidateKit.isNullOrEmpty(selectMail)){
                addError("state","请选择邮件接收人员！");
            }
            else if(ValidateKit.isNullOrEmpty(selectSms)){
                addError("state","请选择短信接收人员！");
            }*/
            else if(c.getParaToLong("order.level")==-1){
                addError("state","请选择故障等级！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.description"))){
                addError("state","故障单描述不能为空！");
            }
        }
        else if(getActionKey().equals("/report/saveByTemplate")){
            if(ValidateKit.isNullOrEmpty(c.getPara("order.title"))){
                addError("state","客户姓名不能为空！");
            }
            //else if(Order.dao.findBy(" offer_user=? and title=? ", c.getPara("order.offer_user"),c.getPara("order.title").trim()).size()>0){
            //    addError("state","已存在该故障单，请重新输入！");
            //}禁止同一账户下出现重复故障标题、现已改为客户姓名，因此不能启用改判断
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.client_num"))){
                addError("state","请填写客户编号！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.client_phone"))){
                addError("state","请填写终端手机号！");
            }
            else if(!ValiKit.isPhone(c.getPara("order.client_phone"))){
              addError("state","终端手机号格式不正确！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.accident_place"))){
                addError("state","请填写故障发生地！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.contact_name"))){
                addError("state","请填写联系人姓名！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.contact_phone"))){
                addError("state","请填写联系电话！");
            }
            else if(!ValiKit.isPhone(c.getPara("order.contact_phone"))){
              addError("state","联系电话格式不正确！");
            }            
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.product_type"))){
                addError("state","请填写产品类型！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.smart_phones"))){
                addError("state","请填写手机品牌！");
            }
            //else if(ValidateKit.isNullOrEmpty(c.getPara("order.otherss"))){
            //    addError("state","请填写其他！");
            //}
            //else if(ValidateKit.isNullOrEmpty(c.getPara("order.typess"))){
            //    addError("state","请选择故障总类！");
            //}
            else if(c.getPara("selecttotalType2")==""){
                addError("state","请选择故障总类！");
            }
            else if(c.getParaToLong("selectType")==-1){
                addError("state","请选择故障大类！");
            }
            else if(c.getParaToLong("childTypeid")==-1){
                addError("state","请选择故障小类！");
            }
            else if(c.getParaToLong("order.selectTemplate")==-1){
                addError("state","请选择模板！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.description"))){
                addError("state","故障单描述不能为空！");
            }
        }
        else if(getActionKey().equals("/report/update")){
            Order order=Order.dao.findById(c.getParaToInt("order.id"));
            if(order.getInt("status")==1){
                addError("state","失败：工单正在处理，无法修改。");
            }else if(order.getInt("status")==0){
                addError("state","失败：工单已处理，无法修改。");
            }else if(!ValidateKit.isNullOrEmpty(order.get("deleted_at"))){
                addError("state","失败：工单已撤销，无法修改。");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.title").trim())){
                addError("state","故障单标题不能为空！");
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("order.description").trim())){
                addError("state","故障单描述不能为空！");
            }
        }
        else if(getActionKey().equals("/report/updateanswer")){
            if(!ValidateKit.isDateTime(c.getPara("comment.add_at"))){
                addError("state","请选择处理时间！");
            }
            else if(!ValidateKit.isDateTime(c.getPara("comment.predict_at"))){
                addError("state","请选择预计完成时间！");
            }
            else if(!DateKit.dateBetween(c.getPara("comment.add_at"), c.getPara("comment.predict_at"))){
                addError("state","预计完成时间应该大于处理时间！");
            }
            else if(c.getParaToInt("progress")==-1){
                addError("state","请选择处理进度！");
            }
            else if(!valiProcess(c.getParaToInt("progress"))){
                addError("state","处理进度参数错误！");//防止人为修改参数。
            }
            else if(ValidateKit.isNullOrEmpty(c.getPara("comment.context").trim())){
                addError("state","处理意见不能为空！");
            }
        }
        else if(getActionKey().equals("/report/recall")){
            Order order=Order.dao.findById(c.getParaToInt("order.id"));
            if(order.getInt("status")!=2){
                addError("state","工单已处理，撤回失败！");
            }else if(!ValidateKit.isNullOrEmpty(order.get("deleted_at"))){
                addError("state","工单已撤回，请勿重复操作！");
            }
        }
    }

    @Override
    protected void handleError(Controller c) {
        if(getActionKey().equals("/report/save")){
            c.renderJson("state", c.getAttr("state"));
        }
        else if(getActionKey().equals("/report/saveByTemplate")){
            c.renderJson("state", c.getAttr("state"));
        }
        else if(getActionKey().equals("/report/update")){
            c.renderJson("state", c.getAttr("state"));
        }
        else if(getActionKey().equals("/report/updateanswer")){
            c.renderJson("state", c.getAttr("state"));
        }
        else if(getActionKey().equals("/report/recall")){
            c.renderJson("state", c.getAttr("state"));
        }
        
    }
    /**
     * 判断处理情况参数是否有异常
     * @param process
     * @return
     */
    public static boolean valiProcess(int process){
        int[] paras={-1,0,1,2,3,4,5,6};
        return ArrayUtils.contains(paras, process);
    }
}
