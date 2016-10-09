//用户申购情况

Ext.require([ 'Ext.chart.*', 'Ext.layout.container.Fit', 'Ext.window.MessageBox', 'Ext.grid.Panel' ]);

Ext.define('Forestry.app.operate.InvestChannel2', {
	extend : 'Ext.panel.Panel',
	initComponent : function() {
		var me = this;
		Ext.apply(this, {
			layout : 'border',
			items : [ Ext.create('Forestry.app.operate.Channel2', {
				cButtons : me.cButtons,
				cName : me.cName
			}),Ext.create('Forestry.app.report.channel2NumGraph'),Ext.create('Forestry.app.report.channel2RateGraph'),Ext.create('Forestry.app.report.channel2MoneyGraph')  ]
		});
		this.callParent(arguments);
	}
});

 

Ext.define('graphNumData',{
    extend:'Ext.data.Model',
    fields:[
        {name:'date',type:'string'},
        {name:'reg_num',type:'string'},
        {name:'bound_num',type:'string'},
        {name:'user_num',type:'String'},
        {name:'first_user_num',type:'String'},
        {name:'reg_num_ration',type:'String'},
        {name:'bound_num_ration',type:'String'},
        {name:'user_num_ration',type:'String'},
        {name:'first_user_num_ration',type:'String'}
        
    ]
});
var graphNumDataStore=Ext.create('Ext.data.Store',{
    model:'graphNumData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/operate/getChannel2NumGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	}
});

 
  
 

Ext.define('Forestry.app.report.channel2NumGraph', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	width : '40%',
	split : true,
	initComponent : function() {
		var chart = new Ext.chart.Chart({
			  style: 'background:#fff',
	            animate: true,//是否显示动画效果
	            shadow: true,//是否显示阴影部分
	            store: graphNumDataStore,//序列
	             //tbar: ttoolbar1,
	            legend: {
	                position: 'bottom' //图例显示位置
	            },
	            //坐标轴定义
	            axes: [{
	                type: 'Numeric',
	                position: 'left',
	                fields: ['reg_num'],
	                label: {
	                    renderer: Ext.util.Format.numberRenderer('0')
	                },
	                title: '人数',
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
	                  width: 200	,//提示框宽度
	                  height: 28,
	                  renderer: function (storeItem, item) {
	                       this.setTitle( '人数:' + storeItem.get(item.yField)+ ',环比: '+ storeItem.get(item.yField+'_ration') );
	                  }
	                },
	                title: ['注册用户数', '绑卡用户数', '用户申购数', '首申用户数'],
	                label: {
	                   display: 'insideEnd',
	                  'text-anchor': 'middle',
	                    field: ['reg_num','bound_num' , 'user_num', 'first_user_num'],
	                    renderer: Ext.util.Format.numberRenderer('0'),
	                    orientation: 'horizontal',
	                    color: '#333'
	                },
	                xField: 'date',
	                yField: ['reg_num','bound_num' , 'user_num', 'first_user_num']
	            }] 
		});


		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			items : [chart]
		});
 
		
		Ext.apply(this, {
			layout : 'fit',
			region : "west",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});

 

Ext.define('graphRateData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'date',
		type : 'String'
	}, {
		name : 'new_user_rate',
		type : 'String'
	} , {
		name : 'new_user_rate_ration',
		type : 'String'
	}]
});

var graphRateDataStore = Ext.create('Ext.data.Store', {
	model : 'graphRateData',
	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/operate/getChannel2RateGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'date',
		direction : 'asc'
	} ]
});


