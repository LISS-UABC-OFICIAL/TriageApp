package com.example.registroincidentes;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.lang.String;

public class LoginRequest extends StringRequest {
    /*
    String domain = getString(R.string.domainURL);
    String url = domain + "/LoginDoctor.php";
     */
    private static final String LOGIN_REQUEST_URL= ServerURL.url + "Login.php";

    private Map<String,String> params;
    public  LoginRequest(String username, String pword, Response.Listener<String> listener ){
        super(Request.Method.POST, LOGIN_REQUEST_URL,listener,null);
        params= new HashMap<>();
        params.put("username",username+"");
        params.put("pword", pword);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
