package salmon.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import salmon.community.Constants;
import salmon.community.dto.PaginationDTO;
import salmon.community.model.User;
import salmon.community.service.NotificationService;
import salmon.community.service.QuestionService;
import salmon.community.service.SoupService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SalmonC
 * @date 2021-08-31 17:45
 */
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SoupService soupService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        size= Constants.QUESTION_PAGE_SIZE;
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            PaginationDTO pagination = questionService.listForProfile(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
        } else if ("replies".equals(action)) {
            PaginationDTO pagination = notificationService.list(user.getId(), page, Constants.NOTIFICATION_PAGE_SIZE);
            model.addAttribute("pagination",pagination);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        String soup = soupService.getSoup();
        model.addAttribute("soup", soup);
        return "profile";
    }
}
