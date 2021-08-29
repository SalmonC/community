package salmon.community.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import salmon.community.community.dto.AccessTokenDTO;
import salmon.community.community.dto.GithubUser;
import salmon.community.community.mapper.UserMapper;
import salmon.community.community.model.User;
import salmon.community.community.provider.GithubProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author SalmonC
 * @date 2021-08-28 23:01
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println(githubUser);

        if(githubUser != null) {
            //登录成功, 写Cookie和Session
            request.getSession().setAttribute("user",githubUser);
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(githubUser.getId()+"");
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            System.out.println();
            userMapper.insertUser(user);
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
}
