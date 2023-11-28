package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.project.common.convention.exception.ServiceException;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dao.mapper.ShortLinkMapper;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.service.ShortLinkService;
import org.yzy.shortlink.project.util.HashUtil;

import java.util.Objects;


/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortURICreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {

        String shortURI = generateSuffix(shortLinkCreateReqDTO);
        ShortLinkDO shortLinkDO = BeanUtil.copyProperties(shortLinkCreateReqDTO, ShortLinkDO.class);
        String fullShortUrl = shortLinkCreateReqDTO.getDomain() + "/" + shortURI;

        shortLinkDO.setShortUri(shortURI);
        shortLinkDO.setEnableStatus(0);
        shortLinkDO.setFullShortUrl(fullShortUrl);


        // TODO 是否只允许一个短连接对应一个原始链接 ?
        try {
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException e) {
            log.info("短链接已存在 {}", fullShortUrl);
            throw new ServiceException("短链接已存在，请勿重复创建");
        }
        shortURICreateCachePenetrationBloomFilter.add(fullShortUrl);

        return BeanUtil.copyProperties(shortLinkDO, ShortLinkCreateRespDTO.class);
    }

    /**
     * 生成短链接后缀
     *
     * @param shortLinkCreateReqDTO 短链接创建请求参数
     * @return 短链接后缀
     */
    public String generateSuffix(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        String shortURI = "";
        String originUrl = shortLinkCreateReqDTO.getOriginUrl();
        String domain = shortLinkCreateReqDTO.getDomain();
        int maxCount = 10;
        int currentCount = 0;
        while (true) {
            if (currentCount >= maxCount) {
                throw new ServiceException("生成短链接失败");
            }
            shortURI = HashUtil.generateShortLink(originUrl);
            shortURI += System.currentTimeMillis();
            if (!shortURICreateCachePenetrationBloomFilter.contains(domain + "/" + shortURI)) {
                break;
            }
            currentCount++;
        }
        return HashUtil.generateShortLink(Objects.requireNonNull(shortURI));
    }
}