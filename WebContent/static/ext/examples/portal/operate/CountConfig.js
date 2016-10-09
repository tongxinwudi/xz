// 基金配置导入
Ext.onReady(function() {
	Ext.tip.QuickTipManager.init();
	var fundDataType = new Ext.data.SimpleStore({  
        fields : ['key', 'value'],  
        data : [['1','货基'],['0','非货基']]  
    }); 
	Ext.define('App.ImportWindow', {
		extend : 'Ext.window.Window',
		constructor : function(config) {
			config = config || {};
			var scope = this;
			Ext.apply(config, {
				title : '数据导入',
				width : 500,
				height : 190,
				bodyPadding : '10 5',
				modal : true,
				layout : 'fit',
				items : [ {
					xtype : 'form',
					fieldDefaults : {
						labelAlign : 'left',
						labelWidth : 70,
						anchor : '100%'
					},
					items : [ {
						xtype : 'fileuploadfield',
						fieldLabel : '选择文件',
						afterLabelTextTpl : '<span style="color:#FF0000;">*</span>',
						buttonText : '请选择...',
						name : 'importedFile',
						emptyText : '请选择Excel文件',
						blankText : 'Excel文件不能为空',
						allowBlank : false,
						listeners : {
							change : function(view, value, eOpts) {
								scope.checkImportFile(view, value);
							}
						}
					}, {
						columnWidth : 1,
						xtype : 'fieldset',
						title : '导入须知',
						layout : {
							type : 'table',
							columns : 1
						},
						collapsible : false,// 是否可折叠
						defaultType : 'label',// 默认的Form表单组件
						items : [ {
							html : '1、导入文件大小不超过2MB.'
						}, {
							//html : '2、支持Microsoft Office Excel的xls和xlsx文件,模板<a href="' + appBaseUri + '/operate/config/downloadMoneyFundRateFile")>点此下载</a>。非货基模板<a href="' + appBaseUri + '/operate/config/downloadNotMoneyFundRateFile")>点此下载</a>。'
							html : '2、支持Microsoft Office Excel的xls和xlsx文件,模板<a href="' + appBaseUri + '/operate/config/downloadMoneyFundRateFile")>点此下载</a>。'
						} ]
					} ],
					buttons : [ '->', {
						text : '导入',
						iconCls : 'icon-excel',
						handler : function(btn) {
							scope.importForestryFile(btn);
						}
					}, {
						text : '取消',
						iconCls : 'icon-cancel',
						handler : function(btn) {
							btn.up('window').close();
						}
					}, '->' ]
				} ]
			});
			App.ImportWindow.superclass.constructor.call(this, config);
		},
		checkImportFile : function(fileObj, fileName) {
			var scope = this;
			if (!(scope.getFileType(scope.getFileSuffix(fileName)))) {
				globalObject.errTip('导入文件类型有误！');
				fileObj.reset();// 清空上传内容
				return;
			}
		},
		getFileType : function(suffix) {
			var typestr = 'xls,xlsx';
			var types = typestr.split(',');
			for (var i = 0; i < types.length; i++) {
				if (suffix == types[i]) {
					return true;
				}
			}
			return false;
		},
		getFileSuffix : function(fileName) {
			var suffix = '';// 后缀
			var index = fileName.lastIndexOf('.');// 文件名称中最后一个.的位置
			if (index != -1) {
				suffix = fileName.substr(index + 1).toLowerCase();// 后缀转成小写
			}
			return suffix;
		},
		importForestryFile : function(btn) {
			var windowObj = btn.up('window');// 获取Window对象
			var formObj = btn.up('form');// 获取Form对象
			if (formObj.isValid()) { // 验证Form表单
				formObj.form.doAction('submit', {
					url : appBaseUri + '/operate/config/importFundRate',
					method : 'POST',
					submitEmptyText : false,
					waitMsg : '正在导入文件,请稍候...',
					timeout : 60000, // 60s
					success : function(response, options) {
						var result = options.result;
						if (!result.success) {
							globalObject.errTip(result.msg);
							return;
						}
						globalObject.infoTip(result.msg);
						// var url = result.data;
						windowObj.close();// 关闭窗体
						Ext.getCmp('countconfiggrid').getStore().reload();
					},
					failure : function(response, options) {
						globalObject.errTip(options.result.msg);
					}
				});
			}
		}
	});

	Ext.define('App.FundRateImportWindow', {
		extend : 'Ext.window.Window',
		constructor : function(config) {
			config = config || {};
			Ext.apply(config, {
				title : '基金结算配置信息',
				width : 450,
				height : 250,
				bodyPadding : '10 20',
				modal : true,
				layout : 'fit',
				items : [ {
					xtype : 'form',
					fieldDefaults : {
						labelAlign : 'right',
						labelWidth : 150,
						anchor : '100%'
					},
					items : [ {
						name : "cmd",
						xtype : "hidden",
						value : 'new'
					}, {
						xtype : 'hiddenfield',
						name : 'id'
					}, {
						xtype : 'combobox',
						fieldLabel : '基金类型',
						name : 'type',
						store : fundDataType,
						valueField : 'key',
						displayField : 'value',
						//typeAhead : true,
						queryMode : 'local',
						allowBlank : false,
						editable : false,
						listeners : {
							select : function(combo, record, index) {
								if(combo.getValue()=="0"){
									Ext.getCmp("importwindow-sellrate").setVisible(false);
								}else{
									Ext.getCmp("importwindow-sellrate").setVisible(true);
								}
							}
						}
					}, {
						xtype : 'textfield',
						name : 'fdcode',
						fieldLabel : '基金代码',
						maxLength : 100,
						allowBlank : false
					}, {
						xtype : 'textfield',
						name : 'name',
						fieldLabel : '基金名称',
						maxLength : 200,
						allowBlank : false
					}, {
						xtype : 'numberfield',
						name : 'sellrate',
						id : 'importwindow-sellrate',
						fieldLabel : '销售服务费率（%）',
						allowBlank : true,
						decimalPrecision : 2,//精确到小数点后两位
						//allowDecimals : true,//允许输入小数
						nanText :'请输入有效的整数',//无效数字提示
						maxValue : 100,//最大值
						minValue : 0,
						emptyText:'请输入百分比'						
					}, {
						xtype : 'numberfield',
						name : 'managerate',
						fieldLabel : '管理费分成比例（%）',
						allowBlank : true,
						decimalPrecision : 4,//精确到小数点后两位
						//allowDecimals : true,//允许输入小数
						nanText :'请输入有效的整数',//无效数字提示
						maxValue : 100,//最大值
						minValue : 0,
						emptyText:'请输入百分比'
					}],
					buttons : [ '->', {
						id : 'countconfigwindow-save',
						text : '保存',
						iconCls : 'icon-save',
						width : 80,
						handler : function(btn, eventObj) {
							var window = btn.up('window');
							var form = window.down('form').getForm();
							if (form.isValid()) {
								window.getEl().mask('数据保存中，请稍候...');
								var vals = form.getValues();
								Ext.Ajax.request({
									url : appBaseUri + '/operate/config/saveFundRate',
									params : {
										cmd : vals['cmd'],
										id : vals['id'],
										fdcode : vals['fdcode'],
										name : vals['name'],
										sellrate : vals['sellrate'],
										managerate : vals['managerate'],
										type : vals['type']
									},
									method : "POST",
									success : function(response) {
										// var result = eval("(" + response.responseText + ")");
										globalObject.msgTip('添加成功！');
										Ext.getCmp('countconfiggrid').getStore().reload();
									},
									failure : function(response) {
										globalObject.errTip('添加失败！');
									}
								});
								window.getEl().unmask();
								window.close();
							}
						}
					}, {
						id : 'countconfigwindow-cancel',
						text : '取消',
						iconCls : 'icon-cancel',
						width : 80,
						handler : function() {
							this.up('window').close();
						}
					}, {
						id : 'countconfigwindow-accept',
						text : '确定',
						hidden : true,
						iconCls : 'icon-accept',
						width : 80,
						handler : function() {
							this.up('window').close();
						}
					}, '->' ]
				} ]
			});
			App.FundRateImportWindow.superclass.constructor.call(this, config);
		}
	});
	

	Ext.define('Forestry.app.operate.CountConfig', {
		extend : 'Ext.panel.Panel',
		initComponent : function() {
			var me = this;
			Ext.apply(this, {
				layout : 'border',
				items : [ Ext.create('Forestry.app.operate.CountConfig.FundRate', {
					cButtons : me.cButtons,
					cName : me.cName
				}),Ext.create('Forestry.app.operate.CountConfig.RateConfig', {
					cButtons : me.cButtons,
					cName : me.cName
				})]
			});
			this.callParent(arguments);
		}
	});
	Ext.define('Forestry.app.operate.CountConfig.FundRate',{
		extend : 'Ext.ux.custom.GlobalGridPanel',
		region : 'center',
		initComponent : function() {
			var scope = this;

			Ext.define('ModelList', {
				extend : 'Ext.data.Model',
				idProperty : 'id',
				fields : [ {
					name : 'id',
					type : 'long'
				}, 'fdcode', 'name', {
					name : 'sellrate',
					type : 'float'
				}, {
					name : 'managerate',
					type : 'float'
				},'type']
			});

			var store = scope.createStore({
				modelName : 'ModelList',
				
				proxyUrl : appBaseUri + '/operate/config/getFundRate',
				proxyDeleteUrl : appBaseUri + '/operate/config/deleteFundRate',
				//proxyExportUrl : appBaseUri + '/operate/config/exportFundRate',
				extraParams : scope.extraParams,
				sorters : [ {
					property : 'id',
					direction : 'ASC'
				} ]
			});

			var columns = [ {
				text : "ID",
				dataIndex : 'id',
				hidden : true
			}, {
				text : "基金代码",
				dataIndex : 'fdcode',
				width : '10%',
				sortable : false
			}, {
				text : "基金名称",
				dataIndex : 'name',
				width : '35%'
			}, {
				text : "销售服务费率",
				dataIndex : 'sellrate',
				width : '15%',
				renderer : function(data){
					if(data!=null&&data>0)
						return Ext.util.Format.number(data, '0.00') + '%';
				}
			}, {
				text : "管理费分成比例",
				dataIndex : 'managerate',
				width : '18%',
				renderer : function(data){
					if(data!=null&&data>0)
						return Ext.util.Format.number(data, '0.00') + '%';
				}
			}, {
				text : '基金类型',
				dataIndex : 'type',
				width : '10%',
				renderer : function(data){
					if(data=="0")
						return "非货基";
					else
						return "货基";
				}
			}, {
				text : '操作',
				xtype : 'actioncolumn',
				width : '15%',
				items : [{
					iconCls : 'icon-view',
					tooltip : '修改',
					//disabled : globalObject.haveActionMenu(scope.cButtons, 'Edit'),
					handler : function(grid, rowIndex, colIndex) {
						var gridRecord = grid.getStore().getAt(rowIndex);
						var type=gridRecord.get("type");
						var win = new App.FundRateImportWindow({
							hidden : true
						});
						var form = win.down('form').getForm();
						form.loadRecord(gridRecord);
						form.findField("cmd").setValue("edit");
						form.findField('type').setVisible(false);
						form.findField('fdcode').setReadOnly(true);
						form.findField('name').setReadOnly(false);
						form.findField('sellrate').setReadOnly(false);
						form.findField('managerate').setReadOnly(false);
						if(type=="0"){
							form.findField('sellrate').setVisible(false);
						}else{
							form.findField('sellrate').setVisible(true);
						}
						win.show();
					}
				}]
			} ];
			
			Ext.apply(this, {
				id : 'countconfiggrid',
				store : store,
				columns : columns,
				dockedItems : [{
				    xtype: 'toolbar',
				    dock: 'top',
				    items : [ {
						xtype : 'textfield',
						id : 'fund-fdcode',
						name : 'fdcode',
						fieldLabel : '基金代码',
						labelWidth : 60,
						width : '20%'
					},  {
						xtype : 'textfield',
						id : 'fund-name',
						name : 'name',
						fieldLabel : '基金名称',
						labelWidth : 60,
						width : '25%'
					}, {
						xtype : 'combobox',
						fieldLabel : '基金类型',
						id : 'fund-type',
						name : 'type',
						store : fundDataType,
						valueField : 'key',
						displayField : 'value',
						typeAhead : true,
						queryMode : 'local',
						emptyText : '请选择...',
						editable : false,
						labelWidth : 60,
						width : '30%',
						maxWidth : 150
					}, {
						xtype : 'button',
						text : '查询',
						iconCls : 'icon-search',
						//disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
						width : '10%',
						maxWidth : 60,
						handler : function(btn, eventObj) {
							var searchParams = {
								fdcode : Ext.getCmp('fund-fdcode').getValue(),
								name : Ext.getCmp('fund-name').getValue(),
								type: Ext.getCmp('fund-type').getValue()
							};
							Ext.apply(store.proxy.extraParams, searchParams);
							store.reload();
						}
					}, {
						xtype : 'button',
						text : '重置',
						iconCls : 'icon-reset',
						width : '10%',
						maxWidth : 60,
						handler : function(btn, eventObj) {
							Ext.getCmp('fund-fdcode').setValue(null);
							Ext.getCmp('fund-name').setValue(null);
							Ext.getCmp('fund-type').setValue(null);
							 
						}
					}]
				}],
			});

			store.loadPage(1);

			this.callParent(arguments);
		},
		
		
		onAddClick : function() {
			new App.FundRateImportWindow().show();
		},
		onImportClick : function() {
			new App.ImportWindow().show();
		},
		onViewClick : function() {
			var grid = Ext.getCmp("countconfiggrid");
			var id = grid.getSelectionModel().getSelection()[0].get('id');
			var gridRecord = grid.getStore().findRecord('id', id);
			var win = new App.FundRateImportWindow({
				hidden : true
			});
			var form = win.down('form').getForm();
			form.loadRecord(gridRecord);
			form.findField('fdcode').setReadOnly(true);
			form.findField('name').setReadOnly(true);
			form.findField('sellrate').setReadOnly(true);
			form.findField('managerate').setReadOnly(true);
			Ext.getCmp('countconfigwindow-save').hide();
			Ext.getCmp('countconfigwindow-cancel').hide();
			Ext.getCmp('countconfigwindow-accept').show();
			win.show();
		}
	});
	
	Ext.define('Forestry.app.operate.CountConfig.RateConfig',{
		extend : 'Ext.ux.custom.GlobalGridPanelFund',
		region : 'west',
		width : '30%',
		split: true,
		initComponent : function() {
			var scope = this;
			
			Ext.define('ModelList', {
				extend : 'Ext.data.Model',
				idProperty : 'id',
				fields : [ {
					name : 'id',
					type : 'long'
				}, 'title', {
					name : 'rate',
					type : 'float'
				}]
			});

			var store = scope.createStore({
				modelName : 'ModelList',
				
				proxyUrl : appBaseUri + '/operate/config/getAllRateConfig',
				proxyDeleteUrl : appBaseUri + '/operate/config/deleteRateConfig',
				//proxyExportUrl : appBaseUri + '/operate/config/exportFundRate',
				extraParams : scope.extraParams
			});

			var columns = [ {
				text : "ID",
				width : '13%',
				dataIndex : 'id',
				hidden : false,
			}, {
				text : "费率",
				dataIndex : 'title',
				width : '35%',
				sortable : false
			}, {
				text : "默认数值",
				dataIndex : 'rate',
				width : '25%'
			}, {
				text : "操作",
				sortable : false,
				xtype : 'actioncolumn',
				width : '15%',
				items : [{
					iconCls : 'icon-view',
					tooltip : '修改',
					handler : function(grid, rowIndex, colIndex) {
						var gridRecord = grid.getStore().getAt(rowIndex);
						var win = new App.AddRateConfigWindow({
							hidden : true
						});
						var form = win.down('form').getForm();
						form.loadRecord(gridRecord);
						form.findField("cmd").setValue("edit");
						form.findField('title').setReadOnly(false);
						form.findField('rate').setReadOnly(false);
						win.show();
					}
				}]
			} ];
			

			Ext.apply(this, {
				id : 'rateconfiggrid',
				store : store,
				columns : columns,
			});
			
			store.loadPage(1);

			this.callParent(arguments);
		},
		onAddClick : function() {
			new App.AddRateConfigWindow().show();
		},
		onImportClick : function() {
			new App.AddRateConfigWindow().show();
		},
		onViewClick : function() {
			var grid = Ext.getCmp("rateconfiggrid");
			var id = grid.getSelectionModel().getSelection()[0].get('id');
			var gridRecord = grid.getStore().findRecord('id', id);
			var win = new App.AddRateConfigWindow({
				hidden : true
			});
			var form = win.down('form').getForm();
			form.loadRecord(gridRecord);
			form.findField('title').setReadOnly(true);
			form.findField('rate').setReadOnly(true);
			Ext.getCmp('rateconfigwindow-save').hide();
			Ext.getCmp('rateconfigwindow-cancel').hide();
			Ext.getCmp('rateconfigwindow-accept').show();
			win.show();
		}
	});
	
	Ext.define('App.AddRateConfigWindow', {
		extend : 'Ext.window.Window',
		constructor : function(config) {
			config = config || {};
			Ext.apply(config, {
				title : '费率默认配置信息',
				width : 350,
				height : 250,
				bodyPadding : '10 20',
				modal : true,
				layout : 'fit',
				items : [ {
					xtype : 'form',
					fieldDefaults : {
						labelAlign : 'right',
						labelWidth : 80,
						anchor : '100%'
					},
					items : [ {
						name : "cmd",
						xtype : "hidden",
						value : 'new'
					}, {
						xtype : 'textfield',
						name : 'id',
						fieldLabel : 'id',
						maxLength : 10,
						allowBlank : false
					}, {
						xtype : 'textfield',
						name : 'title',
						fieldLabel : '费率',
						maxLength : 100,
						allowBlank : false
					}, {
						xtype : 'numberfield',
						name : 'rate',
						fieldLabel : '默认数值',
						maxLength : 200,
						allowBlank : false,
						decimalPrecision : 2,//精确到小数点后两位
						//allowDecimals : true,//允许输入小数
						nanText :'请输入有效的整数',//无效数字提示
						maxValue : 100,//最大值
						minValue : 0,
						emptyText:'请输入百分比'
					}],
					buttons : [ '->', {
						id : 'rateconfigwindow-save',
						text : '保存',
						iconCls : 'icon-save',
						width : 80,
						handler : function(btn, eventObj) {
							var window = btn.up('window');
							var form = window.down('form').getForm();
							if (form.isValid()) {
								window.getEl().mask('数据保存中，请稍候...');
								var vals = form.getValues();
								Ext.Ajax.request({
									url : appBaseUri + '/operate/config/saveRateConfig',
									params : {
										cmd : vals['cmd'],
										id : vals['id'],
										title : vals['title'],
										rate : vals['rate']
									},
									method : "POST",
									success : function(response) {
										// var result = eval("(" + response.responseText + ")");
										globalObject.msgTip('添加成功！');
										Ext.getCmp('rateconfiggrid').getStore().reload();
									},
									failure : function(response) {
										globalObject.errTip('添加失败！');
									}
								});
								window.getEl().unmask();
								window.close();
							}
						}
					}, {
						id : 'rateconfigwindow-cancel',
						text : '取消',
						iconCls : 'icon-cancel',
						width : 80,
						handler : function() {
							this.up('window').close();
						}
					}, {
						id : 'rateconfigwindow-accept',
						text : '确定',
						hidden : true,
						iconCls : 'icon-accept',
						width : 80,
						handler : function() {
							this.up('window').close();
						}
					}, '->' ]
				} ]
			});
			App.AddRateConfigWindow.superclass.constructor.call(this, config);
		}
	});
});