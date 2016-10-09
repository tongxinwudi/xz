//用户盈利情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.fund.FundHold', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [  Ext.create('Forestry.app.fund.FundHoldList')]
		
		});
		this.callParent(arguments);
	}
});

Ext.define('graphData', {
	extend : 'Ext.data.Model',
	fields : [{
		name : 'fdcode',
		type : 'String'
	}, {
		name : 'abbrev',
		type : 'String'
	},{
		name : 'holdTime',
		type : 'long'
	}]
});

var graphDataStore=Ext.create('Ext.data.Store',{
    model:'graphData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getFundHoldGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'holdTime',
		direction : 'ASC'
	} ]
});

Ext.define('Ext.chart.theme.CustomBlue', {
    extend: 'Ext.chart.theme.Base',
    
    constructor: function(config) {
        var titleLabel = {
            font: 'bold 18px Arial'
        }, axisLabel = {
            fill: 'rgb(8,69,148)',
            font: '12px Arial',
            spacing: 2,
            padding: 5
        };
        
        this.callParent([Ext.apply({
           axis: {
               stroke: '#084594'
           },
           axisLabelLeft: axisLabel,
           axisLabelBottom: axisLabel,
           axisTitleLeft: titleLabel,
           axisTitleBottom: titleLabel
       }, config)]);
    }
});

Ext.define('Forestry.app.fund.FundHoldGraph', {
	extend : 'Ext.panel.Panel',
	region : 'north',
	height : '50%',
	split : true,
	initComponent : function() {
		
		var chart = Ext.create('Ext.chart.Chart',{
		    animate: true,
		    shadow: true,
		    store: graphDataStore,
		    height : '200000',
		    axes: [{
                type: 'Numeric',
                position: 'bottom',
                fields: ['holdTime'],
                label: {
                    renderer: Ext.util.Format.numberRenderer('0,0')
                },
                title: '持有时间',
                grid: true,
                minimum: 0
            }, {
                type: 'Category',
                position: 'left',
                fields: ['abbrev'],
                title: '基金名称'
            }],
            theme: 'CustomBlue',
            background: {
              gradient: {
                id: 'backgroundGradient',
                angle: 45,
                stops: {
                  0: {
                    color: '#ffffff'
                  },
                  100: {
                    color: '#eaf1f8'
                  }
                }
              }
            },
		    series: [{
                type: 'bar',
                axis: 'bottom',
                highlight: true,
                //style: { width: 20 },
                tips: {
                  trackMouse: true,
                  width: 140,
                  height: 28,
                  renderer: function(storeItem, item) {
                    this.setTitle(storeItem.get('abbrev') + ': ' + storeItem.get('holdTime') + ' 天');
                  }
                },
                label: {
                  display: 'insideEnd',
                    field: 'holdTime',
                    renderer: Ext.util.Format.numberRenderer('0'),
                    orientation: 'horizontal',
                    color: '#333','text-anchor': 'middle',
                },
                xField: 'abbrev',
                yField: ['holdTime']
            }]
		});
		
		
		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			autoScroll : true,
			items : chart
		});

		Ext.apply(this, {
			layout : 'fit',
			region : "center",
			items : [ chart ]
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
		name : 'fdcode',
		type : 'String'
	}, {
		name : 'name',
		type : 'String'
	},{
		name : 'abbrev',
		type : 'String'
	},{
		name : 'holdTime',
		type : 'long'
	}]
});

var forestryquerystore = Ext.create('Ext.data.Store', {
	model : 'ModelList',
	autoLoad : true,
	remoteSort : true,
	pageSize : globalPageSize,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/fund/getFundHold',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data',
			totalProperty : 'totalRecord',
			successProperty : "success"
		}
	},
	sorters : [ {
		property : 'holdTime',
		direction : 'DESC'
	} ]
});

var columns = [/* {
	text : "ID",
	dataIndex : 'id',
	width : '10%'
},*/ {
	text : "基金代码",
	dataIndex : 'fdcode',
	width : '8%'
}, {
	text : "基金名称",
	dataIndex : 'abbrev',
	width : '12%'
}, {
	text : "持有时间",
	dataIndex : 'holdTime',
	width : '80%',
	renderer: function (data, metadata, record, rowIndex, columnIndex, store) {
		if(data>0){
	      var html='<table><tr><td width="'+(data/50)+'" style="background-color: rgb(148, 174, 10);">'+data+'</td></tr></table>';
	      return html;  
		}else
			return 0;
	  }
} ];
 

Ext.define('Forestry.app.fund.FundHoldList', {
	extend : 'Ext.grid.Panel',
	viewConfig:{  
		   enableTextSelection:true  
		}  ,
	region : 'center',
	//height : '50%',
	initComponent : function() {
		var me = this;

		var fundDataType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['0','全部基金'],['99','现金宝'],['1', '股票基金'], ['2', '债券基金'],['3', '货币基金'], ['4', '混合基金'],['5', '指数基金'], ['6', '保本'],['7', 'ETF'], ['8', 'QDII'], ['20', '其他']]  
        }); 
		
		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ {
				xtype : 'textfield',
				id : 'fund-fdcode_hold',
				name : 'fdcode',
				fieldLabel : '基金代码',
				labelWidth : 60,
				width : '20%'
			},  {
				xtype : 'textfield',
				id : 'fund-name_hold',
				name : 'name',
				fieldLabel : '基金名称',
				labelWidth : 60,
				width : '25%'
			}, {
				xtype : 'combobox',
				fieldLabel : '基金类型',
				id : 'fund-type_hold',
				name : 'type',
				store : fundDataType,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '请选择...',
				editable : false,
				labelWidth : 60,
				width : '30%',
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
						fdcode : Ext.getCmp('fund-fdcode_hold').getValue(),
						name : Ext.getCmp('fund-name_hold').getValue(),
						type: Ext.getCmp('fund-type_hold').getValue()
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
					Ext.getCmp('fund-fdcode_hold').setValue(null);
					Ext.getCmp('fund-name_hold').setValue(null);
					Ext.getCmp('fund-type_hold').setValue(null);
					 
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
					//form.findField('name').setReadOnly(true);
					form.findField('abbrev').setReadOnly(true);
					form.findField('holdTime').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});

