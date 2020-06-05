/** layui前台通用js */

/**
 * 前端通用函数调用
 * kf:kcb front
 */
layui.define(['layer', 'jquery', 'myutil'], function(exports){
	var layer = layui.layer,
	$ = layui.jquery,
	U = layui.myutil();
  
	/**
	 * 防止里面元素滚动到底外部容器不滚动实例页面
	 */
	$.fn.scrollUnique = function() {
		return $(this).each(function() {
	        var eventType = 'mousewheel';
	        if (document.mozHidden !== undefined) {
	            eventType = 'DOMMouseScroll';
	        }
	        $(this).on(eventType, function(event) {
	            // 一些数据
	            var scrollTop = this.scrollTop,
	                scrollHeight = this.scrollHeight,
	                height = this.clientHeight;

	            var delta = (event.originalEvent.wheelDelta) ? event.originalEvent.wheelDelta : -(event.originalEvent.detail || 0);        

	            if ((delta > 0 && scrollTop <= delta) || (delta < 0 && scrollHeight - height - scrollTop <= -1 * delta)) {
	                // IE浏览器下滚动会跨越边界直接影响父级滚动，因此，临界时候手动边界滚动定位
	                this.scrollTop = delta > 0? 0: scrollHeight;
	                // 向上滚 || 向下滚
	                event.preventDefault();
	            }        
	        });
	    });	
	};
	
	var kf = {
		/**
		 * layui表格行单选
		 * @param table_id 表格id
		 * @param allCheck 是否全选：true-全选；false-禁止全选；
		 */
		rowSingleSelection:function(table_id, allCheck){
			if(table_id.lastIndexOf('#') == -1) table_id = '#'+table_id;
			
			if(allCheck != true) allCheck = false;// 默认禁止全选
			if(!allCheck){
				var th = $(table_id).next().find('.layui-table-header table.layui-table thead');
				$(th).on('click', 'div.layui-form-checkbox', function(){
					var check = $(this);
					if(check.hasClass('layui-form-checked')){
						kf.msg({con:'列表禁止全选', icon:'w'});
						check.click().removeClass('layui-form-checked');// 移除复选框选中样式
					}
				});
			}
			
			var tb = $(table_id).next().find('.layui-table-body table.layui-table tbody');
			$(tb).on('click', 'div.layui-form-checkbox', function(){
				var row = $(this).parentsUntil('tr').parent();// 当前行
				if($(this).hasClass('layui-form-checked')){// 已选中
					$(row).addClass('layui-table-click');
				  	// 取消其他选中行的选中状态
				  	var c = $(row).parent().find('tr[data-index!="'+$(row).attr('data-index')+'"]').find('.layui-form-checked');
				  	for(var i = 0; i < c.length; i++){
				  		if(c.hasClass('layui-form-checked')){// 已选中
				  			$(c).click().removeClass('layui-form-checked');// 移除复选框选中样式
				  			$(c).parent().parent().removeClass('layui-table-click');
				  			$(c).parent().parent().parent().removeClass('layui-table-click');// 移除选中背景样式
				  		}
				  	}
				}else{// 未选中
					row.removeClass('layui-table-click');
				}
			});
		},
		/**
		 * layui列表单元格，多行展示
		 * @param arr 数据展示数组 eg: [{label: '姓名', text: '张三', color: '#000'}]
		 */
		layListCell: function(arr){
			let ht = '<div class="my-lay-list-cell">';
			
			for(let i = 0; i < arr.length; i++){
				let label = arr[i].label == undefined ? '' : '<label>'+arr[i].label+'：</label>';
				let color = arr[i].color == undefined ? '' : ' style="color:'+arr[i].color+'"';
				
				ht += '<p'+color+'>'+label+arr[i].text+'</p>';
			}
			
			return ht+'</div>';
		},
		/**
		 * layui表格分页获取当前页，当前页不存在，自动获取最后一页
		 * @param table_id 表格id
		 */
		getCurrPage:function(table_id){
			if(table_id.lastIndexOf('#') == -1) table_id = '#'+table_id;
			
			var layui_page = $(table_id).next().find('.layui-table-page span.layui-laypage-skip');
			return $(layui_page).find('input.layui-input').val();
		},
		/********layer封装方法***********/
		autoHeight: function(){// 自动调节一个弹框的高度
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.iframeAuto(index);
		},
		close:function(index){// 关闭所有父级弹层，主要针对iframe
			top.layer.close(index);
		},
		curClose:function(index){// 当前window弹层
			layer.close(index);
		},
		closeNew: function(){// 关闭最新的弹层
			top.layer.close(top.layer.index);
		},
		closeIfNew: function(){// 关闭iframe最新的弹层
			// 先得到当前iframe层的索引
			let index = parent.layer.getFrameIndex(window.frameElement.id); 
			parent.layer.close(index); // 再执行关闭  
		},
		closeAll:function(){// 关闭所有父级弹层，主要针对iframe
			top.layer.closeAll();
		},
		closeParent:function(){// 删除父级弹窗
			var iframe_id = $('input[name="parent_layer_iframe_id"]', window.top.document);
			top.layer.close($(iframe_id).attr('data-layer-index'));
			$(iframe_id).remove();
		},
		oloading:function(){// 打开遮罩
			top.layer.load(1, {
			    shade: [0.3,'#fff'] //0.1透明度的白色背景
			});
		},
		cloading:function(){// 关闭遮罩
			top.layer.closeAll('loading');
		},
		/**
		 * 【信息提示框】layer普通弹层提示，点击确定关闭弹层
		 * @param d string=信息内容；json=弹框属性设置；
		 * @param fn 消息回调函数，可以不传入
		 */
		alert:function(d, fn){
			if(typeof(d) == 'string'){//如果传入参数是字符串，则将该参数作为消息弹窗的内容，其他使用默认设置属性
				d = {con:d, t:'提示信息', yes:'确认', icon:7, scr:false, shad:false};
			}else{//如果不是字符串，是json对象
				if(d.icon == undefined){
					 d.icon = 7;
				}else{
					// 1-正确；2-错误；3-询问；4-锁图标；5-难过脸图标；6-笑脸图标；7-黄色感叹号；大于7都是黄色感叹号
					var i = d.icon;
					if(i == 'ok'){
						d.icon = 1;// 正确、成功图标
					}else if(i == 'e'){
						d.icon = 2;// 错误、失败图标
					}else if(i == 'i'){
						d.icon = 6;// 笑脸
					}else if(i == 'no'){// 不显示图标
						d.icon = -1;
					}else{
						d.icon = 7;// 警告
					}
				}
				
				if(d.con == undefined) d.con = this.showTitle('抱歉，您未设置弹框内容', 'c-red');
				if(d.t == undefined) d.t = '提示信息';
				if(d.yes == undefined) d.yes = '确认';
				if(d.scr == undefined) d.scr = false;
				if(d.shad == undefined) d.shad = false;
			}
			
			top.layer.alert(d.con, {
				icon: d.icon,
				title: d.t,
				closeBtn: 0,// 不显示右上角关闭按钮
				btn: [d.yes],
				scrollbar: d.scr,//禁用浏览器滚动条
				shadeClose: d.shad //点击弹层外区域关闭弹层
			}, function(index){
				if(fn){
					fn();
				}
				top.layer.close(index);
			});
		},
		showTitle:function(str, cls){// 针对内容太长，当鼠标悬浮时显示其内容
			var c = '';
			if(str == '未设置' || str == '无'){
				c = ' class="fs-12 c-gray"';
			}else{
				if(cls != undefined && cls != ''){
					c = ' class="'+cls+'"';
				}else{
					c = '';
				}
			}
			
			return '<span'+c+'>'+str+'</span>';
		},
		msg:function(d){// 提示框
			if(typeof(d) == 'string'){//如果传入参数是字符串，则将该参数作为消息弹窗的内容，其他使用默认设置属性
				d = {con:d, icon:6, time:3000};
			}else{//如果不是字符串，是json对象
				if(d.icon == undefined){
					 d.icon = 7;
				}else{
					// 1-正确；2-错误；3-询问；4-锁图标；5-难过脸图标；6-笑脸图标；7-黄色感叹号；大于7都是黄色感叹号
					var i = d.icon;
					if(i == 'ok'){
						d.icon = 1;// 正确、成功图标
					}else if(i == 'e'){
						d.icon = 2;// 错误、失败图标
					}else if(i == 'n'){
						d.icon = 5;// 难过图标
					}else if(i == 'i'){
						d.icon = 6;// 笑脸
					}else{
						d.icon = 7;// 笑脸
					}
				}
				
				if(d.con == undefined) d.con = this.showTitle('抱歉，您未设置弹框内容', 'c-red');
				if(d.time == undefined) d.time = 3000;
			}
			top.layer.msg(d.con, {icon: d.icon, time: d.time}); 
		},
		msgFalse:function(d){// 提示框
			this.msg(d);
			
			return false;
		},
		/**
		 * 错误提示，并返回false
		 * @param con 提示内容
		 */
		errorFalse:function(con){
			return this.msgFalse({con: con, icon: 'n'});
		},
		/**
		 * layer询问框
		 * @param d 询问框属性：json对象
		 * @param yes 【确定】消息回调函数，可以不传入
		 * @param no 【取消】消息回调函数，可以不传入
		 */
		confirm:function(d, yes, no){
			if(typeof(d) == 'string'){//如果传入参数是字符串，则将该参数作为消息弹窗的内容，其他使用默认设置属性
				d = {con:d, t:'询问信息', yes:'确定', no:'取消'};
			}else{//如果不是字符串
				if(d.con == undefined) d.con = '抱歉，你没有为询问框设置消息内容';
				if(d.t == undefined) d.t = '询问信息';
				if(d.yes == undefined) d.yes = '确定';
				if(d.no == undefined) d.no = '取消';
			}
			top.layer.confirm(
				d.con,
				{
					title:d.t,
					closeBtn: 0,
					icon: 3,// 询问图标
					btn: [d.yes, d.no]
				}, 
				function(index){// 确定
					if(yes){
			    		yes();
			    	}
			    	top.layer.close(index);
				},
				function(index){// 取消
					if(no){
			    		no();
			    	}
			    	top.layer.close(index);
				}
			);
		},
		/**
		 * layer询问填写框
		 * @param d 询问填写框属性：json对象
		 * @param yes 【确定】消息回调函数，可以不传入
		 * @param no 【取消】消息回调函数，可以不传入
		 */
		prompt:function(d, yes, no){
			if(typeof(d) == 'string'){//如果传入参数是字符串，则将该参数作为消息弹窗的内容，其他使用默认设置属性
				d = {t:d, ft:2, maxl:50, yes:'确定', no:'取消'};
			}else{//如果不是字符串
				if(d.t == undefined) d.t = '抱歉，你没有为询问框设置消息内容';
				if(d.ft == undefined) d.ft = 2;
				if(d.maxl == undefined) d.maxl = 50;
				if(d.yes == undefined) d.yes = '确定';
				if(d.no == undefined) d.no = '取消';
			}
			top.layer.prompt(
				{
					title:d.t,
					formType: d.ft, // 输入框类型，支持0（文本）默认1（密码）2（多行文本）
					maxlength: d.maxl, //可输入文本的最大长度，默认500
					btn: [d.yes, d.no]
				}, 
				function(value, index, elem){// 确定：value-表单值；index-索引；elem-表单元素
					if(yes){
			    		yes(value);
			    	}
			    	top.layer.close(index);
				},
				function(index){// 取消
					if(no){
			    		no();
			    	}
			    	top.layer.close(index);
				}
			);
		},
		/**
		 * 弹层-面板
		 * @param dat {area: ['350px', '400px'], title: 标题, con: 内容，可以是html内容，也可以是id选择器，eg：#test, showClose: 是否显示右上角关闭按钮}
		 * @param clanceFun 关闭弹框的回调方法
		 */
		showPlane:function(dat, clanceFun){
			// 初始化处理
			if(dat == null || dat == undefined) dat = {};
			if(dat.area == undefined) dat.area = ['350px', '400px'];
			if(dat.title == undefined) dat.title = false;
			if(dat.showClose == undefined) dat.showClose = false;
			if(clanceFun == undefined || clanceFun == null) clanceFun = function(){};
			
			let context = '面板内容';
			// 存在id选择器标志#，则表示传入的不是html内容
			if(dat.con != undefined && dat.con.indexOf('#') != -1) context = $(dat.con);
			
			layer.open({
			  type: 1,
			  title: dat.title,
			  closeBtn: 1,
			  area: dat.area,
			  skin: 'layui-layer-nobg', // 没有背景色
			  shadeClose: dat.showClose,
			  content: context,
			  cancel: clanceFun
			});
		},
		/**
		 * 弹层展示图片
		 * @param srcval 传入的图片路径/图片二进制字符串
		 */
		showUpImg:function(srcval, wh){
			if(wh == undefined || wh == ''){
				wh = 'auto';
			}else{
				wh = wh.split(',');
				
				// 限制最大高度不超过800，最大宽度不超过1000
				var w = parseInt(wh[0].replace('px', ''));
				var h = parseInt(wh[1].replace('px', ''));
				if(w > 1000){
					w = 1000;
				}
				if(h > 700){
					h = 700;
				}
				wh = [w+'px', h+'px'];
			}
			var ht = '<img style="width:100%;height:100%;" src="'+$.trim(srcval)+'" />';
			top.layer.open({
			  type: 1,
			  title: false,
			  closeBtn: 0,
			  area: wh,
			  skin: 'layui-layer-nobg', //没有背景色
			  shadeClose: true,
			  content: ht
			});
		},
		/**
		 * 弹层-展示支付二维码
		 * @param dat {area: ['350px', '400px'], money: 金额显示, note: 顶部第二行文本, imgurl: 图片url, tip: 底部提示文本}
		 * @param clanceFun 关闭弹框的回调方法
		 */
		showQRPay:function(dat, clanceFun){
			// 初始化处理
			if(dat == null || dat == undefined) dat = {};
			if(dat.area == undefined) dat.area = ['350px', '400px'];
			if(dat.money == undefined) dat.money = '0.00';
			if(dat.note == undefined) dat.note = '上车地点 → 下车地点';
			if(dat.imgurl == undefined) dat.imgurl = '/resources/new-version/commons/images/kcb_wx.jpg';
			if(dat.tip == undefined) dat.tip = '打开微信扫一扫，立即支付车款';
			if(clanceFun == undefined) clanceFun = function(){};
			
			let ht = '<div class="my-qr-pay">'+
						'<div class="mqp-body">'+
							'<div class="mb-money">'+
								'待支付车款：'+
								'<span class="mm-txt">'+
									'<b class="mt-money">'+ dat.money +'</b> 元'+
								'</span>'+
							'</div>'+
							'<div class="mb-note">'+ dat.note +'</div>'+
						'</div>'+
						'<div class="mqp-qrcode">'+
							'<img src="'+ dat.imgurl +'" />'+
						'</div>'+
						'<div class="mqp-tip">'+ dat.tip +'</div>'+		
					 '</div>';
			
			top.layer.open({
			  type: 1,
			  title: false,
			  closeBtn: 1,
			  area: dat.area,
			  skin: 'layui-layer-nobg', // 没有背景色
			  shadeClose: false,
			  content: ht,
			  cancel: clanceFun
			});
			
			// 禁止元素之中的内容选中、复制
			U.disSelCopy('.my-qr-pay');
		},
		/**
		 * 创建layui-layer-iframe弹层
		 * data 传入json对象参数
		 * @param t 标题
		 * @param w 弹层宽度
		 * @param h 弹层高度
		 * @param con 弹层内容：url、string、dom、obj
		 * @param sc 是否滚动条：yes-是，no-否
		 * @param scr 是否有浏览器滚动条：false-否，true-是
		 * @param shc 点击弹层之外灰色区域是否关闭弹层：false-否，true-是
		 * @param isSaveId 是否保存父级id：false-否，true-是
		 * @param closeBtn 是否显示右上角的按钮：true-显示（默认）；false-不显示；
		 */
		iframe:function(d){
			if(d.sc == undefined) d.sc = 'no';
			if(d.scr == undefined) d.scr = false;
			if(d.shc == undefined) d.shc = false;
			if(d.isSaveId == undefined) d.isSaveId = false;
			if(d.closeBtn == undefined || d.closeBtn == true){
				d.closeBtn = 1;
			}else{
				d.closeBtn = 0;
			}
			let area = null;
			if(d.h == undefined){
				area = d.w+'px';
			}else{
				area = [d.w+'px', d.h+'px'];
			}
			
			let findex = top.layer.open({
				//skin: 'layui-layer-rim', //加上边框
				shadeClose: d.shc,
				scrollbar: d.scr,
			    type: 2,
			    title: d.t,
			    area: area,
			    closeBtn: d.closeBtn,
			    resize: false,// 禁止拖动
			    content: [ d.con, d.sc ], //iframe的url
			    success: function(layero, index){
			    	if(d.isSaveId == true){
			    		/*let iframe_id = $('input[name="parent_layer_iframe_id"]', window.top.document);
				    	if(iframe_id.length > 0){//存在
				    		iframe_id.val('layui-layer-iframe'+index).attr('data-layer-index', index);
				    	}else{//不存在
				    		//给父级保存id
					    	$(window.top.document.body).append('<input name="parent_layer_iframe_id" value="layui-layer-iframe'+index+'" data-layer-index="'+index+'" />');
				    	}*/
			    	}
			    },
			    cancel: function(index, layero){ 
			    	if(d.isSaveId == true){
			    		//$('input[value="layui-layer-iframe'+index+'"]', window.top.document).remove();
			    	}
				}
			});
			
			return findex;
		},
		/**
		 * 让滚动条滚动到指定位置，即显示到指定位置，方便查看
		 * @param parentSel 有滚动条的元素选择器
		 * @param targetSel 目标元素选择器
		 */
		rollToPos:function(f, targetSel){// 滚动到指定位置
			var t = $(targetSel).offset().top;// 获取目标元素的文档高度
			var h = $(targetSel).height();
			$(f).animate({scrollTop: Math.abs(t-h)}, 500);
		},
		/**
		 * 发送短信成功后-按钮操作
		 * @param smsBtn 发送短信按钮
		 */
		smsFn:function(smsBtn){
			if(timer != null) clearInterval(timer); // 清除计时器
			
			smsBtn.prop('disabled', true);// 禁用按钮
			smsBtn.addClass('layui-btn-disabled');// 添加禁用按钮样式
			smsBtn.removeProp('data-smscode');// 删除属性
			
			let t = 120;
			let timer = setInterval(function(){
				if(t > 0){
					t--;
					smsBtn.text('剩余时间 '+t+'s');
				}else{
					smsBtn.text('重新发送短信');
					smsBtn.prop('disabled', false);// 启用按钮
					smsBtn.removeClass('layui-btn-disabled');// 添加禁用按钮样式
					smsBtn.prop('data-smscode', 'true');
					clearInterval(timer); // 清除计时器
				}
			}, 1000);
		},
		/**
		 * 为layui的select控件设置值
		 * @param selEl select对象
		 * @param val 	设置的值
		 */
		setSelVal: function(selEl, val){
			$(selEl).val(val);
			$(selEl).next().find('input').val(val);
		},
		/**
		 * 禁用/启用-元素事件
		 * @param el 	操作元素对象
		 * @param i 	0-禁用；1-启用；
		 */
		setEvent: function(el, i){
			if(i == 1){
				$(el).css('pointer-events', 'auto');
			}else if(i == 0){
				$(el).css('pointer-events', 'none');
			}
		},
	};	
	
  	exports('front', function(){
  		return kf;
  	}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});