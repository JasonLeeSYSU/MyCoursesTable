package activity;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import model.NotificationInfo;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import xml.NotificationContentHandler;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.example.coursetable02.R;

import download.HttpDownloader;

public class NotificationList extends ListActivity{
	private List<NotificationInfo> NotificationInfos = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_list);
		updateListView();
	}
	private SimpleAdapter buildSimpleAdapter(List<NotificationInfo> NotificationInfos){
		//����adapter����
		//����һ��List���󣬲�����simpleAdapter�ı�׼����NotificationInfos��ӵ�List����ȥ
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for (Iterator<NotificationInfo> iterator = NotificationInfos.iterator(); iterator.hasNext();) {
			NotificationInfo fi = (NotificationInfo) iterator.next();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("publisher",fi.getPublisher() );
			map.put("detail", fi.getDetail());
			map.put("date", fi.getDate());
			list.add(map);
		}
		System.out.println("???????????????????????????????????????");
		System.out.println(list);
		//����һ��simpleAdapter����
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,R.layout.item_notification,new String[]{"publisher","detail","date"},new int[]{R.id.notification_publisher,R.id.notification_name,R.id.notification_time});
		return simpleAdapter;
	}
	private void updateListView(){
		
		//���ذ����ļ���Ϣ��xml�ļ�
		String xml = downloadXML("http://10.0.2.2/course/notification.xml");
		System.out.println("xml----------------" + xml);
		//��xml�ļ����н�������������������õ�NotificationInfos�����У�����NotificationInfo������õ�List������
		NotificationInfos = parse(xml);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(NotificationInfos);
		//��simpleAdapter�������õ�listActivity����
		setListAdapter(simpleAdapter);
	}
	private String downloadXML(String urlStr){
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlStr);
		return result;
	}
	
	
	private List<NotificationInfo> parse(String xmlStr){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<NotificationInfo> infos = new ArrayList<NotificationInfo>();
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			
			NotificationContentHandler NotificationContentHandler = new NotificationContentHandler(infos);
			xmlReader.setContentHandler(NotificationContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator<NotificationInfo> iterator = infos.iterator(); iterator.hasNext();) {
				NotificationInfo notificationInfo = (NotificationInfo) iterator.next();
				System.out.println(notificationInfo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return infos;
	}
	/*@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//�����û�����õ���Ӧ�ļ�����
		//��ȡҪ���ص��ļ�����
		NotificationInfo fi = NotificationInfos.get(position);
		System.out.println("file-----"+fi);
		//�������磬���ض�Ӧ�ļ�
			//1.��Ҫ����һ��service,�ȼ��Ƚϸߣ��������ڳ�����activity�л��п�����ʱ���ж�
			//2.ÿ��ÿ���ļ������ض���Ҫ��һ���������߳��н��У���֤�û��Ľ���
		Intent intent = new Intent();
		//���ļ�����洢��intent������
		intent.putExtra("NotificationInfo", fi);
		intent.setClass(FileList.this, DownloadService.class);
		startService(intent);
		//֪ͨ�û����صĽ��
		super.onListItemClick(l, v, position, id);
	}*/
}
