package salmon.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.exception.CustomizeException;
import salmon.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author SalmonC
 * @date 2021-09-09 23:41
 */
@Controller
public class DeleteController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long questionId,
            HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                questionService.deleteByToken(token, questionId);
            }
        }
        if(token == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        return "redirect:/";
    }

}
