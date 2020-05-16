package top.p3wj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import top.p3wj.service.BookService;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 12:28 PM
 */
@Controller
public class BookController {
    @Autowired
    private BookService bookService;
}
