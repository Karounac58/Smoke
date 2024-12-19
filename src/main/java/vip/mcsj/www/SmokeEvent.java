package vip.mcsj.www;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import vip.mcsj.www.data.SmokeData;
import vip.mcsj.www.gui.SmokeInvHolder;

import static vip.mcsj.www.data.SmokeData.*;
import static vip.mcsj.www.Smoke.*;

public class SmokeEvent implements Listener {
    //变量lore，用于降耐久
    private List<String> lore;

    public SmokeEvent() {
    }

    private void doSmokeParticle(Player p,String smokename) throws NullPointerException{
        //出现了很多次，可以写成一个方法
        FileConfiguration config = Smoke.instance.getConfig();

        int level = Integer.parseInt(config.getString(smokelist.get(smokename).get((0))));
        int duration = Integer.parseInt(config.getString(smokelist.get(smokename).get((1))));
        String potioneffect = config.getString(smokelist.get(smokename).get(2));

        SmokeParticle.spawnParticle(p);
        SmokeParticle.applyPotionEffect(p, PotionEffectType.getByName(potioneffect), duration, level);
    }
    @EventHandler
    public void onPlayerSmoke(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand() != null) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains("§e世纪香烟")) {

                //a,b减耐久用
                int a ,b;
                Player p = e.getPlayer();
                //lore列表
                ItemMeta itemMeta = item.getItemMeta();
                //保存监听到的香烟的lore，方便后面减耐久
                this.lore = itemMeta.getLore();
                String durability = itemMeta.getLore().get(1);
                //为什么要substring(2)?
                //getDisplayname会包括颜色符，而这里要获取纯中文才能使用smokelist
                String smokename = itemMeta.getDisplayName().substring(2);
                //香烟名列表
                List<String> smokenames = new ArrayList<>(SmokeData.smokelist.keySet());

                //①判定目前的香烟名列表里是否有smokename，有则继续操作
                if (smokenames.contains(smokename)) {


                    a = Integer.parseInt(durability.substring(4));
                    if (a <= 0) {
                        //耐久为0则不能使用，直接返回
                        return;
                    }
                    //生成粒子
                    try {
                        doSmokeParticle(p, smokename);
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                        Bukkit.getLogger().info("生成粒子效果时出错！");
                    }
                    /*
                    SmokeParticle.spawnParticle(p);
                    SmokeParticle.applyPotionEffect(p, PotionEffectType.SPEED, 20, 2);
                     */
                    //使用一次耐久减1
                    b = a - 1;
                    //因为耐久减1，要设置对应的lore
                    this.lore.set(1,"耐久度:" + b);
                    itemMeta.setLore(this.lore);
                    item.setItemMeta(itemMeta);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerInteractInventory(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof SmokeInvHolder){
            e.setCancelled(true);
            if(e.getCurrentItem() != null && e.getClickedInventory().getHolder() instanceof SmokeInvHolder) {
                String smokename = (e.getCurrentItem().getItemMeta().getDisplayName()).substring(2);
                FileConfiguration config = Smoke.instance.getConfig();
                int price = Integer.parseInt(config.getString(smokelist.get(smokename).get(3)));
                OfflinePlayer p = (OfflinePlayer) e.getWhoClicked();
                if(econ.getBalance(p) >= price){
                    //给东西
                    giveItemToOp((Player)p,smokename);
                    //扣钱
                    econ.withdrawPlayer(p,price);
                    ((Player) p).sendMessage("§a购买成功！扣除了"+price+"金币~");
                }else{
                    ((HumanEntity) p).sendMessage("§c你的金币不足以购买香烟!");
                }
            }

        }
    }


}