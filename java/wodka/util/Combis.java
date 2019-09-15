/*
 * Created on Dec 28, 2003
 *  
 */
package wodka.util;

import java.util.*;

/**
 * @author wwagner4
 *  
 */
public class Combis {

    private Iterator combis = null;

    public Combis(List sizes) {
        super();
        LinkedList list = createCombis(sizes);
        combis = list.iterator();
    }
    public boolean hasNext() {
        return combis.hasNext();
    }
    public List next() {
        return (List) combis.next();
    }

    private LinkedList createCombis(List sizes) {
        LinkedList result = new LinkedList();
        if (sizes.isEmpty()) {
            result.add(new LinkedList());
        } else {
            int first = first(sizes);
            List restSizes = rest(sizes);
            LinkedList rest = createCombis(restSizes);
            for (int i = 0; i < first; i++) {
                Iterator iter = rest.iterator();
                while (iter.hasNext()) {
                    LinkedList l = (LinkedList) iter.next();
                    LinkedList l1 = (LinkedList)l.clone();
                    append(i, l1);
                    result.add(l1);
                }
            }
        }
        return result;
    }
    private int first(List l) {
        Integer i = (Integer) l.get(0);
        return i.intValue();
    }
    private List rest(List l) {
        List rest = new ArrayList();
        for (int i = 1; i < l.size(); i++) {
            rest.add(l.get(i));
        }
        return rest;
    }
    private void append(int i, LinkedList l) {
        l.addFirst(new Integer(i));
    }

}
