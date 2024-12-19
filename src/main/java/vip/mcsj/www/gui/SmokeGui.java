package vip.mcsj.www.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static vip.mcsj.www.data.SmokeData.*;

/**
 * SmokeGui为菜单对象
 */
public class SmokeGui{
    Inventory in;
    Player p;
    int slots;

    public SmokeGui(Player p,int slots){
        this.p = p;
        this.slots = slots;
    }


    public void createANInventory(){
        in = Bukkit.createInventory(new SmokeInvHolder(),this.slots,"§a香烟市场");
        List<String> smokenames = new ArrayList<>(smokelist.keySet());
        smokenames.stream().forEach(i -> in.addItem(createSmokeItem(i)));
        this.p.openInventory(in);
    }
}
