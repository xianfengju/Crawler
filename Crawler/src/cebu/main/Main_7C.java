package cebu.main;

import java.util.ArrayList;

import org.junit.Test;

import cebu.model.Ticket;
import cebu.util.service.CrawlerService_7C;

public class Main_7C {
	
	public static void main(String[] args) {
		Main_7C main = new Main_7C();
		main.getTickets();
		System.out.println("done");
	}
	
	@Test
	public void getTickets() {
		
		// 获取Ticket信息
		System.out.println("-------------- one way ----------------");
		CrawlerService_7C service = CrawlerService_7C.getInstance();
		ArrayList<Ticket> tickets = service.getTickets("WEH", "ICN", "2016-06-30", "", 2);
		System.out.println(tickets);
		
		
		
		// 往返模式，尚未完成
/*		System.out.println("-------------- return ----------------");
		service = CrawlerService_7C.getInstance();
		tickets = service.getTickets("WEH", "ICN", "2016-06-30", "2016-07-31", 2);
		System.out.println(tickets);*/
		
		
		
		// 插入数据库
		// TicketService.insert(tickets);
	}
}
