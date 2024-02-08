package caleb.keep.xp.mixin;

import com.mojang.authlib.GameProfile;
import caleb.keep.xp.ModGameRules;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "copyFrom", at = @At("HEAD"))
	private void injected(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		if (!alive && this.getWorld().getGameRules().getBoolean(ModGameRules.KEEP_EXP)) {
			this.experienceLevel = oldPlayer.experienceLevel;
			this.totalExperience = oldPlayer.totalExperience;
			this.experienceProgress = oldPlayer.experienceProgress;
			this.setScore(oldPlayer.getScore());
		}
	}

	@Override
	protected void dropXp() {
		if (this.getWorld().getGameRules().getBoolean(ModGameRules.KEEP_EXP)) {
			return;
		}
		super.dropXp();
	}
}
