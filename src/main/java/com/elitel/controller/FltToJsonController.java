package com.elitel.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/fltToJson")
public class FltToJsonController {
//	private CustomerToolbox customerToolbox = CustomerToolbox.getInstance();
	private static CustomerToolbox customerToolbox = null;
	
	@RequestMapping("/convert")
	@ResponseBody
	public JSONObject convertFltToJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", "convert");
		
		
		//�� tomcat ����2�ַ�ʽ�����Զ���toolbox
		//��1�֣����ýű�����������arctoolbox�ĳ���
//		Runtime runtime = Runtime.getRuntime();
//		try {
//			Process process = runtime.exec("cmd /c start C:/Users/elitel0329/Desktop/tool/startup1.bat");
//			process.waitFor();
//			
//			int exitValue = process.exitValue();
//			process.destroy();
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//��2�У��ȳ�ʼ��
		CustomerToolbox.initCallArcToolbox();
		
		
		return jsonObject;
	}
	
	@RequestMapping("/doConvert")
	@ResponseBody
	public JSONObject doConvert(){
		System.out.println("doconvert");
		
		CustomerToolbox.testCallToolbox();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", "doconvert");
		
		return jsonObject;
	}
	
}
