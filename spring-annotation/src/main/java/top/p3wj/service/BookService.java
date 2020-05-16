package top.p3wj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.p3wj.dao.BookDao;

import javax.annotation.Resource;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 12:29 PM
 */
@Service
public class BookService {

    @Autowired(required = false)
    private BookDao bookDao;

    public void print(){
        System.out.println(bookDao);
    }
    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
