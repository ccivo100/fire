<fieldset class="solid" >
	
	<div>
		<label>故障类型</label><br>
		<#if order??>
		<select id="selectType" name="selectType" disabled>
		<#else>
		<select id="selectType" name="selectType" >
		</#if>
			<option value="-1" selected="selected">请选择</option>
			<#if order??>
				<#list typeList as type>
					<option value="${type.id}"  <#if order.typeid=type.id>selected="selected"</#if> >${type.name}</option>
				</#list>
			<#else>
				<#list typeList as type>
					<option value="${type.id}">${type.name}</option>
				</#list>
			</#if>
		
		</select>
		
	</div>
	<input type="hidden" name="userid" value="${(userinfo.userid)!}">
	<input type="hidden" name="orderid" value="${(order.orderid)!}">
	<div>
		<label>申报人</label><br>
		<input type="text" name="offer_user" value="${(userinfo.fullname)!}" readonly />${usernameMsg!}
	</div>
	<div>
		<label>联系电话</label><br>
		<input type="text" name="phone" value="${(userinfo.phone)!}" readonly />${phoneMsg!}
	</div>
		<div>
		<label>所属单位</label><br>
		<input type="text" name="branchid" value="${(userinfo.branch)!}" readonly />${phoneMsg!}
	</div>
	<div>
		<label>故障描述</label><br>
		<textarea name="description"  cols="80" rows="7" >${(order.description)!}</textarea>${descriptionMsg!}
	</div>

	<div>
		<label>&nbsp;</label>
		<input value="提交" type="submit">
	</div>
</fieldset>