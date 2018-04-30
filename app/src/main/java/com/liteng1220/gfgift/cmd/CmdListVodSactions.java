package com.liteng1220.gfgift.cmd;

import com.liteng1220.lyt.utility.TextConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CmdListVodSactions extends BaseCmd {

    private static final String ACTION_URL = "eJzETTyzI1C/LT9HPySwuCcEtKPCU5MLsnMzyvWg9AAuKILK5g=="; // /api/vod/listVodSactions.action

    private static final String JSON_KEY_HAS_INTRODUCTION = "eJzPLSCz2zCspykP8CpTS7JzM8DADE0CBj8="; // hasIntroduction
    private static final String JSON_KEY_ROWS = "eJwQryiQ8WvBgAEWegHM"; // rows
    private static final String JSON_KEY_START_ROW = "eJwTrLkksKgTnSKLwcAD2cDSZw=="; // startRow
    private static final String JSON_KEY_VOD_UUID = "eJwFry08JFLCc1MAQALCuALh"; // vodUuid
    private static final String JSON_KEY_UUID = "eJwGrLcG1YMAQAEYbQG4"; // uuid
    private static final String JSON_KEY_NUM = "eJzELK8E0DFAAKkDAVE="; // num
    private static final String JSON_KEY_ORI_URL = "eJzDLL8oMDLVcoBAAjNVAn4="; // oriUrl

    @Override
    protected String getAction() {
        return TextConverter.decode(ACTION_URL);
    }

    @Override
    protected String createSignData(Object... params) {
        if (params.length < 1) {
            return null;
        }

        return TextConverter.decode(JSON_KEY_HAS_INTRODUCTION) + "=1&" + TextConverter.decode(JSON_KEY_ROWS) + "=5000&" + TextConverter.decode(JSON_KEY_START_ROW) + "=0&" + TextConverter.decode(JSON_KEY_VOD_UUID) + "=" + params[0] + "&";
    }

    @Override
    protected JSONObject createJsonData(Object... params) {
        if (params.length < 1) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TextConverter.decode(JSON_KEY_HAS_INTRODUCTION), 1);
            jsonObject.put(TextConverter.decode(JSON_KEY_ROWS), 5000);
            jsonObject.put(TextConverter.decode(JSON_KEY_START_ROW), 0);
            jsonObject.put(TextConverter.decode(JSON_KEY_VOD_UUID), params[0]);
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

        VodData vodData = new VodData();
        if (jsonData instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) jsonData;
            if (jsonArray.length() > 0) {
                parseVodJson(jsonArray, vodData);
            }
        }

        return vodData;
    }

    private void parseVodJson(JSONArray vodJsonArray, VodData vodData) {
        List<DataItem> dataItemList = new ArrayList<>();
        int len = vodJsonArray.length();
        for (int k = 0; k < len; k++) {
            DataItem dataItem = new DataItem();
            JSONObject vodJsonObject = vodJsonArray.optJSONObject(k);
            dataItem.uuid = vodJsonObject.optString(TextConverter.decode(JSON_KEY_UUID));
            dataItem.oriUrl = vodJsonObject.optString(TextConverter.decode(JSON_KEY_ORI_URL));
            dataItem.num = vodJsonObject.optInt(TextConverter.decode(JSON_KEY_NUM));
            dataItemList.add(dataItem);
        }

        vodData.dataItemList = dataItemList;
    }

    public static class VodData extends Data {
        public List<DataItem> dataItemList;
    }

    public static class DataItem {
        public String uuid;
        public String oriUrl;
        public int num;
    }

}
