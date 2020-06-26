import java.util.HashSet;

/**
 * @Author: Willendless
 * @Date: 2020-06-25
 * @Description: Do not edit
 * @LastEditTime: 2020-06-25
 * @FilePath: \code\LinearHash.java
 */

public class LinearHash implements Hash {

    KeyVal[] container;
    int size = 0;

    private LinearHash(int volume) {
        container = new KeyVal[volume];
    }

    LinearHash() {
        this(8);
    }

    @Override
    public void set(long key, long value) {
        if ((size + 1) << 1 > container.length)
            grow();

        int i;
        int beg = (int)getPosition(key);
        boolean firstTry = true;
        for (i = beg;
             i != beg || firstTry;
             i = (i + 1) % container.length) {
            firstTry = false;
            if (container[i] == null) {
                container[i] = new KeyVal(key, value);
                size++;
                check("set new");
                return;
            } else if (container[i].key == key) {
                container[i].value = value;
                check("set old");
                return;
            }
        }
        System.err.println("Fail to set");
    }

    @Override
    public Long get(long key) {
        Integer p = find(key);
        if (p == null)
            return null;
        return container[p].value;
    }

    @Override
    public void delete(long key) {

        check("delete pre");
        int LEN = container.length;
        Integer p = find(key);
        if (p == null)
            return;
        container[p] = null;
        size--;
        
        adjust(p);
        check("delete");
    }

    private void adjust(int p) {

        int i;
        int recx = -1;
        final int LEN = container.length;

        for (i = (p + 1) % LEN; i < LEN; i++) {
            if (container[i] == null)
                break;
            int ox = (int)getPosition(container[i].key);
            if (ox > i || (ox <= p))
                recx = i;
        }

        if (i == LEN) {
            for (i = 0; i < p; i++) {
                if (container[i] == null)
                    break;
                int ox = (int)getPosition(container[i].key);
                if (ox > i && ox <= p)
                    recx = i;
            }
        }

        if (recx != -1) {
            container[p] = container[recx];
            adjust(recx);
        }
    }

    private Integer find(long key) {
        int i;
        int beg = (int)getPosition(key);
        boolean firstTry = true;
        for (i = beg; i != beg || firstTry; i = (i + 1) % container.length) {
            firstTry = false;

            if (container[i] == null)
                break;
            if (container[i].key == key)
                return i;
        }
        return null;
    }

    private void grow() {
        LinearHash ret = new LinearHash(container.length << 1);

        for (int i = 0; i < container.length; i++) {
            if (container[i] != null) {
                ret.set(container[i].key, container[i].value);
            }
        }

        container = new KeyVal[container.length << 1];

        System.arraycopy(ret.container, 0, container, 0, container.length);
    }

    private long getPosition(long key) {
        return key % container.length;
    }

    private void check(String src) {
        for (int i = 0; i < container.length; i++) {
            if (container[i] != null && find(container[i].key) == null) {
                System.out.println(src + " fail");
                System.exit(-1);
            }
        }
    }

}
