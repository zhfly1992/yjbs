/** 外部js文件模块化配置 */
layui.config({// config的设置是全局的
	base: '/commons/js/',
	version: 'v1.0'
}).extend({// 设定模块别名
	soulTable: 'soulTable',
	myutil: 'myutil',// 如果 myutil.js 是在根目录，也可以不用设定别名
	front: 'front',// 相对于上述 base 目录的子目录（值可以是：'admin/front'）
	myform: 'myform',
});