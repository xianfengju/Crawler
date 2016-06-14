package cebu.main;

import java.util.ArrayList;
import java.util.Map;
import org.apache.http.client.CookieStore;
import org.junit.Test;

import cebu.dao.service.TicketService;
import cebu.model.FormParams;
import cebu.model.Ticket;
import cebu.util.Crawler;
import cebu.util.HtmlParser;
import cebu.util.FormUtil.DestStation;
import cebu.util.FormUtil.OrgStation;
import cebu.util.FormUtil.TravelOption;

public class Main {
	
	// 文件保存目录
	private final static String Dir = "D:/Documents/Github/Crawler/Data/";
	//private final static String Dir = "C:/Users/lihaijun/Documents/GitHub/Crawler/Data/";
	
	// 文件路径前缀和后缀
	private final static String Prefix_Save_File = Dir + "post_response_";
	private final static String Suffix_Save_File = ".html";
	
	// 原始url
	private final static String URL = "https://book.cebupacificair.com/Search.aspx?culture=en-us";
	// 提交表单url
	private final static String PostUrl = "https://book.cebupacificair.com/Search.aspx";
	
	private final static String SAVE_PATH = Dir + "test_savedHtmlByUrl.html";
	private final static String SAVE_PATH_IniFile = Dir + "Book a Trip.html";
	
	// 爬虫类和html解析类
	private static Crawler crawler = Crawler.getInstance();
	private static HtmlParser htmlParser = HtmlParser.getInstance();
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Main main = new Main();
		
	}

	// 时间信息
	@Test
	public void getTimeInfo() {
		System.out.println("---------------------- url ---------------------");
		String html = crawler.getHtmlByUrl(URL);
		Map<String, ArrayList<String>> map = htmlParser.parseTime(html);
		System.out.println(map);
		
		System.out.println("---------------------- file ---------------------");
		map = htmlParser.parseTime(SAVE_PATH);
		System.out.println(map);
	}
	
	// 站点信息
	// 只能在原网页另存为本地文件后进行解析
	// 直接传递url的方式无法获取到站点信息
	@Test
	public void getStationInfo() {
		System.out.println("---------------------- url ---------------------");
		String html = crawler.getHtmlByUrl(URL);
		Map<String, String> map= htmlParser.parseStation(html);
		System.out.println(map);	// 输出为空
		
		System.out.println("---------------------- file ---------------------");
		map = htmlParser.parseStation(SAVE_PATH_IniFile);
		System.out.println(map);	// 输出站点信息
	}
	
	/**
	 * 获取信息同时保存相关html文件
	 * 单程
	 */
	@Test
	public void get_save_TicketOneWay() {
		// 构建表单变量
		TravelOption travelOption = TravelOption.OneWay;
		OrgStation orgStation = OrgStation.HKG;
		DestStation destStation = DestStation.MNL;
		String depTime = "2016-06-20";
		int adultNum = 2;
		int childNum = 0;
		
		FormParams formParams = new FormParams();
		formParams.setTravelOption(travelOption)
			.setOrgStation(orgStation)
			.setDestStation(destStation)
			.setDepartureTime(depTime)
			.setAdultNum(adultNum)
			.setChildNum(childNum)
			.build();
		
		/** 文件保存路径  **/
		// post response
		String savePathPost = Prefix_Save_File 
				+ travelOption + "_" 
				+ orgStation + "_"
				+ destStation + "_"
				+ depTime + Suffix_Save_File;
		// radio value html
		String savePathRadioBase = savePathPost.replace(Suffix_Save_File, "_radio" + Suffix_Save_File);
		
		// 获取提交查询表单之后的response html，记录cookieStore，以数组方式传址
		CookieStore[] cookieStores = new CookieStore[1];
		String html = crawler.getPostResponseHtmlByParams(PostUrl, formParams, cookieStores);
		crawler.saveHtmlToFile(html, savePathPost);	// 保存文件
		
		// 获取提交表单之后的response html中的航班的radio value信息
		ArrayList<String> radioValues = htmlParser.parseRadioValue(html);
		
		// 依次提交每个radio value信息，get方式获取对应的html，包含航班的价格信息
		ArrayList<String> radioValueGeneratedHtmls = crawler.getHtmlByRadio(cookieStores[0], radioValues);
		// 保存文件
		for(int i = 0; i < radioValueGeneratedHtmls.size(); i++) {
			String priceHtml = radioValueGeneratedHtmls.get(i);
			String savePathRadio = savePathRadioBase.replace(Suffix_Save_File, "_" + (i + 1) + Suffix_Save_File);
			crawler.saveHtmlToFile(priceHtml, savePathRadio);
		}
			
		// 根据html 和radio value html 解析完整的航班信息
		ArrayList<Ticket> tickets = htmlParser.parseTicket(html, radioValueGeneratedHtmls);
		
		System.out.println(tickets);
		
		// 插入数据库
		TicketService.insert(tickets);
	}
	

}
