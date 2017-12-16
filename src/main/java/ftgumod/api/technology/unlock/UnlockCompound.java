package ftgumod.api.technology.unlock;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CompoundIngredient;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnlockCompound implements IUnlock {

	private final NonNullList<IUnlock> list;
	private final boolean display;
	private final Ingredient icon;

	public UnlockCompound(NonNullList<IUnlock> list) {
		this.list = list;
		Stream<IUnlock> stream = list.stream().filter(IUnlock::isDisplayed);
		display = stream.count() > 0;
		if (display)
			icon = new CompoundIngredient(stream.map(IUnlock::getIcon).collect(Collectors.toList())) {
			};
		else
			icon = null;
	}

	@Override
	public boolean isDisplayed() {
		return display;
	}

	@Override
	public Ingredient getIcon() {
		return icon;
	}

	@Override
	public boolean unlocks(ItemStack stack) {
		return list.stream().anyMatch(unlock -> unlock.unlocks(stack));
	}

	@Override
	public void unlock(EntityPlayerMP player) {
		list.forEach(unlock -> unlock.unlock(player));
	}

	@Override
	public void lock(EntityPlayerMP player) {
		list.forEach(unlock -> unlock.lock(player));
	}

}
