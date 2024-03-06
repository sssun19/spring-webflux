package com.fastcampus.springwebflux.fn

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
// 클라이언트로부터 전달 받은 요청을 해석하고 그에 맞는 핸들러로 전달하는 역할
// configuration annotation 으로 설정해주어야 함 Bean 으로 라우터 함수 등록해주어야 함
class Router {

    @Bean
    fun helloRouter(handler: HelloHandler) : RouterFunction<ServerResponse> =
        route()
            .GET("/", handler::sayHello)
//            .POST("/", handler::sayHello)
            .build()

    @Bean
    fun userRouter(handler: UserHandler) : RouterFunction<ServerResponse> =
//        route() // route 함수를 사용하면 http get post push delete 함수 사용 가능
//            .GET("/users/{id}", handler::getUser)
//            .GET("/users", handler::getAll)
//            .build()
        router {
            "/users".nest {
                GET("/{id}", handler::getUser)
                GET("", handler::getAll)
            }
        }
}
