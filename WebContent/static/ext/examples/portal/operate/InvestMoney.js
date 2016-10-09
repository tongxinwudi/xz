//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.InvestMoney', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.money', {
				cButtons : me.cButtons,
				cName : me.cName
			}) ]
		});
		this.callParent(arguments);
	}
});

 
 

Ext.define('Forestry.app.operate.money', {
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
			         {name:'cashbao_num',type:'string'},
			         {name:'single_num',type:'string'},
			         {name:'jinqu_num',type:'String'},
			         {name:'baoshou_num',type:'String'},
			         {name:'wenjian_num',type:'String'},
			         {name:'licai_num',type:'string'},
			         {name:'licai2_num',type:'string'},
			         {name:'yanglao_num',type:'String'},
			         {name:'maifang_num',type:'String'},	
			         {name:'zinv_num',type:'String'},		 
			         {name:'jiehun_num',type:'String'},		 
			         {name:'duanqi_num',type:'String'}	 
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
				url : appBaseUri + '/sys/operate/getMoneyList',
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
			width : '20%'
		},  {
			text : "活期宝新用户申购金额",
			dataIndex : 'cashbao_num',
			width : '20%'
		}, {
			text : "单只基金新用户申购金额",
			dataIndex : 'single_num',
			width : '20%',
			sortable : true
		} ,{
			text : "进取型新用户申购金额",
			dataIndex : 'jinqu_num',
			width : '10%',
			sortable : true
		} ,{
			text : "保守型新用户申购金额",
			dataIndex : 'baoshou_num',
			width : '10%',
			sortable : true
		} ,{
			text : "稳健性新用户申购金额",
			dataIndex : 'wenjian_num',
			width : '10%',
			sortable : true
		},{
			text : "理财增值型新用户申购金额",
			dataIndex : 'licai_num',
			width : '10%',
			sortable : true
		},{
			text : "理财增值型新用户申购金额(新20档)",
			dataIndex : 'licai2_num',
			width : '20%',
			sortable : true
		},{
			text : "养老型新用户申购金额",
			dataIndex : 'yanglao_num',
			width : '10%',
			sortable : true
		},{
			text : "买房置业型新用户申购金额",
			dataIndex : 'maifang_num',
			width : '10%',
			sortable : true
		} ,{
			text : "子女教育型新用户申购金额",
			dataIndex : 'zinv_num',
			width : '10%',
			sortable : true
		} ,{
			text : "结婚生子型用户申购金额",
			dataIndex : 'jiehun_num',
			width : '10%',
			sortable : true
		} ,{
			text : "小额短期理财型用户申购金额",
			dataIndex : 'duanqi_num',
			width : '10%',
			sortable : true
		} ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [  {
	            xtype: 'datefield',
	            id : 'query-start_investMoney',
	            name: 'start',
	            fieldLabel: '日期',
	            labelWidth : 60,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_investMoney',
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
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_investMoney').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investMoney').getValue() ),'Y-m-d')
						 
						 
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
					Ext.getCmp('query-start_investMoney').setValue(null);
					Ext.getCmp('query-end_investMoney').setValue(null);
					 
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					 
						sdate =  Ext.util.Format.date(new Date( Ext.getCmp('query-start_investMoney').getValue() ),'Y-m-d') ,
						edate = Ext.util.Format.date(new Date( Ext.getCmp('query-end_investMoney').getValue() ),'Y-m-d') 
						 
					 
					 window.location.href = appBaseUri + '/sys/operate/exportMoneyList?&limit=100000&sdate=' +sdate +'&edate='+edate ;
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
					form.findField('totol_num').setReadOnly(true);
					form.findField('new_num').setReadOnly(true);
					form.findField('cashbao_num').setReadOnly(true);
					form.findField('single_num').setReadOnly(true);
					form.findField('jinqu_num').setReadOnly(true);
					form.findField('baoshou_num').setReadOnly(true);
					form.findField('wenjian_num').setReadOnly(true);
					form.findField('licai_num').setReadOnly(true);
					form.findField('licai2_num').setReadOnly(true);
					form.findField('yanglao_num').setReadOnly(true);
					form.findField('mafang_num').setReadOnly(true);
					form.findField('zinv_num').setReadOnly(true);
					form.findField('jiehun_num').setReadOnly(true);
					form.findField('duanqi_num').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});