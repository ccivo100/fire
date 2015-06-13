package com.poicom.function.controller;

import com.jfinal.core.Controller;
import cn.dreampie.routebind.ControllerKey;

@ControllerKey(value="/aceAdmin",path="/admin")
public class AceAdminController extends Controller{
	
	//index-主页
	private final static String ACE_INDEX_PAGE="index/index.html";
	
	//ui-界面元素
	private final static String ACE_UI_TOP_MENU="ui_elements/two_menu.html";
	private final static String ACE_UI_TWO_MENU1="ui_elements/two_menu_1.html";
	private final static String ACE_UI_TWO_MENU2="ui_elements/two_menu_2.html";
	private final static String ACE_UI_DEFAULT_MOBILE_MENU="ui_elements/default_mobile_menu.html";
	private final static String ACE_UI_MOBILE_MENU1="ui_elements/mobile_menu_2.html";
	private final static String ACE_UI_MOBILE_MENU2="ui_elements/mobile_menu_3.html";
	private final static String ACE_UI_TYPOGRAPHY="ui_elements/typography.html";
	private final static String ACE_UI_ELEMENTS="ui_elements/elements.html";
	private final static String ACE_UI_BUTTON_ICONS="ui_elements/buttons_icons.html";
	private final static String ACE_UI_CONTENTSLIDER="ui_elements/contentslider.html";
	private final static String ACE_UI_TREEVIEW="ui_elements/treeview.html";
	private final static String ACE_UI_JQUERY_UI="ui_elements/jquery_ui.html";
	private final static String ACE_UI_NESTABLE_LIST="ui_elements/nestable_list.html";
	
	//tables-表格
	private final static String ACE_TABLE_SIMPLE_DYNAMIC="tables/simple_dynamic.html";
	private final static String ACE_TABLE_JQGRID="tables/jqgrid_plugin.html";
	
	//forms-表单
	private final static String ACE_FORMS_ELEMEMTS="forms/form_elements.html";
	private final static String ACE_FORMS_ELEMENTS2="forms/form_elements2.html";
	private final static String ACE_FORMS_WIZARD="forms/wizard_validation.html";
	private final static String ACE_FORMS_WYSWYG_MARKDOWN="forms/wysiwyg_markdown.html";
	private final static String ACE_FORMS_DROPZONE_FILE_UPLOAD="forms/dropzone_file_upload.html";
	
	//widgets-辅助小工具
	private final static String ACE_WIDGETS_PAGE="widgets/widgets.html";
	
	//gallery-图片相册
	private final static String ACE_GALLERY_PAGE="gallery/gallery.html";
	
	//morepage-页面展示
	private final static String ACE_MOREPAGE_PROFILE="more_pages/userprofile.html";
	private final static String ACE_MOREPAGE_INBOX="more_pages/inbox.html";
	private final static String ACE_MOREPAGE_PRICING_TABLE="more_pages/pricing_table.html";
	private final static String ACE_MOREPAGE_INVOICE="more_pages/invoice.html";
	private final static String ACE_MOREPAGE_TIMELINE="more_pages/timeline.html";
	private final static String ACE_MOREPAGE_LOGIN="more_pages/login.html";
	
	//
	private final static String ACE_OTHERPAGE_FAQ="other_pages/faq.html";
	private final static String ACE_OTHERPAGE_404="other_pages/error404.html";
	private final static String ACE_OTHERPAGE_500="other_pages/error500.html";
	private final static String ACE_OTHERPAGE_GRID="other_pages/grid.html";
	private final static String ACE_OTHERPAGE_BLANK="other_pages/blank.html";
	
	
	//calendar-日期控件
	private final static String ACE_CALENDAR_PAGE="calendar/calendar.html";
	
	private final static String FRONT_PAGE="front/blank.html";
	
	
	public void front(){
		render(FRONT_PAGE);
	}
	
	/**
	 * index-主页
	 */
	public void index(){
		render(ACE_INDEX_PAGE);
	}
	
	/**
	 * ui-顶部菜单布局
	 */
	public void top_menu(){
		render(ACE_UI_TOP_MENU);
	}
	
	/**
	 * ui-两列菜单布局1
	 */
	public void two_menu_1() {
		render(ACE_UI_TWO_MENU1);
	}

	/**
	 * ui-两列菜单布局2
	 */
	public void two_menu_2() {
		render(ACE_UI_TWO_MENU2);
	}
	
	/**
	 * ui-默认手机菜单布局
	 */
	public void default_mobile_menu() {

		render(ACE_UI_DEFAULT_MOBILE_MENU);
	}

	/**
	 * ui-手机菜单布局1
	 */
	public void mobile_menu_1() {

		render(ACE_UI_MOBILE_MENU1);
	}

