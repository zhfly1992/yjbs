package com.fx.commons.utils.other;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.fx.commons.utils.tools.QC;
import com.fx.entity.cus.BaseUser;

/**
 * 密码计算工具
 */
@Component
public class PasswordHelper {

    /** 实例化RandomNumberGenerator对象，用于生成一个随机数 */
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    
    /** 散列算法名称 */
    private String algorithName = QC.ALGORITHNAME;
    
    /** 散列迭代次数 */
    private int hashInterations = QC.HASHNUM;


    /**  
	 * 获取 实例化RandomNumberGenerator对象，用于生成一个随机数  
	 * @return randomNumberGenerator
	 */
	public RandomNumberGenerator getRandomNumberGenerator() {
		return randomNumberGenerator;
	}
	/**  
	 * 设置 实例化RandomNumberGenerator对象，用于生成一个随机数  
	 * @param randomNumberGenerator
	 */
	public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}
	/**  
	 * 获取 散列算法名称  
	 * @return algorithName
	 */
	public String getAlgorithName() {
		return algorithName;
	}
	/**  
	 * 设置 散列算法名称  
	 * @param algorithName
	 */
	public void setAlgorithName(String algorithName) {
		this.algorithName = algorithName;
	}
	/**  
	 * 获取 散列迭代次数  
	 * @return hashInterations
	 */
	public int getHashInterations() {
		return hashInterations;
	}
	/**  
	 * 设置 散列迭代次数  
	 * @param hashInterations
	 */
	public void setHashInterations(int hashInterations) {
		this.hashInterations = hashInterations;
	}
	

	/**
	 * 加密算法-加密登录
	 * @param user 登录用户baseUser（密码是明文）
	 * @return 加密后的密码（密码是密文）
	 */
    public BaseUser encryptPassword(BaseUser user) {
        if (StringUtils.isNotBlank(user.getLpass())) {
            // 如果没有盐值就进行随机生成盐值，但是Shiro进行密码校验并不会再次生成盐值，因为是随机盐，Shiro会根据数据库中储存的盐值以及你注入的加密方式进行校验，而不是使用这个工具类进行校验的。
            //对user对象设置盐：salt；这个盐值是randomNumberGenerator生成的随机数，所以盐值并不需要我们指定
            //user.setSalt(randomNumberGenerator.nextBytes().toHex());

            //调用SimpleHash指定散列算法参数：1、算法名称；2、用户输入的密码；3、盐值（随机生成的）；4、迭代次数
            String newPassword = new SimpleHash(
                    algorithName,
                    user.getLpass(),
                    ByteSource.Util.bytes(user.getSalt()),
                    hashInterations).toHex();
            user.setLpass(newPassword);
        }
    	
        return user;
    }
    
    /**
     * 加密算法
     * @param pass 明文密码
     * @param salt 盐值
     * @return 加密后的密文密码
     */
    public String encryptPassword(String pass, String salt) {
    	String newPassword = "";
    	
    	if(StringUtils.isEmpty(pass)) pass = QC.DEF_LOGIN_PASS;
    	
    	if(StringUtils.isNotEmpty(pass)) {
    		//调用SimpleHash指定散列算法参数：1、算法名称；2、用户输入的密码；3、盐值（随机生成的）；4、迭代次数
            newPassword = new SimpleHash(
                algorithName,
                pass,
                ByteSource.Util.bytes(salt),
                hashInterations).toHex();
    	}
    	
    	return newPassword;
    }
    
}
