package io.barinek.continuum

import com.fasterxml.jackson.databind.ObjectMapper
import io.barinek.continuum.restsupport.BasicApp
import io.barinek.continuum.restsupport.BasicHandler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.HandlerList
import java.io.OutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TestApp(defaultPort: Int, val block: (mapper: ObjectMapper, outputStream: OutputStream) -> Unit) : BasicApp(defaultPort) {

    override fun handlerList() = HandlerList().apply {
        addHandler(object : BasicHandler() {
            override fun handle(s: String, request: Request, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
                block(mapper, httpServletResponse.outputStream)
                httpServletResponse.status = HttpServletResponse.SC_OK
                request.isHandled = true
            }
        })
    }
}