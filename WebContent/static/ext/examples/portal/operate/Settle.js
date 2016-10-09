//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.Settle', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.settle', {
				cButtons : me.cButtons,
				cName : me.cName
			})  ]
		});
		this.callParent(arguments);
	}
});

 
 
 



Ext.define('Forestry.app.operate.settle', {
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
			 fields:['fdCode', 'fdName', 'dayInventory', 'inventory','suml6','counts6','invest', 'redempl5w', 'redemps5w','fees','saleFee', 'manageFee', 'payCost','supFee','techFee']
		});

		var channelDateType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['7', '近一周'], ['30', '近一个月'], ['90', '近3个月'], ['', '全部']]  
        }); 
		
		var platformType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['qianjing', '钱景'], ['baigujing', '白骨精'], ['minxindai', '民信贷'], ['jintaipingyang', '金太平洋'], ['yinlaicaifu', '银来财富'], ['stockradar', '股票雷达'],['renrendai', '人人贷']]  
        }); 
		
		var fundType = new Ext.data.SimpleStore({  
	        fields : ['key', 'value'],  
	        data : [['1','货基'],['0','非货基']]  
	    }); 
		
		  
		
		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/operate/getSettleList',
			 	//proxyExportUrl : appBaseUri + '/sys/forestry/exportForestry',
				extraParams : me.extraParams ||  {
					sdate :Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d"),
					edate :Ext.util.Format.date( Ext.Date.add(new Date(),Ext.Date.DAY,-1) ,"Y-m-d"),
					platform :'qianjing',
					fdType :'qianjing'},
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'fdCode',
				direction : 'ASC'
			} ]
		});

		var columns = [ {
			text : "基金代码",
			dataIndex : 'fdCode',
			width : '10%'
		}  , {
			text : "基金名称",
			dataIndex : 'fdName',
			width : '20%',
			sortable : true
		} ,{
			text : "日均保有量(元)",
			dataIndex : 'dayInventory',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		} ,{
			text : "总保有量(元)",
			dataIndex : 'inventory',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		},{
			text : "累计申购额(>=6666.67元)",
			dataIndex : 'suml6',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		},{
			text : "申购笔数(<6666.67元)",
			dataIndex : 'counts6',
			width : '10%',
//			renderer : function(data){
//				if(data!=null&&data>0)
//					return Ext.util.Format.number(data, '0.00');
//			},
			sortable : true
		},{
			text : "申购额(元)",
			dataIndex : 'invest',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		} ,{
			text : "赎回笔数(大于5万元)",
			dataIndex : 'redempl5w',
			width : '10%',
			sortable : true
		} ,{
			text : "赎回笔数(小于5万元)",
			dataIndex : 'redemps5w',
			width : '10%',
			sortable : true
		}  ,{
			text : "认/申购服务费(元)",
			dataIndex : 'fees',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		},{
			text : "销售服务费(元)",
			dataIndex : 'saleFee',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		} ,{
			text : "管理费分成(元)",
			dataIndex : 'manageFee',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		} ,{
			text : "支付成本(元)",
			dataIndex : 'payCost',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		} ,{
			text : "监督成本(元)",
			dataIndex : 'supFee',
			width : '10%',
			renderer : function(data){
				if(data!=null&&data>0)
					return Ext.util.Format.number(data, '0.00');
			},
			sortable : true
		} ,{
			text : "技术服务费基数(元)",
			dataIndex : 'techFee',
			width : '10%',
//			renderer : function(data){
//				if(data!=null&&data>0)
//					return Ext.util.Format.number(data, '0.00');
//			},
			sortable : true
		}  ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ 
			{
				xtype : 'combobox',
				fieldLabel : '选择日期',
				id : 'query-date_settleChannel3',
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
							  Ext.getCmp('query-start_settleChannel3').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,0- record[0].get("key") ),"Y-m-d") );
						  }else{
							  Ext.getCmp('query-start_settleChannel3').setValue('1970-01-01');
						  }
						  Ext.getCmp('query-end_settleChannel3').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
						 
					   }  
				}
			},{
	            xtype: 'datefield',
	            id : 'query-start_settleChannel3',
	            name: 'start',
	            //fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '10%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_settleChannel3',
	            name: 'end',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '12%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date( Ext.Date.add(new Date(),Ext.Date.DAY,-1) ,"Y-m-d")  
	        },{
				xtype : 'combobox',
				fieldLabel : '平台',
				id : 'query-platform_settleChannel3',
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
			}, {
				xtype : 'textfield',
				id : 'query-settle_fdCode',
				name : 'fdcode',
				fieldLabel : '基金代码',
				labelWidth : 60,
				width : '20%'
			},{
				xtype : 'combobox',
				fieldLabel : '基金类别',
				id : 'query-fdtype_settleChannel3',
				name : 'fdtype',
				store : fundType,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '非货基',
				editable : true,
				labelWidth : 30,
				width : '20%',
				maxWidth : 150
			}
			 ,  {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_settleChannel3').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_settleChannel3').getValue() ),'Y-m-d'),
						platform :  Ext.isEmpty(Ext.getCmp('query-platform_settleChannel3').getValue(),false)==true ?'qianjing': Ext.getCmp('query-platform_settleChannel3').getValue(),
						fdCode : Ext.getCmp('query-settle_fdCode').getValue() ,
						fdType : Ext.getCmp('query-fdtype_settleChannel3').getValue() 
						 
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
					Ext.getCmp('query-start_settleChannel3').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_settleChannel3').setValue(Ext.util.Format.date( Ext.Date.add(new Date(),Ext.Date.DAY,-1) ,"Y-m-d") );
					Ext.getCmp('query-platform_settleChannel3').setValue("qianjing");
					Ext.getCmp('query-settle_fdCode').setValue(null);
					Ext.getCmp('query-date_settleChannel3').setValue(null);
					Ext.getCmp('query-fdtype_settleChannel3').setValue(null);
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					 
					sdate =Ext.util.Format.date(new Date( Ext.getCmp('query-start_settleChannel3').getValue() ),'Y-m-d') ,
					edate =  Ext.util.Format.date(new Date( Ext.getCmp('query-end_settleChannel3').getValue() ),'Y-m-d'),
					platform = Ext.getCmp('query-platform_settleChannel3').getValue(),
					fdCode = Ext.getCmp('query-settle_fdCode').getValue(),
					fdType =  Ext.getCmp('query-fdtype_settleChannel3').getValue()  ,
					platformName =  Ext.getCmp('query-platform_settleChannel3').getRawValue()  
					
					 
					 if(Ext.isEmpty(platform,false)==true){
						 platform='qianjing';
						
					 }
					 if(Ext.isEmpty(platformName,false)==true){
						 platformName='钱景';
					 }
					 
					 
					 
					 if(Ext.isEmpty(fdCode,false)==true){
						 fdCode='';
					 }
					
					 window.location.href = appBaseUri + '/sys/operate/exportSettleList?&limit=100000&sdate=' +sdate +'&edate='+edate +'&platform='+platform +'&fdCode='+fdCode+'&fdType='+fdType+'&platformName='+platformName ;
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
//				
//					var grid = this;
//					var fdCode = grid.getSelectionModel().getSelection()[0].get('fdCode');
//					var fdName = grid.getSelectionModel().getSelection()[0].get('fdName');
//					//var gridRecord = grid.getStore().findRecord('fdCode', fdCode);
//				 
//					//alert(Ext.getCmp('query-platform_investChannel3').getValue());
//					Ext.getCmp('query-settle_fdCode1').setValue(fdCode);
//					Ext.getCmp('query-channel_fdName1').setValue(fdName);
//					var win = new  App.InventoryQueryWindow({
//						hidden : true
//					});
//					
//					//var form = win.down('form').getForm();
//					 //form.findField('fdCode').setReadOnly(true);
//					//form.loadRecord(gridRecord);
//					 
//					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
		
		
		
});



 