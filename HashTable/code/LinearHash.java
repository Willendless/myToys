/**
 * @Author: Willendless
 * @Date: 2020-06-25
 * @Description: Do not edit
 * @LastEditTime: 2020-06-26
 * @FilePath: \code\LinearHash.java
 */

public class LinearHash implements Hash {

    KeyVal[] container;
    int size = 0;

    private LinearHash(int volume, int size) {
        this.container = new KeyVal[volume];
        this.size = size;
    }

    LinearHash() {
        this(8, 0);
    }

    @Override
    public void set(long key, long value) {
        if ((size + 1) << 1 > container.length) {
            grow();
        }

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
                return;
            } else if (container[i].key == key) {
                container[i].value = value;
                return;
            }
        }
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
        Integer p = find(key);
        if (p == null)
            return;
        container[p] = null;
        size--;

        for (int i = (p + 1) % container.length;
             container[i] != null;
             i = (i + 1) % container.length) {
            long k = container[i].key;
            long value = container[i].value;
            container[i] = null;
            set(k, value);
        }

        // check("delete");
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
        LinearHash ret = new LinearHash(container.length << 1, size);

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
                System.out.println(src + " fail" + " " + container[i].key + " " + container[i].value);
                System.exit(-1);
            }
        }
    }

}
