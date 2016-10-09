package core.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.forestry.core.Constant;
import com.forestry.model.sys.SysUser;

import core.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
 		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		
 	
 		
		
		
		String contextPath = request.getContextPath();
		String url = request.getRequestURI();
		url = url.replace(contextPath, "");
		//System.out.println(url);
		if (url.startsWith("/sys") && !url.contains("/sys/sysuser/login") && !url.contains("/sys/attachment/getFlower") && !url.contains("/sys/sysuser/externalVerifySysUser")) {
			SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.SESSION_SYS_USER);
			
			
			if (null == sysUser) {
				/*
				 * if (response.getHeader("x-requested-with") != null &&
				 * response.getHeader("x-requested-with").equalsIgnoreCase(
				 * "XMLHttpRequest")) { response.sendRedirect(contextPath +
				 * "/login.jsp"); }
				 */
				if (url.startsWith("/sys/sysuser/home")) {
					response.sendRedirect(contextPath + "/login.jsp");
				} else {
					String json = new String(request.getReader().readLine().getBytes(), "utf-8");
					 
					 JSONObject obj = JSONObject.fromObject(json);
					
					String user_id = StringUtil.trimNull(obj.get("user_id"));
					String token = StringUtil.trimNull(obj.get("token"));

					String access_token = StringUtil.trimNull(request.getSession().getServletContext().getAttribute("token_" + user_id));

					if (token.equals(access_token)&&!"".equals(access_token)) {
						request.setCharacterEncoding("UTF-8");
						// }
						filterChain.doFilter(request, response);
						return;
					}

					response.sendError(999);
				}

				return;
			}
			
			// SessionThreadLocal.setThreadSysUser(sysUser);
		}
		//if (request.getMethod().equalsIgnoreCase("get")) {
		//	request = new GetHttpServletRequestWrapper(request, "UTF-8");
			request.setCharacterEncoding("UTF-8");
		//}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
