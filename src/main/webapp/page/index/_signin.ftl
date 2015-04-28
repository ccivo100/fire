<#include "/page/common/_layoutsign.html"/>
<@layout activebar="signin" html_title="登录">

<div class="div-center" style=' height:105px;line-height:105px;overflow:hidden;'></div>
<div class="row">
	
	<div class="col-xs-7">
		<div class="jumbotron-sign">
			<div id="myCarousel" class="carousel slide">
			   <!-- 轮播（Carousel）指标 -->
			   <ol class="carousel-indicators">
			      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			      <li data-target="#myCarousel" data-slide-to="1"></li>
			      <li data-target="#myCarousel" data-slide-to="2"></li>
			   </ol>   
			   <!-- 轮播（Carousel）项目 -->
			   <div class="carousel-inner">
			      <div class="item active">
			         <img src="${ContextPath}/res/background/main-bg1.jpg" alt="First slide">
			      </div>
			      <div class="item">
			         <img src="${ContextPath}/res/background/main-bg2.jpg" alt="Second slide">
			      </div>
			      <div class="item">
			         <img src="${ContextPath}/res/background/main-bg3.jpg" alt="Third slide">
			      </div>
			   </div>
			   <!-- 轮播（Carousel）导航 -->
			   <a class="carousel-control left" href="#myCarousel" 
			      data-slide="prev">&lsaquo;</a>
			   <a class="carousel-control right" href="#myCarousel" 
			      data-slide="next">&rsaquo;</a>
			</div> 
		</div>
	</div>
	<div class="col-xs-1">
	</div>
	<div class="col-xs-4">
		<div class="jumbotron-sign">
	
			<form class="form-signin" role="form" method="post" action="${ContextPath}/signin">
				<h2 class="form-signin-heading">请登录</h2>
				<!-- <input class="form-control" type="email" placeholder="用户名" required autofocus> -->
				<input class="form-control" type="text" name="username" value="${(username)!}" placeholder="用户名" required autofocus autocomplete="off">
				<input class="form-control" type="password" name="password" value="${(password)!}" placeholder="密码" required>
				<div>
			    <label >验证码:</label>
			
			    <div >
			      <input type="text" name="captcha" value="" class="form-control captcha" ng-minlength="4" ng-maxlength="4"
			             placeholder="验证码" required autocomplete="off">
			      <img id="captcha" class="captcha" src="${ContextPath}/captcha?width=128&height=45&fontsize=30&time=${.now?time}">
			      <a href="javascript:ref_captcha()">验证码看不清</a>
			    </div>
			  	</div>
				<div class="checkbox">
					<label>
						<input type="checkbox" name="rememberMe" <#if rememberMe??><#if rememberMe="1">checked value="1"</#if></#if>>记住我
					</label>
					<label style="float:right;"><a href="${ContextPath}/retrieve">忘记密码?</a></label>
				</div>
				<@shiro.isLoginFailure name="shiroLoginFailure">
			        <div class="alert alert-danger alert-dismissable" style="background-image: none;">
			          <@resource.loginException name="shiroLoginFailure"/>
			        </div>
			    </@shiro.isLoginFailure>
				<button class="btn btn-lg btn-primary btn-block" data-loading-text="正在提交..." type="submit">登录</button>
			</form>
	
		</div>
	</div>
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
	var url="/captcha?width=128&height=45&fontsize=30&time="+timestamp;
	func_get(url,function(){
		if(xmlhttp.readyState==4&&xmlhttp.status==200){
			var captchaText=xmlhttp.responseText;
			document.getElementById("captcha").src=url;
		}
	
	});

}

</script>


</@layout>