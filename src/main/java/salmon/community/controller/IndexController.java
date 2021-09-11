package salmon.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import salmon.community.Constants;
import salmon.community.dto.PaginationDTO;
import salmon.community.dto.QuestionDTO;
import salmon.community.service.QuestionService;
import salmon.community.service.SoupService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SalmonC
 * @date 2021-08-28 20:35
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SoupService soupService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "search", required = false) String search,
                        HttpServletRequest httpServletRequest) {
        PaginationDTO pagination = questionService.listForIndex(search, page, Constants.QUESTION_PAGE_SIZE);
        List<QuestionDTO> hotQuestions = questionService.selectHot();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("hotQuestions", hotQuestions);
        String soup = soupService.getSoup();
        model.addAttribute("soup", soup);
        return "index";
    }
}