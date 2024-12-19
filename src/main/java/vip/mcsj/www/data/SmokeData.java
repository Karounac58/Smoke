package vip.mcsj.www.data;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mcsj.www.Smoke;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class SmokeData {
    public static JavaPlugin instance = Smoke.instance;
    public static Map<String, List<String>> smokelist;

    //从config读取所有键名
    private static Set<String> takeMsgFromConfig(){
        FileConfiguration config = instance.getConfig();
        return config.getKeys(true);
    }

    //目前暂时还没有使用的必要
    /*
    private void removeMsgFromConfig(String key){
        FileConfiguration config = instance.getConfig();
        config.set("Smokes."+key,null);
    }
     */

    private static List<String> filterData(Set<String> s){
        //过滤不需要的元素
        return s.stream().filter(i -> i.split("\\.").length > 2)
                //收集
                .collect(Collectors.toList());
    }

    public static void getSmokelist(){
        //香烟信息列表
        List<String> stringList = filterData(takeMsgFromConfig());
        //提取香烟名作为键值，并以香烟名分组
        Map<String,List<String>> smokemap = stringList.stream().collect(Collectors.groupingBy(SmokeData::regxChinese));
        smokelist = smokemap;
    }

    //匹配中文字符。提取过滤后的字符串中的中文，即香烟名
    private static String regxChinese(String source){
        // 将上面要匹配的字符串转换成小写
        source = source.toLowerCase();
        // 匹配的字符串的正则表达式
        String regCharset = "[\\u4E00-\\u9FFF]+";
        Pattern p = Pattern.compile(regCharset);
        Matcher m = p.matcher(source);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }
        return sb.toString();
    }

    //命令调用，给玩家对应的香烟Item
    public static void giveItemToOp(Player p, String smokename) throws NullPointerException{
        ItemStack item = createSmokeItem(smokename);
        p.getInventory().addItem(item);
    }

    //创建香烟物品
    public static ItemStack createSmokeItem(String smokename) throws NullPointerException{
        List<String> lore = new ArrayList();
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta itemMeta = item.getItemMeta();
        FileConfiguration config = Smoke.instance.getConfig();
        if(config.contains("Smokes."+smokename)) {
            itemMeta.setDisplayName("§a" + smokename);
            lore.add("§e世纪香烟");
            lore.add("耐久度:100");
            //扩展信息
            lore.add("§a药水效果:"+config.getString(smokelist.get(smokename).get(2)));
            lore.add("§a效果等级:"+config.getString(smokelist.get(smokename).get(0)));
            lore.add("§a持续时间:"+config.getString(smokelist.get(smokename).get(1))+"tick");
            lore.add("§a价格:"+config.getString(smokelist.get(smokename).get(3))+"$");
            itemMeta.setLore(lore);
        }else{
            //config中没有这种烟则报错
            throw new NullPointerException();
        }
        item.setItemMeta(itemMeta);
        return item;
    }

}
