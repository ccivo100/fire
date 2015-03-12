define(
		[ 'app', 'bootstrapvalidator.zh_CN', 'user.model', 'order.model' ],
		function() {
			'use strict';
			return $(function() {
				$.fn.bootstrapValidator.DEFAULT_OPTIONS = $.extend({},
						$.fn.bootstrapValidator.DEFAULT_OPTIONS, {
							excluded : [ ':disabled' ],
							feedbackIcons : {
								valid : 'glyphicon glyphicon-ok',
								invalid : 'glyphicon glyphicon-remove',
								validating : 'glyphicon glyphicon-refresh'
							}
						});
				return App.Service.MemberSrv = {
					branch : function() {
						var branches;
						branches = null;
						return App.Model.Branch
								.all(function(data) {
									var brhselect, regselect;
									if (data.branches) {
										branches = data.branches;
										regselect = $("select[name='region_id']");
										brhselect = $("select[name='branch_id']");
										regselect
												.change(function() {
													var branch, val, _i, _len;
													val = $(this).val() * 1;
													App.Service.ConfigSrv
															.multiselect()
															.clean(
																	"select[name='branch_id']");
													for (
															_i = 0,
															_len = branches.length; _i < _len; _i++) {
														branch = branches[_i];
														if (branch.region_id === val) {
															brhselect
																	.find(
																			"option[value='']")
																	.before(
																			"<option value='"
																					+ branch.id
																					+ "'>"
																					+ branch.name
																					+ "</option>");
														}
													}
													if (brhselect
															.attr("select")
															&& brhselect
																	.attr("select") !== "") {
														return App.Service.ConfigSrv
																.multiselect()
																.reselect(
																		brhselect,
																		brhselect
																				.attr("select"));
													} else {
														return App.Service.ConfigSrv
																.multiselect()
																.rebuild(
																		"select[name='branch_id']");
													}
												});
										if (regselect.attr("select")
												&& regselect.attr("select") !== "") {
											return App.Service.ConfigSrv
													.multiselect()
													.reselect(
															regselect,
															regselect
																	.attr("select"));
										}
									}
								});
					},
					"delete" : function() {
						var modal;
						return modal = App.Service.ConfigSrv
								.confirmModal(function(t) {
									return App.Model.User.control({
										'user.id' : $("a.delete")
												.attr("userid"),
										'do' : 'delete'
									}, function(data) {
										var td;
										if (data.state === 'success') {
											if (data.user) {
												$("a.update").attr("deletedat",
														data.user.deleted_at);
												td = t.parent();
												td.siblings()
														.eq(td.index() - 1)
														.text("已删除");
												modal.modal("hide");
												return $("a.delete").remove();
											}
										}
									});
								});
					},
					control : function(todo, form, modal, btn) {
						var conf;
						conf = {
							submitButtons : btn,
							fields : {
								'region_id' : {
									validators : {
										notEmpty : {
											message : '请选择区域'
										}
									}
								},
								'branch_id' : {
									validators : {
										notEmpty : {
											message : '请选择支行'
										}
									}
								},
								'user.last_name' : {
									validators : {
										notEmpty : {}
									}
								},
								'user.phone' : {
									validators : {
										notEmpty : {},
										phone : {
											country : 'CN'
										}
									}
								},
								'user.username' : {
									validators : {
										notEmpty : {},
										regexp : {
											regexp : /^\w{5,18}$/i,
											message : '请输入5至18个包含下划线,字母,数字的字符'
										}
									}
								},
								'user.password' : {
									validators : {
										notEmpty : {},
										regexp : {
											regexp : /^\w{5,18}$/i,
											message : '请输入5至18个包含下划线,字母,数字的字符'
										}
									}
								},
								'repassword' : {
									validators : {
										notEmpty : {},
										identical : {
											field : 'user.password',
											message : '确认秘密不正确'
										}
									}
								}
							}
						};
						if (todo === 'update') {
							conf = {
								submitButtons : btn,
								fields : {
									'user.phone' : {
										validators : {
											phone : {
												country : 'CN'
											}
										}
									},
									'user.username' : {
										validators : {
											regexp : {
												regexp : /^(\w{5,18})?$/i,
												message : '请输入5至18个包含下划线,字母,数字的字符'
											}
										}
									},
									'user.password' : {
										validators : {
											regexp : {
												regexp : /^(\w{5,18})?$/i,
												message : '请输入5至18个包含下划线,字母,数字的字符'
											}
										}
									},
									'repassword' : {
										validators : {
											identical : {
												field : 'user.password',
												message : '确认秘密不正确'
											}
										}
									}
								}
							};
						}
						$(form)
								.bootstrapValidator(conf)
								.on(
										'success.form.bv',
										function(e) {
											e.preventDefault();
											App.Model.User
													.control(
															$(form).serialize(),
															function(data) {
																if (data.state === 'success') {
																	$(modal)
																			.modal(
																					"hide");
																	$(form)
																			.bootstrapValidator(
																					'resetForm',
																					true);
																	return location
																			.reload(true);
																} else {
																	if (data.user_usernameMsg) {
																		return $(
																				form)
																				.find(
																						"span.error")
																				.html(
																						data.user_usernameMsg);
																	}
																}
															});
											return false;
										});
						$(btn).click(function() {
							return $(form).bootstrapValidator('validate');
						});
						$(form).find('input[name="user.username"]').change(
								function() {
									return $(form).find("span.error").html("");
								});
						if (todo === 'update') {
							return $(modal)
									.on(
											'show.bs.modal',
											function(e) {
												var brhselect, regselect;
												btn = $(e.relatedTarget);
												$(this)
														.find(
																"input[name='user.id']")
														.val(btn.attr("userid"));
												regselect = $(this)
														.find(
																"select[name='region_id']");
												brhselect = $(this)
														.find(
																"select[name='branch_id']");
												App.Service.ConfigSrv
														.multiselect()
														.reselect(
																regselect,
																btn
																		.attr("regionid"));
												App.Service.ConfigSrv
														.multiselect()
														.reselect(
																brhselect,
																btn
																		.attr("branchid"));
												$(this)
														.find(
																"input[name='user.first_name']")
														.val(
																btn
																		.attr("firstname"));
												$(this)
														.find(
																"input[name='user.last_name']")
														.val(
																btn
																		.attr("lastname"));
												$(this)
														.find(
																"input[name='user.phone']")
														.val(btn.attr("phone"));
												$(this)
														.find(
																"input[name='user.username']")
														.val(
																btn
																		.attr("username"));
												if (btn.attr("deletedat") !== "") {
													$(this)
															.find(
																	"div.deleted_at span")
															.html(
																	btn
																			.attr("deletedat"));
													return $(this).find(
															"div.deleted_at")
															.show();
												} else {
													return $(this).find(
															"div.deleted_at")
															.hide();
												}
											});
						}
					}
				};
			});
		});
