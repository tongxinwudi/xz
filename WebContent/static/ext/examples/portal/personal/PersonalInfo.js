//用户盈利情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);
Ext.require(['Ext.Window', 'Ext.fx.target.Sprite', 'Ext.layout.container.Fit']);



Ext.define('Forestry.app.personal.PersonalInfo', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.personal.info', {
				cButtons : me.cButtons,
				cName : me.cName
			})]
		});
		this.callParent(arguments);
	}
});
 
 

 
Ext.define('Forestry.app.personal.info', {
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
			  'uid',
			  'name',
			  'mobile', 
			  'identity',
			  'regDate',
			  'boundDate',
			  'bank',
			  'city',
			  'province']
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {

			
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/personal/getPersonalInfoList',
				extraParams : me.extraParams ||  {
					s_date_bound :Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d"),
					e_date_bound :Ext.util.Format.date( new Date() ,"Y-m-d")  },
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'regDate',
				direction : 'DESC'
			} ]
		});

		var columns = [ {
			text : "UID",
			dataIndex : 'uid',
			width : '12%'
		}, {
			text : "姓名",
			dataIndex : 'name',
			width : '7%'
		}, {
			text : "电话号码",
			dataIndex : 'mobile',
			width : '12%'
		}, {
			text : "身份证",
			dataIndex : 'identity',
			width : '15%'
		}, {
			text : "注册日期",
			dataIndex : 'regDate',
			width : '8%',
			sortable : true
		} , {
			text : "绑卡日期",
			dataIndex : 'boundDate',
			width : '16%',
			sortable : true
		} ,{
			text : "开户行",
			dataIndex : 'bank',
			width : '8%',
			sortable : true
		} ,{
			text : "开户城市",
			dataIndex : 'city',
			width : '8%',
			sortable : true
		} ,{
			text : "开户省份",
			dataIndex : 'province',
			width : '8%',
			sortable : true
		} ,  {
			text:"操作",
            xtype:'actioncolumn',
            width:50,
            items: [{
                //这里直接通过URL设置图标
            	icon: appBaseUri+ '/static/ext/examples/shared/icons/fam/details.gif',
                tooltip: '查看',
                disabled : !globalObject.haveActionMenu(me.cButtons, 'View'),
                //这里是图标的点击事件
                //参数中有点击行的record , 所以很方便做处理
               /* handler: function(grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    alert("Edit " + rec.get('name'));
                }
              */
                handler : function(grid, rowIndex, colIndex) {
					var gridRecord = grid.getStore().getAt(rowIndex);
					var win = new App.PersonalInfo({
						hidden : true
					});
					var form = win.down('form').getForm();
					 
					win.show();
				}
            },{
                icon:appBaseUri+ '/static/img/reset.png',
                tooltip: 'Delete',
                handler: function(grid, rowIndex, colIndex) {
                    var rec = grid.getStore().getAt(rowIndex);
                    alert("Terminate " + rec.get('firstname'));
                }
            }]
        } ];

	
		
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			 renderTo: document.body, 
			 width: 700, 
			items : [   {
	            xtype: 'datefield',
	            id : 'query-start_reg_date',
	            name: 'start_reg',
	            fieldLabel: '注册日期',
	            labelWidth : 30,
	            width : '13%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-15),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_reg_date',
	            name: 'end_reg',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '13  %',
	            format:'Y-m-d',  
                value:Ext.util.Format.date( new Date() ,"Y-m-d")  
	        },   {
	            xtype: 'datefield',
	            id : 'query-start_bound_date',
	            name: 'start_bound',
	            fieldLabel: '绑卡日期',
	            labelWidth : 30,
	            width : '13%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-15),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_bound_date',
	            name: 'end_bound',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '13  %',
	            format:'Y-m-d',  
                value:Ext.util.Format.date( new Date() ,"Y-m-d")  
	        },{
				xtype : 'textfield',
				id : 'query-name_personal',
				name : 'name',
				fieldLabel : '姓名',
				labelWidth :20,
				width : '7%'
			},{
				xtype : 'textfield',
				id : 'query-mobile_personal',
				name : 'mobile',
				fieldLabel : '手机号',
				labelWidth : 20,
				width : '10%'
			},{
				xtype : 'textfield',
				id : 'query-code_personal',
				name : 'code',
				fieldLabel : '邀请码',
				labelWidth : 20,
				width : '10%'
			},{
				xtype : 'textfield',
				id : 'query-boundCode_personal',
				name : 'boundCode',
				fieldLabel : '绑定码',
				labelWidth : 20,
				width : '10%'
			},{
				xtype : 'button',
				text : '查询',
				id: 'button-query-personalInfo',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
							s_date_reg :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_reg_date').getValue() ),'Ymd') ,
							e_date_reg :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_reg_date').getValue() ),'Ymd'),
							s_date_bound :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_bound_date').getValue() ),'Y-m-d') ,
							e_date_bound :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_bound_date').getValue() ),'Y-m-d'),
							name : Ext.getCmp('query-name_personal').getValue(),
							mobile : Ext.getCmp('query-mobile_personal').getValue(),
							code : Ext.getCmp('query-code_personal').getValue(),
							boundCode : Ext.getCmp('query-boundCode_personal').getValue()
						 
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
				maxWidth :60,
				handler : function(btn, eventObj) {
					Ext.getCmp('query-start_reg_date').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_reg_dater').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
					Ext.getCmp('query-start_bound_date').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_bound_dater').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
					Ext.getCmp('query-name_personal').setValue(null);
					Ext.getCmp('query-mobile_personal').setValue(null);
					Ext.getCmp('query-code_personal').setValue(null);
					Ext.getCmp('query-boundCode_personal').setValue(null);
					 
				}
			} ]
		});

		
		
		Ext.define('App.PersonalInfo', {
			extend : 'Ext.window.Window',
			constructor : function(config) {
				config = config || {};
				Ext.apply(config, {
					title : '用户详情',
					width : 350,
					height : 280,
					bodyPadding : '10 5',
					modal : true,
					layout : 'fit',
					items : [ {
						xtype : 'form',
						fieldDefaults : {
							labelAlign : 'left',
							labelWidth : 90,
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
							xtype : 'textfield',
							name : 'userName',
							fieldLabel : '用户名',
							allowBlank : false,
							maxLength : 30
						}, {
							xtype : 'textfield',
							name : 'password',
							fieldLabel : '密码',
							allowBlank : false,
							maxLength : 32
						}, {
							xtype : 'textfield',
							name : 'realName',
							fieldLabel : '姓名',
							maxLength : 30
						}, {
							xtype : 'textfield',
							name : 'tel',
							fieldLabel : '手机号',
							maxLength : 15
						}, {
							xtype : 'textfield',
							name : 'email',
							fieldLabel : '邮箱',
							vtype : 'email',
							maxLength : 30
						}, {
							xtype : 'textfield',
							name : 'lastLoginTime',
							fieldLabel : '最后登录时间',
							hidden : true
						} ] 
					} ]
				});
				App.PersonalInfo.superclass.constructor.call(this, config);
			}
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
					var id = grid.getSelectionModel().getSelection()[0].get('regDate');
					var gridRecord = grid.getStore().findRecord('regDate', id);
					var win = new App.ForestryQueryWindow({
						hidden : true
					});
					var form = win.down('form').getForm();
					form.loadRecord(gridRecord);
					form.findField('uid').setReadOnly(true);
					form.findField('regDate').setReadOnly(true);
					form.findField('boundDate').setReadOnly(true);
					form.findField('identity').setReadOnly(true);
					form.findField('name').setReadOnly(true);
					form.findField('mobile').setReadOnly(true);
					form.findField('identity').setReadOnly(true);
					form.findField('bank').setReadOnly(true);
					form.findField('city').setReadOnly(true);
					form.findField('province').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});

