package com.liteng1220.gfgift.cmd;

import com.liteng1220.lyt.utility.TextConverter;

import org.json.JSONException;
import org.json.JSONObject;

public class CmdGetSactionInfo extends BaseCmd {

    private static final String ACTION_URL = "eJzDTTyzI1C/LT9FPTy0JDTJkwuyczP88xLy9eDMAGqJtAta"; // /api/vod/getSactionInfo.action

    private static final String JSON_KEY_SACTION_UUID = "eJwVrTkwuyczPVCBy3NTAEAG2kEBiQ=="; // sactionUuid
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

        return TextConverter.decode(JSON_KEY_SACTION_UUID) + "=" + params[0] + "&";
    }

    @Override
    protected JSONObject createJsonData(Object... params) {
        if (params.length < 1) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TextConverter.decode(JSON_KEY_SACTION_UUID), params[0]);
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

        InfoData infoData = new InfoData();
        if (jsonData instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) jsonData;
            infoData.oriUrl = jsonObject.optString(TextConverter.decode(JSON_KEY_ORI_URL));
        }

        return infoData;
    }

    public static class InfoData extends Data {
        public String oriUrl;
    }

}
