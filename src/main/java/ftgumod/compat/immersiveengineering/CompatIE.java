package ftgumod.compat.immersiveengineering;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import ftgumod.ItemList;
import ftgumod.compat.ICompat;
import ftgumod.technology.Technology;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class CompatIE implements ICompat {

	private final Map<MultiblockHandler.IMultiblock, Technology> unlock = new HashMap<>();

	@Override
	public boolean run(Object... arg) { // Technology, IMultiblock[], Object[]
		if (arg[0] instanceof Technology && arg[1] instanceof MultiblockHandler.IMultiblock[] && arg[2] instanceof Object[]) {
			Technology tech = (Technology) arg[0];
			MultiblockHandler.IMultiblock[] multiblocks = (MultiblockHandler.IMultiblock[]) arg[1];
			Object[] objects = (Object[]) arg[2];

			int size = Math.min(multiblocks.length, objects.length);
			for (int i = 0; i < size; i++) {
				unlock.put(multiblocks[i], tech);

				ItemList list = new ItemList(objects[i], true);
				if (!list.isEmpty())
					tech.getUnlock().add(list);
			}
			return true;
		}
		return false;
	}

	@SubscribeEvent
	public void onMultiblockForm(MultiblockHandler.MultiblockFormEvent evt) {
		if (unlock.containsKey(evt.getMultiblock()) && !unlock.get(evt.getMultiblock()).isResearched(evt.getEntityPlayer()))
			evt.setCanceled(true); // TODO: Send message?
	}

}
