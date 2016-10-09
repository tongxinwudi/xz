// 权限管理
Ext.define('Forestry.app.choice.ChoiceSchema', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			width: 1000,
		    height: 600,
			layout :  {
		        type: 'table',
		        columns: 3
		    },
		    //defaults: { frame: true, width: 300, height: 300 },
			items : [ Ext.create('Forestry.app.choice.ChoiceSchema.TypeGrid'),
					Ext.create('Forestry.app.choice.ChoiceSchema.AgeGraph'),
					Ext.create('Forestry.app.choice.ChoiceSchema.MonthGraph'),
					Ext.create('Forestry.app.choice.ChoiceSchema.SexGraph'),
					Ext.create('Forestry.app.choice.ChoiceSchema.AgeSumGraph'),
					Ext.create('Forestry.app.choice.ChoiceSchema.MonthSumGraph')]
		});
		this.callParent(arguments);
	}
});

// 组合列表
Ext.define('Forestry.app.choice.ChoiceSchema.TypeGrid', {
	extend : 'Ext.grid.Panel',
	id : 'choiceSchema-typeGrid',
	width : 200,
	height: 350,
	//split: true,
	initComponent : function() {
		var me = this;
		Ext.define('SchemaTypeList', {
			extend : 'Ext.data.Model',
			idProperty : 'key',
			fields : [ {
				name : 'key',
				type : 'short'
			}, 'value' ]
		});

		var schematypestore = new Ext.data.SimpleStore({
			fields : [ 'key', 'value' ],
			data : [ [ '1', '理财增值' ], [ '2', '退休养老' ], [ '3', '买房置业' ],
					[ '4', '子女教育' ], [ '5', '结婚生子' ], [ '6', '小额、短期理财' ],
					[ '7', '新20档组合' ] ]
		});
		var schemetypecolumns = [ {
			text : "类型值",
			dataIndex : 'key',
			hidden : true,
			sortable : false
		}, {
			text : "组合类型",
			dataIndex : 'value',
			sortable : false,
			width : '85%',
			editor : {
				allowBlank : false
			}
		} ];
		Ext.apply(this, {
			store : schematypestore,
			//selModel : Ext.create('Ext.selection.CheckboxModel'),
			columns : schemetypecolumns,
			listeners : {
				'itemclick' : function(item, record) {
					var searchParams = {
						type : record.get('key')
					};
					Ext.apply(sexGraphDataStore.proxy.extraParams,searchParams);
					Ext.apply(ageGraphDataStore.proxy.extraParams,searchParams);
					Ext.apply(monthGraphDataStore.proxy.extraParams,searchParams);
					Ext.apply(ageSumGraphDataStore.proxy.extraParams,searchParams);
					Ext.apply(monthSumGraphDataStore.proxy.extraParams,searchParams);
					
					sexGraphDataStore.reload();
					ageGraphDataStore.reload();
					monthGraphDataStore.reload();
					ageSumGraphDataStore.reload();
					monthSumGraphDataStore.reload();
				}
			}
		});
		this.callParent(arguments);
	}
});

Ext.define('sexGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'sex',
		type : 'String'
	}, {
		name : 'count',
		type : 'long'
	} ]
});

var sexGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'sexGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSchemaSexGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'sex',
		direction : 'DESC	'
	} ]
});

Ext.define('ageGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'age',
		type : 'String'
	}, {
		name : 'count',
		type : 'long'
	} ]
});

var ageGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'ageGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSchemaAgeGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'age',
		direction : 'DESC	'
	} ]
});

Ext.define('monthGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'opDate',
		type : 'String'
	}, {
		name : '90s',
		type : 'int'
	}, {
		name : '80s',
		type : 'int'
	}, {
		name : '70s',
		type : 'int'
	}, {
		name : '60s',
		type : 'int'
	}, {
		name : '50s',
		type : 'int'
	}, {
		name : 'other',
		type : 'int'
	} ]
});

var monthGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'monthGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSchemaMonthDistribute',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'opDate',
		direction : 'ASC'
	} ]
});

Ext.define('ageSumGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'age',
		type : 'String'
	}, {
		name : 'count',
		type : 'double'
	} ]
});

var ageSumGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'ageSumGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSchemaAgeSumGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'age',
		direction : 'DESC	'
	} ]
});

Ext.define('monthSumGraphData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'opDate',
		type : 'String'
	}, {
		name : '90s',
		type : 'double'
	}, {
		name : '80s',
		type : 'double'
	}, {
		name : '70s',
		type : 'double'
	}, {
		name : '60s',
		type : 'double'
	}, {
		name : '50s',
		type : 'double'
	}, {
		name : 'other',
		type : 'double'
	} ]
});

var monthSumGraphDataStore = Ext.create('Ext.data.Store', {
	model : 'monthSumGraphData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/choice/getSchemaMonthSumDistribute',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'opDate',
		direction : 'ASC'
	} ]
});