	/**
	 * ui-手机菜单布局2
	 */
	public void mobile_menu_2() {

		render(ACE_UI_MOBILE_MENU2);
	}
	
	/**
	 * ui-页面排版
	 */
	public void typography() {

		render(ACE_UI_TYPOGRAPHY);
	}
	
	/**
	 * ui-界面元素
	 */
	public void elements() {

		render(ACE_UI_ELEMENTS);
	}
	
	/**
	 * ui-按钮和图标
	 */
	public void buttons_icons() {

		render(ACE_UI_BUTTON_ICONS);
	}
	
	/**
	 * ui-滑块元素
	 */
	public void content_slider() {

		render(ACE_UI_CONTENTSLIDER);
	}
	
	/**
	 * ui-属性视图
	 */
	public void treeview() {

		render(ACE_UI_TREEVIEW);
	}
	
	/**
	 * ui-jquery用户界面
	 */
	public void jquery_ui() {

		render(ACE_UI_JQUERY_UI);
	}
	
	/**
	 * ui-嵌套列表视图
	 */
	public void nestable_list() {

		render(ACE_UI_NESTABLE_LIST);
	}
	
	/**
	 * tables-简易表格和动态表格
	 */
	public void simpleDynamic(){
		
		render(ACE_TABLE_SIMPLE_DYNAMIC);
	}
	
	/**
	 * tables-jqGrid插件
	 */
	public void jqGridPlugin(){
		
		render(ACE_TABLE_JQGRID);
	}
	
	/**
	 * forms-基础表单元素
	 */
	public void form_elements(){
		
		render(ACE_FORMS_ELEMEMTS);
	}
	
	/**
	 * forms-高级表单元素
	 */
	public void form_elements2(){
		
		render(ACE_FORMS_ELEMENTS2);
	}
	
	/**
	 * forms-向导和校验
	 */
	public void wizard_validation(){
		
		render(ACE_FORMS_WIZARD);
	}
	
	/**
	 * forms-wysiwyg_markdown
	 */
	public void wysiwyg_markdown(){
		
		render(ACE_FORMS_WYSWYG_MARKDOWN);
	}
	
	/**
	 * forms-拖曳式文件上传
	 */
	public void dropzone_file_upload(){
		
		render(ACE_FORMS_DROPZONE_FILE_UPLOAD);
	}
	
	/**
	 * widgets-辅助小工具
	 */
	public void widgets(){
		render(ACE_WIDGETS_PAGE);
	}
	
	/**
	 * gallery-图片相册
	 */
	public void gallery(){
		render(ACE_GALLERY_PAGE);
	}
	
	/**
	 * morepage-用户详情
	 */
	public void profile(){
		
		render(ACE_MOREPAGE_PROFILE);
	}
	
	/**
	 * morepage-收件箱
	 */
	public void inbox(){
		
		render(ACE_MOREPAGE_INBOX);
	}
	
	/**
	 * morepage-价格表
	 */
	public void pricingTable(){
		
		render(ACE_MOREPAGE_PRICING_TABLE);
	}
	
	/**
	 * morepage-通知
	 */
	public void invoice(){
		
		render(ACE_MOREPAGE_INVOICE);
	}
	
	/**
	 * morepage-时间轴
	 */
	public void timeline(){
		
		render(ACE_MOREPAGE_TIMELINE);
	}
	
	/**
	 * morepage-登陆
	 */
	public void login(){
		
		render(ACE_MOREPAGE_LOGIN);
	}
	
	public void faq(){
		
		render(ACE_OTHERPAGE_FAQ);
	}
	
	/**
	 * JfinalAction的简单用法，默认查找route中和Action同名的页面，这里是个示例demo
	 * 跳转至页面error404.html
	 */
	public void error404(){
		render(ACE_OTHERPAGE_404);
	}
	
	/**
	 * JfinalAction的简单用法，默认查找route中和Action同名的页面，这里是个示例demo
	 * 跳转至页面error500.html
	 */
	public void error500(){
		render(ACE_OTHERPAGE_500);
	}
	
	/**
	 * JfinalAction的简单用法，默认查找route中和Action同名的页面，这里是个示例demo
	 * 跳转至页面grid.html
	 */
	public void grid(){
		render(ACE_OTHERPAGE_GRID);
	}
	
	/**
	 * JfinalAction的简单用法，默认查找route中和Action同名的页面，这里是个示例demo
	 * 跳转至页面blank.html
	 */
	public void blank(){
		render(ACE_OTHERPAGE_BLANK);
	}
	
	/**
	 * calendar-日期控件
	 */
	public void calendar(){
		render(ACE_CALENDAR_PAGE);
	}

}
