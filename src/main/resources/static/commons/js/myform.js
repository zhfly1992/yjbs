/*****此js文件为公共函数文件*******/
/**
 * 前端、后台通用函数调用
 * qb:qfc my_form
 */

layui.define(['layer', 'jquery', 'form'], function(exports){
	var layer = layui.layer,
	$ = layui.jquery,
	form = layui.form;
	
	// 增加自定义表单验证
	form.verify({
		lphone:function(value, item){// value：表单的值、item：表单的DOM对象
			if(!/^1[3-9]\d{9}$/.test(value)){
				return '手机号由11位纯数字组成';
			}
		},
		recPhone:function(value, item){// 推荐人手机号验证
			if(value != ''){
				if(!/^1[3-9]\d{9}$/.test(value)){
					return '推荐用户手机号由11位纯数字组成';
				}
			}
		},
		lpass:function(value, item){// 密码输入验证
			if(!/^[\.A-Wa-z0-9_-]{6,18}$/.test(value)){
				return '密码由.、大小写字母、数字、_、-组成，6~18位字符';
			}
		},
		imgcode:function(value, item){// 图片验证码输入验证
			if(!/^[A-Wa-z0-9]{4}$/.test(value)){
				return '图片验证码由4位英文大小写字母、数字组成';
			}
		},
		smscode:function(value, item){// 短信验证码输入验证
			if(!/^[0-9]{4}$/.test(value)){
				return '短信验证码由4位纯数字组成';
			}
		},
		isNumber:function(value, item){// 只能输入正数（正小数[最多两位小数]、正整数）
			if(!/^([1-9]([0-9]?)+(\.[0-9]{2}|\.[0-9]{1})?|0\.[1-9]|0\.[1-9][0-9]|0\.0[1-9])$/.test(value)){
				return '请输入正确的正数';
			}
		},
		isInteger:function(value, item){// 只能输入正整数
			if(!/^\+?[1-9][0-9]*$/.test(value)){
				return '请输入正确的正整数';
			}
		}
	});
		
  	exports('myform', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});