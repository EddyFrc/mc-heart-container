package fr.tayaut.ehc.mixin;

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

    @Override
    public boolean ehc$onDragonKilled() {
        if (!isDragonKilled()) {
            // le dragon n'a jamais été tué
            setDragonKilled(true);
            return false;

        } else {
            // le dragon a déjà été tué
            return true;
        }

    }

    @Override
    public boolean ehc$onWitherKilled() {
        if (!isWitherKilled()) {
            // le wither n'a jamais été tué
            setWitherKilled(true);
            return false;

        } else {
            // le wither a déjà été tué
            return true;
        }
    }

    @Override
    public boolean ehc$onElderGuardianKilled() {
        if (!isElderGuardianKilled()) {
            // le elder guardian n'a jamais été tué
            setElderGuardianKilled(true);
            return false;

        } else {
            // le elder guardian a déjà été tué
            return true;
        }
    }


    @Inject(method = "writeData", at = @At("HEAD"))
    protected void injectWriteMethod(WriteView view, CallbackInfo ci) {
        view.putBoolean("ehc.dragon_killed", isDragonKilled());
        view.putBoolean("ehc.wither_killed", isWitherKilled());
        view.putBoolean("ehc.elder_guardian_killed", isElderGuardianKilled());
    }

    @Inject(method = "readData", at = @At("HEAD"))
    protected void injectReadMethod(ReadView view, CallbackInfo ci) {
        dragonKilled = view.getBoolean("ehc.dragon_killed", false);
        witherKilled = view.getBoolean("ehc.wither_killed", false);
        elderGuardianKilled = view.getBoolean("ehc.elder_guardian_killed", false);
    }
}
