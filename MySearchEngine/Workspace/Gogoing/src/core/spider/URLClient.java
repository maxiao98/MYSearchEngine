package core.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLClient {

	public URLClient()
	{}
			
	public String getDocument(URL url)
	{
		
		URL hostURL = url;
		StringBuffer document = new StringBuffer();
		try {
			URLConnection conn = hostURL.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			
			String line = null;
			while((line = reader.readLine()) != null)
			{
				if(!line.trim().isEmpty())
					document.append(line + "\n");
			}
				
		} catch (MalformedURLException  e) {
			System.out.println("无法连接到 URL: " + url.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document.toString();
	}
}
