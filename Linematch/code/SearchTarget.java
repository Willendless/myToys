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

public class SearchTarget {

    Set<SearchTargetItem> searchSet = new HashSet<>();

    static class SearchTargetItem {
        String _target;
        String _nonTarget;

        SearchTargetItem(String target, String nonTarget) {
            _target = target;
            _nonTarget = nonTarget;
        }

        @Override
        public String toString() {
            String re = "";
            if (!_target.equals(""))
                re = _target;
            if (!_nonTarget.equals("")) {
                if (!re.equals(""))
                    re = re + " && " + "!" + _nonTarget;
                else
                    re = "!" + _nonTarget;
            }
            return re;
        }
    }

    SearchTarget() {

    }

    public void and(SearchTarget other) {
        Set<SearchTargetItem> newSet = new HashSet<>();
        for (SearchTargetItem s1 : searchSet) {
            for (SearchTargetItem s2 : other.searchSet) {
                newSet.add(new SearchTargetItem((s1._target + " " + s2._target).trim(),
                                                (s1._nonTarget + " " + s2._nonTarget).trim()));
            }
        }
        searchSet = newSet;
    }

    public void or(SearchTarget other) {
        for (SearchTargetItem item : other.searchSet) {
            add(item);
        }
    }

    public void add(SearchTargetItem item) {
        searchSet.add(item);
    }

    public void remove(SearchTargetItem item) {
        searchSet.remove(item);
    }

    @Override
    public String toString() {
        StringBuilder re = new StringBuilder();
        for (SearchTargetItem s : searchSet) {
            re.append("{" + s +"}" + " ");
        }
        return re.toString();
    }

}
