
/* 通用函数调用 */
layui.define(['layer', 'jquery'], function(exports){
	var layer = layui.layer,
	$ = layui.jquery;
  
	var u = {
		/**
		 * 获取项目名称
		 * @return 项目名称
		 */
		pro:function(){
			return window.location.origin;
		},
		/**
		 * 禁止某个元素中的文本：选中、复制等操作
		 * @param elm 需要处理文本的元素
		 */
		disSelCopy:function(elm){
			let kvs = [];
			kvs.push({key:'oncontextmenu', val:'return false'});// 禁止显示右键菜单
			kvs.push({key:'onselectstart', val:'return false'});// 禁止选择文本
			kvs.push({key:'ondragstart', val:'return false'});// 禁止拖动
			kvs.push({key:'onbeforecopy', val:'return false'});// 禁止复制文本
			kvs.push({key:'oncopy', val:'document.selection.empty()'});// 禁止复制文本
			kvs.push({key:'onselect', val:'document.selection.empty()'});// 禁止选中文本
			
			for(var i = 0; i < kvs.length; i++){
				$(elm).attr(kvs[i].key, kvs[i].val);
			}
		},
		/**
		 * 获取提交按钮所在表单的id
		 * @param btn 提交按钮
		 * @returns 表单id，并带#前缀
		 */
		fid:function(btn){
			return $(btn).parentsUntil('form').parent().attr('id');
		},
		/**
		 * 获取提交按钮所在表单
		 * @param el 触发元素
		 * @returns 表单对象
		 */
		fm:function(el){
			return $(el).parentsUntil('form').parent();
		},
		/**
		 * 获取元素的值，并去掉前后空格
		 * @param _id 与$(_id)一样
		 */
		v:function(_id){
			return $.trim($(_id).val());
		},
		/**
		 * 获取元素的文本，并去掉前后空格
		 * @param _id 与$(_id)一样
		 */
		t:function(_id){
			return $.trim($(_id).text());
		},
		/**
		 * 获取select元素下的option元素选中的文本，并去掉前后空格
		 * @param _id 与$(_id)一样，传入的是select元素
		 */
		seltxt:function(_id){
			return $.trim($(_id).find('option:selected').text());
		},
		/**
		 * 获取指定name属性值的被选中的input元素（eg：checked、radio等）
		 * @param _fId 指定input元素的上一级（可以为''）
		 * @param _tagName 指定input元素的name属性值
		 */
		ched:function(_fId, _tagName){
			if(_fId.indexOf('#') == -1) _fId = '#'+_fId;
			if($.trim(_fId) != '') _fId = _fId+' ';
			return $(_fId+'input[name="'+_tagName+'"]:checked');
		},
		/**
		 * 获取指定name属性值的input元素
		 * @param _fId 指定input元素的上一级（可以为''）
		 * @param _tagName 指定input元素的name属性值
		 */
		inp:function(_fId, _tagName){
			if(_fId.indexOf('#') == -1) _fId = '#'+_fId;
			if($.trim(_fId) != '') _fId = _fId+' ';
			return $(_fId+'input[name="'+_tagName+'"]');
		},
		/**
		 * 获取指定name属性值的input元素的值
		 * @param _fId 指定input元素的上一级（可以为''）
		 * @param _tagName 指定input元素的name属性值
		 */
		inpv:function(_fId, _tagName){
			if(_fId.indexOf('#') == -1) _fId = '#'+_fId;
			if($.trim(_fId) != '') _fId = _fId+' ';
			return $.trim($(_fId+'input[name="'+_tagName+'"]').val());
		},
		/**
		 * 获取指定name属性值的select元素
		 * @param _fId 指定select元素的上一级（可以为''）
		 * @param _tagName 指定select元素的name属性值
		 */
		sel:function(_fId, _tagName){
			if(_fId.indexOf('#') == -1) _fId = '#'+_fId;
			if($.trim(_fId) != '') _fId = _fId+' ';
			return $(_fId+'select[name="'+_tagName+'"]');
		},
		/**
		 * 获取指定name属性值的textarea元素
		 * @param _fId 指定textarea元素的上一级（可以为''）
		 * @param _tagName 指定textarea元素的name属性值
		 */
		texa:function(_fId, _tagName){
			if(_fId.indexOf('#') == -1) _fId = '#'+_fId;
			if($.trim(_fId) != '') _fId = _fId+' ';
			return $(_fId+'textarea[name="'+_tagName+'"]');
		},
		/**
		 * 获取指定name属性值的标签元素
		 * @param _fId 指定标签元素的上一级（可以为''）
		 * @param _tagName 指定标签元素的name属性值
		 * @param _tag 指定标签元素
		 */
		tag:function(_fId, _tagName, _tag){
			if(_fId.indexOf('#') == -1) _fId = '#'+_fId;
			if($.trim(_fId) != '') _fId = _fId+' ';
			return $(_fId+_tag+'[name="'+_tagName+'"]');
		},
		/**
		 * 使用html5方法，将blob/file对象转换成dataURL字符串
		 * @param blob blob/file对象
		 * @param callback 回调方法：可以return dataurl字符串
		 * 使用eg：
		 * kcb.fileBlobToDataURL(file, function (dataurl){
		 *	 return dataurl;
		 * });
		 */
		fileBlobToDataURL:function(blob, callback){
			var a = new FileReader();
		    a.onload = function(e) {callback(e.target.result);};
		    a.readAsDataURL(blob);
		},
		/**
		 * 使用html5方法，将dataURL字符串转换成blob/file对象
		 * @param dataurl dataURL字符串
		 * 使用eg：kcb.dataURLtoBlob(dataurl);
		 */
		dataURLtoBlob:function(dataurl){
			var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
		    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
		    while(n--){
		        u8arr[n] = bstr.charCodeAt(n);
		    }
		    return new Blob([u8arr], {type:mime});
		},
		/**
		 * 使用html5方法，将表单中的参数装入FormData对象
		 * @param tagArrs 表单中需要封装的参数对象
		 * 使用eg：
		 * var tagArrs = $(_fId).find('input[type="hidden"]);
		 * kc.getFormData(tagArrs);
		 */
		getFormData:function(tagArrs){
			var myForm = new FormData();//创建一个FormData对象
			for(var i = 0; i < tagArrs.length; i++){
				var ci = $(tagArrs[i]);//当前遍历元素
				//当前元素值,最终装入表单值,当前标签名称
				var currVal = '', finalVal, 
					currTag = ci[0].tagName;
				//根据不同的标签获取当前值的方式不同
				if(currTag == 'INPUT' || currTag == 'RADIO'){//输入框或者单选按钮
					currVal = $.trim(ci.val());
				}else if(currTag == 'TEXTAREA'){//多行文本框
					currVal = $.trim(ci.text());
				}else if(currTag == 'SELECT'){//下拉列表框
					currVal = $.trim(ci.find('option:selected').val());
				}
				if(currVal.indexOf('data:image') != -1){//此处参数为图片对象blob
					finalVal = this.dataURLtoBlob(currVal);
				}else{
					if(ci.hasClass('defile')){//此项为空处理
						finalVal = '';
					}else{//此处正常参数
						finalVal = currVal;
					}
				}
				myForm.append(ci.attr('name'), finalVal);
			}
			return myForm;
		},
		/**
	     * 将一个时间字符串转换成时间类型
	     * @param dateStr 时间字符串
	     */
	    str2date: function(dateStr){
	        dateStr = dateStr.replace(/-/g, "/");
	        return new Date(dateStr);
	    },
	    /**
	     * 时间转换为字符串
	     * @param date 时间字符串/时间Date对象
	     * @param format 格式，默认：yyyy-MM-dd HH:mm
	     */
	    date2str: function(date, format){
	        if(format == undefined) format = 'yyyy-MM-dd HH:mm';
	    	
	    	let fmt = format;

	        // 传入的是时间字符串，则转换成时间类型
	        if(typeof(date) === 'string') date = this.str2date(date);

	        if(format.indexOf('-') != -1) format = format.replace(/-/g, '/');

	        var pad = function(i){return (i < 10 ? '0' : '') + i;};

	        let res = format
	          .replace('yyyy', date.getFullYear())
	          .replace('MM', pad(date.getMonth() + 1))
	          .replace('dd', pad(date.getDate()))
	          .replace('HH', pad(date.getHours()))
	          .replace('mm', pad(date.getMinutes()))
	          .replace('ss', pad(date.getSeconds()));

	        if(fmt.indexOf('-') != -1) res = res.replace(/\//g, '-');

	        return res;
	    },
		/**
		 * 对指定时间进行格式化
		 * 用法：U.dateFormat(new Date().getTime(), 'yyyy-MM-dd HH:mm:ss')
		 * @param time 传入时间毫秒数
		 * @param format 输出格式
		 * @returns 指定格式的时间字符串
		 */
		dateFormat:function(time, format){
			if(format == '' || format == undefined) format = 'yyyy-MM-dd HH:mm:ss';
		    var t = new Date(time);
		    var tf = function(i){return (i < 10 ? '0' : '') + i;};
		    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
		        switch(a){
		            case 'yyyy':
		                return tf(t.getFullYear());
		                break;
		            case 'MM':
		                return tf(t.getMonth() + 1);
		                break;
		            case 'mm':
		                return tf(t.getMinutes());
		                break;
		            case 'dd':
		                return tf(t.getDate());
		                break;
		            case 'HH':
		                return tf(t.getHours());
		                break;
		            case 'ss':
		                return tf(t.getSeconds());
		                break;
		        }
		    });
		},
		/**
		* 对多位小数截取 保留两位小数
		* num 欲处理的数据
		*/
		decimal:function(num){
			var bb = num + '';  
		    var dian = bb.indexOf('.');
		    if(bb == '.00'){
		    	bb = '0.00';
		    }else if(dian == -1){
		    	bb = bb + '.00';
		    }else if(dian == 0){
		    	bb = '0'+bb;
		    }else{
		    	if(bb.length == 3){
		    		bb = bb + '0';
		    	}else if(bb.length > 3){
		    		bb = bb.substring(0, dian+3);
		    	}
		    	//如果值是：1.20，即0结尾的，去掉0
		    	if(bb.substr(bb.length-1) == '0'){
		    		bb = bb.substring(0, bb.length-1);//去掉0
		    	}
		    }
		    return bb; 
		},
		/**
	     * 验证一个对象是否为空
	     * @param obj 验证对象
	     * @returns true-为空; false-不为空;
	     */
	    isEmpty: function(obj){
	        if(typeof(obj) === 'object'){// 对象
	        	if(obj instanceof Date){// 时间对象
	                return false;
	            }
	        	
	            for(name in obj){
	                if(obj.hasOwnProperty(name)){
	                    return false;// 返回false，不为空对象
	                }
	            }

	            if(obj != null && (obj.size !== undefined && obj.size != null && obj.size > 0) && 
	                (obj.type != undefined && obj.type != null && obj.type.indexOf('image') != -1)){
	                return false;// 返回false，文件对象不为空
	            }
	            
	            return true;// 返回true，为空对象
	        }else if(obj !== undefined && obj !== '' && obj !== null){
	            return false;
	        }else{// 其他数据类型
	            return true;
	        }
	    },
		/**
		 * 启用/禁用-指定按钮
		 * @param btn 	指定按钮（必须是layui中的button标签按钮）
		 * @param i 	0-禁用按钮；1-启用按钮；
		 */
		setBtn: function(btn, i){
			if(i == 0 || i == undefined){// 禁用按钮
				$(btn).prop('disabled', true);
				$(btn).addClass('layui-btn-disabled');
			}else{// 启用按钮
				$(btn).removeAttr('disabled');
				$(btn).removeClass('layui-btn-disabled');
			}
		},
		/**
		 * 短信发送-按钮
		 * @param btn 	指定按钮
		 * @param t 	间隔秒数，默认：120秒
		 */
		sendSmsBtn: function(btn, t){
			if(t == undefined) t = 120;
			
			let si = null;
			btn.text(t+' s');	// 设置按钮文本
			this.setBtn(btn, 0);	// 禁用按钮
			si = setInterval(() => {
				if(t <= 1){
					this.setBtn(btn, 1);		// 启用按钮
					$(btn).text('发送短信');	// 设置按钮文本
					clearInterval(si);		// 清除定时
				}else{
					t--;
					btn.text(t+' s');		// 设置按钮文本
				}
			}, 1000);
			
		}
	};	
	
  	exports('myutil', function(){
  		return u;
  	}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});