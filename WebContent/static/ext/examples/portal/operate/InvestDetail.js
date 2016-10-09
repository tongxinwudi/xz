//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.InvestDetail', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.detail', {
				cButtons : me.cButtons,
				cName : me.cName
			}) ]
		});
		this.callParent(arguments);
	}
});

 
 

Ext.define('Forestry.app.operate.detail', {
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
			//idProperty : 'date',
			 fields:[
			         {name:'uid',type:'string'},
			         {name:'name',type:'String'},
			         {name:'reg_time',type:'String'},
			         {name:'boundT',type:'string'},
			         {name:'first_time',type:'string'},
			         {name:'first_money',type:'String'},
			         {name:'first_stype',type:'String'},
			         {name:'first_platform',type:'String'},
			         {name:'first_channel',type:'string'},
			         {name:'second_time',type:'string'},
			         {name:'second_money',type:'String'},
			         {name:'second_stype',type:'String'}			    
			     ]
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/operate/getTradeUserList',
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
				property : 'reg_time',
				direction : 'DESC'
			} ]
		});

		var columns = [ {
			text : "UID",
			dataIndex : 'uid',
			width : '20%'
		}, {
			text : "姓名",
			dataIndex : 'name',
			width : '10%'
		}, {
			text : "注册时间",
			dataIndex : 'reg_time',
			width : '10%'
		} , {
			text : "绑卡时间",
			dataIndex : 'boundT',
			width : '20%'
		}, {
			text : "首次投资时间",
			dataIndex : 'first_time',
			width : '20%',
			sortable : true
		} ,{
			text : "首次投资金额",
			dataIndex : 'first_money',
			width : '10%',
			sortable : true
		} ,{
			text : "首次投资购买产品名称",
			dataIndex : 'first_stype',
			width : '10%',
			sortable : true
		} ,{
			text : "首次投资平台",
			dataIndex : 'first_platform',
			width : '10%',
			sortable : true
		},{
			text : "首次投资渠道",
			dataIndex : 'first_channel',
			width : '10%',
			sortable : true
		},{
			text : "二次投资时间",
			dataIndex : 'second_time',
			width : '20%',
			sortable : true
		},{
			text : "二次投资金额",
			dataIndex : 'second_money',
			width : '10%',
			sortable : true
		},{
			text : "二次投资产品名称",
			dataIndex : 'second_stype',
			width : '10%',
			sortable : true
		} ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'query-uid_investDetail',
				name : 'uid',
				fieldLabel : 'UID',
				labelWidth : 30,
				width : '15%'
			},{
				xtype : 'textfield',
				id : 'query-name_investDetail',
				name : 'name',
				fieldLabel : '姓名',
				labelWidth : 30,
				width : '15%'
			},{
	            xtype: 'datefield',
	            id : 'query-start_investDetail',
	            name: 'start',
	            fieldLabel: '注册日期',
	            labelWidth : 60,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_investDetail',
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
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_investDetail').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investDetail').getValue() ),'Y-m-d'),
						uid : Ext.getCmp('query-uid_investDetail').getValue(),
						name : Ext.getCmp('query-name_investDetail').getValue()
						 
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
					Ext.getCmp('query-start_investDetail').setValue(null);
					Ext.getCmp('query-end_investDetail').setValue(null);
					Ext.getCmp('query-name_investDetail').setValue(null);
					Ext.getCmp('query-uid_investDetail').setValue(null);
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					 
						sdate =  Ext.util.Format.date(new Date( Ext.getCmp('query-start_investDetail').getValue() ),'Y-m-d') ,
						edate = Ext.util.Format.date(new Date( Ext.getCmp('query-end_investDetail').getValue() ),'Y-m-d'),
						uid=Ext.getCmp('query-uid_investDetail').getValue(),
						name=Ext.getCmp('query-name_investDetail').getValue()
					 
					 window.location.href = appBaseUri + '/sys/operate/exportTradeUserList?&limit=100000&sdate=' +sdate +'&edate='+edate +'&uid='+uid +'&name='+name;
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
					form.findField('uid').setReadOnly(true);
					form.findField('name').setReadOnly(true);
					form.findField('reg_time').setReadOnly(true);
					form.findField('boundT').setReadOnly(true);
					form.findField('first_time').setReadOnly(true);
					form.findField('first_money').setReadOnly(true);
					form.findField('first_stype').setReadOnly(true);
					form.findField('first_platform').setReadOnly(true);
					form.findField('first_channel').setReadOnly(true);
					form.findField('second_time').setReadOnly(true);
					form.findField('second_money').setReadOnly(true);
					form.findField('second_stype').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});