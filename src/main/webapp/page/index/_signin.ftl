<#include "/page/common/_layout.html"/>
<@layout activebar="signin" html_title="登录">
<div class="jumbotron">

<form class="form-signin" role="form" method="post" action="/signin">
	<h2 class="form-signin-heading">请登录</h2>
	<!-- <input class="form-control" type="email" placeholder="用户名" required autofocus> -->
	<input class="form-control" type="text" name="username" value="${(username)!}" placeholder="用户名" required autofocus autocomplete="off">
	<input class="form-control" type="password" name="password" value="${(password)!}" placeholder="密码" required>
	<div>
    <label >验证码:</label>

    <div >
      <input type="text" name="captcha" value="" class="form-control captcha" ng-minlength="4" ng-maxlength="4"
             placeholder="验证码" required autocomplete="off">
      <img id="captcha" class="captcha" src="/captcha?width=128&height=45&fontsize=30&time=${.now?time}">
      <a href="javascript:ref_captcha()">验证码看不清</a>
    </div>
  	</div>
	<div class="checkbox">
		<label>
			<input type="checkbox" value="remember-me">记住我
		</label>
	</div>
	<@shiro.isLoginFailure name="shiroLoginFailure">
        <div class="alert alert-danger" style="background-image: none;">
          <@shiro.loginException name="shiroLoginFailure"/>
        </div>
    </@shiro.isLoginFailure>
	<button class="btn btn-lg btn-primary btn-block" data-loading-text="正在提交..." type="submit">Sign in</button>
</form>
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
</div>



</@layout>