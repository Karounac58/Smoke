package vip.mcsj.www;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
插件工具类：构建Smoke对象
 1.spawnParticle: 被事件监听器所调用的生成粒子的方法
 2.giveItemToOp: 被命令监听器所调用的给予调用者smoke的方法
 3.smokeMethod: 被spawnParticle方法调用，单次粒子的生成逻辑
 */
public class SmokeParticle {
    private PotionEffect pe;
    private ItemStack item;
    private ItemMeta itemMeta;
    private Particle particle;
    private int durability;

    public SmokeParticle(ItemStack item, PotionEffect pe, int durability) {
        this.item = item;
        this.pe = pe;
        this.durability = durability;
    }

    public static void spawnParticle(Player p) {
        Location location = p.getLocation();
        World playerWorld = location.getWorld();
        List<double[]> AO = generateLine(0.2D, 0.2D, 1.0D, -1.0D, 10);
        List<double[]> BO = generateLine(0.2D, 0.2D, 1.0D, 0.0D, 10);
        List<double[]> CO = generateLine(0.2D, 0.2D, 1.0D, 1.0D, 10);
        List<double[]> DO = generateLine(0.2D, 0.2D, 0.0D, 1.0D, 10);
        List<double[]> EO = generateLine(0.2D, 0.2D, -1.0D, 1.0D, 10);
        List<double[]> FO = generateLine(0.2D, 0.2D, -1.0D, 0.0D, 10);
        List<double[]> GO = generateLine(0.2D, 0.2D, -1.0D, -1.0D, 10);
        List<double[]> HO = generateLine(0.2D, 0.2D, 0.0D, -1.0D, 10);
        float yaw = location.getYaw();
        if (location.getYaw() < 0.0F) {
            yaw += 360.0F;
        }

        if ((double)yaw <= 157.5D && (double)yaw > 112.5D) {
            smokeMethod(GO, playerWorld, location);
        }

        if ((double)yaw <= 112.5D && (double)yaw > 67.5D) {
            smokeMethod(FO, playerWorld, location);
        }

        if ((double)yaw <= 67.5D && (double)yaw > 22.5D) {
            smokeMethod(EO, playerWorld, location);
        }

        if ((double)yaw <= 22.5D || (double)yaw > 337.5D) {
            smokeMethod(DO, playerWorld, location);
        }

        if ((double)yaw <= 337.5D && (double)yaw > 292.5D) {
            smokeMethod(CO, playerWorld, location);
        }

        if ((double)yaw <= 292.5D && (double)yaw > 247.5D) {
            smokeMethod(BO, playerWorld, location);
        }

        if ((double)yaw <= 247.5D && (double)yaw > 202.5D) {
            smokeMethod(AO, playerWorld, location);
        }

        if ((double)yaw <= 202.5D && (double)yaw > 157.5D) {
            smokeMethod(HO, playerWorld, location);
        }

    }

    public static void smokeMethod(List<double[]> list, World world, Location loc) {
        world.spawnParticle(Particle.SMOKE_NORMAL, loc.clone().add(((double[])list.get(0))[0], 1.5D, ((double[])list.get(0))[1]), 1, 0.0D, 0.0D, 0.0D, 0.1D);
        world.playSound(loc.clone(), Sound.BLOCK_FIRE_AMBIENT, 1.0F, 1.0F);
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            double[] P1 = (double[])var3.next();
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc.clone().add(P1[0], 1.5D, P1[1]), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.playSound(loc.clone(), Sound.BLOCK_FIRE_AMBIENT, 1.0F, 1.0F);
        }

    }

    public static List<double[]> generateLine(double startX, double startZ, double endX, double endZ, int resolution) {

        double XStep = (endX - startX) / (double)resolution;
        double ZStep = (endZ - startZ) / (double)resolution;
        List<double[]> result = new ArrayList();

        for(int i = 0; i <= resolution; ++i) {
            double[] point = new double[]{startX, startZ};
            result.add(point);
            startX += XStep;
            startZ += ZStep;
        }

        return result;
    }

    public static void applyPotionEffect(Player p, PotionEffectType petype, int duration, int amplifier) {
        PotionEffect pe = new PotionEffect(petype, duration, amplifier);
        pe.apply((LivingEntity)Objects.requireNonNull(p));
    }
}