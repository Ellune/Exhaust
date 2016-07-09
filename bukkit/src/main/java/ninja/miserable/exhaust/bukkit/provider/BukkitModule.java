package ninja.miserable.exhaust.bukkit.provider;

import com.sk89q.intake.parametric.AbstractModule;
import com.sk89q.intake.parametric.provider.EnumProvider;
import org.bukkit.Achievement;
import org.bukkit.Art;
import org.bukkit.ChatColor;
import org.bukkit.CoalType;
import org.bukkit.Difficulty;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.SandstoneType;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionType;

public class BukkitModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(World.class).toProvider(new WorldProvider());

        // add more as their required
        this.bind(EntityType.class).toProvider(new EnumProvider<>(EntityType.class));
        this.bind(Material.class).toProvider(new EnumProvider<>(Material.class));
        this.bind(WeatherType.class).toProvider(new EnumProvider<>(WeatherType.class));
        this.bind(SkullType.class).toProvider(new EnumProvider<>(SkullType.class));
        this.bind(PortalType.class).toProvider(new EnumProvider<>(PortalType.class));
        this.bind(CoalType.class).toProvider(new EnumProvider<>(CoalType.class));
        this.bind(SandstoneType.class).toProvider(new EnumProvider<>(SandstoneType.class));
        this.bind(Sound.class).toProvider(new EnumProvider<>(Sound.class));
        this.bind(TreeType.class).toProvider(new EnumProvider<>(TreeType.class));
        this.bind(Skeleton.SkeletonType.class).toProvider(new EnumProvider<>(Skeleton.SkeletonType.class));
        this.bind(Villager.Profession.class).toProvider(new EnumProvider<>(Villager.Profession.class));
        this.bind(WorldType.class).toProvider(new EnumProvider<>(WorldType.class));
        this.bind(Biome.class).toProvider(new EnumProvider<>(Biome.class));
        this.bind(Statistic.class).toProvider(new EnumProvider<>(Statistic.class));
        this.bind(Effect.class).toProvider(new EnumProvider<>(Effect.class));
        this.bind(Achievement.class).toProvider(new EnumProvider<>(Achievement.class));
        this.bind(Difficulty.class).toProvider(new EnumProvider<>(Difficulty.class));
        this.bind(GameMode.class).toProvider(new EnumProvider<>(GameMode.class));
        this.bind(Instrument.class).toProvider(new EnumProvider<>(Instrument.class));
        this.bind(TreeSpecies.class).toProvider(new EnumProvider<>(TreeSpecies.class));
        this.bind(Ocelot.Type.class).toProvider(new EnumProvider<>(Ocelot.Type.class));
        this.bind(PotionType.class).toProvider(new EnumProvider<>(PotionType.class));
        this.bind(Art.class).toProvider(new EnumProvider<>(Art.class));
        this.bind(ChatColor.class).toProvider(new EnumProvider<>(ChatColor.class));
        this.bind(DyeColor.class).toProvider(new EnumProvider<>(DyeColor.class));
        this.bind(FireworkEffect.Type.class).toProvider(new EnumProvider<>(FireworkEffect.Type.class));
        this.bind(World.Environment.class).toProvider(new EnumProvider<>(World.Environment.class));
    }

}
