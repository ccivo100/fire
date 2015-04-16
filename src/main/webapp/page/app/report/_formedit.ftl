<div class="page-header">
<ul class="nav nav-tabs">
   <li ><a href="${ContextPath}/report/query">故障申报列表</a></li>
   <li class="active"><a href="#">修改工单</a></li>
</ul>
</div>
<div >
	<input type="hidden" name="uuserid" value="${(uuserid)!}">
	<input type="hidden" name="oorderid" value="${(oorderid)!}">
	<input type="hidden" name="otypeid" value="${(otypeid)!}">
	<input type="hidden" name="olevelid" value="${(olevelid)!}">
	
	
	<div class="form-group">
		<label class="col-sm-2 control-label">故障类型</label>

		<div class="col-sm-4">
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
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-label">故障等级</label>

		<div class="col-sm-4">
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
		<label class="col-sm-2 control-label">申报人</label>
		<div class="col-sm-6">
			<input type="text" class="form-control" name="ufullname" value="${(ufullname)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">联系电话</label>
		<div class="col-sm-6">
			<input type="text" class="form-control" name="uphone" value="${(uphone)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">所属单位</label>
		<div class="col-sm-6">
			<input type="text" class="form-control" name="bname" value="${(bname)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">故障描述</label>
		<div class="col-sm-6">
			<textarea id="area" name="odescription" class="form-control"
				placeholder="故障描述，不少于25字。"  maxlength="250" cols="80" rows="7"
				required>${(odescription)!}</textarea>
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
			<button id="submit" value="提交" type="submit" class="btn btn-primary save" data-loading-text="正在提交...">提交</button>
			<#if odeletetime?exists>
			
			<#else>
			<button id="recall" value="撤回"  class="btn btn-success" data-loading-text="正在撤回...">撤回</button>
			</#if>
			
		</div>
	</div>
</div>
