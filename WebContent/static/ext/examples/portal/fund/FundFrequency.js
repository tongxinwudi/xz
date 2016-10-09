//频率

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.fund.FundFrequency', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [  Ext.create('Forestry.app.fund.FundFrequencyList', {
				cButtons : me.cButtons,
				cName : me.cName
			}),Ext.create('Forestry.app.report.fund.FundFrequencyMonth'),
			Ext.create('Forestry.app.report.fund.AssemblyFrequencyMonth'),
			Ext.create('Forestry.app.report.fund.CashbaoFrequencyMonth')]
		});
		this.callParent(arguments);
	}
});

Ext.define('monthData',{
    extend:'Ext.data.Model',
    fields:[
        {name:'buy',type:'int'},
        {name:'sale',type:'int'},
        {name:'opMonth',type:'String'}
    ]
});
var monthDataStore=Ext.create('Ext.data.Store',{
    model:'monthData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getFundFrequencyMonth',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

var assemblyMonthDataStore=Ext.create('Ext.data.Store',{
    model:'monthData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getAssemblyFrequencyMonth',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

var cashbaoMonthDataStore=Ext.create('Ext.data.Store',{
    model:'monthData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getCashbaoFrequencyMonth',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

Ext.define('Forestry.app.report.fund.FundFrequencyMonth', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	height : '50%',
	width : '33%',
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
			style: 'background:#fff',
		    animate: true,
		    store: monthDataStore,
		    shadow: true,
            theme: 'Category1',
            
            legend: {
                position: 'bottom'
            },
            
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['buy', 'sale'],
                title: '单只基金操作频率',
                minorTickSteps: 1,
                grid: {
                    odd: {
                        opacity: 1,
                        fill: '#ddd',
                        stroke: '#bbb',
                        'stroke-width': 0.5
                    }
                }
            }, {
                type: 'Category',
                grid: true,
                position: 'bottom',
                fields: ['opMonth'],
                title: '时间：年-月'
            }],
            series: [{
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                xField: 'opMonth',
                yField: 'buy',
                title : '申购交易频率',
                markerConfig: {
                	type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('opMonth'));
                        this.update(storeItem.get('buy'));
                    }
                }
            }, {
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                smooth: true,
                xField: 'opMonth',
                yField: 'sale',
                title:'赎回交易频率',
                markerConfig: {
                    type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('opMonth'));
                        this.update(storeItem.get('sale'));
                    }
                }
            }]
		});


		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			region : "west",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

Ext.define('Forestry.app.report.fund.AssemblyFrequencyMonth', {
	extend : 'Ext.panel.Panel',
	region : 'center',
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
			style: 'background:#fff',
		    animate: true,
		    store: assemblyMonthDataStore,
		    shadow: true,
            theme: 'Category1',
            legend: {
                position: 'bottom'
            },
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['buy', 'sale'],
                title: '组合基金操作频率',
                minorTickSteps: 1,
                grid: {
                    odd: {
                        opacity: 1,
                        fill: '#ddd',
                        stroke: '#bbb',
                        'stroke-width': 0.5
                    }
                }
            }, {
                type: 'Category',
                grid: true,
                position: 'bottom',
                fields: ['opMonth'],
                title: '时间：年-月'
            }],
            series: [{
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                xField: 'opMonth',
                yField: 'buy',
                title : '申购交易频率',
                markerConfig: {
                	type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('opMonth'));
                        this.update(storeItem.get('buy'));
                    }
                }
            }, {
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                smooth: true,
                xField: 'opMonth',
                yField: 'sale',
                title:'赎回交易频率',
                markerConfig: {
                    type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('opMonth'));
                        this.update(storeItem.get('sale'));
                    }
                }
            }]
		});


		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			region : "center",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

