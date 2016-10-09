// 基金配置导入
Ext.onReady(function() {
	//Ext.tip.QuickTipManager.init();
	
	var partnerStore = new Ext.data.SimpleStore({  
        fields : ['key', 'value'],  
        data : [['0','钱景'],['101','民信贷'],['102','银来财富'],['103','聚爱财'],['104','金太平洋'],['105','白骨精'],['106','股票雷达'],['107','人人贷'],['108','zrwwtm'],['109','金谷网盈']]  
    }); 
	Ext.define('Forestry.app.operate.FundInvest', {
		extend : 'Ext.panel.Panel',
		initComponent : function() {
			var me = this;
			Ext.apply(this, {
				layout : 'border',
				items : [ Ext.create('Forestry.app.operate.FundInvest.FundInvestSum', {
					cButtons : me.cButtons,
					cName : me.cName
				}),Ext.create('Forestry.app.operate.FundInvest.FundInvestEveryday', {
					cButtons : me.cButtons,
					cName : me.cName
				})]
			});
			this.callParent(arguments);
		}
	});
	
	Ext.define('Forestry.app.operate.FundInvest.FundInvestSum',{
		extend : 'Ext.grid.Panel',
		region : 'center',
		initComponent : function() {
			var scope = this;

			Ext.define('ModelList', {
				extend : 'Ext.data.Model',
				//idProperty : 'id',
				fields : [ 'fdcode', 'abbrev','partner',{
					name : 'sum',
					type : 'float'
				},{
					name : 'opDate',
					type : 'datetime',
					dateFormat : 'Y-m-d H:i:s'
				}]
			});

			var fundsumstore = Ext.create('Ext.data.Store', {
				model : 'ModelList',
				// autoDestroy: true,
				autoLoad : true,
				remoteSort : true,
				pageSize : globalPageSize,
				proxy : {
					type : 'ajax',
					url : appBaseUri + '/operate/config/getFundInvestSum',
					extraParams : scope.extraParams || null,
					reader : {
						type : 'json',
						root : 'data',
						totalProperty : 'totalRecord',
						successProperty : "success"
					}
				},
				sorters : [ {
					property : 'fdcode',
					direction : 'ASC'
				} ]
			});

			var columns = [  {
				text : "基金代码",
				dataIndex : 'fdcode',
				width : '10%',
				sortable : false
			}, {
				text : "基金名称",
				dataIndex : 'abbrev',
				width : '35%'
			}, {
				text : "平台",
				dataIndex : 'partner',
				width : '15%',
				renderer : function(value){  
					if (value == null) {
						return '';
					} else {
						return partnerStore.findRecord('key',value).get('value');
					}
				}  
			}, {
				text : "投资总额",
				dataIndex : 'sum',
				width : '15%',
				//renderer: Ext.util.Format.numberRenderer('0,0.00')
			}];
			
			var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
				items : [ {
					xtype : 'textfield',
					id : 'fund-fdcode',
					name : 'fdcode',
					fieldLabel : '基金代码',
					labelWidth : 60,
					width : '15%'
				}, {
					xtype : 'textfield',
					id : 'fund-abbrev',
					name : 'name',
					fieldLabel : '名称',
					labelWidth : 30,
					width : '15%'
				},{
		            xtype: 'datefield',
		            id : 'fund-opStart',
		            name: 'opStart',
		            fieldLabel: '日期',
		            labelWidth : 30,
		            width : '80',
		            format:'Y-m-d',  
	                //value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
		        },{
		            xtype: 'datefield',
		            id : 'fund-opEnd',
		            name: 'opEnd',
		            fieldLabel: '至',
		            labelWidth : 15,
		            width : '80',
		            format:'Y-m-d',  
	                value:Ext.util.Format.date( new Date() ,"Y-m-d")  
		        },{
		            xtype: 'hiddenfield',
		            id : 'hid-fdcode',
		            name: 'hidFdcode'
		        },{
		            xtype: 'hiddenfield',
		            id : 'hid-abbrev',
		            name: 'hidAbbrev'
		        },{
		            xtype: 'hiddenfield',
		            id : 'hid-partner',
		            name: 'hidPartner'
		        },{
		            xtype: 'hiddenfield',
		            id : 'hid-opStart',
		            name: 'hidOpStart',
		            format:'Y-m-d'
		        },{
		            xtype: 'hiddenfield',
		            id : 'hid-opEnd',
		            name: 'hidOpEnd',
		            format:'Y-m-d'
		        }, {
					xtype : 'combobox',
					fieldLabel : '平台',
					id : 'fund-partner',
					name : 'partner',
					store : partnerStore,
					valueField : 'key',
					displayField : 'value',
					typeAhead : true,
					queryMode : 'local',
					emptyText : '请选择...',
					editable : false,
					labelWidth : 30,
					width : '20%',
					maxWidth : 150
				}, {
					xtype : 'button',
					text : '查询',
					iconCls : 'icon-search',
					//disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
					width : '10%',
					maxWidth : 60,
					handler : function(btn, eventObj) {
						var searchParams = {
							fdcode : Ext.getCmp('fund-fdcode').getValue(),
							abbrev : Ext.getCmp('fund-abbrev').getValue(),
							partner: Ext.getCmp('fund-partner').getValue(),
							opStart: Ext.getCmp('fund-opStart').getValue(),
							opEnd: Ext.getCmp('fund-opEnd').getValue()
						};
						Ext.getCmp('hid-fdcode').setValue(Ext.getCmp('fund-fdcode').getValue());
						Ext.getCmp('hid-abbrev').setValue(Ext.getCmp('fund-abbrev').getValue());
						Ext.getCmp('hid-partner').setValue(Ext.getCmp('fund-partner').getValue());
						Ext.getCmp('hid-opStart').setValue(Ext.Date.format(Ext.getCmp('fund-opStart').getValue(), 'Y-m-d H:i:s'));
						Ext.getCmp('hid-opEnd').setValue(Ext.Date.format(Ext.getCmp('fund-opEnd').getValue(), 'Y-m-d H:i:s'));
						Ext.apply(fundsumstore.proxy.extraParams, searchParams);
						fundsumstore.reload();
					}
				}, {
					xtype : 'button',
					text : '重置',
					iconCls : 'icon-reset',
					width : '10%',
					maxWidth : 60,
					handler : function(btn, eventObj) {
						Ext.getCmp('fund-fdcode').setValue(null);
						Ext.getCmp('fund-abbrev').setValue(null);
						Ext.getCmp('fund-partner').setValue(null);
						Ext.getCmp('fund-opStart').setValue(null);
						Ext.getCmp('fund-opEnd').setValue(null);
						Ext.getCmp('hid-fdcode').setValue(null);
						Ext.getCmp('hid-abbrev').setValue(null);
						Ext.getCmp('hid-partner').setValue(null);
						Ext.getCmp('hid-opStart').setValue(null);
						Ext.getCmp('hid-opEnd').setValue(null);
					}
				}, {
					xtype : 'button',
					text : '导出',
					iconCls : 'icon-export',
					width : '10%',
					maxWidth : 60,
					handler : function(btn, eventObj) {
						var url=appBaseUri + '/operate/config/exportFundInvestSum?fdcode='+Ext.getCmp('hid-fdcode').getValue();
						url+='&abbrev='+Ext.getCmp('hid-abbrev').getValue();
						url+='&partner='+ Ext.getCmp('hid-partner').getValue();
						url+='&opStart='+ Ext.getCmp('hid-opStart').getValue();
						url+='&opEnd='+ Ext.getCmp('hid-opEnd').getValue();
						location.href = url;
					}
				}]
			});
			
			Ext.apply(this, {
				id : 'fundInvestSum',
				store : fundsumstore,
				columns : columns,
				tbar : ttoolbar,
				bbar : Ext.create('Ext.PagingToolbar', {
					store : fundsumstore,
					displayInfo : true
				}),
				listeners : {
					'itemclick' : function(item, record) {
						var searchParams = {
							fdcode : record.get('fdcode'),
							partner: record.get('partner'),
							opStart: Ext.getCmp('hid-opStart').getValue(),
							opEnd: Ext.getCmp('hid-opEnd').getValue()
						};
						Ext.apply(fundeverydaystore.proxy.extraParams,searchParams);
						fundeverydaystore.reload();
					}
				}
			});

			this.callParent(arguments);
		}
	});
	
	Ext.define('FundModelData', {
		extend : 'Ext.data.Model',
		//idProperty : 'id',
		fields : [ 'fdcode', 'abbrev', {
			name : 'partner',
			type : 'int'
		},{
			name : 'sum',
			type : 'float'
		},{
			name : 'opDate',
			type : 'datetime',
			dateFormat : 'Y-m-d H:i:s'
		}]
	});
	
	var fundeverydaystore = Ext.create('Ext.data.Store', {
		model : 'FundModelData',
		// autoDestroy: true,
		autoLoad : false,
		remoteSort : true,
		pageSize : globalPageSize,
		proxy : {
			type : 'ajax',
			url : appBaseUri + '/operate/config/getFundInvestEveryday',
			extraParams : null,
			reader : {
				type : 'json',
				root : 'data',
				totalProperty : 'totalRecord',
				successProperty : "success"
			}
		},
		sorters : [ {
			property : 'opDate',
			direction : 'ASC'
		} ]
	});
	
	Ext.define('Forestry.app.operate.FundInvest.FundInvestEveryday',{
		extend : 'Ext.grid.Panel',
		region : 'east',
		width : '30%',
		split: true,
		initComponent : function() {
			var scope = this;

			var columns = [ {
				text : "基金代码",
				dataIndex : 'fdcode',
				width : '35%',
				sortable : false
			}, {
				text : "投资总额",
				dataIndex : 'sum',
				width : '25%',
				//renderer: Ext.util.Format.numberRenderer('0,0.00')
			}, {
				text : "日期",
				width : '40%',
				dataIndex : 'opDate',
				renderer:function(value){  
					if (value == null || value == 0) {
						return 'null'
					} else {
						//时间转好,将时间戳转换成Ext显示的日期格式
						return Ext.util.Format.date(new Date(parseInt(value)), 'Y-m-d')
					}
				}  
			} ];
			
			var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
				items : [ {
					xtype : 'button',
					text : '导出',
					iconCls : 'icon-export',
					width : '100%',
					maxWidth : 60,
					handler : function(btn, eventObj) {
						var url=appBaseUri + '/operate/config/exportFundInvestEveryday?';
						url+='fdcode='+fundeverydaystore.proxy.extraParams.fdcode;
						url+='&partner='+fundeverydaystore.proxy.extraParams.partner;
						url+='&opStart='+fundeverydaystore.proxy.extraParams.opStart;
						url+='&opEnd='+fundeverydaystore.proxy.extraParams.opEnd;					
						location.href = url;
					}
				}]
			});

			Ext.apply(this, {
				id : 'fundInvestEveryday',
				store : fundeverydaystore,
				columns : columns,
				tbar : ttoolbar,
				bbar : Ext.create('Ext.PagingToolbar', {
					store : fundeverydaystore,
					displayInfo : true
				})
			});

			this.callParent(arguments);
		}
		
	});
});