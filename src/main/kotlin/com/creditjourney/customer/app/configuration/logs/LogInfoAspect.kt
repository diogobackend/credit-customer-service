package com.creditjourney.customer.app.configuration.logs

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class LogInfoAspect {

    private val log = KotlinLogging.logger {}

    @Around("@annotation(logInfo)")
    fun logMethod(
        joinPoint: ProceedingJoinPoint,
        logInfo: LogInfo
    ): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method

        val result = joinPoint.proceed()

        log.info {
            buildLogMessage(
                method = method,
                args = joinPoint.args,
                result = result,
                logInfo = logInfo
            )
        }

        return result
    }

    private fun buildLogMessage(
        method: Method,
        args: Array<Any?>,
        result: Any?,
        logInfo: LogInfo
    ): String {
        val message = StringBuilder("M=${method.name}")

        if (logInfo.logParameters) {
            message.append(", parameters=${buildParameters(method, args)}")
        }

        if (logInfo.logReturn && result !is Unit) {
            message.append(", return=$result")
        }

        return message.toString()
    }

    private fun buildParameters(
        method: Method,
        args: Array<Any?>
    ): Map<String, Any?> =
        method.parameters
            .mapIndexedNotNull { index, parameter ->
                val annotation = parameter.getAnnotation(LogParameter::class.java)
                    ?: return@mapIndexedNotNull null

                val name = annotation.name.ifBlank { parameter.name }

                name to args.getOrNull(index)
            }
            .toMap()
}