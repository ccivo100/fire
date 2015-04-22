<#include "/page/common/_layout.html" />
<@layout activebar="task" html_title="任务分配">

<div class="index-background">
<div class="page-header">
<ul class="nav nav-tabs">
   <li class="active"><a href="#">任务分配</a></li>
</ul>
</div>
	<table class="table table-bordered table-hover">
		<thead>
			<tr>
				<th width="2%">序号</th>
				<th width="3%">单位</th>
				<th width="3%">部门</th>
				<th width="3%">姓名</th>
				<th width="15%">类型</th>
				<th width="4%">操作</th>
			</tr>
		</thead>

		<tbody>
			<#list userPage.getList() as user>
			<tr>
				<td>${user_index+1}</td>
				<td>${user.branch}</td>
				<td>${user.aname}</td>
				<td>${user.fullname}</td>
				
				<form class="form-horizontal" role="form" action="${ContextPath}/operate/doassign" method="post" >
				<input type="hidden" name="userid" value="${(user.userid)}">
				<td>
					<#list typeList as type>
						
						<input type="checkbox" id="inlineCheckbox1" name="types" value="${(type.id)!}"
							<#list user.remark as t>
								<#if t.id=type.id>
								checked 
								</#if>
							</#list>
						> ${(type.name)!}

					</#list>
				
				</td>
				<td>
					<a href="${ContextPath}/operate/assign?id=${user.userid}">分配</a>
					<button value="提交" type="submit" class="btn btn-primary btn-xs " data-loading-text="正在提交...">提交</button>
				</td>
				</form>
			</tr>
			</#list>
		</tbody>
		
		<tfoot>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
		</tfoot>
	</table>
	
	<#include "/page/common/_paginate.html" />
	<@paginate currentPage=userPage.pageNumber totalPage=userPage.totalPage actionUrl="/operate/task/" />
	
</div>
</@layout>