// 性别分布图
Ext.define('Forestry.app.choice.ChoiceSchema.SexGraph', {
	extend : 'Ext.panel.Panel',
	split : true,
	width : 180,
	
	initComponent : function() {

		var chart = new Ext.chart.Chart({
			width : 130,
			height : 160,
			animate : true,
			store : sexGraphDataStore,
			renderTo : Ext.getBody(),
			shadow : false,
			series : [ {
				type : 'pie',
				field : 'count',
				tips : {
					trackMouse : true,
					width : 200,
					height : 28,
					renderer : function(graphDataStore, item) {
						var total = 0;
						sexGraphDataStore.each(function(rec) {
							total += rec.get('count');
						});
						this.setTitle("用户数："
								+ graphDataStore.get('count')
								+ "，所占比例："
								+ Math.round(graphDataStore.get('count')
										/ total * 100) + "%");
					}
				},
				label : {// 这里能够使拼饼上面显示，该拼饼属于的部分
					field : 'sex',
					display : 'rotate',
					// constrast:true,
					font : '14px Arial',
				},
				highlight : {// 这里是突出效果的声明，margin越大，鼠标悬停在拼饼上面，拼饼突出得越多
					segment : {
						margin : 10
					}
				}
			} ]
		});
		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			autoScroll : true,
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			//region : "center",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

// 年龄分布图
Ext.define('Forestry.app.choice.ChoiceSchema.AgeGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	split : true,
	//heigth : '300',
	width : '300',
	
	initComponent : function() {

		var chart = Ext.create('Ext.chart.Chart', {
			width : 300,
			height : 200,
			animate : true,
			shadow : true,
			store : ageGraphDataStore,
			axes : [ {
				type : 'Numeric',
				position : 'left',
				fields : [ 'count' ],
				label : {
					renderer : Ext.util.Format.numberRenderer('0,0')
				},
				title : '用户数',
				grid : true,
				minimum : 0
			}, {
				type : 'Category',
				position : 'bottom',
				fields : [ 'age' ],
				title : '年龄'
			} ],
			//theme : 'CustomBlue',
			background : {
				gradient : {
					id : 'backgroundGradient',
					angle : 45,
					stops : {
						0 : {
							color : '#ffffff'
						},
						100 : {
							color : '#eaf1f8'
						}
					}
				}
			},
			series : [ {
				type : 'column',
				axis : 'left',
				highlight : true,
				label : {
					display : 'outside',
					'text-anchor' : 'middle',
					field : 'count',
					renderer : Ext.util.Format.numberRenderer('0'),
					orientation : 'horizontal',
					color : '#333',
				},
				xField : 'age',
				yField : 'count'
			} ]
		});

		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			autoScroll : true,
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			//region : "east",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

// 时间分布
Ext.define('Forestry.app.choice.ChoiceSchema.MonthGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	height : '200',
	width : '650',
	split : true,
	initComponent : function() {

		var chart = new Ext.chart.Chart({
			width : 500,
			height : 300,
			style : 'background:#fff',
			animate : true,
			store : monthGraphDataStore,
			shadow : true,
			theme : 'Category1',
			legend : {
				position : 'right'
			},
			axes : [ {
				type : 'Numeric',
				//minimum : 0,
				position : 'left',
				fields : [ '90s', '80s', '70s', '60s', '50s', 'other' ],
				//title : '用户数',
				minorTickSteps : 1,
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
				fields : [ 'opDate' ],
				title : '年/月'
			} ],
			series : [ {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'opDate',
				yField : '90s',
				title : '90后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('90s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '80s',
				title : '80后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('80s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '70s',
				title : '70后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('70s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '60s',
				title : '60后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('60s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '50s',
				title : '50后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('50s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : 'other',
				title : '其他',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('other'));
					}
				}
			} ]
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

//投资额的年龄分布图
Ext.define('Forestry.app.choice.ChoiceSchema.AgeSumGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	split : true,
	//heigth : '300',
	width : '300',
	
	initComponent : function() {

		var chart = Ext.create('Ext.chart.Chart', {
			width : 400,
			height : 200,
			animate : true,
			shadow : true,
			store : ageSumGraphDataStore,
			axes : [ {
				type : 'Numeric',
				position : 'left',
				fields : [ 'count' ],
				label : {
					renderer : Ext.util.Format.numberRenderer('0,0')
				},
				title : '金额(元)',
				grid : true,
				minimum : 0
			}, {
				type : 'Category',
				position : 'bottom',
				fields : [ 'age' ],
				title : '年龄'
			} ],
			//theme : 'CustomBlue',
			background : {
				gradient : {
					id : 'backgroundGradient',
					angle : 45,
					stops : {
						0 : {
							color : '#ffffff'
						},
						100 : {
							color : '#eaf1f8'
						}
					}
				}
			},
			series : [ {
				type : 'column',
				axis : 'left',
				highlight : true,
				label : {
					display : 'outside',
					'text-anchor' : 'middle',
					field : 'count',
					renderer : Ext.util.Format.numberRenderer('0'),
					orientation : 'horizontal',
					color : '#333',
				},
				xField : 'age',
				yField : 'count'
			} ]
		});

		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			autoScroll : true,
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			//region : "east",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

//投资额的时间分布
Ext.define('Forestry.app.choice.ChoiceSchema.MonthSumGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	height : '300',
	width : '650',
	split : true,
	initComponent : function() {

		var chart = new Ext.chart.Chart({
			width : 500,
			height : 200,
			style : 'background:#fff',
			animate : true,
			store : monthSumGraphDataStore,
			shadow : true,
			theme : 'Category1',
			legend : {
				position : 'right'
			},
			axes : [ {
				type : 'Numeric',
				//minimum : 0,
				position : 'left',
				fields : [ '90s', '80s', '70s', '60s', '50s', 'other' ],
				//title : '用户数',
				minorTickSteps : 1,
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
				fields : [ 'opDate' ],
				title : '年/月'
			} ],
			series : [ {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				xField : 'opDate',
				yField : '90s',
				title : '90后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('90s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '80s',
				title : '80后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('80s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '70s',
				title : '70后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('70s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '60s',
				title : '60后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('60s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : '50s',
				title : '50后',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('50s'));
					}
				}
			}, {
				type : 'line',
				highlight : {
					size : 7,
					radius : 7
				},
				axis : 'left',
				smooth : true,
				xField : 'opDate',
				yField : 'other',
				title : '其他',
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
						this.setTitle(storeItem.get('opDate'));
						this.update(storeItem.get('other'));
					}
				}
			} ]
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