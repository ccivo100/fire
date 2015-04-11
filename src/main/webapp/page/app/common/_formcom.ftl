<div class="page-header">
<ul class="nav nav-tabs">
   <li><a href="${ContextPath}${(back)!}">返回</a></li>
   <li class="active"><a href="#">查看工单</a></li>
</ul>
</div>
<div class="row" >
	<div class="col-sm-6 col-md-6 col-lg-6">
	<div >
		<h3><span class="label label-primary ">故障描述</span></h3>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">故障类型</label>
		<div class="col-sm-6">
		<select id="selectType" name="selectType" class="form-control"  disabled>
			<option value="-1" >请选择</option>
		<#list typeList as type>
			<option value="${type.id}"  <#if otypeid=type.id>selected="selected"</#if> >${type.name}</option>
		</#list>
	
		</select>
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-2 control-label">申报人</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="offer_user" value="${(uoffername)!}" readonly/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">联系电话</label>
		<div class="col-sm-8">
			<input type="text" class="form-control" name="offer_phone" value="${(uofferphone)!}" readonly />
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
			<input type="text" class="form-control"  name="offer_time" value="${(ooffertime)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">故障描述</label>
		<div class="col-sm-8">
			<textarea name="description"  class="form-control"  cols="80" rows="7" readonly>${(odescription)!}</textarea>
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
			<input type="text" class="form-control"  name="offer_user" value="<#if ostatus=0>已处理<#elseif ostatus=1>未处理<#elseif ostatus=2>超时未处理</#if>" readonly/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理人</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="deal_user" value="${(udealname)!}" readonly />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">联系电话</label>
		<div class="col-sm-8">
			<input type="text" class="form-control"  name="phone" value="${(udealphone)!}" readonly>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">所属单位</label>
		<div class="col-sm-8">
			<input type="text"  class="form-control" name="offer_branch" value="${(deal_branch)!}" readonly>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理时间</label>
		<div class="col-sm-8">
			<input type="text"   class="form-control" name="deal_time" value="${(odealtime)!}" readonly>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">处理意见</label>
		<div class="col-sm-8">
			<textarea name="commen"  class="form-control"   cols="80" rows="7"  readonly >${(ocomment)!}</textarea>
		</div>
	</div>
	</div>
</div>