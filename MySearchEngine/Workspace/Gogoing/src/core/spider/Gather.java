package core.spider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Gather implements Runnable {

	private Dispatcher disp;
	private String ID;
	private URLClient client = new URLClient();
	private WebAnalyzer analyzer = new WebAnalyzer();
	private File file;
	private BufferedWriter bfWriter;
	
	public Gather(String ID, Dispatcher disp)
	{
		this.ID = ID;
		this.disp = disp;
		
		file = new File("Raws\\RAW" + ID + ".txt");           //�趨������ļ���

		try {
			file.createNewFile();
			bfWriter = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		int counter = 0;
		while(counter++ <= 100)		//ÿ���߳���ȡ100����ҳ
		{
			URL url = disp.getURL();
			System.out.println("�ڵ� " + ID + "������" + " �õ�url: " + url.toString());
			String htmlDocument = client.getDocument(url);
			
			//doAnalyzer���url������ø�ʽ��document			
			if(htmlDocument.length() != 0)
			{
				ArrayList<URL> newURL = analyzer.analyze(bfWriter, url, htmlDocument);
				//htmlDocumentΪ�գ���Ҫɾ��
				if(newURL.size() != 0)
					disp.insert(newURL);
			}
			
		}
		
	}

}
