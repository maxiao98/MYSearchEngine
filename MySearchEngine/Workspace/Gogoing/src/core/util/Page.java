package core.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;



import configure.Configuration;
//Page封装类
public class Page {
	private Configuration conf;
	private String url;
	private int offset;
	private String content;
	private String rawName;
	
	public Page()
	{
		
	}
	
	public String getUrl() {
		return url;
	}

	public int getOffset() {
		return offset;
	}

	public String getContent() {
		return content;
	}

	public String getRawName() {
		return rawName;
	}

	public Page(String url, int offset, String content, String rawName)
	{
		this.url = url;
		this.offset = offset;
		this.content = content;
		this.rawName = rawName;
	}
	
	public void setPage(String url, int offset, String content, String rawName)
	{
		this.url = url;
		this.offset = offset;
		this.content = content;
		this.rawName = rawName;
	}

	public void store() throws FileNotFoundException {
		conf = new Configuration();
		String fileName1 = conf.getValue("STOREPATH")+"\\Store\\Offset.txt";
		String fileName2 = conf.getValue("STOREPATH")+"\\Store\\URL.txt";
		String fileName3 = conf.getValue("STOREPATH")+"\\Store\\Content.txt";
		String fileName4 = conf.getValue("STOREPATH")+"\\Store\\Raws.txt";
	
		method(fileName1,offset);
		method(fileName2,url);
		method(fileName3,content);
		method(fileName4,rawName);
	
}

	public void method(String fileName, String content) {  
		try {  
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw"); // 打开一个随机访问文件流，按读写方式  
			long fileLength = randomFile.length(); // 文件长度，字节数   
			randomFile.seek(fileLength);  // 将写文件指针移到文件尾。  
			randomFile.writeBytes(content+"\r\n");  
			randomFile.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
}  
	public void method(String fileName, int offset) {  
		try {  
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw"); // 打开一个随机访问文件流，按读写方式  
			long fileLength = randomFile.length(); // 文件长度，字节数   
			randomFile.seek(fileLength);  // 将写文件指针移到文件尾。  
			randomFile.writeBytes(offset+"\r\n");  
			randomFile.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
}  
}
