package cn.szh.filter;

import cn.szh.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    //令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (URLFilter.hasAuthorize(path)) {
            return chain.filter(exchange);
        }
//        if (path.startsWith("/api/user/login") || path.startsWith("/api/brand/search/")) {
//            //放行
//            Mono<Void> filter = chain.filter(exchange);
//            return filter;
//        }
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        if(StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        HttpCookie first = request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (first != null) {
            token =first.getValue();
        }
        if(StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return response.setComplete();
        }
        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            request.mutate().header(AUTHORIZE_TOKEN,claims.toString());
            request.mutate().header(AUTHORIZE_TOKEN,"Bearer"+token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
