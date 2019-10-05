package edu.usfca.cs.dfs.init;

import com.google.gson.Gson;

public class ConfigSystemParams {

    private ConfigSystemParam[] params;

    public ConfigSystemParams(String filename) {
        params = this.buildConfigSystemParams(filename);
    }

    public ConfigSystemParam[] getParams() {
        return params;
    }

    /**
     * Reads the configSystem.json file into ArrayList<ConfigSystemParam> params
     * @param filename
     */
    private ConfigSystemParam[] buildConfigSystemParams(String filename) {
        ConfigSystemParam[] configList = (new Gson().fromJson(Init.readUsingFileChannel(filename,4096), ConfigSystemParam[].class));
        return configList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConfigSystemParams :>\n");
        for(int i=0; i<this.params.length; i++) {
            sb.append(this.params[i].toString());
        }
        return sb.toString();
    }


}
