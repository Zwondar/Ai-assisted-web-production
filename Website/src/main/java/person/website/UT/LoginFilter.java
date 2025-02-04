package person.website.UT;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/Log/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request =(HttpServletRequest)servletRequest;
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // 如果是预检请求，则直接通过，不进行后续处理
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }


         try {

           String jwt= request.getHeader("Token");

           JwtUtils.parseJWT(jwt);

           filterChain.doFilter(servletRequest,servletResponse);

       }catch (Exception e){
           System.out.println("令牌非法");
           e.printStackTrace();
       }

    }
}
