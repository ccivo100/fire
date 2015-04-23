<div class="page-header">
<ul class="nav nav-tabs">
   <li ><a href="${ContextPath}/report/reports">故障申报列表</a></li>
   <li class="active"><a href="#">修改工单</a></li>
</ul>
</div>
<div class="row">
	<div class="col-xs-2">
	</div>
	<div class="col-xs-8">
	
	<input id="uuserid" type="hidden" name="uuserid" value="${(uuserid)!}">
	<input id="oorderid"  type="hidden" name="oorderid" value="${(oorderid)!}">
	<input id="otypeid" type="hidden" name="otypeid" value="${(otypeid)!}">
	<input id="olevelid" type="hidden" name="olevelid" value="${(olevelid)!}">
	
	<input id="ufullname" type="hidden" name="ufullname" value="${(ufullname)!}">
	<input id="uphone"  type="hidden" name="uphone" value="${(uphone)!}">
	<input id="bname" type="hidden" name="bname" value="${(bname)!}">
	<input id="aname" type="hidden" name="aname" value="${(aname)!}">
	<input id="pname" type="hidden" name="pname" value="${(pname)!}">
	
	<div class="form-group">
		<div >
			<h4><span class="label label-primary ">个人信息</span></h4>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-label">申报人员：</label>
		<div class="col-sm-2">
			<label class=" control-label"><span class="label label-info ">${(ufullname)!}</span></label>
		</div>
		<label class="col-sm-2 control-label">联系电话：</label>
		<div class="col-sm-2">
			<label class=" control-label"><span class="label label-info ">${(uphone)!}</span></label>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">单位：</label>
		<div class="col-sm-2">
			<label class=" control-label"><span class="label label-info ">${(bname)!}</span></label>
		</div>
		<label class="col-sm-2 control-label">部门：</label>
		<div class="col-sm-2">
			<label class=" control-label"><span class="label label-info ">${(aname)!}</span></label>
		</div>
		<label class="col-sm-2 control-label">职位：</label>
		<div class="col-sm-2">
			<label class=" control-label"><span class="label label-info ">${(pname)!}</span></label>
		</div>
	</div>
	
	<hr>
	<div class="form-group">
		<div >
			<h4><span class="label label-primary ">故障描述</span></h4>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" >标题：</label>
		<div class="col-sm-8">
			<input id="otitle" type="text" class="form-control" placeholder="故障工单标题" maxlength="30" name="otitle" value="${(otitle)!}" autofocus  required autocomplete="off"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">故障类型：</label>

		<div class="col-sm-3">
			<#if otypeid??>
				<select id="selectType" name="selectType" class="form-control" disabled required autofocus>
			<#else>
	
			<select id="selectType" name="selectType" class="form-control" required autofocus>
			
			</#if>
						<option value="" selected="selected">请选择故障类型</option>
				<#if otypeid??>
					<#list typeList as type>
						
						<option value="${type.id}"  
							<#if ((otypeid!'-1')?number)=type.id>
								selected="selected"
							</#if> >
							${type.name}
						</option>
					</#list>
				<#else>
					<#list typeList as type>
						<option value="${type.id}">${type.name}</option>
					</#list>
				</#if>
			
			</select>
		</div>

		<label class="col-sm-2 control-label">故障等级：</label>

		<div class="col-sm-3">
			<#if olevelid??>
				<select id="selectLevel" name="selectLevel" class="form-control" disabled required autofocus>
			<#else>
	
			<select id="selectLevel" name="selectLevel" class="form-control" required autofocus>
			
			</#if>
						<option value="" selected="selected">请选择故障等级</option>
				<#if olevelid??>
					<#list levelList as level>
						
						<option value="${level.id}"  
							<#if ((olevelid!'-1')?number)=level.id>
								selected="selected"
							</#if> >
							${level.name}
						</option>
					</#list>
				<#else>
					<#list levelList as level>
						<option value="${level.id}">${level.name}</option>
					</#list>
				</#if>
			
			</select>
		</div>
	</div>
	

	
	<div class="form-group">
		<label class="col-sm-2 control-label">故障描述：</label>
		<div class="col-sm-8">
			<textarea id="odescription" name="odescription" class="form-control"
				placeholder="故障描述，不少于25字。"  maxlength="250" cols="80" rows="7"
				required <#if odeletetime?exists>readonly</#if>>${(odescription)!}</textarea>
		</div>

	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-6">
			<#if descriptionMsg??>
			<div class="alert alert-danger" role="alert">
  				${descriptionMsg!}
			</div>
				
			</#if>
		</div>
	</div>


	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			
			<#if odeletetime?exists>
			<h3><span class="label label-danger">已撤回</span></h3>
			<#else>
			<button id="reportupdate" value="提交" type="submit" class="btn btn-primary save" data-loading-text="正在提交...">提交</button>
			<a id="recall" value="撤回"  class="btn btn-danger" href="${ContextPath}/report/recall?id=${(oorderid)!}" data-loading-text="正在撤回...">撤回</a>
			</#if>
			
		</div>
	</div>
	<div class="col-xs-2">
	</div>
</div>
</div>

<script>
$(document).ready(function(){
	$("#reportupdate1").click(function(){
		$.ajax({
			type:"post",
			url:"${ContextPath}/report/upup",
			data:{"orderid":$('#oorderid').val(),"title":$('#otitle').val(),"description":$('#odescription').val()},
			success:function(msg){
				alert(msg.state);
			}
		});
	});
});
</script>