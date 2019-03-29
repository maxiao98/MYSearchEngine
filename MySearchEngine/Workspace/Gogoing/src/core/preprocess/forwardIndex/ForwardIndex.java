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
ForwardIndex�ཨ����ҳ������������Ӧ��ϵΪurlӳ�䵽��ҳ��������,Ϊ����������׼��
�����������£�
�����ݿ���ȡ��url���жϸ���ҳ�Ƿ��Ѿ���������
���û�з������������ļ�����ƫ��
�õ���ҳ�����ݣ�����ҳ���ݽ��зִʣ�
�õ��������Ĵ��飬Ȼ��ʹ��url����������������
*/

//����ĳ����ҳ�����÷ִ�ϵͳ�����зִʣ�
//Ȼ��õ���ҳ�ĵ�ַurlӳ�䵽��������� 

public class ForwardIndex {
	private Configuration configuration;
	private HashMap<String, ArrayList<String>> indexMap = new HashMap<String, ArrayList<String>>();
	private originalPageGetter pageGetter = new originalPageGetter();
	private DictionarySegment dictionarySegment = new DictionarySegment();
	
	public ForwardIndex()
	{}
	
	public HashMap<String, ArrayList<String>> createForwardIndex()
	{
		//��������
		configuration = new Configuration();
		try
		{
			//·���趨
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
			
			//��LineNumberReader����Է����¼��ȡ��������
			LineNumberReader lnReader1 = new LineNumberReader(fileReader1);
			LineNumberReader lnReader2 = new LineNumberReader(fileReader2);
			LineNumberReader lnReader3 = new LineNumberReader(fileReader3);
			LineNumberReader lnReader4 = new LineNumberReader(fileReader4);
	
			// �����ݿ��ж���Page��Ȼ��õ���ҳ֮����зִʴ���
			String temp1,temp2,temp3,temp4;
			while((temp2 = lnReader2.readLine())!=null){//������һ��һ��������������
				
				temp1 = lnReader1.readLine(); 
				temp3 = lnReader3.readLine();
				temp4 = lnReader4.readLine(); 
				
				url = temp2;
				System.out.println(url);
				fileName = temp4;//��ȡԭʼ��ҳ���·��
				offset = Integer.parseInt(temp1);// offset��ʾƫ��ֵ
				String htmlDoc = pageGetter.getContent(fileName, offset);		
				segResult = dictionarySegment.SegmentFile(htmlDoc);//�ִʽ��
				indexMap.put(url, segResult);//��������
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
		System.out.println("��������������ɣ�");
		System.out.println("���������Ĵ�СΪ: " + indexMap.size());
		
		return indexMap;
	}
	

	public static void main(String[] args) {
		
		//����
		ForwardIndex forwardIndex = new ForwardIndex();
		HashMap<String, ArrayList<String>> indexMap = forwardIndex.createForwardIndex();
		
		for (Iterator iter = indexMap.entrySet().iterator(); iter.hasNext();) {
			
			Map.Entry entry = (Map.Entry) iter.next();    //map.entry ͬʱȡ����ֵ��
		    String url = (String) entry.getKey();
		    ArrayList<String> words = (ArrayList<String>) entry.getValue();

		    System.out.println(url + " ��Ӧ�ķִʽ���ǣ� " + words.size());
		}
		
	}

}
