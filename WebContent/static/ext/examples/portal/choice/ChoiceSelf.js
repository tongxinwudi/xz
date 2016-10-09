//自选基金

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.choice.ChoiceSelf', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			width: 1000,
		    height: 800,
			layout :  {
		        type: 'table',
		        columns: 2
		    },
			items : [ Ext.create('Forestry.app.choice.ChoiceSelfPieSumGraph'), Ext.create('Forestry.app.choice.ChoiceSelfLineSumGraph'),Ext.create('Forestry.app.choice.ChoiceSelfPieNumGraph'), Ext.create('Forestry.app.choice.ChoiceSelfLineRedempGraph')]
		});
		this.callParent(arguments);
	}
});

 
Ext.define('graphDataPieSum',{
    extend:'Ext.data.Model',
    fields:[
        {name:'graphName',type:'string'},
        {name:'graphData',type:'String'},
        {name:'rates',type:'string'}
    ]
});
var graphPieSumDataStore=Ext.create('Ext.data.Store',{
    model:'graphDataPieSum',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getChoiceSelfGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

Ext.define('Forestry.app.choice.ChoiceSelfPieSumGraph', {
	extend : 'Ext.panel.Panel',
	region : 'north',
	height : '50%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
		    width: 250,
		    height: 300,
		    store: graphPieSumDataStore,
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
					width :400,
					height : 25,

					renderer : function(graphDataStore, item) {
							this.setTitle( graphDataStore.get('graphName') +"申购总额为" +  graphDataStore.get('graphData')+"元,比例为：" +graphDataStore.get('rates') );
					}
				},  
				 title: ['股票基金', '债券基金', '货币基金','混合基金', '指数基金','保本基金','ETF','QDII','其他','活期宝'],
		        label: {// 这里能够使拼饼上面显示，该拼饼属于的部分
		            field: 'rates' ,
		            display: 'rotate',
		            font: '12px Arial'
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
			region : "center",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});


Ext.define('monthLineSumGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'date',
		type : 'String'
	}, {
		name : 'stock_fund',
		type : 'int'
	}, {
		name : 'bond_fund',
		type : 'int'
	}, {
		name : 'money_fund',
		type : 'int'
	}, {
		name : 'mix_fund',
		type : 'int'
	}, {
		name : 'index_fund',
		type : 'int'
	}, {
		name : 'preserv_fund',
		type : 'int'
	} , {
		name : 'etf_fund',
		type : 'int'
	}, {
		name : 'qdii_fund',
		type : 'int'
	} , {
		name : 'other_fund',
		type : 'int'
	} , {
		name : 'cashbao',
		type : 'int'
	}  ]
});

var monthLineSumGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'monthLineSumGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSelfMonthSumlineGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'date',
		direction : 'ASC'
	} ]
});

Ext.define('Forestry.app.choice.ChoiceSelfLineSumGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	height : '300',
	width : '850',
	split : true,
	initComponent : function() {

		var chart = new Ext.chart.Chart({
			width : 800,
			height : 300,
			style : 'background:#fff',
			animate : true,
			store : monthLineSumGraphDataStore,
			shadow : true,
			theme : 'Category1',
			legend : {
				position : 'right'
			},
			axes : [ {
				type : 'Numeric',
				 //minimum : 0,
				// maximum :1000000,
				position : 'left',
				fields :['stock_fund', 'bond_fund', 'money_fund','mix_fund', 'index_fund','preserv_fund','etf_fund','qdii_fund','other_fund','cashbao'],
				//title : '用户数',
				minorTickSteps : 1,
			    title: '申购金额(万元)',
				grid : {
					odd : {
						opacity : 1,
						fill : '#ddd',
						stroke : '#bbb',
						'stroke-width' : 0.5
					}
				}
			}, {
				type : 'Category',
				grid: true,
				position : 'bottom',
				fields : [ 'date' ],
				title : '月份'
			} ],
			series : [ {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'stock_fund',
				title : '股票基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('stock_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'bond_fund',
				title : '债券基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('bond_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'money_fund',
				title : '货币基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('money_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'mix_fund',
				title : '混合基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('mix_fund'));
					}
				}
			},{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'index_fund',
				title : '指数基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('index_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'preserv_fund',
				title : '保本基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('preserv_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'etf_fund',
				title : 'ETF',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('etf_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'qdii_fund',
				title : 'QDII',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('qdii_fund'));
					}
				}
			}  ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'other_fund',
				title : '其它',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('other_fund'));
					}
				}
			},{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'cashbao',
				title : '活期宝',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('cashbao'));
					}
				}
			}  ]
		});

		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			//autoScroll : true,
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			//region : "south",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

