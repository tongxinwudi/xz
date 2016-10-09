//用户盈利情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);
Ext.require(['Ext.Window', 'Ext.fx.target.Sprite', 'Ext.layout.container.Fit']);

Ext.define('Forestry.app.member.MemberRation', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.member.ration', {
				cButtons : me.cButtons,
				cName : me.cName
			})]
		});
		this.callParent(arguments);
	}
});




 
 
 
 
Ext.define('Forestry.app.member.ration', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'center',
	split  : 'true',
	//height : '50%',
	//width:   '100%',
	initComponent : function() {
		var me = this;

		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
		//	idProperty : 'sdate',
			fields : [  
			  'sdate',
			  'edate',
			  {name:'sum_common',type:'String'}, 
			  'num_common',
			  'num_vip',
			  'sum_vip',
			  'ration_vip']
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/member/getMemberRationList',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'sdate',
				direction : 'DESC'
			} ]
		});

		var columns = [ {
			text : "起始日期",
			dataIndex : 'sdate',
			width : '12%'
		}, {
			text : "截止日期",
			dataIndex : 'edate',
			width : '12%'
		}, {
			text : "普通会员数",
			dataIndex : 'num_common',
			width : '12%'
		}, {
			text : "普通会员投资金额（元）",
			dataIndex : 'sum_common',
			width : '15%'
		}, {
			text : "VIP会员数",
			dataIndex : 'num_vip',
			width : '12%',
			sortable : true
		} , {
			text : "VIP会员投资金额(元)",
			dataIndex : 'sum_vip',
			width : '15%',
			sortable : true
		} ,{
			text : "VIP会员增长率",
			dataIndex : 'ration_vip',
			width : '17%',
			sortable : true
		}  ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			 renderTo: document.body, 
			 width: 700, 
			items : [   {
	            xtype: 'datefield',
	            id : 'query-start_member',
	            name: 'start',
	            fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_member',
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
							sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_member').getValue() ),'Y-m-d') ,
							edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_member').getValue() ),'Y-m-d')
						 
					};
					Ext.apply(forestryquerystore.proxy.extraParams, searchParams);
					forestryquerystore.reload();
					//Ext.apply(graphDataStore.proxy.extraParams, searchParams);
					// store1.reload();
				}
			}, {
				xtype : 'button',
				text : '重置',
				iconCls : 'icon-reset',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					Ext.getCmp('query-start_member').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_member').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
					//var xq = '';
				   // for(var i = 0; i < xqCheck.length; i++){
					// if(xqCheck.get(i).checked){
					//	 xqCheck.get(i).setValue(false);
					 // xq += '+' + xqCheck.get(i).boxLabel;
					 //  }
					//}
					 //  Ext.MessageBox.alert('提示', '您的兴趣是' + xq);
				}
			} ]
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
					var id = grid.getSelectionModel().getSelection()[0].get('sdate');
					var gridRecord = grid.getStore().findRecord('sdate', id);
					var win = new App.ForestryQueryWindow({
						hidden : true
					});
					var form = win.down('form').getForm();
					form.loadRecord(gridRecord);
					form.findField('sdate').setReadOnly(true);
					form.findField('edate').setReadOnly(true);
					form.findField('num_common').setReadOnly(true);
					form.findField('sum_common').setReadOnly(true);
					form.findField('num_vip').setReadOnly(true);
					form.findField('sum_vip').setReadOnly(true);
					form.findField('ration_vip').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});