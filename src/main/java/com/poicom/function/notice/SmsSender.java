package com.poicom.function.notice;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poicom.basic.kit.StringKit;
import com.poicom.function.model.NoticeSms;

/**
 * 发送短信
 * @author FireTercel 2015年7月24日 
 *
 */
public class SmsSender implements Sender {
	
	public final static Logger log = LoggerFactory.getLogger(SmsSender.class);
	
	private String message;
	private String phone;
	private String[] phones;
	
	private int recode;
	
	private final static String department="研发部";
	private final static String name="firetercel";
	private final static String type="1";
	private final static String URL="http://sms.poicom.net/postsms.php";
	
	public String getMessage() {
		return message;
	}
	public SmsSender setMessage(String message) {
		this.message = message;
		return this;
	}
	public String getPhone() {
		return phone;
	}
	public SmsSender setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	public String[] getPhones() {
		return phones;
	}
	public SmsSender setPhones(String[] phones) {
		this.phones = phones;
		return this;
	}
	
	public int getRecode() {
		return recode;
	}
	public SmsSender setRecode(int recode) {
		this.recode = recode;
		return this;
	}
	@Override
	public void send() {
		int recode = sendSms(getMessage(),getPhones());
		this.setRecode(recode);
		new NoticeSms()
		.set("message", getMessage())
		.set("phones", StringKit.arrayToString(getPhones()))
		.set("recode", getRecode())
		.set("created_at", DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).save();
	}
	
	private static int sendSms(String msg,String... phone){
		log.debug("进入发送短信主方法");
		String phones = StringKit.arrayWithDot(phone);
		try{
			URL url=new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// 提交模式
			conn.setDoOutput(true);// 是否输入参数
			StringBuffer params = new StringBuffer();
			// 表单参数与get形式一样
	        params.append("phones").append("=").append(phones).append("&")
			              .append("department").append("=").append(department).append("&")
			              .append("name").append("=").append(name).append("&")
			              .append("type").append("=").append(type).append("&")
			              .append("content").append("=").append(msg);
	        byte[] bypes = params.toString().getBytes();
	        OutputStream outStream= conn.getOutputStream();
	        outStream.write(bypes);// 输入参数
	        outStream.flush();
	        outStream.close();
	        int result = conn.getResponseCode();
	        if(result == 200){
	        	log.debug("===  表单提交响应代码："+result+"  ===  发送成功  ===");
	        }else {
	        	log.debug("===  表单提交响应代码："+result+"  ===  发送失败  ===");
	        }
	        return result;
		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Test
	public void test(){
		String[] str = new String[]{"15900088260","18924518660","15574998546", "15815729271", "13715689614", "13590963226", "18688112112", "15602820055", "13415347240", "18207609337", "15089947635", "18676196968", "15917232704", "13424567023", "15017349221", "13824710793", "15973398234", "15602597861", "18919649659", "13257912939", "18975814016", "15873114629", "18983860002", "15905535145", "18856935949", "13767498841", "15256933378", "18356972754", "15905694927", "15979059418", "15070371822", "13755753113", "18870066595", "13767012721", "13870848533", "18898216057", "13635411032", "15973155581", "13755003972", "13874931300", "13973111883", "13667319698", "13786153705", "15367899085", "13807312394", "13203157423", "18256527157", "13527185129"};
		sendSms("尊敬的用户，话务部的章佳微于2015-08-05 19:11:12，提交关于“易百年手机不能拨打电话”的故障单。现由软件研发部的陈宇佳处理完毕，详情请登陆系统查看。",str);
	}

}
