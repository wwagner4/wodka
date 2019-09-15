/*
 * CategorizedInfoModel.java
 *
 * Created on 03. Februar 2004, 09:32
 */

package wodka.view;

/**
 *
 * @author  wolfgang wagner
 */
public interface CategorizedInfoModel {
    
    
    public String getCategoryName(int i);
    public String getCategoryInfo(int i);
    public int categoryCount();
    
}
