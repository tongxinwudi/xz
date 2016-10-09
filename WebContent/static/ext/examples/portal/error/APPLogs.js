//用户盈利情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);
Ext.require(['Ext.Window', 'Ext.fx.target.Sprite', 'Ext.layout.container.Fit']);

Ext.define('Forestry.app.error.APPLogs', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.error.applogs', {
				cButtons : me.cButtons,
				cName : me.cName
			})]
		});
		this.callParent(arguments);
	}
});




 
 
 
 
Ext.define('Forestry.app.error.applogs', {
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
			  'user_id',
			  'mobile', 
			  'version',
			  'system_level',
			  'system_version',
			  'model',
			  'channel',
			  'network_type',
			  'time' ,
			  'error_url' ,
			  'error_url_params' ,
			  'error_json' ,
			  'error_detail' ,
			  'sys_time'
			  ]
		});
		
		var osTypeStore = Ext.create('Ext.data.JsonStore', {
			autoLoad : true,
			proxy : {
				type : 'ajax',
				 url : appBaseUri + '/sys/error/getOsTypeList',
				reader : {
					type : 'json',
					root : 'list',
					idProperty : 'key'
				}
			},
			fields : [ 'key', 'value' ]
		});
		
//		var exceptionType = new Ext.data.SimpleStore({  
//            fields : ['key', 'value'],  
//            data : [['E0001', 'E0001'], ['E0002', 'E0002'],['', '全部']]  
//        }); 

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/error/getAppLogList',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'sys_time',
				direction : 'DESC'
			} ]
		});

		var columns = [ {
			text : "用户ID",
			dataIndex : 'user_id',
			width : '12%'
		}, {
			text : "电话号码",
			dataIndex : 'mobile',
			width : '12%'
		}, {
			text : "程序版本",
			dataIndex : 'version',
			width : '10%'
		}, {
			text : "系统版本代号",
			dataIndex : 'system_level',
			width : '10%',
			sortable : true
		} , {
			text : "系统版本号",
			dataIndex : 'system_version',
			width : '10%',
			sortable : true
		} ,{
			text : "机型",
			dataIndex : 'model',
			width : '10%',
			sortable : true
		}  ,{
			text : "渠道名称",
			id:'channel',
			/*dataIndex : 'log',
		      renderer: function(value, metadata, record, rowIndex, columnIndex, store) { 
		    	    metadata.attr = 'ext:qtip="log:<br/>' +  Ext.util.Format.htmlEncode(record.data.log) +'"'; 
		    	    return Ext.util.Format.htmlEncode(record.data.log); 
		    	       }    ,*/
			width : '10%',
			sortable : true
		} ,{
			text : "网络类型",
			dataIndex : 'network_type',
			width : '10%',
			sortable : true
		} ,{
			text : "报错时间",
			dataIndex : 'time',
			width : '10%',
			sortable : true
		} ,{
			text : "服务器时间",
			dataIndex : 'sys_time',
			width : '10%',
			sortable : true
		},{
			text : "问题URL",
			dataIndex : 'error_url',
			width : '15%',
			sortable : true
		},{
			text : "问题URL参数",
			dataIndex : 'error_url_params',
			width : '10%',
			sortable : true
		},{
			text : "报错JSON",
			dataIndex : 'error_json',
			width : '25%',
			sortable : true,
			renderer: function(value, meta, record) {
                meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';   
                return value;   
           }
		},{
			text : "异常详情",
			dataIndex : 'error_detail',
			width : '30%',
			sortable : true,
			renderer: function(value, meta, record) {
                meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';   
                return value;   
           }
		}];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			 renderTo: document.body, 
			 width: 1000, 
			items : [ {
				xtype : 'textfield',
				id : 'query-applog_mobile',
				name : 'mobile',
				fieldLabel : '电话',
				labelWidth : 25,
				width : '15%'
			    }
			,{
				xtype : 'textfield',
				id : 'query-applog_model',
				name : 'model',
				fieldLabel : '机型',
				labelWidth : 15,
				width : '15%'
			    }
			, {
				xtype : 'combobox',
				id : 'query-applog_osType',
				name : '程序版本',
				store : osTypeStore,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '请选择',
				editable : false,
				labelWidth : 20,
				width : '15%',
				maxWidth : 150	
					
				},{
	            xtype: 'datefield',
	            id : 'query-start_appLog',
	            name: 'start',
	            fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '20%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_appLog',
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
							sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_appLog').getValue() ),'Y-m-d') ,
							edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_appLog').getValue() ),'Y-m-d'),
							$like_mobile : Ext.getCmp('query-applog_mobile').getValue(),
							$like_model : Ext.getCmp('query-applog_model').getValue(),
							$like_version : Ext.getCmp('query-applog_osType').getValue()
						 
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
					Ext.getCmp('query-start_appLog').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_appLog').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
					Ext.getCmp('query-applog_mobile').setValue(null);
					Ext.getCmp('query-applog_model').setValue(null);
					Ext.getCmp('query-applog_osType').setValue(null);
					 
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
					var id = grid.getSelectionModel().getSelection()[0].get('sys_time');
					var gridRecord = grid.getStore().findRecord('sys_time', id);
					var win = new App.ForestryQueryWindow({
						hidden : true
					});
					var form = win.down('form').getForm();
					/*form.loadRecord(gridRecord);
					form.findField('sdate').setReadOnly(true);
					form.findField('edate').setReadOnly(true);
					form.findField('num_common').setReadOnly(true);
					form.findField('sum_common').setReadOnly(true);
					form.findField('num_vip').setReadOnly(true);
					form.findField('sum_vip').setReadOnly(true);
					form.findField('ration_vip').setReadOnly(true);*/
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});