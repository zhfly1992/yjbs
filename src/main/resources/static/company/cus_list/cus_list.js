/** 页面-js */
layui.define(['layer', 'jquery', 'form', 'table', 'soulTable', 'front', 'myutil', 'fv'], function(exports){
	let layer = layui.layer,
	$ = layui.jquery,
	form = layui.form,
	table = layui.table,
	soulTable = layui.soulTable,
	U = layui.myutil(),
	fv = layui.fv(),
	kf = layui.front();

	/** 页面最高-选择器 */
	let dgId = 'dg_cus_list';
	let tbId = '#tb_cus_list';
	let tbarId = '#tbar_cus_list';
	let barId = '#bar_cus_list';
	let currPage = 1;// 当前页码
	
	kf.oloading();
	table.render({
		title: '用户-数据列表',
	    elem: "#"+dgId,
	    method: 'post',
	    url: '/company/cus/getCusList',
	    request: { pageName: 'page', limitName: 'rows' },    
	    height: 625,
	    size: 'lg',
	    page: true,
	    loading: true,
	    limit: 10,
	    limits: [10, 20, 30, 90, 200, 1000],
	    cellMinWidth: 60,
	    toolbar: tbarId, // 开启头部工具栏，并为其绑定左侧模板
        defaultToolbar: [
        	'filter', 'exports', 'print', 
        	{
	          title: '提示',
	          layEvent: 'LAYTABLE_TIPS',
	          icon: 'layui-icon-tips'
        	}
        ],
        contextmenu: {
            header: false, // false 禁用右键（组织浏览器的右键菜单）
            body: false,
            total: false
        },
	    cols: [[
			{checkbox: true},
			{field:'uname', width:150, title:'用户名', align:'left',
		      	templet:function(d){
	      			return d.baseUserId.uname;
	      	}},
	      	{field:'phone', width:150, title:'用户信息', align:'left',
	      		templet:function(d){
	      			return kf.layListCell([
      					{label: '手机号', text: d.baseUserId.phone}, 
      					{label: '姓名', text: d.baseUserId.realName}
	      			]);
	      		}
	      	},
	      	{field:'regWay', width:150, title:'注册方式', align:'left', style: 'font-weight: 600;',
		      	templet:function(d){
	      			return d.baseUserId.regWay;
	      	}},
	      	{field:'ustate', width:100, title:'用户状态', align:'left',
		      	templet:function(d){
	      			return d.baseUserId.ustate;
	      	}},
	      	{field:'sortNo', width:80, title:'排序', align:'center', contextmenu: {
	      		// 表格内容右键菜单配置
                body: [
                	{
                        name: '向上移动',
                        icon: 'layui-icon layui-icon-up',
                        click: function(obj) {
                        	let cityName = $(tbId+' select[name="cityName"]').val();
                        	if(U.isEmpty(cityName)){
                        		kf.errorFalse('请先选择城市');
                        	}else{// 只有选择了城市，才可以更新序号
                        		updSortNo({id: obj.row.id, sortType: 'UP'});
                        	}
                        }
                    },
                    {
                        name: '向下移动',
                        icon: 'layui-icon layui-icon-down',
                        click: function(obj) {
                        	let cityName = $(tbId+' select[name="cityName"]').val();
                        	if(U.isEmpty(cityName)){
                        		kf.errorFalse('请先选择城市');
                        	}else{// 只有选择了城市，才可以更新序号
                        		updSortNo({id: obj.row.id, sortType: 'DOWN'});
                        	}
                        }
                    },
                    {
                        name: '序号交换',
                        icon: 'layui-icon layui-icon-template',
                        click: function(obj) {
                        	let cityName = $(tbId+' select[name="cityName"]').val();
                        	if(U.isEmpty(cityName)){
                        		kf.errorFalse('请先选择城市');
                        	}else{// 只有选择了城市，才可以更新序号
                        		let checkStatus = table.checkStatus(dgId);
                        	    let dat = checkStatus.data;
                        	    if(dat.length != 2){
                        	    	kf.errorFalse('请选择2个交换序号的景点');
                        	    }else{
                        	    	updSortNo({id: dat[0].id, sortType: 'EXCH', id2: dat[1].id});
                        	    }
                        	}
                        }
                    },
                    {
                        name: '初始化序号',
                        icon: 'layui-icon layui-icon-refresh',
                        click: function(obj) {
                        	let cityName = $(tbId+' select[name="cityName"]').val();
                        	if(U.isEmpty(cityName)){
                        		kf.errorFalse('请先选择城市');
                        	}else{// 只有选择了城市，才可以更新序号
                        		top.layer.confirm("初始化【"+cityName+"】的景点排序号，您确定吗？", {icon: 3}, function(index){
                        			updSortNo({id: obj.row.id, sortType: 'INIT'});
            					  
            	    				top.layer.close(index);
            	    			});
                        	}
                        }
                    }
                ]
	      	},
	      	templet:function(d){
      			return d.sortNo == 0 ? '无' : d.sortNo;
      		}},
	      	{field:'atime', width:160, title:'添加时间', align:'center',
	      		templet:function(d){
	      			return d.baseUserId.atime;
	      		}
	      	},
	      	{width:100, align:'center', title:'操作', toolbar:barId}
	    ]],
	    done: function(res, curr, count){
	    	currPage = curr;
	    	
	    	// 在 done 中开启
	        soulTable.render(this)
	    	
	    	kf.cloading();
	    	
	    	// 启用列表单选、并禁止全选
	    	//kf.rowSingleSelection(dgId, false);
	    }
	});
	
	/**
	 * 更新景点排序号
	 */
	let updSortNo = function(ps){
		kf.oloading();
		$.post('/travel/scespo/updScenicSpotsSort', ps, 
			function(res){
				kf.cloading();
			
				if(res == null){
					kf.erroFalse('请求错误，请刷新再试');
				}else if(res.code != 1){
					kf.alert(res.msg);
				}else{
					let cityName = $(tbId).find('select[name="cityName"]').val();
		    		let find = $(tbId).find('input[name="find"]').val();
		    		top.refRouteTable({cityName: cityName, find: find});
					
					kf.msg(res.msg);
				}
			}, 
			'json'
		);
	}
	
	// 加载-城市下拉列表框数据
	let loadCityDat = function(){
		// 异步加载-城市列表数据
		$.post('/common/util/getCityList', {}, 
			function(res){
				let list = res.data;
				let city = $(tbId+' select[name="cityName"]');
				
				let ht = '';
				city.find('option:gt(0)').remove();
				for(let i in list){
					ht += '<option value="'+list[i].cityName+'">('+list[i].pinyinSimple+')'+list[i].cityName+'</option>';
				}
				$(ht).appendTo(city);
				form.render('select');
			}, 
			'json'
		);
	}
	// 加载-城市区/县下拉列表框数据
	let loadCountyDat = function(cityName){
		// 异步加载-城市列表数据
		$.post('/common/util/getCountyList', {city: cityName}, 
			function(res){
				let list = res.data;
				let county = $(tbId+' select[name="countyName"]');
				
				let ht = '';
				county.find('option:gt(0)').remove();
				for(let i in list){
					ht += '<option value="'+list[i].countyName+'">'+list[i].countyName+'</option>';
				}
				$(ht).appendTo(county);
				form.render('select');
			}, 
			'json'
		);
	}
	
	
	
	//loadCityDat();// 首次加载城市列表
	// 绑定-城市-下拉列表框的输入框-选择事件
	form.on('select(cityName)', function(data){
		let cityName = data.value;
		
		loadCountyDat(cityName);
		
		let find = $(tbId).find('input[name="find"]').val();
		
		let ps = {};
		ps.cityName = cityName;
		ps.find = find;
		
		top.refRouteTable(ps);
		
		return false;
	});
	// 绑定-下拉列表框的输入框-点击事件
	$(tbId+' select[name="cityName"]').parent().on('click', 'input', function(){
		let cityName = $(this).val();
		if(!U.isEmpty(cityName)){
			// 全选文本，方便输入新的查询城市
			$(this).parent().find('input').select();
		}
	});
	
	// 绑定-城市区县-下拉列表框-选择事件
	form.on('select(countyName)', function(data){
		let countyName = data.value;
		
		let cityName = $(tbId+' select[name="cityName"]').val();
		let find = $(tbId).find('input[name="find"]').val();
		
		let ps = {};
		ps.cityName = cityName;
		ps.countyName = countyName;
		ps.find = find;
		
		top.refRouteTable(ps);
		
		return false;
	});
	
	
	/**
	* 重载数据表格
	* @param pars 额外参数：json对象
	*/
	top.refRouteTable = function(ps){
		table.reload(dgId, {
	        page: { curr: ps.currPage == undefined ? currPage : ps.currPage },
	        where: ps// 额外参数
	    });
	};
	
	// 绑定顶部工具栏事件
	$(tbId+' .layui-btn').on('click', function(){
	    let event = $(this).data('lay-event');
	    
	    let checkStatus = table.checkStatus(dgId);
	    let data = checkStatus.data;
	    
	    switch(event){
	    	case 'searchData':
	    		let cityName = $(tbId).find('select[name="cityName"]').val();
	    		let find = $(tbId).find('input[name="find"]').val();
	    		
	    		top.refRouteTable({cityName: cityName, find: find});
	    	break;
	    
	    	case 'refData':
	    		top.refRouteTable({currPage: 1});
	    	break;
		      
	    	case 'goAddRow':
	    		let url = '/travel/page/goAddScenicSpots';
	    		kf.iframe({t: '添加-景点数据', w: 700, h: 600, con: url, sc: 'yes'});
	    	break;
	      
	    	case 'goUpdRow':
	    		if(data.length != 1){
	    			kf.alert('请选择一条数据');
	    		}else{
	    			let dat = data[0];
	    		  
	    			let url = '/travel/page/goUpdScenicSpots?id='+dat.id;
	    			kf.iframe({t: '修改-景点数据', w: 700, h: 600, con: url, sc: 'yes'}); 
	    		}
	    	break;
	      
	    	case 'delRow':
	    		if(data.length != 1){
	    			kf.alert('请选择一条数据');
	    		}else{
	    			let dat = data[0];
		    	
	    			top.layer.confirm("删除景点数据，您确定吗？", {icon: 3}, function(index){
	    				kf.oloading();
	    				// 发送post请求
	    				$.post('/travel/scespo/delScenicSpotsDat', 
	    						{id: dat.id}, 
	    						function(res){
	    							kf.cloading();
    						  
	    							if(res.code != 1){
	    								kf.alert(res.msg);
	    							}else{
	    								// 重载列表
	    								top.refRouteTable({currPage: 1});
    							  
	    								kf.msg(res.msg);
    							  
	    								/*kf.alert({con:res.msg, icon:'ok'}, function(){
    								  	// 重载列表
    								  	top.refRouteTable();
    							  	});*/ 
	    							}
	    						},
	    						'json'
	    				);
					  
	    				top.layer.close(index);
	    			});
	    		}
	    	break;
	      
	    	case 'goDetail':
	    		if(data.length != 1){
	    			kf.alert('请选择一条数据');
	    		}else{
	    			let dat = data[0];
		    	
	    			if(U.isEmpty(dat.explainId)){
	    				kf.alert('该数据不存在[景点说明]，请先删除再重新添加');
	    			}else{
	    				let url = '/travel/page/goScenicSpotsDetail?id='+dat.id;
	    				kf.iframe({t: '预览-景点详情', w: 800, h: 600, con: url, sc: 'yes'});
	    			}
	    		}
	    	break;
	      
	    	// 自定义头工具栏右侧图标 - 提示
	    	case 'LAYTABLE_TIPS':
	    		layer.alert('这是工具栏右侧自定义的一个图标按钮');
	    	break;
	    };
	});
	
	// 头工具栏事件
	table.on('toolbar('+dgId+')', function(obj){
	    let checkStatus = table.checkStatus(obj.config.id);
	    let data = checkStatus.data;
	    
	    switch(obj.event){
		    case 'refData':
	    		top.refRouteTable({currPage: 1});
	    	break;
		      
	    	case 'goAddRow':
	    		let url = '/travel/page/goAddScenicSpots';
	    		kf.iframe({t: '添加-景点数据', w: 700, h: 600, con: url, sc: 'yes'});
	    	break;
	      
	    	case 'goUpdRow':
	    		if(data.length != 1){
	    			kf.alert('请选择一条数据');
	    		}else{
	    			let dat = data[0];
	    		  
	    			let url = '/travel/page/goUpdScenicSpots?id='+dat.id;
	    			kf.iframe({t: '修改-景点数据', w: 700, h: 600, con: url, sc: 'yes'}); 
	    		}
	    	break;
	      
	    	case 'delRow':
	    		if(data.length != 1){
	    			kf.alert('请选择一条数据');
	    		}else{
	    			let dat = data[0];
		    	
	    			top.layer.confirm("删除景点数据，您确定吗？", {icon: 3}, function(index){
	    				kf.oloading();
	    				// 发送post请求
	    				$.post('/travel/scespo/delScenicSpotsDat', 
	    						{id: dat.id}, 
	    						function(res){
	    							kf.cloading();
							  
	    							if(res.code != 1){
	    								kf.alert(res.msg);
	    							}else{
	    								// 重载列表
	    								top.refRouteTable({currPage: 1});
								  
	    								kf.msg(res.msg);
								  
	    								/*kf.alert({con:res.msg, icon:'ok'}, function(){
									  	// 重载列表
									  	top.refRouteTable();
								  	});*/ 
	    							}
	    						},
	    						'json'
	    				);
					  
	    				top.layer.close(index);
	    			});
	    		}
	    	break;
	      
	    	case 'goDetail':
	    		if(data.length != 1){
	    			kf.alert('请选择一条数据');
	    		}else{
	    			let dat = data[0];
		    	
	    			if(U.isEmpty(dat.explainId)){
	    				kf.alert('该数据不存在[景点说明]，请先删除再重新添加');
	    			}else{
	    				let url = '/travel/page/goScenicSpotsDetail?id='+dat.id;
	    				kf.iframe({t: '预览-景点详情', w: 800, h: 600, con: url, sc: 'yes'});
	    			}
	    		}
	    	break;
	      
	    	// 自定义头工具栏右侧图标 - 提示
	    	case 'LAYTABLE_TIPS':
	    		layer.alert('这是工具栏右侧自定义的一个图标按钮');
	    	break;
	    };
	});
	
	// 监听行工具事件
	table.on('tool('+dgId+')', function(obj){
	    let dat = obj.data;
	    
	    if(obj.event === 'jdExplain'){// 景点说明
		    if(U.isEmpty(dat.explainId)){
	  			kf.alert('该数据不存在[景点说明]，请先删除再重新添加');
	  		}else{
	  			let url = '/travel/page/goScenicSpotsDetail?id='+dat.id;
	  		  	kf.iframe({t: '预览-景点详情', w: 800, h: 600, con: url, sc: 'yes'});
	  		}
	    }
	});
	
});