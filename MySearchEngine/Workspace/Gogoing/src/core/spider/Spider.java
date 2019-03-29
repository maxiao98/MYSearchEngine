package core.spider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Spider {

	private ArrayList<URL> urls;
	private int gatherNumber = 1;//设置收集器数量
	
	public Spider(){}
	
	public Spider(ArrayList<URL> urls)
	{
		this.urls = urls;
	}
	
	public void start() {
		Dispatcher dispatcher = new Dispatcher(urls);
		for(int i = 0; i < gatherNumber; i++)
		{
			Thread gather = new Thread(new Gather(String.valueOf(i), dispatcher));
			gather.start();
		}
	}

	public static void main(String[] args) {

		ArrayList<URL> urls = new ArrayList<URL>();
		try {

			urls.add(new URL("https://www.douban.com/"));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Spider spider = new Spider(urls);
		spider.start();

	}

}
