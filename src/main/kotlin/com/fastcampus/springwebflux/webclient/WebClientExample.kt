package com.fastcampus.springwebflux.webclient

import com.fastcampus.springwebflux.book.Book
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
class WebClientExample {

    val url = "http://localhost:8080/books"

    val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/books/block")
    fun getBooksBlockingWay() : List<Book> {
        log.info("Start RestTemplate")

        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(url, HttpMethod.GET, null,
            object : ParameterizedTypeReference<List<Book>>() {}
            )

//        val response2 = restTemplate.exchange(url, HttpMethod.GET, null,
//            object : ParameterizedTypeReference<List<Book>>() {}
//        )
//
//        val response3 = restTemplate.exchange(url, HttpMethod.GET, null,
//            object : ParameterizedTypeReference<List<Book>>() {}
//        )

        /** 외국 서버, 응답이 느린 서버의 경우 블로킹 되는 exchange 메서드 특성 상 다른 일을 하지 못하게 됨
         * 위 경우 순차적으로 response 1,2,3 이 처리를 하기 때문에 api 가 느려지는 문제가 발생
         * 복수개의 처리가 필요할 때는 병렬적으로 동작할 수 있도록 논블로킹 api 를 사용해주어야 함.
         * 이 과정이 불편하므로 논블로킹 방식 , 블로킹 방식 둘 다 가능한 webClient 로 해결할 수 있다. **/

        val result = response.body!!
        log.info("result : {}", result)
        log.info("Finish RestTemplate")

        return result
    }

    @GetMapping("/books/nonblock")
    fun getBooksNonBlockingWay() : Flux<Book> {

        log.info("Start WebClient")
        val flux = WebClient.create()
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Book::class.java)
            .map {
                log.info("result : {}", it)
                it
            }
        log.info("Finish WebClient") // 논블로킹 방식이므로 응답이 올 때까지 기다리지 않고 result 보다 먼저 출력

        return flux

    }


}