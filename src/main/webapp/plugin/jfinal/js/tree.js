//Bootstrap 的tree功能。
$(function() {
	$('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title',
			'Collapse this branch');
	$('.tree li.parent_li > span').on(
			'click',
			function(e) {
				var children = $(this).parent('li.parent_li')
						.find(' > ul > li');
				if (children.is(":visible")) {
					children.hide('fast');
					$(this).attr('title', 'Expand this branch').find(' > i')
							.addClass('icon-plus-sign').removeClass(
									'icon-minus-sign');
				} else {
					children.show('fast');
					$(this).attr('title', 'Collapse this branch').find(' > i')
							.addClass('icon-minus-sign').removeClass(
									'icon-plus-sign');
				}
				e.stopPropagation();
			});
});

/* admin/role中的权限管理 */

/**
 * service.js原文件
 */
$(document).ready(function() {
	$("button#hide").click(function() {
		$("#test").hide(1000);
	});
	$("button#show").click(function() {
		$("#test").show(1000);
	});
	$("button#slow").click(function() {
		$("#test").show("slow");
	});
	$("button#fast").click(function() {
		$("#test").hide("fast");
	});
});

/* !使用.attr("属性名称")可以获得该属性的值 */
$(document).ready(function() {
	$("a#update").click(function() {
		alert($(this).attr("userid"));
		alert($("select#sex").val());
		alert($("input[name='check']:checked").length);
		if ($("input[name='check']:checked").length > 0) {
			var allCheckValue = "";
			$("input[name='check']:checked").each(function() {
				allCheckValue += $(this).val() + " ";
			});
			alert("value:" + allCheckValue);
		}
	});
});

/** 点击提交按钮动画*/
$(document).ready(function() {
	$('button[data-loading-text]').click(function() {
		var btn = $(this).button('loading');
		setTimeout(function() {
			btn.button('reset');
		}, 2000);
	});
	
});

/**
 * 字符处理
 * @param str
 * @return
 */
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 下拉菜单效果
 */
$(document).ready(function() {
	$('li.dropdown').mouseenter(function() {
		$(this).attr("class", "dropdown open");
	});
	$('li.dropdown').mouseleave(function() {
		$(this).attr("class", "dropdown");
	});
});

/**
 * 登陆框透明效果
 */
/*$(document).ready(function(){
	$('.sign-div-top').mouseenter(function(){
		$('.sign-div').css({"opacity":"1","filter":"alpha(opacity=100)"});
	});
	$('.sign-div-top').mouseleave(function(){
		$('.sign-div').css({"opacity":"0.3","filter":"alpha(opacity=30)"});
	});
	
});*/

/**
 * 提示牌 点击申报。
 * @return
 */
function getnums() {
	$('.popover-toggle').popover('toggle');
}
$(document).ready(function() {
	setTimeout(getnums, 500);
	setTimeout(getnums, 3500);
});


/** 用于获得角色-权限等。（测试用）*/
function gettree(result) {
	$.each(result.permissionList, function(i, item) {
		$("#myDiv3").append(
				"<div>" + item.id + "</div>" + "<div>" + item.name + "</div>"
						+ "<div>" + item.value + "</div><hr/>");

	});
}
function getper(result) {
	$.each(result.permissions, function(i, item) {
		$("#per").append(
				"<div>" + item.id + "</div>" + "<div>" + item.name + "</div>"
						+ "<div>" + item.value + "</div><hr/>");

	});
}

$(function() {
	$("#ajaxgetBtn").click(function() {
		$("#myDiv3").html("");
		$.ajax( {
			type : "get",
			url : "/bootstrap/ajaxget?param=1",
			dataType : "json",
			success : function(result) {
				gettree(result);
			}
		});
	});
	$("#ajaxrole").click(function() {
		$("#per").html("");
		$.ajax( {
			type : "post",
			url : "/admin/ajaxgetper",
			data : {
				roleid : $(this).attr("roleid")
			},
			dataType : "json",
			success : function(result) {
				getper(result);
			}
		});
	});
	$("#ajaxpostBtn").click(function() {
		$("#ajaxresult").html("");
		$.ajax( {
			type : "post",
			url : "/param/ajaxpost",
			data : {
				param1 : 1,
				param2 : 2
			},
			dataType : "json",
			success : function(result) {
				$("#ajaxresult").html(result.msg);
			}
		});
	});
});

/** 提交建议使用AJAX （不再使用） */
$(function() {
	$("#submit").click(function() {
		$.ajax( {
			type : "post",
			url : "/user/contactMe",
			data : {
				name : $("#cname").val(),
				phone : $("#cphone").val(),
				context : $("#ccontext").val()
			},
			dataType : "json",
			success : function(result) {
				alert(result.state);
				return;
			}
		});
	});
});

/**
 * 测试方法
 */
$(function() {
	$("#hasten1").click(function() {

		$.ajax( {
			type : "post",
			url : "/report/hasten",
			data : {
				id : $("#cname").val(),
				phone : $("#cphone").val(),
				context : $("#ccontext").val()
			},
			dataType : "json",
			success : function(result) {
				alert(result.state + 2);
				return;
			},
			error : function(result) {
				alert(result.state + 1);
				$('#contactMe').on('hiden.bs.modal', function() {
					alert(result.contactMsg);
				});
				return;
			}
		});

	});

});

/**
 * 时间格式化，将时间转为 xx之前
 * @param datetime
 * @return
 */
function formatDateTime(datetime) {
    //datetime = datetime.replace("-", "/");
    var current_date = new Date().getTime();
    var _date = datetime.split(" ")[0];
    var _time = datetime.split(" ")[1];
    var date = new Date();
    date.setFullYear(_date.split("-")[0]);
    date.setMonth(_date.split("-")[1] - 1);
    date.setDate(_date.split("-")[2]);
    date.setHours(_time.split(":")[0]);
    date.setMinutes(_time.split(":")[1]);
    date.setSeconds(_time.split(":")[2]);
    var mul = current_date - date.getTime();
    var time = parseInt(mul / 1000);
    if (time < 60) {
        return "刚刚";
    } else if (time < 3600) {
        return parseInt(time / 60) + " 分钟前";
    } else if (time < 86400) {
        return parseInt(time / 3600) + " 小时前";
    } else if (time < 604800) {
        return parseInt(time / 86400) + " 天前";
    } else if (time < 2419200) {
        return parseInt(time / 604800) + " 周前";
    } else if (time < 31536000) {
        return parseInt(time / 2592000) + " 个月前";
    } else {
        return parseInt(time / 31536000) + " 年前";
    }
    return datetime;
}

/*
 * $(document).ready(function(){ $("a.delete").click(function(){
 * $('#confirmModal').modal('show').on('shown.bs.modal',function(){
 * $("input#userid").attr('value','/report/print?id='+$("a.delete").attr("userid"));
 * $(".btn-primary").attr('href','/report/print?id='+$("a.delete").attr("userid"));
 * });
 * 
 * });
 * 
 * });
 */