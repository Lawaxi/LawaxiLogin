package net.lawaxi.login.config;

import com.google.gson.*;
import net.lawaxi.login.utils.list;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AccountConfig {
    
    private static Gson gson = new GsonBuilder().create();
    private JsonObject jsonObject;

    private static void testConfigExist(){
        if(!list.fatherfolder.exists())
            list.fatherfolder.mkdir();

        if(!list.folder.exists())
            list.folder.mkdir();

        if(!list.config_player.exists())
        {
            try{
                list.config_player.createNewFile();

                //debug
                FileWriter fw = new FileWriter(list.config_player);
                JsonObject debug = new JsonObject();
                JsonObject debugnotch = new JsonObject();
                JsonArray debugips = new JsonArray();
                debugnotch.addProperty("password","123456");
                debugnotch.addProperty("email","example@lawaxi.net");
                debugips.add("11.451.41.19");
                debugnotch.add("trusted-ips",debugips);
                debug.add("Notch",debugnotch);
                fw.write(debug.toString());
                fw.close();
            }
            catch (IOException e)
            {
                System.out.print(list.SystemOutPrefix+"创建玩家账号配置时出现错误");
            }
        }
    }


    public AccountConfig(){

        testConfigExist();

        try {
            FileReader fr = new FileReader(list.config_player);
            jsonObject = gson.fromJson(fr,JsonObject.class);
            fr.close();
        }
        catch (IOException e)
        {
            System.out.print(list.SystemOutPrefix+"加载玩家账号配置时出现错误");
        }
    }

    public String getPassword(String name)
    {
        return jsonObject.get(name).getAsJsonObject().get("password").getAsString();
    }

    public String getEmail(String name)
    {
        return jsonObject.get(name).getAsJsonObject().get("email").getAsString();
    }

    public boolean hasIP(String name,String IP)
    {
        if(!list.accountConfig.hasRegisterEmail(name))
            return false;

        JsonArray IPs = jsonObject.get(name).getAsJsonObject().get("trusted-ips").getAsJsonArray();
        for(int i=0;i<IPs.size();i++)
        {
            if(IP.equals(IPs.get(i).getAsString())){
                return true;
            }
        }
        return false;
    }


    public void setPassword(String name,String passworld)
    {
        JsonObject newi;
        if(!jsonObject.has(name))
        {

            newi =new JsonObject();
            jsonObject.add(name,newi);
        }
        else
        {
            newi=jsonObject.get(name).getAsJsonObject();
            newi.remove("password");
            jsonObject.remove(name);
        }

        newi.addProperty("password",passworld);
        jsonObject.add(name,newi);
        save();
    }

    public void setEmail(String name,String email)
    {
        JsonObject newi =jsonObject.get(name).getAsJsonObject();
        newi.addProperty("email",email);
        jsonObject.remove(name);
        jsonObject.add(name,newi);
        save();
    }

    public void newIP(String name,String ip)
    {
        JsonObject newi =jsonObject.get(name).getAsJsonObject();
        JsonArray new2;

        if(newi.has("trusted-ips"))
        {
            new2 = newi.get("trusted-ips").getAsJsonArray();
        }
        else
        {
            new2=new JsonArray();
        }

        new2.add(ip);
        newi.add("trusted-ips",new2);
        jsonObject.remove(name);
        jsonObject.add(name,newi);
        save();
    }

    public boolean hasRegister(String name)
    {
        if(!jsonObject.has(name))
            return false;
        else
            return jsonObject.get(name).getAsJsonObject().has("password");
    }

    public boolean hasRegisterEmail(String name)
    {
        return jsonObject.get(name).getAsJsonObject().has("email");
    }

    private void save(){

        try {
            testConfigExist();

            //list.config_player.delete();
            //list.config_player.createNewFile();
            FileWriter fw = new FileWriter(list.config_player);
            fw.write(jsonObject.toString());
            fw.close();
        }
        catch (IOException e)
        {

        }
    }

    public boolean alreadyRegister(String email){
        return jsonObject.toString().indexOf(email)==-1;
    }
}
