package org.yzy.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yzy.shortlink.common.convention.result.Result;
import org.yzy.shortlink.common.convention.result.Results;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.yzy.shortlink.project.service.ShortLinkService;

import java.util.List;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/12/2 18:22
 */
@RequestMapping("/api/shortLink/project/v1/shortlink")
@RestController
@RequiredArgsConstructor
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     *
     * @param requestParam 请求参数
     * @return 短链接
     */
    @PostMapping("/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PutMapping("/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     *
     * @param requestParam 请求参数
     * @return 短链接列表
     */
    @GetMapping("/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }
    /**
     * 查询短链接分组内数量
     */
    @GetMapping("/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam){
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }
}
