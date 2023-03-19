package service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import service.model.brand.ResultAPI
import service.model.meta.ReplaceKey
import service.model.meta.ReplaceKeyService

@RestController
class APIController(
    private val resultAPI: ResultAPI,
    private val replaceKeyService: ReplaceKeyService
) {
    @GetMapping("/test")
    fun controlViewer7(): String {
        return "asfsdafads"
    }

    @GetMapping("/test5")
    fun controlViewer5(@RequestBody param: Map<String, String>): String {
        val title = param.get("title").orEmpty()
        resultAPI.add(title)
        replaceKeyService.insertData(ReplaceKey(title = title))
        return resultAPI.allPrint(title)
    }

    @GetMapping("/test6")
    fun controlViewer6(): String {
        return resultAPI.print()
    }
}
