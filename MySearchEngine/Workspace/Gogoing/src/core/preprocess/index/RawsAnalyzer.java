package core.preprocess.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.util.MD5;
import core.util.Page;

//RawsAnalyzer��ʵ���˴�ԭʼ��ҳ����Raws�ķ���������
//������ҳURL����ҳ����ժҪ����ҳ��Raws��ƫ�Ƶ�ӳ�䡢����Raws��ӳ�� 
//��Ҫ�������е��ڶ��ļ�

public class RawsAnalyzer {

	private MD5 md5 = new MD5();
	private int offset;
	private Page page;
	private String rootDirectory;

	public RawsAnalyzer(String rootName) {
		this.rootDirectory = rootName;
		page = new Page();
	}

	// �ļ��ж�ȡ
	public void createPageIndex() {
		ArrayList<String> fileNames = getSubFile(rootDirectory);
		for (String fileName : fileNames)
			createPage(fileName);
	}

	public void createPage(String fileName) {
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bfReader = new BufferedReader(fileReader);

			String word;
			offset = 0;
			int oldOffset = 0;

			while ((word = bfReader.readLine()) != null) {
				oldOffset = offset;
				
				offset += word.length() + 1;
				
				String url = readRawHead(bfReader);
				
				String content = readRawContent(bfReader);

				System.out.println(fileName +"ƫ��"+ offset);
				
				String contentMD5 = md5.getMD5ofString(content);//MD5����
				
				page.setPage(url, oldOffset, contentMD5, fileName);
				
				page.store();
			}

			bfReader.close();

			System.out.println("�ó�ʼ��ҳ��������");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//��getRawHead���ƵĲ���
	private String readRawHead(BufferedReader bfReader) {
		String urlLine = null;
		try {

			urlLine = bfReader.readLine();
			offset = offset + urlLine.length() + 1;
			if (urlLine != null)
				urlLine = urlLine.substring(urlLine.indexOf(":") + 1, urlLine.length());

			String temp;
			while (!(temp = bfReader.readLine()).trim().isEmpty()) {
				offset = offset + temp.length() + 1;
			}
			offset += 1;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return urlLine;
	}
	//��getRawContent���ƵĲ���
	private String readRawContent(BufferedReader bfReader) {
		StringBuffer strBuffer = new StringBuffer();

		try {
			String word;
			while ((word = bfReader.readLine()) != null) {
				offset = offset + word.length() + 1;
				if (word.trim().isEmpty())
					break;
				else
					strBuffer.append(word + "\n");
			}
			offset += 2;//��������
		} catch (IOException e) {
			e.printStackTrace();
		}

		return strBuffer.toString();
	}
	//����ļ����������ļ���
	public static ArrayList<String> getSubFile(String fileName) {

		ArrayList<String> fileNames = new ArrayList<String>();

		File rootFile = new File(fileName);

		if (!rootFile.exists()) {
			System.out.println("�ļ��в�����");
			return null;
		}
		if (rootFile.isFile()) {
			System.out.println("���Ǹ��ļ�");
			fileNames.add(rootFile.getAbsolutePath());
			return fileNames;
		}

		System.out.println(fileName + "�Ǹ��ļ���");
		String[] subFiles = rootFile.list();
		for (int i = 0; i < subFiles.length; i++) {
			fileNames.add(fileName + "\\" + subFiles[i]);
		}

		return fileNames;
	}

	public static void main(String[] args) {
		//����Ч��
		RawsAnalyzer analyzer = new RawsAnalyzer("Raws");
		analyzer.createPageIndex();

	}

}
