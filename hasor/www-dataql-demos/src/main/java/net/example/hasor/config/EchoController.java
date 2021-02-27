package net.example.hasor.config;
import net.hasor.dataway.config.Result;
import net.hasor.web.annotation.Any;
import net.hasor.web.annotation.MappingTo;

/**
 * 健康检查
 */
@MappingTo("/_ehc.html")
public class EchoController {
    @Any
    public Result<Boolean> echo() {
        return Result.of(true);
    }
}