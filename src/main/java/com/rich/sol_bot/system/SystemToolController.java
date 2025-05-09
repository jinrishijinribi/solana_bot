package com.rich.sol_bot.system;

import com.rich.sol_bot.i18n.I18nTranslator;
import com.rich.sol_bot.system.tool.AesTool;
import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统")
@RestController
@RequestMapping("/tool")
public class SystemToolController {
    @Operation(summary = "encode")
    @GetMapping("/encode")
    public String encode(String val) {
        return aesTool.encrypt(val);
    }


    @Operation(summary = "decode")
    @GetMapping("/decode")
    public String decode(String val) {
        return aesTool.decrypt(val);
    }

    @Operation(summary = "翻译")
    @GetMapping("/translate")
    public String translate(String val, I18nLanguageEnum language) {
        return i18nTranslator.getContent(language, val);
    }

    @Resource
    private AesTool aesTool;
    @Resource
    private I18nTranslator i18nTranslator;
}
