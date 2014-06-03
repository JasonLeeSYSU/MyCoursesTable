package activity;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import model.TopicInfo;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import xml.NotificationContentHandler;
import xml.TopicContentHandler;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.example.coursetable02.R;

import download.HttpDownloader;

public class TopicList extends ListActivity{
	private List<TopicInfo> TopicInfos = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_list);
		updateListView();
	}
	private SimpleAdapter buildSimpleAdapter(List<TopicInfo> TopicInfos){
		//����adapter����
		//����һ��List���󣬲�����simpleAdapter�ı�׼����TopicInfos��ӵ�List����ȥ
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		for (Iterator<TopicInfo> iterator = TopicInfos.iterator(); iterator.hasNext();) {
			TopicInfo fi = (TopicInfo) iterator.next();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("publisher",fi.getPublisher() );
			map.put("detail", fi.getDetail());
			map.put("update", fi.getUpdate());
			list.add(map);
		}
		System.out.println("???????????????????????????????????????");
		System.out.println(list);
		//����һ��simpleAdapter����
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,R.layout.item_topic,new String[]{"publisher","detail","update"},new int[]{R.id.topic_publisher,R.id.topic_name,R.id.topic_update});
		return simpleAdapter;
	}
	private void updateListView(){
		
		//���ذ����ļ���Ϣ��xml�ļ�
		String xml = downloadXML("http://10.0.2.2/course/topic.xml");
		System.out.println("xml----------------" + xml);
		//��xml�ļ����н�������������������õ�TopicInfos�����У�����TopicInfo������õ�List������
		TopicInfos = parse(xml);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(TopicInfos);
		//��simpleAdapter�������õ�listActivity����
		setListAdapter(simpleAdapter);
	}
	private String downloadXML(String urlStr){
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlStr);
		return result;
	}
	
	
	private List<TopicInfo> parse(String xmlStr){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<TopicInfo> infos = new ArrayList<TopicInfo>();
		try{
			XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
			
			TopicContentHandler NotificationContentHandler = new TopicContentHandler(infos);
			xmlReader.setContentHandler(NotificationContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator<TopicInfo> iterator = infos.iterator(); iterator.hasNext();) {
				TopicInfo TopicInfo = (TopicInfo) iterator.next();
				System.out.println(TopicInfo);
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
		TopicInfo fi = TopicInfos.get(position);
		System.out.println("file-----"+fi);
		//�������磬���ض�Ӧ�ļ�
			//1.��Ҫ����һ��service,�ȼ��Ƚϸߣ��������ڳ�����activity�л��п�����ʱ���ж�
			//2.ÿ��ÿ���ļ������ض���Ҫ��һ���������߳��н��У���֤�û��Ľ���
		Intent intent = new Intent();
		//���ļ�����洢��intent������
		intent.putExtra("TopicInfo", fi);
		intent.setClass(FileList.this, DownloadService.class);
		startService(intent);
		//֪ͨ�û����صĽ��
		super.onListItemClick(l, v, position, id);
	}*/
}