Ext.define('Forestry.app.report.channel2RateGraph', {
	extend : 'Ext.panel.Panel',
	// region : 'east',
	region : 'west',
	width : '30%',
	split : true,
	
	initComponent : function() {

		var chart = Ext.create('Ext.chart.Chart', {
			width : 400,
			height : 200,
			animate : true,
			shadow : true,
			store : graphRateDataStore,
			//legend: {
	           //     position: 'bottom' //图例显示位置
	         //   },
			axes : [ {
				type : 'Numeric',
				position : 'left',
				fields : [ 'new_user_rate' ],
				label : {
					renderer : Ext.util.Format.numberRenderer('0,0')
				},
				title : '新用户申购占比(%)',
				grid : true,
				minimum : 0
			}, {
				type : 'Category',
				position : 'bottom',
				fields : [ 'date' ],
				title : '日期'
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
					display : 'insideEnd',
					'text-anchor' : 'middle',
					field : 'new_user_rate',
					renderer : Ext.util.Format.numberRenderer('0'),
					orientation : 'horizontal',
					color : '#333',
				},
				 tips: {
	                  trackMouse: true,//数据提示框是否跟着鼠标移动
	                  width: 250	,//提示框宽度
	                  height: 28,
	                  renderer: function (storeItem, item) {
	                       this.setTitle( '新用户申购占比:' + storeItem.get(item.yField)+ '%,环比: '+ storeItem.get(item.yField+'_ration') );
	                  }
	                },
				xField : 'date',
				yField : 'new_user_rate'
			} ]
		});

		var panel1 = Ext.create('widget.panel', {
			layout : 'fit',
			//autoScroll : false,
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



Ext.define('graphMoneyData',{
    extend:'Ext.data.Model',
    fields:[
        {name:'date',type:'String'},
        {name:'new_money_num',type:'float'},
        {name:'new_money_num_ration',type:'String'}
    ]
});
var graphMoneyDataStore=Ext.create('Ext.data.Store',{
    model:'graphMoneyData',
    autoLoad : true,
    proxy : {
		type : 'ajax',
		url : appBaseUri + '/sys/operate/getChannel2MoneyGraph',
		extraParams : null,
		reader : {
			type : 'json',
			root : 'data'
		}
	},
	sorters : [ {
		property : 'date',
		direction : 'ASC'
	}]
});



Ext.define('Forestry.app.report.channel2MoneyGraph', {
	extend : 'Ext.panel.Panel',
	region : 'west',
	width : '30%',
	split : true,
	initComponent : function() {
		
		var chart = new Ext.chart.Chart({
			width : 200,
			height : 100,
			style: 'background:#fff',
		    animate: true,
		    store: graphMoneyDataStore,
		    shadow: true,
            theme: 'Category1',
           // legend: {
             //   position: 'right'
           // },
            axes: [{
                type: 'Numeric',
                // minimum: -100,
                position: 'left',
                fields:  'new_money_num' ,
                title: '新用户申购金额(元)',
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
                grid:  true,
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
                yField: 'new_money_num',
               // title : '新用户申购金额',
                markerConfig: {
                	type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0
                },
                tips: {
                    trackMouse: true,
                    width: 250,
                    height: 28,
                    renderer: function(storeItem, item) {
                        //this.setTitle(storeItem.get('date'));
                        this.setTitle("新用户申购额:"+storeItem.get('new_money_num')+"环比:" +storeItem.get('new_money_num_ration') );
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
			//region : "west",
			items : [ panel1 ]
		});

		this.callParent(arguments);
	}
});


Ext.define('Forestry.app.operate.Channel2', {
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
			//idProperty : 'date',
			 fields:[
			         {name:'date',type:'string'},
			         {name:'reg_num',type:'string'},
			         {name:'bound_num',type:'string'},
			         {name:'user_num',type:'String'},
			         {name:'first_user_num',type:'String'},
			         {name:'new_user_num',type:'String'},
			         {name:'new_user_rate',type:'String'},
			         {name:'new_money_num',type:'string'},
			         {name:'money_num',type:'string'}
			     
			     ]
		});

		var channelDateType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['7', '近一周'], ['30', '近一个月'], ['90', '近3个月'], ['', '全部']]  
        }); 
		
		var platformType = new Ext.data.SimpleStore({  
            fields : ['key', 'value'],  
            data : [['IOS', 'IOS'], ['ANDROID', 'ANDROID'], ['BROWSER', 'BROWSER'],['', '全部']]  
        }); 
		
		
		var channeltypeStore = Ext.create('Ext.data.JsonStore', {
			autoLoad : true,
			proxy : {
				type : 'ajax',
				 url : appBaseUri + '/sys/operate/getChannelTypeList',
				reader : {
					type : 'json',
					root : 'list',
					idProperty : 'key'
				}
			},
			fields : [ 'key', 'value' ]
		});
		
		var forestryquerystore = Ext.create('Ext.data.Store', {
			model : 'ModelList',
			// autoDestroy: true,
			autoLoad : true,
			remoteSort : true,
			pageSize : globalPageSize,
			proxy : {
				type : 'ajax',
				url : appBaseUri + '/sys/operate/getChannel2List',
			 	//proxyExportUrl : appBaseUri + '/sys/forestry/exportForestry',
				extraParams : me.extraParams || null,
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
			text : "日期",
			dataIndex : 'date',
			width : '10%'
		}  , {
			text : "注册人数",
			dataIndex : 'reg_num',
			width : '10%',
			sortable : true
		} ,{
			text : "绑卡人数",
			dataIndex : 'bound_num',
			width : '10%',
			sortable : true
		} ,{
			text : "申购用户数",
			dataIndex : 'user_num',
			width : '10%',
			sortable : true
		},{
			text : "首申用户数",
			dataIndex : 'first_user_num',
			width : '10%',
			sortable : true
		},{
			text : "新申购用户数",
			dataIndex : 'new_user_num',
			width : '10%',
			sortable : true
		},{
			text : "新用户申购占比",
			dataIndex : 'new_user_rate',
			width : '10%',
			sortable : true
		}, {
			text : "新用户申购金额",
			dataIndex : 'new_money_num',
			width : '10%',
			sortable : true
		},{
			text : "申购总金额",
			dataIndex : 'money_num',
			width : '10%',
			sortable : true
		}];

		var ttoolbar = Ext.create('Ext.toolbar.Toolbar', {
			items : [ 
			{
				xtype : 'combobox',
				fieldLabel : '平台',
				id : 'query-platform_investChannel2',
				name : 'platformtype',
				store : platformType,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '请选择',
				editable : true,
				labelWidth : 30,
				width : '20%',
				maxWidth : 150
			},{
				xtype : 'textfield',
				id : 'query-channel_investChannels2',
				name : 'channel',
				fieldLabel : '渠道',
				labelWidth : 30,
				width : '10%'
			},{
				xtype : 'combobox',
			//	fieldLabel : '平台',
				id : 'query-channel2_investChannel2',
				name : 'channeltype',
				store : channeltypeStore,
				valueField : 'key',
				displayField : 'value',
				typeAhead : true,
				queryMode : 'local',
				emptyText : '请选择',
				editable : false,
				labelWidth : 30,
				width : '20%',
				maxWidth : 150
				
			}, 
			{
				xtype : 'combobox',
				fieldLabel : '选择日期',
				id : 'query-date_investChannel2',
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
							  Ext.getCmp('query-start_investChannel2').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,0- record[0].get("key") ),"Y-m-d") );
						  }else{
							  Ext.getCmp('query-start_investChannel2').setValue('1970-01-01');
						  }
						  Ext.getCmp('query-end_investChannel2').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
						 
					   }  
				}
			},{
	            xtype: 'datefield',
	            id : 'query-start_investChannel2',
	            name: 'start',
	            //fieldLabel: '选择日期',
	            labelWidth : 60,
	            width : '10%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d")  
	        },{
	            xtype: 'datefield',
	            id : 'query-end_investChannel2',
	            name: 'end',
	            fieldLabel: '至',
	            labelWidth : 15,
	            width : '12%',
	            format:'Y-m-d',  
                value:Ext.util.Format.date( new Date() ,"Y-m-d")  
	        }, {
				xtype : 'button',
				text : '查询',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Query'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					var searchParams = {
						sdate :   Ext.util.Format.date(new Date( Ext.getCmp('query-start_investChannel2').getValue() ),'Y-m-d') ,
						edate :  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investChannel2').getValue() ),'Y-m-d'),
						platform : Ext.getCmp('query-platform_investChannel2').getValue(),
						channel : Ext.getCmp('query-channel_investChannels2').getValue(),
						channel2 : Ext.getCmp('query-channel2_investChannel2').getValue(),
						last : Ext.getCmp('query-date_investChannel2').getValue()
						 
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
					Ext.getCmp('query-start_investChannel2').setValue(Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-1),"Y-m-d") );
					Ext.getCmp('query-end_investChannel2').setValue(Ext.util.Format.date( new Date() ,"Y-m-d") );
					Ext.getCmp('query-channel_investChannels2').setValue(null);
					Ext.getCmp('query-date_investChannel2').setValue(null);
					Ext.getCmp('query-channel2_investChannel2').setValue(null);
					Ext.getCmp('query-platform_investChannel2').setValue(null);
				}
			}, {
				xtype : 'button',
				text : '导出',
				iconCls : 'icon-search',
				disabled : !globalObject.haveActionMenu(me.cButtons, 'Export'),
				width : '10%',
				maxWidth : 60,
				handler : function(btn, eventObj) {
					 
					sdate =Ext.util.Format.date(new Date( Ext.getCmp('query-start_investChannel2').getValue() ),'Y-m-d') ,
					edate =  Ext.util.Format.date(new Date( Ext.getCmp('query-end_investChannel2').getValue() ),'Y-m-d'),
					platform = Ext.getCmp('query-platform_investChannel2').getValue(),
					channel = Ext.getCmp('query-channel_investChannels2').getValue(),
					channel2 =Ext.getCmp('query-channel2_investChannel2').getValue(),
					last = Ext.getCmp('query-date_investChannel2').getValue() 
					 if(Ext.isEmpty(last,false)==true){
						 last='';
					 }
					 if(Ext.isEmpty(platform,false)==true){
						 platform='';
					 }
					 if(Ext.isEmpty(channel2,false)==true){
						 channel2='';
					 }
					 window.location.href = appBaseUri + '/sys/operate/exportChannel2List?&limit=100000&sdate=' +sdate +'&edate='+edate +'&platform='+platform +'&channel='+channel +'&channel2='+channel2 +'&last='+last;
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
					form.findField('date').setReadOnly(true);
					form.findField('reg_num').setReadOnly(true);
					form.findField('bound_num').setReadOnly(true);
					form.findField('new_bound_num').setReadOnly(true);
					form.findField('user_num').setReadOnly(true);
					form.findField('firat_user_num').setReadOnly(true);
					form.findField('new_user_num').setReadOnly(true);
					form.findField('new_user_rate').setReadOnly(true);
					form.findField('new_money_num').setReadOnly(true);
					form.findField('money_num').setReadOnly(true);
					win.show();
				}
			}
		});

		this.callParent(arguments);
	}
});