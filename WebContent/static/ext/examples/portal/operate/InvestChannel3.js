//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.InvestChannel3', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.Channel3', {
				cButtons : me.cButtons,
				cName : me.cName
			})  ]
		});
		this.callParent(arguments);
	}
});

 
 
 



Ext.define('Forestry.app.operate.Channel3', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'center',
	split  : 'true',
	height : '100%',
	width:   '100%',
	initComponent : function() {
		var me = this;

		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			//idProperty : 'date',
			 fields:['fdName', 'fdCode', 'inventory','sum']
		});

		var channelDateType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['7', '近一周'], ['30', '近一个月'], ['90', '近3个月'], ['', '全部']]  
        }); 
		
		var platformType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['qianjing', '钱景'], ['baigujing', '白骨精'], ['minxindai', '民信贷'], ['jintaipingyang', '金太平洋'], ['yinlaicaifu', '银来财富'], ['stockradar', '股票雷达'],['renrendai', '人人贷']]  
        }); 
		
		
		  
		
		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/operate/getInventoryList',
			 	//proxyExportUrl : appBaseUri + '/sys/forestry/exportForestry',
				extraParams : me.extraParams ||  {
					sdate :Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d"),
					edate :Ext.util.Format.date( Ext.Date.add(new Date(),Ext.Date.DAY,-1) ,"Y-m-d"),
					platform :'qianjing'},
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
			text : "基金名称",
			dataIndex : 'fdName',
			width : '30%'
		}  , {
			text : "基金代码",
			dataIndex : 'fdCode',
			width : '20%',
			sortable : true
		} ,{
			text : "日均保有量(元)",
			dataIndex : 'inventory',
			width : '20%',
			sortable : true
		},{
			text : "总保有量(元)",
			dataIndex : 'sum',
			width : '20%',
			sortable : true
		}  ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ 
			{
				xtype : 'combobox',
				fieldLabel : '选择日期',
				id : 'query-date_investChannel3',
				name : 'type',
				store : channelDateType,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '请选择',
				editable : false,
				labelWidth : 60,
				width : '20%',
				maxWidth : 160,
				listeners : {
					  select:function(combo,record,opts) {  
						  
						  if(record[0].get("key")!=''){
							  Ext.getCmp('query-start_investChannel3').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,0- record[0].get("key") ),"Y-m-d") );
						  }else{
							  Ext.getCmp('query-start_investChannel3').setValue('1970-01-01');
						  }
						  Ext.getCmp('query-end_investChannel3').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
						 
					   }  
				}
			},{
	            xtype: 'datefield',
	            id : 'query-start_investChannel3',
	            name: 'start',
	            //fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '10%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_investChannel3',
	            name: 'end',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '12%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date( Ext.Date.add(new Date(),Ext.Date.DAY,-1) ,"Y-m-d")  
	        },{
				xtype : 'combobox',
				fieldLabel : '平台',
				id : 'query-platform_investChannel3',
				name : 'platformtype',
				store : platformType,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '钱景',
				editable : true,
				labelWidth : 30,
				width : '20%',
				maxWidth : 150
			},{
				xtype : 'textfield',
				id : 'query-channel_fdCode',
				name : 'fdcode',
				fieldLabel : '基金代码',
				labelWidth : 60,
				width : '20%'
			},{
				xtype : 'textfield',
				id : 'query-channel_fdCode1',
				name : 'fdcode1',
				fieldLabel : '基金代码',
				labelWidth : 60,
				hidden:true,
				width : '20%'
			},{
				xtype : 'textfield',
				id : 'query-channel_fdName1',
				name : 'fdname1',
				fieldLabel : '基金名称',
				labelWidth : 60,
				hidden:true,
				width : '20%'
			} ,  {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_investChannel3').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investChannel3').getValue() ),'Y-m-d'),
						platform :  Ext.isEmpty(Ext.getCmp('query-platform_investChannel3').getValue(),false)==true ?'qianjing': Ext.getCmp('query-platform_investChannel3').getValue(),
						fdCode : Ext.getCmp('query-channel_fdCode').getValue() 
					 
						 
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
					Ext.getCmp('query-start_investChannel3').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_investChannel3').setValue(Ext.util.Format.date( Ext.Date.add(new Date(),Ext.Date.DAY,-1) ,"Y-m-d") );
					Ext.getCmp('query-platform_investChannel3').setValue("qianjing");
					Ext.getCmp('query-channel_fdCode').setValue(null);
					Ext.getCmp('query-date_investChannel3').setValue(null);
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					 
					sdate =Ext.util.Format.date(new Date( Ext.getCmp('query-start_investChannel3').getValue() ),'Y-m-d') ,
					edate =  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investChannel3').getValue() ),'Y-m-d'),
					platform = Ext.getCmp('query-platform_investChannel3').getValue(),
					fdCode = Ext.getCmp('query-channel_fdCode').getValue()
					 
					 
					 if(Ext.isEmpty(platform,false)==true){
						 platform='qianjing';
					 }
					
					 if(Ext.isEmpty(fdCode,false)==true){
						 fdCode='';
					 }
					
					 window.location.href = appBaseUri + '/sys/operate/exportInventoryList?&limit=100000&sdate=' +sdate +'&edate='+edate +'&platform='+platform +'&fdCode='+fdCode ;
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
					var fdCode = grid.getSelectionModel().getSelection()[0].get('fdCode');
					var fdName = grid.getSelectionModel().getSelection()[0].get('fdName');
					//var gridRecord = grid.getStore().findRecord('fdCode', fdCode);
				 
					//alert(Ext.getCmp('query-platform_investChannel3').getValue());
					Ext.getCmp('query-channel_fdCode1').setValue(fdCode);
					Ext.getCmp('query-channel_fdName1').setValue(fdName);
					var win = new  App.InventoryQueryWindow({
						hidden : true
					});
					
					//var form = win.down('form').getForm();
					 //form.findField('fdCode').setReadOnly(true);
					//form.loadRecord(gridRecord);
					 
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
		
		
		
});





