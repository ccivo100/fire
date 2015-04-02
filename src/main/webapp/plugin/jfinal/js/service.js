$(document).ready(function(){
  $("button#hide").click(function(){
    $("#test").hide(1000);
  });
  $("button#show").click(function(){
	 $("#test").show(1000); 
  });
  $("button#slow").click(function(){
	 $("#test").show("slow"); 
  });
  $("button#fast").click(function(){
	 $("#test").hide("fast"); 
  });  
});

/*!使用.attr("属性名称")可以获得该属性的值 */
$(document).ready(function(){
	$("a#update").click(function(){
		alert($(this).attr("userid"));
		alert($("select#sex").val());
		alert($("input[name='check']:checked").length);
		if($("input[name='check']:checked").length>0){
			var allCheckValue="";
			$("input[name='check']:checked").each(function(){
				allCheckValue+=$(this).val()+" ";
			});
			alert("value:"+allCheckValue);
		}
	});
});

$(document).ready(function(){
	$('button[data-loading-text]').click(function(){
		var btn=$(this).button('loading');
		setTimeout(function(){
			btn.button('reset');
		},3000);
	});
});

function trim(str){ 
    return str.replace(/(^\s*)|(\s*$)/g, "");
}



$(document).ready(function(){
	$('li.dropdown').mouseenter(function(){
		$(this).attr("class","dropdown open");
	});
	$('li.dropdown').mouseleave(function(){
		$(this).attr("class","dropdown");
	});
});

function getnums(){
	$('.popover-toggle').popover('toggle');
}
$(document).ready(function(){  
	setTimeout(getnums, 500);
	setTimeout(getnums, 3500);  
}); 

/*$(document).ready(function(){
	$("a.delete").click(function(){
		$('#confirmModal').modal('show').on('shown.bs.modal',function(){
			$("input#userid").attr('value','/report/print?id='+$("a.delete").attr("userid"));
			$(".btn-primary").attr('href','/report/print?id='+$("a.delete").attr("userid"));
		});
		
	});
	
});*/