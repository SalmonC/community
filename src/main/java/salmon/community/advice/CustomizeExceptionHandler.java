package salmon.community.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import salmon.community.dto.ResultDTO;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.exception.CustomizeException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author SalmonC
 * @date 2021-09-03 17:50
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView handle(HttpServletRequest request,
                               Throwable e,
                               Model model,
                               HttpServletResponse response) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)) {
            // 返回JSON, 不跳转页面
            ResultDTO resultDTO;
            if(e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            }else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            // 错误页面跳转
            if(e instanceof CustomizeException) {
                model.addAttribute("message",e.getMessage());
                model.addAttribute("code",((CustomizeException) e).getCode());
            }else{
                model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        HttpStatus status = HttpStatus.resolve(code);
//        return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
//    }

}