//保有量明细
Ext.define('App.InventoryQueryWindow', {
	extend : 'Ext.window.Window',
	constructor : function(config) {
		/*
		var store1 = new Ext.data.JsonStore({ 
			url:appBaseUri + '/sys/operate/getInventoryList',//返回的是DataProxy对象 
			root:'items', 
			autoLoad : false,
			 fields:[
			         {name:'fdName',type:'string'},
			         {name:'fdCode',type:'string'},
			         {name:'inventory',type:'double'}
			     
			     ]
			}); 
		 
			 
		var grid = new Ext.grid.GridPanel({ 
			store:store1, 
			viewConifg:{ 
			forceFit:true 
			}, 
			columns:[ 
			{header:'基金名称',dataIndex:'fdName'}, 
			{header:'基金代码',dataIndex:'fdCode'}, 
			{header:'保有量',dataIndex:'inventory'}
			 
			] 
			}); 
		*/
		
		Ext.define('ModelList1', {
			extend : 'Ext.data.Model',
			//idProperty : 'date',
			 fields:['date','inventory']
		});
		
		
		var store1 = Ext.create('Ext.data.Store', {
			model : 'ModelList1',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/operate/getInventoryDetailList',
			 	//proxyExportUrl : appBaseUri + '/sys/forestry/exportForestry',
				extraParams :   {
					//sdate :Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d"),
					//edate :Ext.util.Format.date( new Date() ,"Y-m-d"),
					fdCode :Ext.getCmp('query-channel_fdCode1').getValue(),
					platform :Ext.getCmp('query-platform_investChannel3').getValue()==null?'qianjing':Ext.getCmp('query-platform_investChannel3').getValue()
				},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'date',
				direction : 'ASC'
			} ]
		});

		var columns1 = [ {
			text : "日期",
			dataIndex : 'date',
			width : '50%'
		}   ,{
			text : "保有量(元)",
			dataIndex : 'inventory',
			width : '20%',
			sortable : true
		}  ];

		var grid = new Ext.grid.GridPanel({ 
			store:store1, 
			  stripeRows: true,
			 //autoScroll : true,//滚动条
			 region: "center",
		 
			viewConfig:{  
				   enableTextSelection:true 
				   
				}  ,
			columns: columns1
			}); 
		
		config = config || { };
		Ext.apply(config, {
			title :  Ext.getCmp('query-channel_fdName1').getValue(),
			width : 500,
			height : 400,
			  bodyPadding : '10',
			layout : 'fit',
			modal : true,
			 
			/*items : [ {
				xtype : 'panel',
				fieldDefaults : {
					labelAlign : 'left',
					labelWidth : 90,
					anchor : '100%'
				},*/
				 items :  grid  ,
				bbar : Ext.create('Ext.PagingToolbar', {
					 store : store1,
					displayInfo : true
				}) 
			/*} ] */
		});
		 //alert(Ext.getCmp('query-channel_fdCode1').getValue());
		App.InventoryQueryWindow.superclass.constructor.call(this, config);
	}
});
 
 
