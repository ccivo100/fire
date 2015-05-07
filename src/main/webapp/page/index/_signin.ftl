﻿<#include "/page/common/_layoutsign.html"/>
<@layout activebar="signin" html_title="欢迎登陆点通科技故障申报系统">

<div id="loginbox">
	<form id="loginform" class="form-vertical"  method="post" action="${ContextPath}/signin">
		<h3 class="form-signin-heading">欢迎您登录系统</h3>
		<div class="input-group input-sign">
			<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
			<input class="form-control" type="text" name="username" value="${(username)!}" placeholder="用户名" required autofocus autocomplete="off">
		</div>
		
		<div class="input-group input-sign">
			<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
			<input class="form-control" type="password" name="password" value="${(password)!}" placeholder="密码" required >
		</div>
		
	    <div class="input-group input-sign">
	    	<span class="input-group-addon"><i class="glyphicon glyphicon-repeat"></i></span>
	      	<input type="text" name="captcha" value="" class="form-control captcha" ng-minlength="4" ng-maxlength="4"
	             placeholder="验证码" required autocomplete="off" style="width:80px">
	        <a style="float:right;" href="javascript:ref_captcha()">
	    	<img id="captcha" style="height:34px" class="captcha" src="${ContextPath}/captcha?width=128&height=45&fontsize=30&time=${.now?time}">
	      	</a>
	    </div>
	    <div class="input-group input-sign" >
	    	<span class="font-size-span" style="float:right;height:15px;"> 
				<a href="${ContextPath}/retrieve">忘记密码?</a>
			</span>
		</div>
		
		<@shiro.isLoginFailure name="shiroLoginFailure">
		<div class="input-group input-sign">
	        <div class="alert-sign" style="background-image: none;">
	          <@resource.loginException name="shiroLoginFailure"/>
	        </div>
	    </div>
	    </@shiro.isLoginFailure>
	    <div class="form-actions form-line-split">
	    	<span class="pull-center">
				<input type="reset" class="btn btn-primary" style="width: 116px;"  value="重置"  />
			</span>
			<span class="pull-center">
				<input type="submit" class="btn btn-primary" style="width: 116px;" value="登 录" data-loading-text="正在提交..." />
			</span>
	    </div>
	</form>
</div>

<script>
var xmlhttp;
function func_get(url,cfunc){
	/* 创建 XMLHttpRequest*/
	xmlhttp=createXHR();
	xmlhttp.onreadystatechange=cfunc;
	xmlhttp.open("GET",url,true);
	xmlhttp.send();
}
/* 判断IE7+, Firefox, Chrome, Opera, Safari或者是IE6, IE5 */
function createXHR(){
	if(window.XMLHttpRequest){
		return new XMLHttpRequest();
	}else{
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function ref_captcha(){
	var timestamp = new Date().getTime();
	var url="${ContextPath}/captcha?width=128&height=45&fontsize=30&time="+timestamp;
	func_get(url,function(){
		if(xmlhttp.readyState==4&&xmlhttp.status==200){
			var captchaText=xmlhttp.responseText;
			document.getElementById("captcha").src=url;
		}
	
	});

}

</script>


</@layout>