﻿<#include "/page/sign/_layoutsign.html"/>
<@layout activebar="signin" html_title="中山点通故障申报系统">
	<div class="header">
		<div class="row">
			<div class="col-xs-1">
				
			</div>
			<div class="col-xs-4">
				<div>
				<div><img src="${ContextPath}/res/img/logo.png"></div>
				
				</div>
			</div>
			<div class="col-xs-4">
				
			</div>
			<div class="col-xs-1">
			</div>
		</div>
	</div>
	<!-- 遮罩 -->
	<div class="header filter-opacity">
	</div>
	
	<!-- 底部 -->
	<div class="header filter-opacity buttom">
	</div>
	
	<div class="container">
	<div class="row">
		<div class="col-xs-6"></div>
		
		<div class="col-xs-12 col-sm-6">
			<!--登陆控件-->
			<div class="sign-div-top">
			<div id="loginbox">
				<form id="loginform" class="form-vertical"  method="post" action="${ContextPath}/signin">
					<h3 class="form-signin-heading  smaller lighter grey" >用户登陆</h3>
					
					<div class="input-group input-sign">
						<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
						<input  class="form-control form-height" type="text" name="username" value="${(username)!}" placeholder="用户名" required autofocus autocomplete="off">
					</div>
					
					<div class="input-group input-sign">
						<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						<input  class="form-control form-height" type="password" name="password" value="${(password)!}" placeholder="密码" required >
					</div>
					
				    <div class="input-group input-sign">
				    	<span class="input-group-addon"><i class="glyphicon glyphicon-repeat"></i></span>
				      	<input  type="text" name="captcha" value="" class="form-control captcha form-height" ng-minlength="4" ng-maxlength="4"
				             placeholder="验证码" required autocomplete="off" style="width:90px">
				        <a style="float:right;" href="javascript:ref_captcha()">
				    	<img id="captcha" class="captcha form-height" src="${ContextPath}/captcha?width=128&height=45&fontsize=30">
				      	</a>
				    </div>
				    <div class="input-group input-sign margin-buttom" > 
				    	<span class="font-size-span" style="float:right;height:35px;"> 
							<a href="${ContextPath}/retrieve">忘记密码?</a>
						</span>
					</div>
					
					<@shiro.isLoginFailure name="shiroLoginFailure">
					<div class="input-group input-sign">
				        <div class="alert-sign " style="background-image: none;">
				          <@resource.loginException name="shiroLoginFailure"/>
				        </div>
				    </div>
				    </@shiro.isLoginFailure>
				    <div class="form-actions form-line-split radius">
				    	<span class="pull-center">
							<button type="reset" class="btn btn-primary" style="width: 136px;"  value="重置"  />重 置</button>
						</span>
						<span class="pull-center">
							<button id="submit" type="submit" class="btn btn-primary" style="width: 136px;" value="登 录" data-loading-text="正在提交..." />登 录</button>
						</span>
				    </div>
				</form>
			</div>
			</div>
			
			<!--遮盖层-->
			<div class="sign-div">
				<div id="loginbox" class="loginbox-color">
					<form id="loginform" class="form-vertical"  >
						<h3 class="form-signin-heading" style="opacity: 0">登陆</h3>
						<div class="form-actions form-line-split radius form-line-split-color">
			    		</div>
					</form>
				</div>
			</div>
			
		</div>
	</div>
	</div>
	<!--替换背景-->
	
	
	<div id="pt-main" class="pt-perspective">
		<div class="pt-page pt-page-3">
			<div class="container">
			<div class="row">
				<div class="hidden-xs  col-sm-6"><img src="${ContextPath}/res/sign/slide1.png" class="sidder" alt=""></div>
				<div class="col-xs-6"></div>
			</div>
			</div>
		</div>
		<div class="pt-page pt-page-2">
			<div class="container">
			<div class="row">
				<div class="hidden-xs  col-sm-6"><img src="${ContextPath}/res/sign/slide2.png" class="sidder" alt=""></div>
				<div class="col-xs-6"></div>
			</div>
			</div>
		</div>
		<div class="pt-page pt-page-1">
			<div class="container">
			<div class="row">
				<div class="hidden-xs  col-sm-6"><img src="${ContextPath}/res/sign/slide5.png" class="sidder" alt=""></div>
				<div class="col-xs-6"></div>
			</div>
			</div>
		</div>
		<div class="pt-page pt-page-4">
			<div class="container">
			<div class="row">
				<div class="hidden-xs  col-sm-6"><img src="${ContextPath}/res/sign/slide4.png" class="sidder" alt=""></div>
				<div class="col-xs-6"></div>
			</div>
			</div>
		</div>
		
	</div>
		

	<div class="pt-message">
		<p>亲，你的浏览器不支持 CSS 动画，请使用 Chrome,Firefox,Safari 等浏览器浏览.</p>
	</div>
	<script src="${ContextPath}/plugin/page-transitions/js/jquery.dlmenu.js"></script>
	<script src="${ContextPath}/plugin/page-transitions/js/pagetransitions.js"></script>


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