/**
 * @Author: Willendless
 * @Date: 2020-06-25
 * @Description: Do not edit
 * @LastEditTime: 2020-06-26
 * @FilePath: \code\CuckooHash.java
 */

public class CuckooHash implements Hash {

    KeyVal[][] hash = new KeyVal[2][];

    CuckooHash() {
        this(8);
    }

    private CuckooHash(int volume) {
        hash[0] = new KeyVal[volume];
        hash[1] = new KeyVal[volume];
    }

    @Override
    public void set(long key, long value) {
        int p = find(0, key);
        if (p >= 0) {
            hash[0][p].value = value;
            return;
        }
        p = find(1, key);
        if (p >= 0) {
            hash[1][p].value = value;
            return;
        }

        boolean[][] v = new boolean[2][hash[0].length];

        if (!insert(0, new KeyVal(key, value), v)) {
            grow();
            set(key, value);
        }
    }

    private boolean insert(int type, KeyVal en, boolean[][] visit) {
        int p = getPosition(type, en.key);

        if (visit[type][p] == true) {
            return false;
        }

        if (hash[type][p] == null) {
            hash[type][p] = en;
            return true;
        }

        visit[type][p] = true;
        boolean success = insert((type + 1) % 2, hash[type][p], visit);
        if (!success)
            return false;
        hash[type][p] = en;
        return true;
    }

    private void grow() {
        CuckooHash h = new CuckooHash(hash[0].length << 1);

        for (int i = 0; i < hash[0].length; i++) {
            for (int j = 0; j < 2; j++) {
                if (hash[j][i] != null)
                    h.set(hash[j][i].key, hash[j][i].value);
            }
        }

        hash[0] = h.hash[0];
        hash[1] = h.hash[1];
    }

    @Override
    public Long get(long key) {
        int p = find(0, key);
        if (p >= 0)
            return hash[0][p].value;
        p = find(1, key);
        if (p >= 0)
            return hash[1][p].value;
        return null;
    }

    private int find(int type, long key) {
        int pos = getPosition(type, key);
        if (hash[type][pos] == null ||
            hash[type][pos].key != key)
            return -1;
        return pos;
    }

    @Override
    public void delete(long key) {
        int p = find(0, key);
        if (p >= 0) {
            hash[0][p] = null;
            return;
        }
        p = find(1, key);
        if (p >= 0)
            hash[1][p] = null;
    }

    private int getPosition(int type, long key) {
        if (type == 0) {
            return (int)(key % hash[type].length);
        } else if (type == 1) {
            return (int)((key / hash[type].length) % hash[type].length);
        } else {
            System.err.println("Failed to get position");
            System.exit(-1);
        }
        return 0;
    }
    
}
