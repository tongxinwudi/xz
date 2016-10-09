//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.InvestFail', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.Fail1', {
				cButtons : me.cButtons,
				cName : me.cName
			}) ]
		});
		this.callParent(arguments);
	}
});

 
 

Ext.define('Forestry.app.operate.Fail1', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'center',
	height : '100%',
	initComponent : function() {
		var me = this;

		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'date',
			fields : [ 'date','uid','name','mobile','fund','invest','bank','reason'  ]
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/operate/getFailList',
			 	//proxyExportUrl : appBaseUri + '/sys/forestry/exportForestry',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'date',
				direction : 'DESC'
			} ]
		});

		var columns = [ {
			text : "日期",
			dataIndex : 'date',
			width : '18%'
		}, {
			text : "UID",
			dataIndex : 'uid',
			width : '12%'
		}, {
			text : "姓名",
			dataIndex : 'name',
			width : '10%'
		} , {
			text : "手机",
			dataIndex : 'mobile',
			width : '10%',
			sortable : true
		} ,{
			text : "申购产品",
			dataIndex : 'fund',
			width : '20%',
			sortable : true
		} ,{
			text : "绑卡银行",
			dataIndex : 'bank',
			width : '10%',
			sortable : true
		},{
			text : "失败原因",
			dataIndex : 'reason',
			width : '30%',
			sortable : true
		}   ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [{
				xtype : 'textfield',
				id : 'query-name_investFail_investFail',
				name : 'uid',
				fieldLabel : 'UID',
				labelWidth : 20,
				width : '10%'
			}, {
				xtype : 'textfield',
				id : 'query-name_investFail',
				name : 'name',
				fieldLabel : '姓名',
				labelWidth : 30,
				width : '10%'
			}, {
				xtype : 'textfield',
				id : 'query-mobile_investFail',
				name : 'mobile',
				fieldLabel : '手机号',
				labelWidth : 60,
				width : '15%'
			}, {
	            xtype: 'datefield',
	            id : 'query-start_investFail',
	            name: 'start',
	            fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_investFail',
	            name: 'end',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date( new Date() ,"Y-m-d")  
	        }, {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						uid :      Ext.getCmp('query-name_investFail_investFail').getValue()   ,
						name :      Ext.getCmp('query-name_investFail').getValue()   ,
						mobile :   Ext.getCmp('query-mobile_investFail').getValue()   ,
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_investFail').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investFail').getValue() ),'Y-m-d') 
						 
					};
					 Ext.apply(forestryquerystore.proxy.extraParams, searchParams);
					 forestryquerystore.reload();
				}
			}, {
				xtype : 'button',
				text : '重置',
				iconCls : 'icon-reset',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					Ext.getCmp('query-start_investFail').setValue(null);
					Ext.getCmp('query-end_investFail').setValue(null);
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
						uid  =      Ext.getCmp('query-name_investFail_investFail').getValue()   ,
					    name =    Ext.getCmp('query-name_investFail').getValue()   ,
					    mobile = Ext.getCmp('query-mobile_investFail').getValue()   ,
						sdate =  Ext.util.Format.date(new Date( Ext.getCmp('query-start_investFail').getValue() ),'Y-m-d') ,
						edate = Ext.util.Format.date(new Date( Ext.getCmp('query-end_investFail').getValue() ),'Y-m-d')
						 
					 
					 window.location.href = appBaseUri + '/sys/operate/exportFailList?&limit=100000&sdate=' +sdate +'&edate='+edate +'&uid='+uid +'&name='+name +'&mobile='+mobile;
					//Ext.apply(forestryquerystore.proxyExportUrl.extraParams, searchParams);
					// graphDataStore.reload();
				}
			}]
		});

		Ext.apply(this, {
			store : forestryquerystore,
			columns : columns,
			tbar : ttoolbar,
			bbar : Ext.create('Ext.PagingToolbar', {
				store : forestryquerystore,
				displayInfo : true
			}),
			listeners : {
				itemdblclick : function(dataview, record, item, index, e) {
					var grid = this;
					var id = grid.getSelectionModel().getSelection()[0].get('id');
					var gridRecord = grid.getStore().findRecord('id', id);
					var win = new App.ForestryQueryWindow({
						hidden : true
					});
					var form = win.down('form').getForm();
					form.loadRecord(gridRecord);
					form.findField('date').setReadOnly(true);
					form.findField('investTotalPersonNum').setReadOnly(true);
					form.findField('investTotalNum').setReadOnly(true);
					form.findField('investTotalMoney').setReadOnly(true);
					form.findField('investFailPersonNum').setReadOnly(true);
					form.findField('investFailNum').setReadOnly(true);
					form.findField('investFailMoney').setReadOnly(true);
					form.findField('investSuccessPersonNum').setReadOnly(true);
					form.findField('investSuccessNum').setReadOnly(true);
					form.findField('investSuccessMoney').setReadOnly(true);
					form.findField('investPartSuccessNum').setReadOnly(true);
					form.findField('investPartSuccessMoney').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});