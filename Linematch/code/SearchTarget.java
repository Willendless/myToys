/**
 * @Author: Willendless
 * @Date: 2020-06-16
 * @Description: SearchTarget class, value produced by interpreter
 * @LastEditTime: 2020-06-16
 * @FilePath: \code\SearchTarget.java
 */
package code;

import java.util.HashSet;
import java.util.Set;

import code.SearchTarget.SearchTargetItem.SearchType;

public class SearchTarget {

    Set<SearchTargetItem> searchSet = new HashSet<>();

    static class SearchTargetItem {
        static enum SearchType {
            EXIST,
            NONEXIST
        }
        SearchType _type;
        String _target;

        SearchTargetItem(SearchType type, String target) {
            _type = type;
            _target = target;
        }

        SearchTargetItem(SearchTargetItem other) {
            _type = other._type;
            _target = other._target;
        }
    }

    SearchTarget() {

    }

    SearchTarget(SearchTarget other) {
        for (SearchTargetItem s : other.searchSet) {
            add(new SearchTargetItem(s));
        }
    }

    public void and(SearchTarget other) {
        for (SearchTargetItem s1 : searchSet) {
            for (SearchTargetItem s2 : other.searchSet) {
                if (s1._type == s2._type) {
                    add(new SearchTargetItem(s1._type, s1._target + " " + s2._target));
                    remove(s1);
                    remove(s2);
                } else {
                    remove(s1);
                    remove(s2);
                    add(new SearchTargetItem(SearchType.NONEXIST,
                                             s1._target + s2._target));
                    add(new SearchTargetItem(SearchType.EXIST, 
                                             s1._type == SearchType.EXIST ?
                                             s1._target : s2._target));
                }
            }
        }
    }

    public void or(SearchTarget other) {
        for (SearchTargetItem s : searchSet) {
            add(s);
        }
        for (SearchTargetItem s : other.searchSet) {
            add(s);
        }
    }

    public void add(SearchTargetItem si) {
        searchSet.add(si);
    }

    public void remove(SearchTargetItem si) {
        searchSet.remove(si);
    }

    @Override
    public String toString() {
        StringBuilder re = new StringBuilder();
        for (SearchTargetItem s : searchSet) {
            re.append(s._type + " " + s._target + "\n");
        }
        return re.toString();
    }

}