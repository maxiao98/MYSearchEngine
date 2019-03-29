package core.preprocess.invertedIndex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import core.preprocess.forwardIndex.ForwardIndex;

/*
InvertedIndex类建立网页倒排索引，对应关系为词组映射url，通过正向索引来建立
建立过程如下：
从正向索引取得索引值，遍历其中url，对于其中每个词组，推入map中作为key
而url作为其value插入，最后得到的map就是倒排索引
*/

//调用正向索引，遍历得到倒排索引

public class InvertedIndex {

	private HashMap<String, ArrayList<String>> fordwardIndexMap;
	private HashMap<String, ArrayList<String>> invertedIndexMap;
	
	public InvertedIndex()
	{
		ForwardIndex forwardIndex = new ForwardIndex();
		fordwardIndexMap = forwardIndex.createForwardIndex();
	}
	
	// 创建倒排索引
	public HashMap<String, ArrayList<String>> createInvertedIndex() {
		
		invertedIndexMap = new HashMap<String, ArrayList<String>>();
		
		// 遍历原来的正向索引，进行倒排
		// Iterator迭代器
		for (Iterator iter = fordwardIndexMap.entrySet().iterator(); iter.hasNext();) 
		{
			// map.entry 同时取出键值对，获得地址和对应分词
			Map.Entry entry = (Map.Entry) iter.next(); 
			String url = (String) entry.getKey();
			ArrayList<String> words = (ArrayList<String>) entry.getValue();
			String word;
			for(int i = 0; i < words.size(); i++)
			{
				word = words.get(i);
				//倒排索引中还没有这个key，可以加入这个词，再把url链接上
				if(!invertedIndexMap.containsKey(word))
				{
					ArrayList<String> urls = new ArrayList<String>();
					urls.add(url);
					invertedIndexMap.put(word, urls);
				}
				
				else
				{
					// 索引中已经含有这个key，不许要加入这个key，
					// 需要找到这个key从而把url链接上
					ArrayList<String> urls = invertedIndexMap.get(word);
					if(!urls.contains(url))
						urls.add(url);
				}
			}
		}

		System.out.println("***************************************************************");
		System.out.println("倒排索引建立完成！");
		System.out.println("正排索引的大小为: " + invertedIndexMap.size());
		return invertedIndexMap;
	}

	public HashMap<String, ArrayList<String>> getInvertedIndex()
	{
		return invertedIndexMap;
	}
 static void main(String[] args) {
		//输入一个关键词，测试倒排索引的效果
	 
		InvertedIndex invertedIndex = new InvertedIndex();
		HashMap<String, ArrayList<String>> invertedIndexMap = invertedIndex.createInvertedIndex();
		
		String key = "电影";
		ArrayList<String> urls = invertedIndexMap.get(key);
		
		if(urls != null)
		{
			System.out.println("得到了结果如下：");
			for(String url : urls)
				System.out.println(url);
		}
		else
		{
			System.out.println("真可惜，没找到您要搜索的关键词");
		}
	}

}
