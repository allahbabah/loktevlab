import java.net.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;


public class Server {
	
	private static int[][] SumMatrix(JSONArray params) throws JSONException {
		JSONArray param1 = params.getJSONArray(0);
    	JSONArray param2 = params.getJSONArray(1);
    	
		int[][] num = new int[param1.length()][param1.length()];
    	JSONArray p1, p2;

    	for (int i = 0; i < param1.length(); ++i) {
    		p1 = param1.getJSONArray(i);
    		p2 = param2.getJSONArray(i);
    		for (int j = 0; j < param1.length(); ++j) {
    			num[i][j] = (Integer) p1.getInt(j) + (Integer) p2.getInt(j);
    		}
    	}
		return num;
	}
    
    private static String doJson(String str) {
	    try{
	    	JSONObject json = new JSONObject(str);
	    	int id = json.getInt("id");
	    	String method = json.getString("method");
	    	JSONArray params = (JSONArray) json.get("params");

	    	if(method.compareTo("SumMatrix") == 0) {
	    		int[][] rez = SumMatrix(params);
	    		JSONObject object = new JSONObject();
	            object.put("jsonrpc", "2.0");
	            object.put("id", id);
	            object.put("result", rez);
	            return object.toString();
	    	}
    	}
		catch(Exception x) { 
			System.out.println(x);
		}
        return str;
    }

	public static void main(String[] args) {
		Socket s = null;
		String line = "";
		
		try {
			ServerSocket server = new ServerSocket(8888);
			System.out.println("Wait...");
			
			s = server.accept();
			System.out.println("Connected");
			
			BufferedReader ps = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintStream ss = new PrintStream(s.getOutputStream());
			
			while(true) {			
				line = ps.readLine();
				
				if(line != null) {
					System.out.println("Request: " + line);
				
					String rezult = doJson(line);
					System.out.println("Response: " + rezult);
					
					ss.println(rezult);
					
					ss.flush();
				}
			}
		
		}
		catch(Exception x) { 
			System.out.println(x);
		}
	}
}