//用户盈利情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.fund.FundLimit', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [  Ext.create('Forestry.app.fund.FundLimitList', {
				cButtons : me.cButtons,
				cName : me.cName
			}),Ext.create('Forestry.app.report.FundLimitGraph'),  Ext.create('Forestry.app.report.FundLimitLineGraph')]
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

Ext.define('graphData',{
    extend:'Ext.data.Model',
    fields:[
        {name:'graphName',type:'string'},
        {name:'graphData',type:'String'},
        {name:'rates',type:'string'}
    ]
});
var graphDataStore=Ext.create('Ext.data.Store',{
    model:'graphData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getFundLimitGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

Ext.define('Forestry.app.report.FundLimitGraph', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	height:  '50%',
	width : '30%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
		    width: 280,
		    height: 200,
		    store: graphDataStore,
		    renderTo: Ext.getBody(),
		    shadow: false,//一定会关闭阴影，否则拼饼突出的时候很不好看。
		    legend : {
				position : 'right'
				//field:'graphName'
				//itemSpacing:5,
				//padding:5
			},
		    series: [{
		        type: 'pie',
		        showInLegend: true,
		        field:'graphData',
				        tips : {
					trackMouse : true,
					width :350,
					height : 28,

					renderer : function(graphDataStore, item) {
							 	this.setTitle( graphDataStore.get('graphName') +"数量为" +  graphDataStore.get('graphData')+"条,比例为：" +graphDataStore.get('rates') );
						// this.setTitle(graphDataStore.get('graphName') +"数量为" +  graphDataStore.get('graphData')+"条");
					}
				},  
				 title: ['申购额<10', '100≤申购额<1000', '1000≤申购额<10000','10000≤申购额<100000', '申购额≥100000'],
		        label: {// 这里能够使拼饼上面显示，该拼饼属于的部分
		            field: 'rates' ,
		            display: 'rotate',
		            font: '8px Arial'
		        },
		        highlight: {//这里是突出效果的声明，margin越大，鼠标悬停在拼饼上面，拼饼突出得越多
		            segment: {
		                margin: 5
		            }
		        },
		        animate: true
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


Ext.define('monthData1',{
    extend:'Ext.data.Model',
    fields:[
        {name:'date',type:'String'},
        {name:'limit10',type:'int'},
        {name:'limit100',type:'int'},
        {name:'limit1000',type:'int'},
        {name:'limit10000',type:'int'},
        {name:'limit100000',type:'int'}
    ]
});
var monthDataStore1=Ext.create('Ext.data.Store',{
    model:'monthData1',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getFundLimitLineGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});



Ext.define('Forestry.app.report.FundLimitLineGraph', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	width : '40%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
			style: 'background:#fff',
		    animate: true,
		    store: monthDataStore1,
		    shadow: true,
            theme: 'Category1',
            legend: {
                position: 'right'
            },
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['limit10', 'limit100', 'limit1000'],
                title: '申购比例',
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
                position: 'bottom',
                fields: ['date'],
                title: '月份'
            }],
            series: [{
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                xField: 'date',
                yField: 'limit10',
                title : '申购额<10',
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
                        this.setTitle(storeItem.get('date'));
                        this.update(storeItem.get('limit10'));
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
                xField: 'date',
                yField: 'limit100',
                title:'10≤申购额<100',
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
                        this.setTitle(storeItem.get('date'));
                        this.update(storeItem.get('limit100'));
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
                xField: 'date',
                yField: 'limit1000',
                title: '100≤申购额<1000',
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
                        this.setTitle(storeItem.get('date'));
                        this.update(storeItem.get('limit1000'));
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
                xField: 'date',
                yField: 'limit1000',
                title: '1000≤申购额<10000',
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
                        this.setTitle(storeItem.get('date'));
                        this.update(storeItem.get('limit10000'));
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
                xField: 'date',
                yField: 'limit10000',
                title: '10000≤申购额<100000',
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
                        this.setTitle(storeItem.get('date'));
                        this.update(storeItem.get('limit100000'));
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
                xField: 'date',
                yField: 'limit10000',
                title: '申购额≥100000',
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
                        this.setTitle(storeItem.get('date'));
                        this.update(storeItem.get('limit100000'));
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


Ext.define('Forestry.app.fund.FundLimitList', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'south',
	height : '50%',
	width : '100%',
	initComponent : function() {
		var me = this;

		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'invest',
			fields : [ {
				name : 'id',
				type : 'String'
			}, 'name','mobile',
			{
				name : 'invest',
				type : 'String' 
			},{
				name : 'fund',
				type : 'String' 
			},
			{
				name : 'opDate',
				type : 'String' 
			}]
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/fund/getFund',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'id',
				direction : 'DESC'
			} ]
		});

		var columns = [ {
			text : "ID",
			dataIndex : 'id',
			width : '15%'
		}, {
			text : "姓名",
			dataIndex : 'name',
			width : '10%'
		}, {
			text : "电话号码",
			dataIndex : 'mobile',
			width : '11%'
		},  {
			text : "申购额",
			dataIndex : 'invest',
			width : '10%',
			sortable : true
		}  , {
			text : "申购产品",
			dataIndex : 'fund',
			width : '25%',
			sortable : true
		}  ,  {
			text : "申购时间",
			dataIndex : 'opDate',
			width : '17%',
			sortable : true
		} ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'fundquery-mobile_limit',
				name : 'mobile',
				fieldLabel : '手机号',
				labelWidth : 50,
				width : '20%'
			},  {
				xtype : 'textfield',
				id : 'fundquery-name_limit',
				name : 'name',
				fieldLabel : '姓名',
				labelWidth : 30,
				width : '10%'
			},  {
				xtype : 'textfield',
				id : 'fundquery-sinvest_limit',
				name : 'sinvest',
				//fieldLabel : '姓名',
				labelWidth : 30,
				width : '10%'
			}, '≤ 申购金额  <' ,
			 {
				xtype : 'textfield',
				id : 'fundquery-einvest_limit',
				name : 'einvest',
				//fieldLabel : '姓名',
				labelWidth : 10,
				width : '10%'
			},
			   {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						mobile : Ext.getCmp('fundquery-mobile_limit').getValue(),
						name : Ext.getCmp('fundquery-name_limit').getValue() ,
						sinvest: Ext.getCmp('fundquery-sinvest_limit').getValue(),
						einvest: Ext.getCmp('fundquery-einvest_limit').getValue()
						 
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
					Ext.getCmp('profitquery-mobile').setValue(null);
					Ext.getCmp('profitquery-name').setValue(null);
					Ext.getCmp('fundquery-sinvest_limit').setValue(null);
					Ext.getCmp('fundquery-einvest_limit').setValue(null);
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
					form.findField('mobile').setReadOnly(true);
					form.findField('invest').setReadOnly(true);
					form.findField('opDate').setReadOnly(true);
					form.findField('fund').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});

