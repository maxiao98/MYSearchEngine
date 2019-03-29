package core.preprocess.forwardIndex;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import configure.Configuration;
import core.preprocess.DictionarySegment;
import core.preprocess.index.originalPageGetter;

/*
ForwardIndex类建立网页正向索引，对应关系为url映射到网页所含词组,为倒排索引做准备
建立过程如下：
从数据库中取出url，判断该网页是否已经分析过，
如果没有分析过，根据文件名和偏移
得到网页的内容，对网页内容进行分词，
得到传回来的词组，然后使得url和这个词组关联起来
*/

//对于某个网页，调用分词系统，进行分词，
//然后得到网页的地址url映射到词组的索引 

public class ForwardIndex {
	private Configuration configuration;
	private HashMap<String, ArrayList<String>> indexMap = new HashMap<String, ArrayList<String>>();
	private originalPageGetter pageGetter = new originalPageGetter();
	private DictionarySegment dictionarySegment = new DictionarySegment();
	
	public ForwardIndex()
	{}
	
	public HashMap<String, ArrayList<String>> createForwardIndex()
	{
		//正排索引
		configuration = new Configuration();
		try
		{
			//路径设定
			ArrayList<String> segResult = new ArrayList<String>();
			String fileName1 = configuration.getValue("STOREPATH")+"\\Store\\Offset.txt";
			String fileName2 = configuration.getValue("STOREPATH")+"\\Store\\URL.txt";
			String fileName3 = configuration.getValue("STOREPATH")+"\\Store\\Content.txt";
			String fileName4 = configuration.getValue("STOREPATH")+"\\Store\\Raws.txt";
			String url,fileName;
			int offset = 0;
			FileReader fileReader1 = new FileReader(fileName1);
			FileReader fileReader2 = new FileReader(fileName2);
			FileReader fileReader3 = new FileReader(fileName3);
			FileReader fileReader4 = new FileReader(fileName4);
			
			//用LineNumberReader类可以方便记录读取到的行数
			LineNumberReader lnReader1 = new LineNumberReader(fileReader1);
			LineNumberReader lnReader2 = new LineNumberReader(fileReader2);
			LineNumberReader lnReader3 = new LineNumberReader(fileReader3);
			LineNumberReader lnReader4 = new LineNumberReader(fileReader4);
	
			// 从数据库中读出Page，然后得到网页之后进行分词处理
			String temp1,temp2,temp3,temp4;
			while((temp2 = lnReader2.readLine())!=null){//遍历，一个一个建立正排索引
				
				temp1 = lnReader1.readLine(); 
				temp3 = lnReader3.readLine();
				temp4 = lnReader4.readLine(); 
				
				url = temp2;
				System.out.println(url);
				fileName = temp4;//获取原始网页库的路径
				offset = Integer.parseInt(temp1);// offset表示偏移值
				String htmlDoc = pageGetter.getContent(fileName, offset);		
				segResult = dictionarySegment.SegmentFile(htmlDoc);//分词结果
				indexMap.put(url, segResult);//建立索引
			}
			lnReader1.close();
			lnReader2.close();
			lnReader3.close();
			lnReader4.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("------------------------------------------------------------------------");
		System.out.println("建立正排索引完成！");
		System.out.println("正排索引的大小为: " + indexMap.size());
		
		return indexMap;
	}
	

	public static void main(String[] args) {
		
		//测试
		ForwardIndex forwardIndex = new ForwardIndex();
		HashMap<String, ArrayList<String>> indexMap = forwardIndex.createForwardIndex();
		
		for (Iterator iter = indexMap.entrySet().iterator(); iter.hasNext();) {
			
			Map.Entry entry = (Map.Entry) iter.next();    //map.entry 同时取出键值对
		    String url = (String) entry.getKey();
		    ArrayList<String> words = (ArrayList<String>) entry.getValue();

		    System.out.println(url + " 对应的分词结果是： " + words.size());
		}
		
	}

}
