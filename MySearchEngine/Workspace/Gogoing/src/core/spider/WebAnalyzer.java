package core.spider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import core.util.HtmlParser;

public class WebAnalyzer {

	private static final String ENDPAGE = "**************************************************";
	
	public WebAnalyzer()
	{}

	//分析，抽取doc中的url并且返回
	public ArrayList<URL> analyze(BufferedWriter bfWriter, URL url, String htmlDoc) {
	
		System.out.println("获得的Document大小为：" + htmlDoc.length());

		ArrayList<URL> urlInHtmlDoc = (new HtmlParser()).urlDetector(htmlDoc);	
		saveDoc(bfWriter, url, htmlDoc);
	
		return urlInHtmlDoc;
	}
	
	//以一下格式储存
	//version:2.0
	//url:https://www.douban.com/
	//date:Wed Apr 25 18:39:53 CST 2018
	//IP:115.182.201.7
	//length:89516

	
	private void saveDoc(BufferedWriter bfWriter, URL url, String htmlDoc) {

		try {
			
			String versionString = "version:2.0\n";
			String URLString = "url:" + url.toString() + "\n";
			
			Date date = new Date();     
			String dateString = "date:" + date.toString() + "\n";
			
			InetAddress address = InetAddress.getByName(url.getHost()); 
			String IPString = address.toString();
			IPString = "IP:" + IPString.substring(IPString.indexOf("/")+1, IPString.length()) + "\n";
			
			String htmlLength = "length:" + htmlDoc.length() + "\n";
			
			//head部分
			bfWriter.append(versionString);
			bfWriter.append(URLString);
			bfWriter.append(dateString);
			bfWriter.append(IPString);
			bfWriter.append(htmlLength);
			//产生新行以为标记
			bfWriter.newLine();
			
			//content部分
			bfWriter.append(htmlDoc);
			//产生新行以为标记
			bfWriter.newLine();
			
			bfWriter.flush();	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	
	
}