Ext.define('graphDataPieNum',{
    extend:'Ext.data.Model',
    fields:[
        {name:'graphName',type:'string'},
        {name:'graphData',type:'String'},
        {name:'rates',type:'string'}
    ]
});
var graphPieNumDataStore=Ext.create('Ext.data.Store',{
    model:'graphDataPieNum',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getChoiceRedempSelfGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

Ext.define('Forestry.app.choice.ChoiceSelfPieNumGraph', {
	extend : 'Ext.panel.Panel',
	region : 'north',
	height : '50%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
		    width: 250,
		    height: 300,
		    store: graphPieNumDataStore,
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
					width :400,
					height : 25,

					renderer : function(graphDataStore, item) {
							this.setTitle( graphDataStore.get('graphName') +"申购总额为" +  graphDataStore.get('graphData')+"元,比例为：" +graphDataStore.get('rates') );
					}
				},  
				 title: ['股票基金', '债券基金', '货币基金','混合基金', '指数基金','保本基金','ETF','QDII','其他','活期宝'],
		        label: {// 这里能够使拼饼上面显示，该拼饼属于的部分
		            field: 'rates' ,
		            display: 'rotate',
		            font: '12px Arial'
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
			region : "center",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

Ext.define('monthLineRedempGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'date',
		type : 'String'
	}, {
		name : 'stock_fund',
		type : 'int'
	}, {
		name : 'bond_fund',
		type : 'int'
	}, {
		name : 'money_fund',
		type : 'int'
	}, {
		name : 'mix_fund',
		type : 'int'
	}, {
		name : 'index_fund',
		type : 'int'
	}, {
		name : 'preserv_fund',
		type : 'int'
	} , {
		name : 'etf_fund',
		type : 'int'
	}, {
		name : 'qdii_fund',
		type : 'int'
	} , {
		name : 'other_fund',
		type : 'int'
	} , {
		name : 'cashbao',
		type : 'int'
	}  ]
});

var monthLineRedempGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'monthLineRedempGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSelfMonthRedemplineGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'date',
		direction : 'ASC'
	} ]
});

Ext.define('Forestry.app.choice.ChoiceSelfLineRedempGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	height : '300',
	width : '300',
	split : true,
	initComponent : function() {

		var chart = new Ext.chart.Chart({
			width : 800,
			height : 300,
			style : 'background:#fff',
			animate : true,
			store : monthLineRedempGraphDataStore,
			shadow : true,
			theme : 'Category1',
			legend : {
				position : 'right'
			},
			axes : [ {
				type : 'Numeric',
				 //minimum : 0,
				// maximum :1000000,
				position : 'left',
				fields :['stock_fund', 'bond_fund', 'money_fund','mix_fund', 'index_fund','preserv_fund','etf_fund','qdii_fund','other_fund','cashbao'],
				//title : '用户数',
				minorTickSteps : 1,
			    title: '赎回金额(万元)',
				grid : {
					odd : {
						opacity : 1,
						fill : '#ddd',
						stroke : '#bbb',
						'stroke-width' : 0.5
					}
				}
			}, {
				type : 'Category',
				grid: true,
				position : 'bottom',
				fields : [ 'date' ],
				title : '月份'
			} ],
			series : [ {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'stock_fund',
				title : '股票基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('stock_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'bond_fund',
				title : '债券基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('bond_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'money_fund',
				title : '货币基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('money_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'mix_fund',
				title : '混合基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('mix_fund'));
					}
				}
			},{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'index_fund',
				title : '指数基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('index_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'preserv_fund',
				title : '保本基金',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('preserv_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'etf_fund',
				title : 'ETF',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('etf_fund'));
					}
				}
			} ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'qdii_fund',
				title : 'QDII',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('qdii_fund'));
					}
				}
			}  ,{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'other_fund',
				title : '其它',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('other_fund'));
					}
				}
			},{
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'date',
				yField : 'cashbao',
				title : '活期宝',
				markerConfig : {
					type : 'circle',
					size : 4,
					radius : 4,
					'stroke-width' : 0
				},
				tips : {
					trackMouse : true,
					width : 80,
					height : 40,
					renderer : function(storeItem, item) {
						this.setTitle(storeItem.get('date'));
						this.update(storeItem.get('cashbao'));
					}
				}
			}  ]
		});

		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			//autoScroll : true,
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			//region : "south",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

 