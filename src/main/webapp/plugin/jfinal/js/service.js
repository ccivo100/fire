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
	$("a.delete").click(function(){
		$('#confirmModal').modal('show').on('shown.bs.modal',function(){
			$("input#userid").attr('value','/report/print?id='+$("a.delete").attr("userid"));
			$(".btn-primary").attr('href','/report/print?id='+$("a.delete").attr("userid"));
		});
		
	});
	
});