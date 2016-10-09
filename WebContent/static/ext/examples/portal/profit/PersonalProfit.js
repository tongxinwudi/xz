//用户盈利情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);
Ext.require(['Ext.Window', 'Ext.fx.target.Sprite', 'Ext.layout.container.Fit']);

Ext.define('Forestry.app.profit.PersonalProfit', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [Ext.create('Forestry.app.report.HumidityReport1') ,   Ext.create('Forestry.app.profit.PersonalProfit1', {
				cButtons : me.cButtons,
				cName : me.cName
			})]
		});
		this.callParent(arguments);
	}
});




 

Ext.define('graphData',{
    extend:'Ext.data.Model',
    fields:[
        {name:'date',type:'string'},
        {name:'invest',type:'String'},
        {name:'investWithCashbao',type:'String'},
        {name:'redemp',type:'string'},
        {name:'redempWithCashbao',type:'string'}
    ]
});
var graphDataStore=Ext.create('Ext.data.Store',{
    model:'graphData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/profit/getProfitGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

 
  
 

Ext.define('Forestry.app.report.HumidityReport1', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	width : '30%',
	split : true,
	initComponent : function() {
		var chart = new Ext.chart.Chart({
			  style: 'background:#fff',
	            animate: true,//是否显示动画效果
	            shadow: true,//是否显示阴影部分
	            store: graphDataStore,//序列
	             //tbar: ttoolbar1,
	            legend: {
	                position: 'bottom' //图例显示位置
	            },
	            //坐标轴定义
	            axes: [{
	                type: 'Numeric',
	                position: 'left',
	                fields: ['invest'],
	                label: {
	                    renderer: Ext.util.Format.numberRenderer('0,0')
	                },
	                title: '金额(元)',
	                grid: true,//是否显示横向网格线
	                minimum: 0
	            }, {
	                type: 'Category',
	                position: 'bottom',
	                fields: ['date'],
	                title: '年月',
	                grid: true //是否显示纵向网格线
	            }],
	            series: [{
	                type: 'column',
	                axis: 'left',
	                highlight: false, //高亮显示
	                //鼠标移动到柱子上所显示的数据提示框
	                tips: {
	                  trackMouse: true,//数据提示框是否跟着鼠标移动
	                  width: 300	,//提示框宽度
	                  height: 28,
	                  renderer: function (storeItem, item) {
	                      //this.setTitle(storeItem.get('date') + ' | ' + item.yField + ': ' + storeItem.get(item.yField)  );
	                	  this.setTitle(storeItem.get('date') + ' | ' +  storeItem.get(item.yField));
	                  }
	                },
	                title: ['申购额', '申购额(含活期宝)', '赎回额', '赎回额(含活期宝)'],
	                label: {
	                  //display: 'insideEnd',
	                  'text-anchor': 'middle',
	                    field: ['invest','investWithCashbao' , 'redemp', 'redempWithCashbao'],
	                    renderer: Ext.util.Format.numberRenderer('0'),
	                    orientation: 'vertical',
	                    color: '#333'
	                },
	                xField: 'date',
	                yField: ['invest','investWithCashbao' , 'redemp', 'redempWithCashbao']
	            }] 
		});


		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			items : [chart]
		});
 
		
		Ext.apply(this, {
			layout : 'fit',
			region : "center",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

 
 
Ext.define('Forestry.app.profit.PersonalProfit1', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'south',
	split  : 'true',
	height : '50%',
	width:   '100%',
	initComponent : function() {
		var me = this;

		Ext.define('ModelList', {
			extend : 'Ext.data.Model',
			idProperty : 'id',
			fields : [ {
				name : 'id',
				type : 'String'
			}, 'name','mobile', {
				name : 'profit',
				type : 'String' 
			}, {
				name : 'invest',
				type : 'String' 
			}, {
				name : 'redemping',
				type : 'String' 
			},'rates'  ]
		});

		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/profit/getProfit',
				extraParams : me.extraParams || null,
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'totalRecord',
					successProperty : "success"
				}
			},
			sorters : [ {
				property : 'invest',
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
			width : '13%'
		}, {
			text : "收益",
			dataIndex : 'profit',
			width : '10%'
		}, {
			text : "投资额",
			dataIndex : 'invest',
			width : '15%',
			sortable : true
		} , {
			text : "赎回额",
			dataIndex : 'redemping',
			width : '10%',
			sortable : true
		} ,{
			text : "盈利比例",
			dataIndex : 'rates',
			width : '17%',
			sortable : false
		}  ];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'profitquery-mobile',
				name : 'mobile',
				fieldLabel : '手机号',
				labelWidth : 60,
				width : '25%'
			}, {
				xtype : 'textfield',
				id : 'profitquery-name',
				name : 'name',
				fieldLabel : '姓名',
				labelWidth : 30,
				width : '20%'
			},    {
				xtype:'checkboxgroup',
				id:'profitquery-cashbao',  
				width : '7%',
                //id: 'cashbao', 
                fieldLabel: '',
                columnWidth:5,   
                items: [  
                    { boxLabel: '活期宝', name: 'cashbao', inputValue: '1' } 
                   
                ]} 
			,{
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						mobile : Ext.getCmp('profitquery-mobile').getValue(),
						cashbao : Ext.getCmp('profitquery-cashbao').items.get(0).checked,
						name : Ext.getCmp('profitquery-name').getValue() 
						 
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
					Ext.getCmp('profitquery-mobile').setValue(null);
					Ext.getCmp('profitquery-name').setValue(null);
					Ext.getCmp('profitquery-cashbao').items.get(0).setValue(false);
					//var xq = '';
				   // for(var i = 0; i < xqCheck.length; i++){
					// if(xqCheck.get(i).checked){
					//	 xqCheck.get(i).setValue(false);
					 // xq += '+' + xqCheck.get(i).boxLabel;
					 //  }
					//}
					 //  Ext.MessageBox.alert('提示', '您的兴趣是' + xq);
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
					form.findField('profit').setReadOnly(true);
					form.findField('invest').setReadOnly(true);
					form.findField('redemping').setReadOnly(true);
					form.findField('rates').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});