package com.liteng1220.gfgift.cmd;

import com.liteng1220.lyt.utility.TextConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CmdSearch extends BaseCmd {

    private static final String ACTION_URL = "eJzDTTyzI1M/NTynNSdUvDTSk0sSs7QS0wuyczPAwB3SPwlw"; // /api/module/search.action

    private static final String JSON_KEY_KEYWORD = "eJzDLTq0sDzRy9KAQAMRIAMG"; // keyword
    private static final String JSON_KEY_VOD_LIST = "eJwFry0/xFyVSwuAQALVlALm"; // vodList
    private static final String JSON_KEY_NAME = "eJzYLS8YxFNBQAEFHgGi"; // name
    private static final String JSON_KEY_UUID = "eJwGrLcG1YMAQAEYbQG4"; // uuid
    private static final String JSON_KEY_STARRING = "eJwBrLkksKsBrLMSwcAD5IDLaw=="; // starring
    private static final String JSON_KEY_TOTAL_NUM = "eJwRryS9JzPREArzQUADz8DAVQ=="; // totalNum
    private static final String JSON_KEY_INTRODUCTION = "eJzULzCspyk8pUTYS7JzM8DACF0YBSM="; // introduction

    @Override
    protected String getAction() {
        return TextConverter.decode(ACTION_URL);
    }

    @Override
    protected String createSignData(Object... params) {
        if (params.length < 1) {
            return null;
        }

        return TextConverter.decode(JSON_KEY_KEYWORD) + "=" + params[0] + "&";
    }

    @Override
    protected JSONObject createJsonData(Object... params) {
        if (params.length < 1) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TextConverter.decode(JSON_KEY_KEYWORD), params[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected Data parseJsonData(Object jsonData) {
        if (jsonData == null) {
            return null;
        }

        SearchData searchData = new SearchData();
        if (jsonData instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) jsonData;
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.optJSONObject(0);
                JSONArray vodJsonArray = jsonObject.optJSONArray(TextConverter.decode(JSON_KEY_VOD_LIST));
                parseVodJson(vodJsonArray, searchData);
            }
        }

        return searchData;
    }

    private void parseVodJson(JSONArray vodJsonArray, SearchData searchData) {
        List<DataItem> dataItemList = new ArrayList<>();
        int len = vodJsonArray.length();
        for (int k = 0; k < len; k++) {
            DataItem dataItem = new DataItem();
            JSONObject vodJsonObject = vodJsonArray.optJSONObject(k);
            dataItem.name = vodJsonObject.optString(TextConverter.decode(JSON_KEY_NAME));
            dataItem.uuid = vodJsonObject.optString(TextConverter.decode(JSON_KEY_UUID));
            dataItem.starring = vodJsonObject.optString(TextConverter.decode(JSON_KEY_STARRING));
            dataItem.totalNum = vodJsonObject.optInt(TextConverter.decode(JSON_KEY_TOTAL_NUM));
            dataItem.intro = vodJsonObject.optString(TextConverter.decode(JSON_KEY_INTRODUCTION));
            dataItemList.add(dataItem);
        }

        searchData.dataItemList = dataItemList;
    }

    public static class SearchData extends Data {
        public List<DataItem> dataItemList;
    }

    public static class DataItem {
        public String name;
        public String uuid;
        public String starring;
        public int totalNum;
        public String intro;
    }

}
