package example.server;

import com.googlecode.gwtrpccommlayer.server.GwtRpcCommLayerServlet;
import example.client.GreetingService;
import example.shared.UserFormData;

import java.util.ArrayList;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends GwtRpcCommLayerServlet/*RemoteServiceServlet*/ implements GreetingService
{
	ArrayList<UserFormData> list = new ArrayList<UserFormData>();
	
	@Override
	public String addUserFormData(UserFormData data)
	{
		list.add(data);
		return "Data saved. Thank you.";
	}

	public UserFormData[] getUserFormData(Integer startIndex, Integer length)
	{
		return list.subList(startIndex, length).toArray(new UserFormData[0]);
	}

	public Integer getUserFormDataCount()
	{
		return list.size();
	}

//	public String greetServer(String input) throws IllegalArgumentException
//	{
//		// Verify that the input is valid. 
//		if (!FieldVerifier.isValidName(input))
//		{
//			// If the input is not valid, throw an IllegalArgumentException back to
//			// the client.
//			throw new IllegalArgumentException("Name must be at least 4 characters long");
//		}
//
//		String serverInfo = getServletContext().getServerInfo();
//		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
//	}
}
