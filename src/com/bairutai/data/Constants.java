package com.bairutai.data;

import java.util.HashMap;

import com.bairutai.sinaweibo.R;

/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public interface Constants {

	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String APP_KEY      = "233509710";

	/** 
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
	 * 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
	 * 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
	 * 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = 
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";
	
	public static final int LOCATION_RESULT_INVAID = 0;
	public static final int LOCATION_RESULT_OK = 1;
	public static final int LOCATION_RESULT_DELETE = 2;

	public static final HashMap<String, Integer> mSmileyMap=new HashMap<String, Integer>(){
		 {
			put("[神马]", Integer.valueOf(R.drawable.f_shenma));  
			put("[浮云]", Integer.valueOf(R.drawable.w_fuyun));  
			put("[给力]", Integer.valueOf(R.drawable.f_geili));  
			put("[围观]", Integer.valueOf(R.drawable.o_weiguan));  
			put("[威武]", Integer.valueOf(R.drawable.f_v5));  
			put("[熊猫]", Integer.valueOf(R.drawable.d_xiongmao));  
			put("[兔子]", Integer.valueOf(R.drawable.d_tuzi));  
			put("[奥特曼]", Integer.valueOf(R.drawable.d_aoteman));  
			put("[囧]", Integer.valueOf(R.drawable.f_jiong));
			put("[织]", Integer.valueOf(R.drawable.f_zhi));  
			put("[doge]", Integer.valueOf(R.drawable.d_doge));  
			put("[猪头]", Integer.valueOf(R.drawable.d_zhutou));  
			put("[黑线]", Integer.valueOf(R.drawable.d_heixian));  
			put("[哈哈]", Integer.valueOf(R.drawable.d_haha));  
			put("[吐]", Integer.valueOf(R.drawable.d_tu));  
			put("[嘘]", Integer.valueOf(R.drawable.d_xu));  
			put("[悲伤]", Integer.valueOf(R.drawable.d_beishang));  
			put("[闭嘴]", Integer.valueOf(R.drawable.d_bizui));  
			put("[吃惊]", Integer.valueOf(R.drawable.d_chijing));  
			put("[可怜]", Integer.valueOf(R.drawable.d_kelian));  
			put("[太开心]", Integer.valueOf(R.drawable.d_taikaixin));  
			put("[喵喵]", Integer.valueOf(R.drawable.d_miao));  
			put("[ok]", Integer.valueOf(R.drawable.h_ok));  
			put("[亲亲]", Integer.valueOf(R.drawable.d_qinqin));  
			put("[生病]", Integer.valueOf(R.drawable.d_shengbing));  
			put("[思考]", Integer.valueOf(R.drawable.d_sikao));  
			put("[失望]", Integer.valueOf(R.drawable.d_shiwang));   
			put("[委屈]", Integer.valueOf(R.drawable.d_weiqu));  
			put("[耶]", Integer.valueOf(R.drawable.h_ye));  
			put("[疑问]", Integer.valueOf(R.drawable.d_yiwen));  
			put("[阴险]", Integer.valueOf(R.drawable.d_yinxian));  
			put("[赞]", Integer.valueOf(R.drawable.h_zan));
			put("[挤眼]", Integer.valueOf(R.drawable.d_jiyan));  
			put("[抱抱]", Integer.valueOf(R.drawable.d_taikaixin));  
			put("[鄙视]", Integer.valueOf(R.drawable.d_bishi));  
			put("[馋嘴]", Integer.valueOf(R.drawable.d_chanzui));  
			put("[鼓掌]", Integer.valueOf(R.drawable.d_guzhang));  
			put("[弱]", Integer.valueOf(R.drawable.h_ruo));  
			put("[可爱]", Integer.valueOf(R.drawable.d_keai));  
			put("[伤心]", Integer.valueOf(R.drawable.l_shangxin));  
			put("[右哼哼]", Integer.valueOf(R.drawable.d_youhengheng));  
			put("[左哼哼]", Integer.valueOf(R.drawable.d_zuohengheng)); 
			put("[来]", Integer.valueOf(R.drawable.h_lai)); 
			put("[酷]", Integer.valueOf(R.drawable.d_ku)); 
			put("[good]", Integer.valueOf(R.drawable.h_good)); 
			put("[偷笑]", Integer.valueOf(R.drawable.d_touxiao)); 
			put("[浪]", Integer.valueOf(R.drawable.d_lang)); 
			put("[礼物]", Integer.valueOf(R.drawable.o_liwu)); 
			put("[泪]", Integer.valueOf(R.drawable.d_lei)); 
			put("[挖鼻]", Integer.valueOf(R.drawable.d_wabishi)); 
			put("[NO]", Integer.valueOf(R.drawable.h_buyao)); 
			put("[钟]", Integer.valueOf(R.drawable.o_zhong)); 
			put("[哼]", Integer.valueOf(R.drawable.d_heng)); 
			put("[互粉]", Integer.valueOf(R.drawable.f_hufen)); 
			put("[困]", Integer.valueOf(R.drawable.d_kun)); 
			put("[爱你]", Integer.valueOf(R.drawable.d_aini)); 
			put("[拍照]", Integer.valueOf(R.drawable.o_zhaoxiangji)); 
			put("[钱]", Integer.valueOf(R.drawable.d_qian)); 
			put("[怒]", Integer.valueOf(R.drawable.d_nu)); 
			put("[拜拜]", Integer.valueOf(R.drawable.d_baibai)); 
			put("[抓狂]", Integer.valueOf(R.drawable.d_zhuakuang)); 
			put("[晕]", Integer.valueOf(R.drawable.d_yun)); 
			put("[心]", Integer.valueOf(R.drawable.l_xin)); 
			put("[怒骂]", Integer.valueOf(R.drawable.d_numa)); 
			put("[害羞]", Integer.valueOf(R.drawable.d_haixiu)); 
			put("[汗]", Integer.valueOf(R.drawable.d_han)); 
			put("[嘻嘻]", Integer.valueOf(R.drawable.d_xixi)); 
			put("[笑cry]", Integer.valueOf(R.drawable.d_xiaoku)); 
			put("[白眼]", Integer.valueOf(R.drawable.d_landelini)); 
			put("[蜡烛]", Integer.valueOf(R.drawable.o_lazhu)); 
			put("[哈欠]", Integer.valueOf(R.drawable.d_dahaqi)); 
			put("[草泥马]", Integer.valueOf(R.drawable.d_shenshou)); 
			put("[话筒]", Integer.valueOf(R.drawable.o_huatong)); 
			put("[色]", Integer.valueOf(R.drawable.d_huaxin)); 
			put("[睡]", Integer.valueOf(R.drawable.d_shuijiao)); 
			put("[微笑]", Integer.valueOf(R.drawable.d_hehe)); 
			put("[随手拍]", Integer.valueOf(R.drawable.o_zhaoxiangji)); 	
			put("[握手]", Integer.valueOf(R.drawable.h_woshou));	
			put("[笑哈哈]", Integer.valueOf(R.drawable.lxh_xiaohaha));
			put("[抠鼻屎]", Integer.valueOf(R.drawable.lxh_koubishi));
			put("[鲜花]", Integer.valueOf(R.drawable.w_xianhua));
			put("[作揖]", Integer.valueOf(R.drawable.h_zuoyi));
			put("[蛋糕]", Integer.valueOf(R.drawable.o_dangao));
			put("[玫瑰]", Integer.valueOf(R.drawable.lxh_meigui));
			put("[悲催]", Integer.valueOf(R.drawable.lxh_beicui));
			put("[被电]", Integer.valueOf(R.drawable.lxh_beidian));
			put("[干杯]", Integer.valueOf(R.drawable.o_ganbei));
			put("[飞机]", Integer.valueOf(R.drawable.o_feiji));
			put("[围脖]", Integer.valueOf(R.drawable.o_weibo));
			put("[音乐]", Integer.valueOf(R.drawable.o_yinyue));
			put("[绿丝带]", Integer.valueOf(R.drawable.o_lvsidai));
			put("[喜]", Integer.valueOf(R.drawable.f_xi));
			put("[萌]", Integer.valueOf(R.drawable.f_meng));
			put("[下雨]", Integer.valueOf(R.drawable.w_xiayu));
			put("[太阳]", Integer.valueOf(R.drawable.w_taiyang));
			put("[微风]", Integer.valueOf(R.drawable.w_weifeng));
			put("[月亮]", Integer.valueOf(R.drawable.w_yueliang));
			put("[沙尘暴]", Integer.valueOf(R.drawable.w_shachenbao));
			put("[肥皂]", Integer.valueOf(R.drawable.d_feizao));
			put("[顶]", Integer.valueOf(R.drawable.d_ding));
			put("[衰]", Integer.valueOf(R.drawable.d_shuai));
			put("[最右]", Integer.valueOf(R.drawable.d_zuiyou));
			put("[男孩儿]", Integer.valueOf(R.drawable.d_nanhaier));
			put("[女孩儿]", Integer.valueOf(R.drawable.d_nvhaier));
		}
	};
}

