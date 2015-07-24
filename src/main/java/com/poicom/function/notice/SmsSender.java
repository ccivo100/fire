package com.poicom.function.notice;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.joda.time.DateTime;
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
	        }
	        return result;
		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
