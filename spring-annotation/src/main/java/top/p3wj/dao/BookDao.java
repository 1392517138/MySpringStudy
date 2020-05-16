package top.p3wj.dao;

import org.springframework.stereotype.Repository;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 12:29 PM
 */
@Repository
public class BookDao {
    private String label = "1";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "BookDao{" +
                "label='" + label + '\'' +
                '}';
    }
}
