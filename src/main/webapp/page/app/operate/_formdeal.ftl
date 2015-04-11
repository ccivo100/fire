<div class="page-header">
<ul class="nav nav-tabs">
   <li><a href="${ContextPath}/operate/deal">故障列表</a></li>
   <li class="active"><a href="#">处理工单</a></li>
</ul>
</div>
<div class="row" >
	<input type="hidden" name="oorderid" value="${(oorderid)!}">
	<input type="hidden" name="uuserid" value="${(uuserid)!}">
	<input type="hidden" name="otypeid" value="${(otypeid)!}">
	<div class="col-sm-6 col-md-6 col-lg-6">
	<div >
		<h3><span class="label label-primary ">故障描述</span></h3>
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-label">故障类型</label>
		<div class="col-sm-6">
		<select id="selectType" name="selectType" class="form-control"   disabled>
			<option value="-1" >请选择</option>
			<#if otypeid??>
				<#list typeList as type>
					<option value="${type.id}"  <#if (otypeid?number)=type.id>selected="selected"</#if> >${type.name}</option>
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
		<label class="col-sm-2 control-label">申报人</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="uoffername" value="${(uoffername)!}" readonly/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">联系电话</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="uofferphone" value="${(uofferphone)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">所属单位</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="offer_branch" value="${(offer_branch)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">申报时间</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="ooffertime" value="${(ooffertime)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">故障描述</label>
		<div class="col-sm-8">
			<textarea name="odescription"  class="form-control"  cols="80" rows="7" readonly>${(odescription)!}</textarea>
		</div>
	</div>
	</div> 	
	
	<div class="col-sm-6 col-md-6 col-lg-6">
	<div >
		<h3><span class="label label-primary ">处理意见</span></h3>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">工单状态</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="ostatus" 
			value="<#if (ostatus?number)=0>已处理<#elseif (ostatus?number)=1>未处理<#elseif (ostatus?number)=2>超时未处理</#if>" readonly/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理人</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="ufullname" value="${(ufullname)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">联系电话</label>
		<div class="col-sm-8">
			<input type="text"  class="form-control"  name="uphone" value="${(uphone)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">所属单位</label>
		<div class="col-sm-8">
			<input type="text"   class="form-control" name="bname" value="${(bname)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理时间</label>
		<div class="col-sm-8">
			<input type="text"  class="form-control"  name="deal_time" value="${(.now)!}" readonly/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理意见</label>
		<div class="col-sm-8">
				<textarea id="area" name="ocomment" class="form-control"
					placeholder="处理意见，不少于25字。" maxlength="250" cols="80" rows="7"
					required autofocus>${(ocomment)!}</textarea>
			</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-6">
			<#if commentMsg??>
			<div class="alert alert-danger" role="alert">
  				${commentMsg!}
			</div>
				
			</#if>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<button id="submit" value="提交" type="submit"  class="btn btn-primary save"  data-loading-text="正在提交..."
                    data-complete-text="提交成功!">
                    提交
            </button>
			<button value="重置" type="reset" class="btn btn-default ">
				重置
			</button>
		</div>
		
	</div>

	</div>
</div>


