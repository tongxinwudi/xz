/**
 * 错误统计报告
 */

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.error.ErrorReport', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.report.ErrorManage'), Ext.create('Forestry.app.report.ErrorGraph'),Ext.create('Forestry.app.report.ErrorMonthGraph')]
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
        {name:'errorType',type:'int'},
        {name:'title',type:'String'},
        {name:'count',type:'long'},
        {name:'ratio',type:'String'}
    ]
});
var graphDataStore=Ext.create('Ext.data.Store',{
    model:'graphData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/error/getCountGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});



Ext.define('Forestry.app.report.ErrorGraph', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	width : '30%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
		    width: 480,
		    height: 400,
		    animate: true,
		    store: graphDataStore,
		    renderTo: Ext.getBody(),
		    shadow: false,//一定会关闭阴影，否则拼饼突出的时候很不好看。
            //insetPadding: 60,
            //theme: 'Base:gradients',
		    //legend : {
	        //    position : 'right' 
	        //    },
		    series: [{
		        type: 'pie',
		        field:'count',
		        //showInLegend : true,
		        //donut:false,
				tips : {
					trackMouse : true,
					width :250,
					height : 28,

					renderer : function(graphDataStore, item) {
						this.setTitle("错误数："+graphDataStore.get('count')+"，所占比例："+graphDataStore.get('ratio')+"%");
					}
				},  
		        label: {// 这里能够使拼饼上面显示，该拼饼属于的部分
		            field: 'title' ,
		            display: 'rotate',
		            //constrast:true,
		            font: '14px Arial',
		        },
		        highlight: {//这里是突出效果的声明，margin越大，鼠标悬停在拼饼上面，拼饼突出得越多
		            segment: {
		                margin: 10
		            }
		        },
		        
		        listeners: {//This Doesnt Work :(
		        	   itemclick: function(obj){
		        		   var searchParams = {
		   						type : obj.storeItem.data['errorType']
		   					};
		        		   Ext.apply(forestryquerystore.proxy.extraParams, searchParams);
		        		   forestryquerystore.reload();
		        		   //alert(obj.storeItem.data['title'] + ' &' + obj.storeItem.data['count']);
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

Ext.define('monthData',{
    extend:'Ext.data.Model',
    fields:[
        {name:'ym',type:'String'},
        {name:'fund',type:'int'},
        {name:'cash',type:'int'},
        {name:'user',type:'int'}
    ]
});
var monthDataStore=Ext.create('Ext.data.Store',{
    model:'monthData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/error/getCountMonth',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});



Ext.define('Forestry.app.report.ErrorMonthGraph', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	//height : '50%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
			style: 'background:#fff',
		    animate: true,
		    store: monthDataStore,
		    shadow: true,
            theme: 'Category1',
            legend: {
                position: 'right'
            },
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['fund', 'cash', 'user'],
                title: '错误数',
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
                fields: ['ym'],
                title: '时间：年/月'
            }],
            series: [{
                type: 'line',
                highlight: {
                    size: 7,
                    radius: 7
                },
                axis: 'left',
                xField: 'ym',
                yField: 'fund',
                title : '基金交易类错误',
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
                        this.setTitle(storeItem.get('ym'));
                        this.update(storeItem.get('fund'));
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
                xField: 'ym',
                yField: 'cash',
                title:'活期宝类错误',
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
                        this.setTitle(storeItem.get('ym'));
                        this.update(storeItem.get('cash'));
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
                xField: 'ym',
                yField: 'user',
                title: '绑卡类错误',
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
                        this.setTitle(storeItem.get('ym'));
                        this.update(storeItem.get('user'));
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

Ext.define('ModelList', {
	extend : 'Ext.data.Model',
	idProperty : 'id',
	fields : [ {
					name : 'id',
					type : 'long'
				},{
					name : 'uid',
					type : 'String'
	           },
	           {
	        	   name : 'name',
	        	   type : 'String'
	           },
	           {
	        	   name : 'mobile',
	        	   type : 'String'
	           },
	           {
	        	   name : 'reason',
	        	   type : 'String'
	           },{
					name : 'opDate',
					type : 'datetime'
				},
	            'state','errorType'
	         ]
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
		url : appBaseUri + '/sys/error/getErrorList',
		extraParams : null,//me.extraParams || 
		reader : {
			type : 'json',
			root : 'data',
			totalProperty : 'totalRecord',
			successProperty : "success"
		}
	},
	sorters : [ {
		property : 'uid',
		direction : 'DESC'
	} ]
});


Ext.define('Forestry.app.report.ErrorManage', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'south',
	width : '100%',
	height : '50%',
	initComponent : function() {
		var me = this;
		var columns = [ {
			text : "ID",
			dataIndex : 'id',
			width : '5%'
		},{
			text : "UID",
			dataIndex : 'uid',
			width : '20%'
		}, {
			text : "姓名",
			dataIndex : 'name',
			width : '10%'
		}, {
			text : "手机号",
			dataIndex : 'mobile',
			width : '15%'
		}, {
			text : "错误原因",
			dataIndex : 'reason',
			width : '30%'
		}, {
			text : "错误类型",
			dataIndex : 'errorType',
			width : '15%',
			renderer: function(value){
				if (Ext.isEmpty(value)) {//判断是否是日期类型的数据  
			           return '未知错误类型';  
			    }else{
			    	switch(value){
			    	case 1:
			    		return '交易类错误';
			    	case 2:
			    		return '活期宝类错误';
			    	case 3:
			    		return '绑卡类错误';
			    	}
			    }
			}
		}, {
			text : "操作日期",
			dataIndex : 'opDate',
			width : '12%',
			renderer: function(value) {
		        if (Ext.isEmpty(value)) {//判断是否是日期类型的数据  
		           return '';  
		       } else {
		    	   return Ext.util.Format.date(new Date(parseInt(value)),'Y-m-d');
		       } 
			}
		}];
		
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'query-name_error',
				name : 'epcId',
				fieldLabel : '姓名',
				labelWidth : 60,
				width : '25%'
			}, {
				xtype : 'textfield',
				id : 'query-mobile_error',
				name : 'name',
				fieldLabel : '手机',
				labelWidth : 30,
				width : '20%'
			},{
	            xtype: 'datefield',
	            id : 'query-start_error',
	            name: 'start',
	            fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '20%'
	        },{
	            xtype: 'datefield',
	            id : 'query-end_error',
	            name: 'end',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '16%'
	        }, {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				//disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						name : Ext.getCmp('query-name_error').getValue(),
						mobile : Ext.getCmp('query-mobile_error').getValue(),
						queryStart : Ext.getCmp('query-start_error').getValue(),
						queryEnd : Ext.getCmp('query-end_error').getValue()
					};
					Ext.apply(forestryquerystore.proxy.extraParams, searchParams);
					forestryquerystore.reload();
					Ext.apply(graphDataStore.proxy.extraParams, searchParams);
					graphDataStore.reload();
					Ext.apply(monthDataStore.proxy.extraParams, searchParams);
					monthDataStore.reload();
				}
			}, {
				xtype : 'button',
				text : '重置',
				iconCls : 'icon-reset',
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					Ext.getCmp('query-name_error').setValue(null);
					Ext.getCmp('query-mobile_error').setValue(null);
				}
			} ]
		});

		Ext.apply(this, {
			store : forestryquerystore,
			columns : columns,
			tbar	: ttoolbar,
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
					form.findField('errorType').setReadOnly(true);
					form.findField('reason').setReadOnly(true);
					form.findField('opDate').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});