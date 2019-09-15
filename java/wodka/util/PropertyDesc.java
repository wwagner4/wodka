/*
 * Created on Apr 19, 2004
 */
package wodka.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a property.
 * 
 */
class PropertyDesc {
    
    private String key = null;
    private String value = null;
    private List comment = new ArrayList();
    private int leadingBlankLines = 1;
    private int trailingBlankLines = 0;
    
    public PropertyDesc() {
        super();
    }

    public List getComment() {
        return comment;
    }

    public void addCommentLine(String desc) {
        this.comment.add(desc);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String name) {
        this.key = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLeadingBlankLines() {
        return leadingBlankLines;
    }

    public void setLeadingBlankLines(int leadingBlankLines) {
        this.leadingBlankLines = leadingBlankLines;
    }

    public int getTrailingBlankLines() {
        return trailingBlankLines;
    }

    public void setTrailingBlankLines(int trailingBlankLines) {
        this.trailingBlankLines = trailingBlankLines;
    }

}
