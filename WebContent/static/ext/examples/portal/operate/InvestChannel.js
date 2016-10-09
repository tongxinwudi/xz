//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.InvestChannel', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.Channel1', {
				cButtons : me.cButtons,
				cName : me.cName
			}) ]
		});
		this.callParent(arguments);
	}
});

 
 

Ext.define('Forestry.app.operate.Channel1', {
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
			         {name:'date',type:'string'},
			         {name:'platform',type:'String'},
			         {name:'channel',type:'String'},
			         {name:'reg_num',type:'string'},
			         {name:'bound_num',type:'string'},
			         {name:'new_bound_num',type:'String'},
			         {name:'user_num',type:'String'},
			         {name:'money_num',type:'string'},
			         {name:'per_money_num',type:'string'},
			         {name:'new_user_num',type:'String'},
			         {name:'new_money_num',type:'String'},
			         {name:'per_new_money_num',type:'string'}
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
				url : appBaseUri + '/sys/operate/getChannelList',
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
			width : '10%'
		}, {
			text : "平台",
			dataIndex : 'platform',
			width : '10%'
		}, {
			text : "渠道",
			dataIndex : 'channel',
			width : '10%'
		} , {
			text : "注册人数",
			dataIndex : 'reg_num',
			width : '10%',
			sortable : true
		} ,{
			text : "绑卡人数",
			dataIndex : 'bound_num',
			width : '10%',
			sortable : true
		} ,{
			text : "新绑卡人数",
			dataIndex : 'new_bound_num',
			width : '10%',
			sortable : true
		},{
			text : "申购用户数",
			dataIndex : 'user_num',
			width : '10%',
			sortable : true
		},{
			text : "申购总金额",
			dataIndex : 'money_num',
			width : '10%',
			sortable : true
		},{
			text : "人均投资金额",
			dataIndex : 'per_money_num',
			width : '10%',
			sortable : true
		},{
			text : "新用户申购数",
			dataIndex : 'new_user_num',
			width : '10%',
			sortable : true
		},{
			text : "新用户申购金额",
			dataIndex : 'new_money_num',
			width : '10%',
			sortable : true
		},{
			text : "新用户人均投资金额",
			dataIndex : 'per_new_money_num',
			width : '10%',
			sortable : true
		}];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'query-platform_investChannel',
				name : 'platform',
				fieldLabel : '平台',
				labelWidth : 30,
				width : '15%'
			},{
				xtype : 'textfield',
				id : 'query-channel_investChannel',
				name : 'channel',
				fieldLabel : '渠道',
				labelWidth : 30,
				width : '15%'
			},{
	            xtype: 'datefield',
	            id : 'query-start1_investChannel',
	            name: 'start',
	            fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end1_investChannel',
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
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start1_investChannel').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end1_investChannel').getValue() ),'Y-m-d'),
						platform : Ext.getCmp('query-platform_investChannel').getValue(),
						channel : Ext.getCmp('query-channel_investChannel').getValue()
						 
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
					Ext.getCmp('query-start1_investChannel').setValue(null);
					Ext.getCmp('query-end1_investChannel').setValue(null);
					Ext.getCmp('query-channel_investChannel').setValue(null);
					Ext.getCmp('query-platform_investChannel').setValue(null);
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					 
						sdate =  Ext.util.Format.date(new Date( Ext.getCmp('query-start1_investChannel').getValue() ),'Y-m-d') ,
						edate = Ext.util.Format.date(new Date( Ext.getCmp('query-end1_investChannel').getValue() ),'Y-m-d'),
						platform=Ext.getCmp('query-platform_investChannel').getValue(),
						channel=Ext.getCmp('query-channel_investChannel').getValue()
					 
					 window.location.href = appBaseUri + '/sys/operate/exportChannelList?&limit=100000&sdate=' +sdate +'&edate='+edate +'&platform='+platform +'&channel='+channel;
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
					form.findField('platform').setReadOnly(true);
					form.findField('channel').setReadOnly(true);
					form.findField('reg_num').setReadOnly(true);
					form.findField('bound_num').setReadOnly(true);
					form.findField('new_bound_num').setReadOnly(true);
					form.findField('user_num').setReadOnly(true);
					form.findField('money_num').setReadOnly(true);
					form.findField('per_money_num').setReadOnly(true);
					form.findField('new_user_num').setReadOnly(true);
					form.findField('new_money_num').setReadOnly(true);
					form.findField('per_new_money_num').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});