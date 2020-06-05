/** 页面-js */
layui.define(['layer', 'jquery', 'form', 'front', 'myutil', 'fv'], function(exports){
	let layer = layui.layer,
	$ = layui.jquery,
	form = layui.form,
	fv = layui.fv(),
	U = layui.myutil(),
	kf = layui.front();
	
	/** 页面最高-选择器 */
	let pid = '.company-register';
	
	$(pid).find('.tr-img-code').attr('src', '/common/getImgCode?_='+new Date().getTime());
	// 图片验证码-点击事件
	$(pid).find('.tr-img-code').click(function(){
		let that = $(this);
		
		setTimeout(() => {
			that.attr('src', that.attr('src').split('?')[0]+'?_='+new Date().getTime());
		}, 500);
	});
	
	// 表单-自定义验证
	let ps = {};// 表单提交参数对象
	form.verify({
		tlphone: function(v){
			if(!fv.isPhone(v)){
				return '注册手机号格式错误';
			}else{
				ps.lphone = v;
			}
		},
		tlpass: function(v){
			if(!fv.isLPass(v)){
				return '登录密码格式错误';
			}else{
				ps.lpass = v;
			}
		},
		retlpass: function(v){
			if(!fv.isLPass(v)){
				return '确认登录密码格式错误';
			}else{
				if(ps.lpass != v){
					return '两次密码不一致';
				}
			}
		},
		tlimgcode: function(v){
			if(v.length != 4){
				return '图片验证码长度为4';
			}else{
				ps.imgCode = v;
			}
		}
	});
	
	// 提交-表单
	form.on('submit(subRegister)', function(data){
		let dat = data.field;
		
		kf.oloading();
		$.post('/company/cus/subRegister', ps, 
			function(res){
				kf.cloading();
				
				if(res == null){
					kf.errorFalse('请求错误，请稍后再试');
				}else if(res.code != 1){
					kf.alert(res.msg);
				}else{
					kf.msg(res.msg);
					// 导航-登录页面
					
				}
			}, 
			'json'
		);
		
		return false;
	});
	
});