/*
 * Copyright 2010 Jeff McHugh (Segue Development LLC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package example.test;

import java.net.URL;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwtrpccommlayer.client.impl.GwtRpcCommLayerClient;

import example.client.GreetingService;
import example.client.GreetingServiceAsync;
import example.shared.UserFormData;

public class Main
{

	public Main()
	{
	}

	public void doAsynchronousCalls() throws Exception
	{
		System.out.println("doAsynchronousCalls --Start--");
		
		URL url = new URL("http://127.0.0.1:8888/example/greet?gwt.codesvr=127.0.0.1:9997");
		GwtRpcCommLayerClient client = new GwtRpcCommLayerClient(url);
		
		/*
		 * NOTE: to make use of asynchronous mode, pass in your GWT asynch class
		 */
		GreetingServiceAsync stub = (GreetingServiceAsync) client.createRemoteServicePojoProxy(GreetingServiceAsync.class);
		

		/*
		 * REQUEST 1
		 */
		UserFormData formData = new UserFormData();
		formData.setAccountId("jsmith@gmail.com");
		formData.setName("jake smith");
		formData.setAge(32);
		stub.addUserFormData(formData, new AsyncCallback<String>() 
		{
			public void onSuccess(String result) 
			{
				System.out.println("Asynch-Call #1 Result->" + result);
			}
			public void onFailure(Throwable caught) 
			{
				System.out.println("Asynch-Call #1 Failure " + caught);
			}
		});
		System.out.println("Executed Asynch-Call #1 ");

		/*
		 * REQUEST 2
		 */
		UserFormData formData2 = new UserFormData();
		formData2.setAccountId("maryt@gmail.com");
		formData2.setName("mary taylor");
		formData2.setAge(27);
		stub.addUserFormData(formData2, new AsyncCallback<String>() 
		{
			public void onSuccess(String result) 
			{
				System.out.println("Asynch-Call #2 Result->" + result);
			}
			public void onFailure(Throwable caught) 
			{
				System.out.println("Asynch-Call #2 Failure " + caught);
			}
		});
		System.out.println("Executed Asynch-Call #2 ");
		

		/*
		 * REQUEST 3
		 */
		stub.getUserFormDataCount(new AsyncCallback<Integer>() 
		{
			public void onSuccess(Integer result) 
			{
				System.out.println("Asynch-Call #3 Result->" + result);
			}
			public void onFailure(Throwable caught) 
			{
				System.out.println("Asynch-Call #3 Failure " + caught);
			}
		});
		System.out.println("Executed Asynch-Call #3 ");

		
		/*
		 * REQUEST 4
		 */
		stub.getUserFormData(0,1,new AsyncCallback<UserFormData[]>() 
		{
			public void onSuccess(UserFormData[] result) 
			{
				ArrayList<UserFormData> list = new ArrayList<UserFormData>();
				list.addAll(java.util.Arrays.asList(result));
				System.out.println("Asynch-Call #4 Result->" + list);
			}
			
			public void onFailure(Throwable caught) 
			{
				System.out.println("Asynch-Call #4 Failure " + caught);
			}
		});
		System.out.println("Executed Asynch-Call #4 ");
		
		
		System.out.println("doAsynchronousCalls --End--");
	}
	
	public void doSynchronousCalls() throws Exception
	{
		System.out.println("doSynchronousCalls --Start--");
		
		URL url = new URL("http://127.0.0.1:8888/example/greet?gwt.codesvr=127.0.0.1:9997");
		GwtRpcCommLayerClient client = new GwtRpcCommLayerClient(url);
		GreetingService stub = (GreetingService) client.createRemoteServicePojoProxy(GreetingService.class);
		
		
		/*
		 * REQUEST 1
		 */
		UserFormData formData = new UserFormData();
		formData.setAccountId("jsmith@gmail.com");
		formData.setName("jake smith");
		formData.setAge(32);
		String resp = stub.addUserFormData(formData);
		System.out.println("Response 1 ->> " + resp);

		/*
		 * REQUEST 2
		 */
		UserFormData formData2 = new UserFormData();
		formData2.setAccountId("maryt@gmail.com");
		formData2.setName("mary taylor");
		formData2.setAge(27);
		String resp2 = stub.addUserFormData(formData2);
		System.out.println("Response 2 ->> " + resp2);
		

		/*
		 * REQUEST 3
		 */
		Integer resp3 = stub.getUserFormDataCount();
		System.out.println("Response 3 ->> " + resp3);

		
		/*
		 * REQUEST 4
		 */
		UserFormData[] resp4 = stub.getUserFormData(0, resp3);
		ArrayList<UserFormData> list = new ArrayList<UserFormData>();
		list.addAll(java.util.Arrays.asList(resp4));
		System.out.println("Response 4 ->> " + list);
		
		
		System.out.println("doSynchronousCalls --End--");
	}
	
	public void run()
	{
		try
		{
			doSynchronousCalls();
			System.out.println("");
			System.out.println("");
			doAsynchronousCalls();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Main main = new Main();
		main.run();
	}

}
