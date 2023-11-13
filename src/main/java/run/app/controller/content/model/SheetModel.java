package run.app.controller.content.model;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import run.app.cache.AbstractStringCacheStore;
import run.app.exception.ForbiddenException;
import run.app.model.entity.Sheet;
import run.app.model.entity.SheetMeta;
import run.app.model.enums.PostStatus;
import run.app.model.support.HaloConst;
import run.app.model.vo.SheetDetailVO;
import run.app.service.OptionService;
import run.app.service.SheetMetaService;
import run.app.service.SheetService;
import run.app.service.ThemeService;
import run.app.service.assembler.SheetRenderAssembler;

/**
 * Sheet model.
 *
 * @author ryanwang
 * @date 2020-01-07
 */
@Component
public class SheetModel {

    private final SheetService sheetService;

    private final SheetRenderAssembler sheetRenderAssembler;

    private final SheetMetaService sheetMetaService;

    private final AbstractStringCacheStore cacheStore;

    private final ThemeService themeService;

    private final OptionService optionService;

    public SheetModel(SheetService sheetService,
        SheetRenderAssembler sheetRenderAssembler,
        SheetMetaService sheetMetaService,
        AbstractStringCacheStore cacheStore,
        ThemeService themeService,
        OptionService optionService) {
        this.sheetService = sheetService;
        this.sheetRenderAssembler = sheetRenderAssembler;
        this.sheetMetaService = sheetMetaService;
        this.cacheStore = cacheStore;
        this.themeService = themeService;
        this.optionService = optionService;
    }

    /**
     * Sheet content.
     *
     * @param sheet sheet
     * @param token token
     * @param model model
     * @return template name
     */
    public String content(Sheet sheet, String token, Model model) {
        SheetDetailVO sheetDetailVo;
        if (StringUtils.isEmpty(token)) {
            sheet = sheetService.getBy(PostStatus.PUBLISHED, sheet.getSlug());
            sheetDetailVo = sheetRenderAssembler.convertToDetailVo(sheet);
        } else {
            // verify token
            String cachedToken = cacheStore.getAny(token, String.class)
                .orElseThrow(() -> new ForbiddenException("您没有该页面的访问权限"));
            if (!cachedToken.equals(token)) {
                throw new ForbiddenException("您没有该页面的访问权限");
            }
            sheetDetailVo = sheetRenderAssembler.convertToPreviewDetailVo(sheet);
        }

        sheetService.publishVisitEvent(sheet.getId());

        List<SheetMeta> metas = sheetMetaService.listBy(sheet.getId());

        // Generate meta keywords.
        if (StringUtils.isNotEmpty(sheet.getMetaKeywords())) {
            model.addAttribute("meta_keywords", sheet.getMetaKeywords());
        } else {
            model.addAttribute("meta_keywords", optionService.getSeoKeywords());
        }

        // Generate meta description.
        if (StringUtils.isNotEmpty(sheet.getMetaDescription())) {
            model.addAttribute("meta_description", sheet.getMetaDescription());
        } else {
            model.addAttribute("meta_description",
                sheetService.generateDescription(sheet.getContent().getContent()));
        }

        // sheet and post all can use
        model.addAttribute("sheet", sheetDetailVo);
        model.addAttribute("post", sheetDetailVo);
        model.addAttribute("is_sheet", true);
        model.addAttribute("metas", sheetMetaService.convertToMap(metas));

        if (themeService.templateExists(
            ThemeService.CUSTOM_SHEET_PREFIX + sheet.getTemplate() + HaloConst.SUFFIX_FTL)) {
            return themeService.render(ThemeService.CUSTOM_SHEET_PREFIX + sheet.getTemplate());
        }
        return themeService.render("sheet");
    }
}
