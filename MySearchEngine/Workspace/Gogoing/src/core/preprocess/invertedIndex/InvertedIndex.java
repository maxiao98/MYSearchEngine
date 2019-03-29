package core.preprocess.invertedIndex;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import core.preprocess.forwardIndex.ForwardIndex;

/*
InvertedIndex�ཨ����ҳ������������Ӧ��ϵΪ����ӳ��url��ͨ����������������
�����������£�
����������ȡ������ֵ����������url����������ÿ�����飬����map����Ϊkey
��url��Ϊ��value���룬���õ���map���ǵ�������
*/

//�������������������õ���������

public class InvertedIndex {

	private HashMap<String, ArrayList<String>> fordwardIndexMap;
	private HashMap<String, ArrayList<String>> invertedIndexMap;
	
	public InvertedIndex()
	{
		ForwardIndex forwardIndex = new ForwardIndex();
		fordwardIndexMap = forwardIndex.createForwardIndex();
	}
	
	// ������������
	public HashMap<String, ArrayList<String>> createInvertedIndex() {
		
		invertedIndexMap = new HashMap<String, ArrayList<String>>();
		
		// ����ԭ�����������������е���
		// Iterator������
		for (Iterator iter = fordwardIndexMap.entrySet().iterator(); iter.hasNext();) 
		{
			// map.entry ͬʱȡ����ֵ�ԣ���õ�ַ�Ͷ�Ӧ�ִ�
			Map.Entry entry = (Map.Entry) iter.next(); 
			String url = (String) entry.getKey();
			ArrayList<String> words = (ArrayList<String>) entry.getValue();
			String word;
			for(int i = 0; i < words.size(); i++)
			{
				word = words.get(i);
				//���������л�û�����key�����Լ�������ʣ��ٰ�url������
				if(!invertedIndexMap.containsKey(word))
				{
					ArrayList<String> urls = new ArrayList<String>();
					urls.add(url);
					invertedIndexMap.put(word, urls);
				}
				
				else
				{
					// �������Ѿ��������key������Ҫ�������key��
					// ��Ҫ�ҵ����key�Ӷ���url������
					ArrayList<String> urls = invertedIndexMap.get(word);
					if(!urls.contains(url))
						urls.add(url);
				}
			}
		}

		System.out.println("***************************************************************");
		System.out.println("��������������ɣ�");
		System.out.println("���������Ĵ�СΪ: " + invertedIndexMap.size());
		return invertedIndexMap;
	}

	public HashMap<String, ArrayList<String>> getInvertedIndex()
	{
		return invertedIndexMap;
	}
 static void main(String[] args) {
		//����һ���ؼ��ʣ����Ե���������Ч��
	 
		InvertedIndex invertedIndex = new InvertedIndex();
		HashMap<String, ArrayList<String>> invertedIndexMap = invertedIndex.createInvertedIndex();
		
		String key = "��Ӱ";
		ArrayList<String> urls = invertedIndexMap.get(key);
		
		if(urls != null)
		{
			System.out.println("�õ��˽�����£�");
			for(String url : urls)
				System.out.println(url);
		}
		else
		{
			System.out.println("���ϧ��û�ҵ���Ҫ�����Ĺؼ���");
		}
	}

}
