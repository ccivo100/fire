<#include "/page/common/_layout.html" />
<@layout activebar="report" html_title="编辑用户">

<div class="index-background">
<div class="page-header">
<ul class="nav nav-tabs">
   <li ><a href="${ContextPath}/admin/user">用户管理</a></li>
   <li class="active"><a href="#">编辑用户</a></li>
</ul>
</div>
	<form class="form-horizontal" role="form" action="${ContextPath}/admin/doedituser" method="post" >
		<div class="row">
			<input type="hidden" name="userid" value="${(userinfo.uuserid)}">
			<div class="form-group">
				<label class="col-sm-2 control-label">姓名</label>
				<div class="col-sm-4">
					<input type="text" class="form-control"  name="offer_user" value="${(userinfo.ufullname)!}" readonly/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">单位</label>
				<div class="col-sm-4">
					<select id="selectType" name="selectBranch" class="form-control"  required autofocus>
						<option value="" selected="selected">请选择</option>
						<#list branchList as branch>
						<option value="${(branch.id)!}" <#if (userinfo.info_branchid)=branch.id>selected="selected"<#else></#if>>${(branch.name)!}	</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">角色类型</label>
				<div class="col-sm-4">
						<#list roleList as role>
						    <label class="checkbox inline">
						    <input type="checkbox" id="inlineCheckbox1" name="roles" value="${(role.id)!}"
							    <#list userrole as r>
							    	<#if r.roleid=role.id>
							    	 checked
							    	</#if>
							    </#list>
						    
						    > ${(role.name)!}
						    </label>
					    </#list>

				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button value="提交" type="submit" class="btn btn-primary save" data-loading-text="正在提交...">提交</button>
				</div>
			</div>
		</div>
		
		
	</form>
	
</div>
</@layout>