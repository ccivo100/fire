	<input type="hidden" name="permission.id" value="${(permission.id)!}" />
	<input type="hidden" name="pid" value="${(pid)!}" />

	<div class="form-group">
		<label class="col-sm-2 control-label">权限名称</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" name="permission.name" value="${(permission.name)!}" placeholder="名称"  required autocomplete="off"/>
		</div>
		<div class="col-sm-4">
			[#if nameMsg??]
			<div class="index-alert index-alert-danger" style="background-image:none;">
				${(nameMsg)!}
			</div>
			[/#if]
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">权限代码</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" name="permission.value" value="${(permission.value)!}" placeholder="如：P_**" required autocomplete="off" />
		</div>
		<div class="col-sm-4">
			[#if valueMsg??]
			<div class="index-alert index-alert-danger" style="background-image:none;">
				${(valueMsg)!}
			</div>
			[/#if]
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">URL</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" name="permission.url" value="${(permission.url)!}" placeholder="如：/admin/**" required autocomplete="off" />
		</div>
		<div class="col-sm-4">
			[#if urlMsg??]
			<div class="index-alert index-alert-danger" style="background-image:none;">
				${(urlMsg)!}
			</div>
			[/#if]
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">父节点</label>
		<div class="col-sm-4">
			[#if pid??]
			<input type="text" class="form-control" name="permission.pid" value="${pid}" readonly  required/>
			[#else]
			<input type="text" class="form-control" name="permission.pid" [#if permission?? ] value="${(permission.pid)!}" [#else]value="0" readonly[/#if] [#if (permission.pid)?? && permission.pid=0]readonly[/#if]  required/>
			[/#if]
		</div>
		<div class="col-sm-4">
			[#if pidMsg??]
			<div class="index-alert index-alert-danger" style="background-image:none;">
				${(pidMsg)!}
			</div>
			[/#if]
		</div>
	</div>


	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<button id="submit" value="提交" type="submit" class="btn btn-primary save" data-loading-text="正在提交...">提交</button>
		</div>
	</div>
