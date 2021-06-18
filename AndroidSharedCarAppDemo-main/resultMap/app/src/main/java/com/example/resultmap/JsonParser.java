package com.example.resultmap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParser {

    private String str;
    private JSONObject jsonObject;
    private boolean check = true;

    public JsonParser(String str) {
        this.str = str;

        if(str == null)
            check = false;

        try {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(str);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getStatus() {
        return jsonObject.get("status") + "";
    }

    public boolean getCheck()
    {
        return check;
    }
    public String getEmail()
    {
        return (String) jsonObject.get("email");
    }

}
