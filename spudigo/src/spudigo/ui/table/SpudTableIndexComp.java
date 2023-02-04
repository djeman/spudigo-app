package spudigo.ui.table;

import java.util.ArrayList;
import java.util.Comparator;

import spudigo.SpudItem;

public class SpudTableIndexComp implements Comparator<Integer> {
	private ArrayList<SpudItem> list = new ArrayList<SpudItem>();
	
	public SpudTableIndexComp(ArrayList<SpudItem> list) {
		this.list = list;
	}
	
	public Integer[] createIndexArray() {
        Integer[] indexes = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++)
            indexes[i] = i;
        
        return indexes;
    }
	
	@Override
    public int compare(Integer index1, Integer index2) {
		if (list.get(index1).getLon() - list.get(index2).getLon() == 0) {
			if (list.get(index1).getLat() - list.get(index2).getLat() == 0)
				return 0;
			else if (list.get(index1).getLat() - list.get(index2).getLat() > 0)
				return 1;
			else if (list.get(index1).getLat() - list.get(index2).getLat() < 0)
				return -1;
		} else {
			if (list.get(index1).getLon() - list.get(index2).getLon() == 0)
				return 0;
			else if (list.get(index1).getLon() - list.get(index2).getLon() > 0)
				return 1;
			else if (list.get(index1).getLon() - list.get(index2).getLon() < 0)
				return -1;
		}
		
		return 0;
    }
}
