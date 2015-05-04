﻿<#include "/page/back/common/_layoutadmin.html"/>
<@layout indexbar='admin-role' activebar="admin-role" html_title="权限分配">
<div class="tree">
<form class="form-horizontal" role="form" action="${ContextPath}/admin/doassignrole" method="post" >
	<div class="row">
			<input type="hidden" name="roleid" value="${(role.id)}">
			<div class="form-group">
				<label class="col-sm-2 control-label">角色名称</label>
				<div class="col-sm-4">
					<input type="text" class="form-control"  name="role.name" value="${(role.name)!}" readonly/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">权限类型</label>
				<div class="col-sm-4">
					<ul>
						<#list permissions as permission>
						<li>
						    <label >
						    <span>
						    <i></i> <input type="checkbox" id="inlineCheckbox1" name="pers" value="${(permission.id)!}" disabled
							    <#list userpermission as p>
							    	<#if p=permission.id>
							    	 checked
							    	</#if>
							    </#list>
						    
						    > ${(permission.name)!}
						    </span>
						    </label>
						    <ul>
						    <#list permission.children as pchild>
						    	<li>
						    	<label >
						    	<span>
						    		<i></i><input type="checkbox" id="inlineCheckbox1" name="pers" value="${(pchild.id)!}"
						    			<#list userpermission as p>
						    				<#if p=pchild.id>
						    				 checked 
						    				</#if>
						    			</#list>
						    		> ${(pchild.name)!}
						    	</span>
						    	</label>
						    	</li>
						    </#list>
						    </ul>
						    </li>
					    </#list>
					</ul>
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