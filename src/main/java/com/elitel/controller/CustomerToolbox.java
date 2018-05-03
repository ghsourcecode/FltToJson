package com.elitel.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.esri.arcgis.geoprocessing.GeoProcessor;
import com.esri.arcgis.geoprocessing.IGeoProcessorResult;
import com.esri.arcgis.geoprocessing.IGpEnumList;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.VarArray;
import com.esri.arcgis.system.esriJobStatus;
import com.esri.arcgis.system.esriLicenseExtensionCode;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;

/**
 * 演示调用ArcObject 10.5 调用自定义 toolbox 工具
 * 在运行该示例工程代码前，一定要配置好 ArcObject 开发环境， jdk 32 位，并安装 ArcObject for java开发包
 * @author DaiH 
 * @date 2018-4-17
 */
public class CustomerToolbox {
	private static GeoProcessor gp = null;
	
	private static class CustomerToolboxHolder {
		private static final CustomerToolbox customerToolbox = new CustomerToolbox();
	}
	
	public static void initCallArcToolbox() {
		new CustomerToolbox();
	}
	
	public static void testCallToolbox() {
		LocalDateTime start = LocalDateTime.now();

		String projectRoot = new File("").getAbsolutePath();
		String path1 = System.getProperty("user.dir");
		String path2 = CustomerToolbox.class.getClassLoader().getResource("/").getPath();
		String path4 = CustomerToolbox.class.getClassLoader().getResource("").getPath();
		String path3 = path2.replaceFirst("/WEB-INF/classes", "").replaceFirst("/", "");
		String path5 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String path6 = CustomerToolbox.class.getResource("").getPath();
		System.out.println(projectRoot + "-" + path2);

		GeoProcessor gpp = null;
		try {
			gpp = new GeoProcessor();
			
			gpp.resetEnvironments();
			
			IGpEnumList gpToolboxList = gpp.listToolboxes("ZCustomer");
			String temp = null;
			if(gpToolboxList != null) {
				temp = gpToolboxList.next();
				if(temp != null && temp.equalsIgnoreCase("ZCustomer")) {
				}
				else {
					gpp.addToolbox("E:/临时工作/江西演示/customer_arctoolbox/resource/customertoolbox/ZCustomer.tbx");
				}
			}
			// Add the BestPath toolbox.
			// Generate the array of parameters.
			VarArray parameters = new VarArray();
			//输入tif路径
			parameters.add("E:/临时工作/江西演示/customer_arctoolbox/resource/data/rain_2016.flt");
			//重采样分类列表
			parameters.add("0 0.013435 1;"
					+ "0.013435 0.037422 2;0.037422 0.080247 3;"
					+ "0.080247 0.156709 4;0.156709 0.293223 5;"
					+ "0.293223 0.536956 6;0.536956 0.972118 7;"
					+ "0.972118 1.749056 8;1.749056 3.136204 9;"
					+ "3.136204 5.612822 10");
			//输出json路径
			parameters.add("E:/临时工作/江西演示/customer_arctoolbox/resource/result/rain_2016.json");
			// Execute the model tool by name.
			IGeoProcessorResult result = gpp.execute("TestProduceJsonFromFlt", parameters, null);
			while (result.getStatus() == esriJobStatus.esriJobSucceeded){

				System.out.println(result.getOutputCount());

				String resultJsonPath = (String) result.getReturnValue();
				System.out.println(resultJsonPath);

				//读取json文件
				BufferedReader reader = new BufferedReader(new FileReader(new File(resultJsonPath)));
				StringBuffer sb = new StringBuffer();
				String line = reader.readLine();
				while(line != null) {
					sb.append(line);
					line = reader.readLine();
				}
				reader.close();
				
				JSONObject jsonObject = JSONObject.parseObject(sb.toString());

				System.out.println(result.getMessageCount());
				break;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(gpp != null)
				gpp.release();
		}

		LocalDateTime end = LocalDateTime.now();
		Duration duration = Duration.between(start, end);

		System.out.println("自定义gp运行耗时：" + duration.toMillis() + "毫秒");
	}
	
	public static final CustomerToolbox getInstance() {
//		return CustomerToolboxHolder.customerToolbox;
		
		//测试读取post GP 服务
//		String urlPath = "http://192.168.0.175:6080/arcgis/rest/services/ProduceJsonFromFltWithNoProject1/GPServer/ProduceJsonFromFltWithNoProject/submitJob";
		
//		try {
//			URL restUrl = new URL(urlPath);
//			HttpURLConnection conn = (HttpURLConnection) restUrl.openConnection();
//			conn.setRequestMethod("POST");
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			
//			//参数
//			String params = "";
//			params += "&fltPath=" + URLEncoder.encode("rain_2016_09_15__08_00_hourly.flt", "utf-8") ;
//			params += "&Reclassification=" + URLEncoder.encode("0 0.240362 1;"
//											+ "0.240362 0.677384 2;"
//											+ "0.677384 1.136257 3;"
//											+ "1.136257 1.638832 4;"
//											+ "1.638832 2.163258 5;"
//											+ "2.163258 2.731386 6;"
//											+ "2.731386 3.430621 7;"
//											+ "3.430621 4.304665 8;"
//											+ "4.304665 5.593879 9", "utf-8");
//			OutputStream outputStream = conn.getOutputStream();
//			outputStream.write(params.getBytes());
//			outputStream.flush();
//			
//			//接收服务返回数据
//			InputStream inputStream = conn.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//			String line = "", resultString = "";
//			while((line = reader.readLine()) != null) {
//				resultString += line;
//			}
//			System.out.println(resultString);
//			reader.close();
//			
//			conn.disconnect();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//另一调用 rest 方式
		String urlPath = "http://192.168.0.175:6080/arcgis/rest/services/ProduceJsonFromFltWithNoProject1/GPServer/ProduceJsonFromFltWithNoProject/submitJob";
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPostRequest = new HttpPost(urlPath);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("fltPath", "rain_2016_09_16__07_00_hourly.flt"));
		params.add(new BasicNameValuePair("Reclassification", "0 0.240362 1;0.240362 0.677384 2;0.677384 1.136257 3;1.136257 1.638832 4;1.638832 2.163258 5;2.163258 2.731386 6;2.731386 3.430621 7;3.430621 4.304665 8;4.304665 5.593879 9"));
		params.add(new BasicNameValuePair("jsonPath", "E:/ArcGISGeoprocess/GPData/rain_16.json"));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params);
			httpPostRequest.setEntity(entity);
			HttpResponse response = client.execute(httpPostRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_MOVED_PERMANENTLY == statusCode 
					|| HttpStatus.SC_MOVED_TEMPORARILY == statusCode 
					|| HttpStatus.SC_SEE_OTHER == statusCode) {

					httpPostRequest.abort();//释放post请求  
					HttpGet httpGet = new HttpGet(response.getLastHeader("location").getValue());
					response = client.execute(httpGet);
					
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						//解析返回内容，判断job状态
						try {
							String resultJsonURL = getResultJsonPath(httpGet.getURI().toString());
							if(resultJsonURL == null) {
								httpGet.abort();
								return null;
							} else {
								httpGet.abort();
							}
							
							URI uri = httpGet.getURI();
							String scheme = uri.getScheme();
							String host = uri.getHost();
							int port = uri.getPort();
							
							String url = scheme + "://" + host + ":" + port + resultJsonURL;
							Document dom = Jsoup.connect(url).get();
							Elements elements = dom.getElementsByTag("pre");
							Iterator<Element> iterator = elements.iterator();
							Element element = elements.first();
							String owntext = element.ownText();
							JSONObject jsonObject = JSONObject.parseObject(owntext);
							JSONObject urlObject = jsonObject.getJSONObject("value");
							String urlString = urlObject.getString("url");
							JSONObject jsonObject2 = getJsonObjectFromURL(urlString);

							System.out.println(jsonObject);

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						String result = EntityUtils.toString(response.getEntity());
						System.out.println(result);
					}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getResultJsonPath(String jobURL) throws IOException, InterruptedException {
		//解析返回内容，判断job状态
		while(true) {
			Document dom = Jsoup.connect(jobURL).get();
			Elements elements = dom.getElementsByTag("b");
			Iterator<Element> iterator = elements.iterator();
			while (iterator.hasNext()) {
				Element element = (Element) iterator.next();
				if(element.text().equalsIgnoreCase("Job Status:")) {
					Node nextNode = element.nextSibling();
					if(nextNode.toString().trim().equalsIgnoreCase("esriJobSucceeded")) {
						elements = dom.getElementsByTag("b");
						iterator = elements.iterator();
						while(iterator.hasNext()) {
							element = (Element) iterator.next();
							if(element.text().equalsIgnoreCase("Results:")) {
								nextNode = element.nextElementSibling();
								List<Node> nodes = nextNode.childNodes();
								for(int i = 0, length = nodes.size(); i < length; i++) {
									Node childnode = nextNode.childNode(i);

									if(childnode.toString().trim().length() > 0) {
										String href = childnode.childNode(0).attr("href");
										return href;
									}
								}
							}
						}
					} else if(nextNode.toString().trim().equalsIgnoreCase("esriJobFailed")) {
						return null;
					}
				}
			}
			
			Thread.currentThread().sleep(100);
		}
	}
	
	private static JSONObject getJsonObjectFromURL(String url) {
		JSONObject responseJsonObject = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputstream = response.getEntity().getContent();
				int count = Integer.parseInt(String.valueOf(response.getEntity().getContentLength()));//返回的字节长度
				byte[] bytes = new byte[count];
				int readCount = 0;
				while(readCount <= count) {
					if(readCount == count)
						break;
					
					readCount += inputstream.read(bytes, readCount, count - readCount);
				}
				
				String readContent = new String(bytes, 0, readCount, "utf-8");
				responseJsonObject = JSONObject.parseObject(readContent);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseJsonObject;
	}
	
	private CustomerToolbox() {
		//C:\Program Files (x86)\Java\jdk1.8.0_144\bin;D:\apache-tomcat-8.5.30-windows-x86\bin
		System.out.println("==========================================");
		System.out.println(System.getProperty("java.library.path"));
		System.out.println("==========================================");
		try {
			// Initialize the engine and licenses.
			EngineInitializer.initializeEngine();
			AoInitialize aoInit = new AoInitialize();
			initializeArcGISLicenses(aoInit);
			
			gp = new GeoProcessor();
			gp.setOverwriteOutput(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printouttest() {
		System.out.println("slkdfsldfkjsdlk");
	}
	
	/**
	 * 调用自定义arctoolbox，从带有投影信息的tif图，导出json
	 * @param tiffPath
	 */
	public void produceFormattedJSONFileFromTiff() {
		LocalDateTime start = LocalDateTime.now();
		
		String projectRoot = new File("").getAbsolutePath();
		
		try {
			// Add the BestPath toolbox.
			gp.addToolbox(projectRoot + "/resource/customertoolbox/ZCustomer.tbx");
			// Generate the array of parameters.
			VarArray parameters = new VarArray();
			//输入tif路径
			parameters.add(projectRoot + "/resource/data/rain_2016.flt");
			//重采样分类列表
			parameters.add("0 0.013435 1;"
					+ "0.013435 0.037422 2;0.037422 0.080247 3;"
					+ "0.080247 0.156709 4;0.156709 0.293223 5;"
					+ "0.293223 0.536956 6;0.536956 0.972118 7;"
					+ "0.972118 1.749056 8;1.749056 3.136204 9;"
					+ "3.136204 5.612822 10");
			//输出json路径
			parameters.add(projectRoot + "/resource/result/rain_2016.json");
			// Execute the model tool by name.
			IGeoProcessorResult result = gp.execute("ProduceJsonFromFltWithNoProject", parameters, null);
			while (result.getStatus() == esriJobStatus.esriJobSucceeded){
				
				System.out.println(result.getOutputCount());
				
				String resultJsonPath = (String) result.getReturnValue();
				System.out.println(resultJsonPath);
				
				//读取json文件
				BufferedReader reader = new BufferedReader(new FileReader(new File(resultJsonPath)));
				StringBuffer sb = new StringBuffer();
				String line = reader.readLine();
				while(line != null) {
					sb.append(line);
					line = reader.readLine();
				}
				
				JSONObject jsonObject = JSONObject.parseObject(sb.toString());
				
			    System.out.println(result.getMessageCount());
			    break;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocalDateTime end = LocalDateTime.now();
		Duration duration = Duration.between(start, end);
		
		System.out.println("自定义gp运行耗时：" + duration.toMillis() + "毫秒");
		
	}
	
	
	public static void main(String[] args) {
		try {
			// Initialize the engine and licenses.
			EngineInitializer.initializeEngine();
			AoInitialize aoInit = new AoInitialize();
			initializeArcGISLicenses(aoInit);
			CustomerToolbox customerToolbox = new CustomerToolbox();
			customerToolbox.produceFormattedJSONFileFromTiff();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the lowest available ArcGIS License
	 */
	private static void initializeArcGISLicenses(AoInitialize aoInit)
	{
		try
		{
			if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngine) 
					== esriLicenseStatus.esriLicenseAvailable)
			{
				aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngine);
			}
			if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeAdvanced) 
					== esriLicenseStatus.esriLicenseAvailable)
			{
				aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeAdvanced);
			}
			else if (aoInit.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeBasic) 
					== esriLicenseStatus.esriLicenseAvailable)
			{
				aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeBasic);
			}
			else
			{
				System.err.println("Could not initialize an Engine or Basic License. Exiting application.");
				System.exit(-1);
			}
			
			aoInit.checkOutExtension(esriLicenseExtensionCode.esriLicenseExtensionCode3DAnalyst);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
}
