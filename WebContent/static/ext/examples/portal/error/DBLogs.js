Ext.require([ 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.error.DBLogs', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.error.dblogs')]
		});
		this.callParent(arguments);
	}
});

Ext.define('ModelList', {
	extend : 'Ext.data.Model',
	idProperty : 'id',
	fields : [ {
					name : 'id',
					type : 'long'
				},{
					name : 'uid',
					type : 'String'
	           },{
	        	   name : 'mobile',
	        	   type : 'String'
	           },{
	        	   name : 'capitalmode',
	        	   type : 'String'
	           },{
	        	   name : 'type',
	        	   type : 'String'
	           },{
	        	   name : 'card',
	        	   type : 'String'
	           },{
	        	   name : 'ecode',
	        	   type : 'String'
	           },{
	        	   name : 'from_page',
	        	   type : 'String'
	           },{
	        	   name : 'emsg',
	        	   type : 'String'
	           },{
	        	   name : 'extra',
	        	   type : 'String'
	           },{
	        	   name : 'error_time',
	        	   type : 'String'
	           },{
	        	   name : 'create_time',
	        	   type : 'datetime'
	           }]
});

var forestryquerystore = Ext.create('Ext.data.Store', {
	model : 'ModelList',
	// autoDestroy: true,
	autoLoad : true,
	remoteSort : true,
	pageSize : globalPageSize,
	split: true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/error/log/getDBLogs',
		extraParams : null,//me.extraParams || 
		reader : {
			type : 'json',
			root : 'data',
			totalProperty : 'totalRecord',
			successProperty : "success"
		}
	},
	sorters : [ {
		property : 'error_time',
		direction : 'DESC'
	} ]
});

Ext.define('Forestry.app.error.dblogs', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'center',
	width : '100%',
	height : '100%',
	initComponent : function() {
		var me = this;
		var columns = [ {
			text : "ID",
			dataIndex : 'id',
			width : '5%',
			hidden : true
		},{
			text : "错误信息",
			dataIndex : 'emsg',
			width : '70%'
		}, {
			text : "发生时间",
			dataIndex : 'error_time',
			width : '30%'
		}];
		
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'uid',
				name : 'UID',
				fieldLabel : 'UID',
				labelWidth : 60,
				width : '20%'
			}, {
				xtype : 'textfield',
				id : 'mobile',
				name : 'mobile',
				fieldLabel : '手机',
				labelWidth : 60,
				width : '20%'
			}, {
				xtype : 'textfield',
				id : 'query-ecode',
				name : 'ecode',
				fieldLabel : '错误码',
				labelWidth : 60,
				width : '20%'
			}, {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						uid : Ext.getCmp('uid').getValue(),
						mobile : Ext.getCmp('mobile').getValue(),
						ecode : Ext.getCmp('query-ecode').getValue()
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
					Ext.getCmp('uid').setValue(null);
					Ext.getCmp('mobile').setValue(null);
					Ext.getCmp('query-ecode').setValue(null);
				}
			}]
		});

		
		Ext.apply(this, {
			store : forestryquerystore,
			columns : columns,
			//tbar	: ttoolbar,
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
					form.findField('mobile').setReadOnly(true);
					form.findField('card').setReadOnly(true);
					form.findField('ecode').setReadOnly(true);
					form.findField('emsg').setReadOnly(true);
					form.findField('from_page').setReadOnly(true);
					form.findField('error_time').setReadOnly(true);
					win.show();
				}
			}
			
		});

		this.callParent(arguments);
	}
});