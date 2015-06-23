<div >
	<input type="hidden" name="role.id" value="${(role.id)!}" />
	<input type="hidden" name="pid" value="${(pid)!}" />

	<div class="form-group">
		<label class="col-sm-2 control-label">角色名称</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" name="role.name" value="${(role.name)!}" placeholder="名称" required autocomplete="off"/>
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
		<label class="col-sm-2 control-label">角色代码</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" name="role.value" value="${(role.value)!}" placeholder="如：R_**" required autocomplete="off"/>
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
		<label class="col-sm-2 control-label">父节点</label>
		<div class="col-sm-4">
			[#if pid??]
			<input type="text" class="form-control" name="role.pid" value="${pid}" readonly  required />
			[#else]
			<input type="text" class="form-control" name="role.pid" [#if role?? ] value="${(role.pid)!}" [#else]value="0" readonly[/#if] [#if (role.pid)?? && role.pid=0]readonly[/#if]  required/>
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
</div>
