/** 页面-js */
layui.define(['layer', 'jquery', 'form', 'front', 'myutil', 'fv'], function(exports){
	let layer = layui.layer,
	$ = layui.jquery,
	form = layui.form,
	U = layui.myutil(),
	fv = layui.fv(),
	kf = layui.front();
	
	/** 页面最高-选择器 */
	let pid = '.back-login';
	
	$(pid).find('.bl-img-code').attr('src', '/common/getImgCode?_='+new Date().getTime());
	// 图片验证码-点击事件
	$(pid).find('.bl-img-code').click(function(){
		let that = $(this);
		
		setTimeout(() => {
			that.attr('src', that.attr('src').split('?')[0]+'?_='+new Date().getTime());
		}, 500);
	});
	
	// 表单-自定义验证
	let ps = {};// 表单提交参数对象
	form.verify({
		blphone: function(v){
			if(!fv.isPhone(v)){
				return '登录手机号格式错误';
			}else{
				ps.lphone = v;
			}
		},
		blpass: function(v){
			if(!fv.isLPass(v)){
				return '登录密码格式错误';
			}else{
				ps.lpass = v;
			}
		},
		blimgcode: function(v){
			if(v.length != 4){
				return '图片验证码长度为4';
			}else{
				ps.imgCode = v;
			}
		},
		blremberMe: function(v){
			if(v == 'true'){
				ps.remberMe = true;
			}else{
				ps.remberMe = false;
			}
		}
	});
	
	// 提交-表单
	form.on('submit(subLogin)', function(data){
		let dat = data.field;
		
		kf.oloading();
		$.post('/back/cus/subLogin', ps, 
			function(res){
				kf.cloading();
				
				if(res == null){
					kf.errorFalse('请求错误，请稍后再试');
				}else if(res.code != 1){
					kf.alert(res.msg);
				}else{
					kf.msg(res.msg);
					// 导航-个人-首页
					window.location.href = res.goUrl;
				}
			}, 
			'json'
		);
		
		return false;
	});
	
});