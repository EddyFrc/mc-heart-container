package fr.tayaut.ehc.mixin;

import fr.tayaut.ehc.EddysHeartContainer;
import fr.tayaut.ehc.data.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {

    @Unique
    private boolean dragonKilled;
    @Unique
    private boolean witherKilled;
    @Unique
    private boolean elderGuardianKilled;
    @Unique
    private boolean wardenKilled;

    @Unique
    public boolean isDragonKilled() {
        return dragonKilled;
    }
    @Unique
    public boolean isWitherKilled() {
        return witherKilled;
    }
    @Unique
    public boolean isElderGuardianKilled() {
        return elderGuardianKilled;
    }
    @Unique
    public void setDragonKilled(boolean dragonKilled) {
        this.dragonKilled = dragonKilled;
    }
    @Unique
    public void setWitherKilled(boolean witherKilled) {
        this.witherKilled = witherKilled;
    }
    @Unique
    public void setElderGuardianKilled(boolean elderGuardianKilled) {
        this.elderGuardianKilled = elderGuardianKilled;
    }
    @Unique
    public boolean isWardenKilled() {
        return wardenKilled;
    }
    @Unique
    public void setWardenKilled(boolean wardenKilled) {
        this.wardenKilled = wardenKilled;
    }

    @Override
    public boolean ehc$onDragonKilled() {
        if (!isDragonKilled()) {
            setDragonKilled(true);
            return false;

        } else {
            return true;
        }
    }

    @Override
    public boolean ehc$onWitherKilled() {
        if (!isWitherKilled()) {
            setWitherKilled(true);
            return false;

        } else {
            return true;
        }
    }

    @Override
    public boolean ehc$onElderGuardianKilled() {
        if (!isElderGuardianKilled()) {
            setElderGuardianKilled(true);
            return false;

        } else {
            return true;
        }
    }

    @Override
    public boolean ehc$onWardenKilled() {
        if (!isWardenKilled()) {
            setWardenKilled(true);
            return false;

        } else {
            return true;
        }
    }

    @Inject(method = "writeData", at = @At("HEAD"))
    protected void injectWriteMethod(WriteView view, CallbackInfo ci) {
        if (view == null) {
            EddysHeartContainer.LOGGER.warn("Can't save data for this mod due to an unexpected situation. Please contact the mod author.");
            EddysHeartContainer.LOGGER.warn("What went wrong : the WriteView \"view\" appears to be null.");
            return;
        }

        view.putBoolean("ehc.dragon_killed", isDragonKilled());
        view.putBoolean("ehc.wither_killed", isWitherKilled());
        view.putBoolean("ehc.elder_guardian_killed", isElderGuardianKilled());
        view.putBoolean("ehc.warden_killed", isWardenKilled());
    }

    @Inject(method = "readData", at = @At("HEAD"))
    protected void injectReadMethod(ReadView view, CallbackInfo ci) {
        if (view == null) {
            EddysHeartContainer.LOGGER.warn("Can't read saved data for this mod due to an unexpected situation. Please contact the mod author.");
            EddysHeartContainer.LOGGER.warn("What went wrong : the ReadView \"view\" appears to be null.");
            return;
        }

        setDragonKilled(view.getBoolean("ehc.dragon_killed", false));
        setWitherKilled(view.getBoolean("ehc.wither_killed", false));
        setElderGuardianKilled(view.getBoolean("ehc.elder_guardian_killed", false));
        setWardenKilled(view.getBoolean("ehc.warden_killed", false));
    }
}
