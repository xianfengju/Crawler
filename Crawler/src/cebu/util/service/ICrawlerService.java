package cebu.util.service;

import java.util.List;

import cebu.model.Ticket;

/**
 * 主调函数接口
 * @author Administrator
 *
 */

public interface ICrawlerService {
	
	/**
	 * 获取Ticket信息
	 * @param depAirport	始发站
	 * @param arrAirport	终点站
	 * @param depTime		出发时间 yyyy-mm-dd 
	 * @param retTime		到达时间
	 * @param adultNum		成人数目
	 * @return
	 */
	public List<Ticket> getTickets(String depAirport, String arrAirport, 
			String depTime, String retTime, int adultNum);
	
	/**
	 * 单程
	 * @param depAirport
	 * @param arrAirport
	 * @param depTime
	 * @param adultNum
	 * @return
	 */
	public List<Ticket> getInfoOw(String depAirport, String arrAirport, 
			String depTime, int adultNum);
	
	/**
	 * 往返
	 * @param depAirport
	 * @param arrAirport
	 * @param depTime
	 * @param retTime
	 * @param adultNum
	 * @return
	 */
	public List<Ticket> getInfoRt(String depAirport, String arrAirport, 
			String depTime, String retTime, int adultNum);
	
}