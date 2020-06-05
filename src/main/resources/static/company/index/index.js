/** 页面-js */
layui.define(['jquery', 'miniAdmin'], function(exports){
	let $ = layui.jquery,
	miniAdmin = layui.miniAdmin;
	
	/** 页面最高-选择器 */
	let pid = '.layui-layout-body';
	
	
	$('.login-out').on("click", function () {
        layer.msg('退出登录成功', function () {
            window.location = '/page/login-1.html';
        });
    });
	
	
});