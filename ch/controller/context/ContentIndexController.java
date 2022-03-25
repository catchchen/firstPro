package com.ch.controller.context;

import com.ch.service.ArticleService;
import com.ch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping
public class ContentIndexController {
    private final ArticleService articleService;
//  UserLogin
    private final UserService userService;
    public ContentIndexController(ArticleService articleService,UserService userService){
        this.articleService = articleService;
        this.userService = userService;
    }

    /**
     * 游客用户 访问网站 页面 对页面进行初始化
     * @param model
     * @param p
     * @return
     */
    @RequestMapping
    public String index(Model model, Integer p){
//        articleService.getArticleById();
        model.addAttribute("art","artList");
        return "index";
    }

    // 登录用户 页面的初始化
    @RequestMapping("/user")
    public String index(Integer p, String token, Model model) {
        log.info("userToken"+token);
        // 如何根据 token 拿到用户数据
//      User user = userService.getUser();
        // 过滤器 实现token到 的转换
        // 用户信息add进去
        model.addAttribute("avr");
        return "index";
    }
}