Ext.define('Forestry.app.report.fund.CashbaoFrequencyMonth', {
	extend : 'Ext.panel.Panel',
	region : 'east',
	height : '50%',
	width : '33%',
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
			style: 'background:#fff',
		    animate: true,
		    store: cashbaoMonthDataStore,
		    shadow: true,
            theme: 'Category1',
            legend: {
                position: 'bottom'
            },            
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['buy', 'sale'],
                title: '现金宝操作频率',
                minorTickSteps: 1,
                grid: {
                    odd: {
                        opacity: 1,
                        fill: '#ddd',
                        stroke: '#bbb',
                        'stroke-width': 0.5
                    }
                }
            }, {
                type: 'Category',
                grid: true,
                position: 'bottom',
                fields: ['opMonth'],
                title: '时间：年-月'
            }],
            series: [{
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                xField: 'opMonth',
                yField: 'buy',
                title : '申购交易频率',
                markerConfig: {
                	type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('opMonth'));
                        this.update(storeItem.get('buy'));
                    }
                }
            }, {
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                smooth: true,
                xField: 'opMonth',
                yField: 'sale',
                title:'赎回交易频率',
                markerConfig: {
                    type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('opMonth'));
                        this.update(storeItem.get('sale'));
                    }
                }
            }]
		});


		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			region : "east",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

 /*
var store1 = Ext.create('Ext.data.JsonStore', {
	fields : [ 'name', 'data1' ],
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/profit/getProfit',
		extraParams : null,
		reader : {
			type : 'array',
			root : ''
		}
	}
})
*/

 

Ext.define('Forestry.app.fund.FundFrequencyList', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'south',
	height : '50%',
	initComponent : function() {
		var me = this;

		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			//idProperty : 'invest',
			fields : [ {
				name : 'id',
				type : 'String'
			}, 'name','operate','count']
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/fund/getFundFrequency',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'count',
				direction : 'DESC'
			} ]
		});

		 var fundFrequencyDataType = new Ext.data.SimpleStore({  
             fields : ['key', 'value'],  
             data : [['1', '申购'], ['2', '赎回']]  
         }); 
		
		
		var columns = [ {
			text : "ID",
			dataIndex : 'id',
			width : '10%'
		}, {
			text : "名称",
			dataIndex : 'name',
			width : '30%'
		}, {
			text : "操作",
			dataIndex : 'operate',
			width : '10%'
		}, {
			text : "次数",
			dataIndex : 'count',
			width : '8%',
			sortable : true
		} ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'fundquery-id_frequency',
				name : 'id',
				fieldLabel : '基金代码',
				labelWidth : 60,
				width : '20%'
			},  {
				xtype : 'textfield',
				id : 'fundquery-name_frequency',
				name : 'name',
				fieldLabel : '基金名称',
				labelWidth : 60,
				width : '25%'
			}, 
			{
				xtype : 'combobox',
				fieldLabel : '操作',
				id : 'fundquery-type_frequency',
				name : 'type',
				store : fundFrequencyDataType,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '请选择...',
				editable : false,
				labelWidth : 30,
				width : '20%',
				maxWidth : 150
			},  {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						id : Ext.getCmp('fundquery-id_frequency').getValue(),
						name : Ext.getCmp('fundquery-name_frequency').getValue() ,
						type: Ext.getCmp('fundquery-type_frequency').getValue()
						 
					};
					Ext.apply(forestryquerystore.proxy.extraParams, searchParams);
					forestryquerystore.reload();
					//Ext.apply(graphDataStore.proxy.extraParams, searchParams);
					//graphDataStore.reload();
				}
			}, {
				xtype : 'button',
				text : '重置',
				iconCls : 'icon-reset',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					Ext.getCmp('profitquery-id').setValue(null);
					Ext.getCmp('profitquery-name').setValue(null);
					Ext.getCmp('fundquery-type_frequency').setValue(null);
					 
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
					var id = grid.getSelectionModel().getSelection()[0].get('id');
					var gridRecord = grid.getStore().findRecord('id', id);
					var win = new App.ForestryQueryWindow({
						hidden : true
					});
					var form = win.down('form').getForm();
					form.loadRecord(gridRecord);
					form.findField('id').setReadOnly(true);
					form.findField('name').setReadOnly(true);
					form.findField('operate').setReadOnly(true);
					form.findField('count').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});

