/*****此js文件为公共函数文件*******/
/**
 * 表单验证
 */
layui.define(['layer', 'jquery', 'form'], function(exports){
	var layer = layui.layer,
	$ = layui.jquery,
	form = layui.form;
	
	var fv = {
		/**
	     * 正则验证
	     * @param reg 正则表达式
	     * @param v 验证的值
	     * @returns 符合-true; 不符合-false;
	     */
	    matchReg: function(reg, v){
	        if(!(reg.test(v))){
	            return false;
	        }else{
	            return true;
	        }
	    },
	    /** 
	     * 是否是-正整数（包含0）
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isInt: function(v){
	        return this.matchReg(/^(\+?[1-9][0-9]*)|[0]$/, v);
	    },
	    /** 
	     * 是否是-正数/正小数（保留2位，包含0）
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isFloat: function(v){
	        return this.matchReg(/^([1-9]([0-9]?)+(\.[0-9]{2}|\.[0-9]{1})?|0\.[1-9]|0\.[1-9][0-9]|0\.0[1-9])|[0]$/, v);
	    },
	    /** 
	     * 是否是-数字
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isNumber: function(v){
	        return this.matchReg(/^[0-9]+.?[0-9]*$/, v);
	    },
	    /** 
	     * 是否是-手机号
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isPhone: function(v){
	        return this.matchReg(/^1[3-9]\d{9}$/, v);
	    },
	    /** 
	     * 是否是-登录密码
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isLPass: function(v){
	        return this.matchReg(/^[\.A-Wa-z0-9_-]{6,18}$/, v);
	    },
	    /** 
	     * 是否是-支付密码
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isPayPass: function(v){
	        return this.matchReg(/^[0-9]{6}$/, v);
	    },
	    /** 
	     * 是否是-验证码
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isCode: function(v){
	        return this.matchReg(/^[0-9]{4}$/, v);
	    },
	    /** 
	     * 是否是-时间格式
	     * @param v 传入的值
	     * @returns true-是；false-不是；
	     */
	    isDate: function(v){
	        let fg = false;
	        
	        try {
	            if(v.indexOf('-') != -1){
	                v = v.replace(/-/g, '/');// 兼容ios处理
	            }
	            if(isNaN(v) && !isNaN(Date.parse(v))){
	            　　fg = true;
	            }
	        } catch (error) {
	            fg = false;
	        }

	        return fg;
	    },
	    /**
	     * 去掉字符串前后空格
	     * @param v 操作的值
	     * @returns 新的字符串
	     */
	    trim: function(v){
	        v = v+'';
	        return v.replace(/(^\s*)|(\s*$)/g, '');
	    },
	    /**
	     * 去掉字符串所有空格
	     * @param v 操作的值
	     * @returns 新的字符串
	     */
	    trimAll: function(v){
	        return v.replace(/\s*/g, '');
	    }
	};
		
  	exports('fv',  function(){
  		return fv;
  	}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});