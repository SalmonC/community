package salmon.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import salmon.community.dto.NotificationDTO;
import salmon.community.enums.NotificationTypeEnum;
import salmon.community.model.User;
import salmon.community.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SalmonC
 * @date 2021-09-07 21:46
 */
@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;


    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,
                          HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.readNotification(id, user);


        if (NotificationTypeEnum.REPLY_COMMENT.getType() == (notificationDTO.getType())
                || NotificationTypeEnum.REPLY_QUESTION.getType() == (notificationDTO.getType())) {
            return "redirect:/question/"+notificationDTO.getOuterid();
        }
        return "profile";
    }
}
