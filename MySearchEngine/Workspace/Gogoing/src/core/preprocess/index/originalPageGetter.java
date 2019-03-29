package core.preprocess.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.omg.CORBA.portable.InputStream;

import configure.Configuration;

import core.util.MD5;
import core.util.Page;

//originalPageGetter��ʵ�ָ������������ԭʼraws�ļ��ж�ȡ��ҳ�Ĺ���

public class originalPageGetter {

	private String url = "";
	private MD5 md5 = new MD5();
	private String date = "";
	private String urlFromHead = "";
	private Configuration configuration = new Configuration();
	private LineNumberReader lnReader2;
	private LineNumberReader lnReader1;
	private LineNumberReader lnReader3;
	private LineNumberReader lnReader4;

	public originalPageGetter() {
	}

	public originalPageGetter(String url) {
		this.url = url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public String getPage() throws IOException {
		Page page = getRawsInfo(url);// ��ȡURL��Ӧ��OFFSET��ֵ
		String content = "";
		try {
			StringBuffer tfileName = new StringBuffer();
			tfileName.append(page.getRawName());

			String fileName = configuration.getValue("RAWSPATH") + "\\" + tfileName.toString();

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bfReader = new BufferedReader(fileReader);

			bfReader.skip(page.getOffset());//����ƫ����

			getRawHead(bfReader);//��ȡԭʼ��ҳ���е���ҳͷ����Ϣ
			content = getRawContent(bfReader);//��ȡԭʼ��ҳ���е���ҳ������Ϣ
			String contentMD5 = md5.getMD5ofString(content);//����MD5

			if (contentMD5.equals(page.getContent()))
				System.out.println("ͬ");
			else
				System.out.println("��ͬ");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	// ͨ��������ļ�����ƫ�Ƶõ���ҳ����
	public String getContent(String file, int offset) {

		String content = "";
		BufferedReader bfReader = null;
		try {
			StringBuffer tfileName = new StringBuffer();
			tfileName.append(file);
			String fileName = configuration.getValue("RAWSPATH") + "\\" + tfileName.toString();

			FileReader fileReader = new FileReader(fileName.toString());
			bfReader = new BufferedReader(fileReader);

			bfReader.skip(offset);
			getRawHead(bfReader);
			content = getRawContent(bfReader);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bfReader != null)
				try {
					bfReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return content;
	}

	public Page getRawsInfo(String url) throws IOException {
		String content = "";
		String raws = "";
		int offset = 0;
		String fileName1 = configuration.getValue("STOREPATH") + "\\Store\\Offset.txt";
		String fileName2 = configuration.getValue("STOREPATH") + "\\Store\\URL.txt";
		String fileName3 = configuration.getValue("STOREPATH") + "\\Store\\Content.txt";
		String fileName4 = configuration.getValue("STOREPATH") + "\\Store\\Raws.txt";
		FileReader fileReader1 = new FileReader(fileName1);
		FileReader fileReader2 = new FileReader(fileName2);
		FileReader fileReader3 = new FileReader(fileName3);
		FileReader fileReader4 = new FileReader(fileName4);
		//��LineNumberReader�����ȡ����
		lnReader1 = new LineNumberReader(fileReader1);
		lnReader2 = new LineNumberReader(fileReader2);
		lnReader3 = new LineNumberReader(fileReader3);
		lnReader4 = new LineNumberReader(fileReader4);
		int number, tempNum = 1;
		String temp2 = lnReader2.readLine();
		String temp1, temp3, temp4;
		//����ҵ�URL����¼�����������������ļ���ת��ָ������
		while (!(temp2.equals(url))) {
			temp2 = lnReader2.readLine();
		}
		number = lnReader2.getLineNumber();
		while (tempNum != number) {
			tempNum++;
			temp1 = lnReader1.readLine();
			temp3 = lnReader3.readLine();
			temp4 = lnReader4.readLine();
		}
		temp1 = lnReader1.readLine();
		temp3 = lnReader3.readLine();
		temp4 = lnReader4.readLine();

		content = temp3;
		offset = Integer.parseInt(temp1);
		raws = temp4;

		return new Page(url, offset, content, raws);//��ȡPage

	}

	private String getRawHead(BufferedReader bfReader) {
		String headString = "";
		try {
			bfReader.readLine(); // ��ȡversion
			urlFromHead = bfReader.readLine();
			headString += urlFromHead;
			
			//��ȡ���У������������Ϊ��ַ
			if (urlFromHead != null)
				urlFromHead = urlFromHead.substring(urlFromHead.indexOf(":") + 1, urlFromHead.length());
			
			//��ȡ���У������������Ϊ����
			date = bfReader.readLine();
			headString += date;
			if (date != null)
				date = date.substring(date.indexOf(":") + 1, date.length());
			
			//��¼���������
			String temp;
			while (!(temp = bfReader.readLine()).trim().isEmpty()) {
				headString += temp;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return headString;
	}
	//��ȡ��ҳ����URL��Ӧ������
	private String getRawContent(BufferedReader bfReader) {
		StringBuffer strBuffer = new StringBuffer();
		try {
			String word;
			while ((word = bfReader.readLine()) != null) {
				if (word.trim().isEmpty())
					break;
				else
					strBuffer.append(word + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return strBuffer.toString();
	}

}
